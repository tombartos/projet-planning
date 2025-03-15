package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Groupe {
    @Id
    private String code;
    private String nom;
    private String formation;

    @ManyToOne
    private Groupe parent;

    @Builder.Default
    @ManyToMany
    private List<Etudiant> etudiants = new ArrayList<Etudiant>();

    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Groupe> sousGroupes = new ArrayList<Groupe>(); //Sous groupes en structure d'arbre

    @Builder.Default
    @ManyToMany
    private List<Module> modules = new ArrayList<Module>();

    @Builder.Default
    @ManyToMany
    private List<Creneau> creneaux = new ArrayList<Creneau>();
}
