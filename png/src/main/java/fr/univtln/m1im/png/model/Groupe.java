package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group entity in the system.
 * A group can have a hierarchical structure with parent and child groups (subgroups).
 * It can also be associated with students, modules, and time slots (creneaux).
 * 
 * Annotations:
 * - @Entity: Marks this class as a JPA entity.
 * - @Table(name="Groupes"): Maps this entity to the "Groupes" table in the database.
 * - @NoArgsConstructor: Generates a no-argument constructor with protected access.
 * - @AllArgsConstructor: Generates a constructor with all fields as arguments.
 * - @Builder: Enables the builder pattern for creating instances of this class.
 * - @Getter, @Setter: Automatically generates getter and setter methods for all fields.
 * - @ToString: Generates a toString method for this class.
 * - @NamedQueries: Defines named queries for this entity.
 * 
 * Named Queries:
 * - "Groupe.getAll": Retrieves all groups.
 * - "Groupe.getAllDTO": Retrieves all groups as DTOs with specific fields (code, nom, formation).
 * - "Groupe.getWeekCreneaux": Retrieves time slots (creneaux) for a group within a specific week.
 * 
 * Fields:
 * - code: The unique identifier for the group.
 * - nom: The name of the group.
 * - formation: The formation or program associated with the group.
 * - parent: The parent group in the hierarchy.
 * - etudiants: The list of students in the group.
 * - sousGroupes: The list of subgroups (child groups) in the hierarchy.
 * - modules: The list of modules associated with the group.
 * - creneaux: The list of time slots (creneaux) associated with the group.
 * 
 * Relationships:
 * - @ManyToOne: The parent group relationship.
 * - @OneToMany: The relationships for students and subgroups.
 * - @ManyToMany: The relationships for modules and time slots.
 * 
 * Methods:
 * - addEtudiant(Etudiant etudiant): Adds a student to the group and propagates the addition to the parent group if it exists.
 */
@Entity
@Table(name="Groupes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@NamedQueries({
    @NamedQuery(
        name = "Groupe.getAll", 
        query = "SELECT g FROM Groupe g"),
    @NamedQuery(
        name = "Groupe.getAllDTO",
        query = "SELECT new fr.univtln.m1im.png.dto.GroupeDTO(g.code, g.nom, g.formation) FROM Groupe g"
    ),
    @NamedQuery(
    name = "Groupe.getWeekCreneaux",
    query = "SELECT c FROM Groupe g JOIN g.creneaux c WHERE g.code = :code AND c.heureDebut BETWEEN :firstDay AND :lastDay"
    )
})
public class Groupe {
    @Id
    private String code;
    private String nom;
    private String formation;

    @ManyToOne
    private Groupe parent;

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "groupe", fetch = FetchType.LAZY)
    private List<Etudiant> etudiants = new ArrayList<Etudiant>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Groupe> sousGroupes = new ArrayList<Groupe>(); //Sous groupes en structure d'arbre

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<Module>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Creneau> creneaux = new ArrayList<Creneau>();


    /**
     * Adds a student to the current group and propagates the addition to the parent group if it exists, add the group to etudiant too.
     *
     * @param etudiant the student to be added to the group
     */
    public void addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getGroupes().add(this);
        if (parent != null) {
            parent.addEtudiant(etudiant);
        }
    }
}
