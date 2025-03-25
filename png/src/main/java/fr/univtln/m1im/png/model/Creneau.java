package fr.univtln.m1im.png.model;

import java.util.List;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="Creneaux")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class Creneau {
    @Id
    @SequenceGenerator(name = "creneau_seq", sequenceName = "creneau_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creneau_seq")
    @Builder.Default
    private Long id = null;
    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;
    private String type; //TD CM TP EXAM

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "creneaux", fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<Module>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "creneaux", fetch = FetchType.LAZY)
    private List<Groupe> groupes = new ArrayList<Groupe>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "creneaux", fetch = FetchType.LAZY)
    private List<Professeur> professeurs = new ArrayList<Professeur>();

    @ManyToOne
    private Salle salle;
}
