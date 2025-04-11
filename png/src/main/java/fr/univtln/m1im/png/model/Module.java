package fr.univtln.m1im.png.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="Modules")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@NamedQueries({
    @NamedQuery(
        name = "Module.getAllCreneaux",
        query = "SELECT c FROM Creneau c JOIN c.modules m WHERE m.code = :codeModule"
    ),
    @NamedQuery(
        name = "Module.getAllModulesCodes",
        query = "SELECT m.code FROM Module m"
    ),
    @NamedQuery(
        name = "Module.getModuleByCode",
        query = "SELECT m FROM Module m WHERE m.code = :codeModule"
    )
})
public class Module {
    @Id
    private String code;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String description;

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "modules", fetch = FetchType.LAZY)
    private List<Professeur> professeurs = new ArrayList<Professeur>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "modules", fetch = FetchType.LAZY)
    private List<Groupe> groupes = new ArrayList<Groupe>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Creneau> creneaux = new ArrayList<Creneau>();

    @Column(nullable = false)
    private int nbHeuresCM;
    @Column(nullable = false)
    private int nbHeuresTD;
    @Column(nullable = false)
    private int nbHeuresTP;
}
