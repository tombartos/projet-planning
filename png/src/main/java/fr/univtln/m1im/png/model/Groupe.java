package fr.univtln.m1im.png.model;

import jakarta.persistence.Column;
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
    ),
    @NamedQuery(
        name = "Groupe.getByCode",
        query = "SELECT g FROM Groupe g WHERE g.code = :code"
    )
})
public class Groupe {
    @Id
    private String code;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String formation;

    @ManyToOne
    private Groupe parent;

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
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

    /**
     * Adds a Creneau (time slot) to the current group and propagates the addition
     * to the parent group if it exists. WARNING: this method does not add the Groupe to the Creneau object.
     *
     * @param creneau the Creneau object to be added to the group
     */
    // public void addCreneau(Creneau creneau) {
    //     this.creneaux.add(creneau);
    //     if (parent != null) {
    //         parent.addCreneau(creneau);
    //     }
    // }
}
