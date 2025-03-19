package fr.univtln.m1im.png.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Module {
    @Id
    private String code;
    private String nom;
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

    private int nbHeuresCM;
    private int nbHeuresTD;
    private int nbHeuresTP;
}
