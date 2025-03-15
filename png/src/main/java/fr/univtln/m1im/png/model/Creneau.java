package fr.univtln.m1im.png.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Creneaux")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Creneau {
    @Id
    @SequenceGenerator(name = "creneau_seq", sequenceName = "creneau_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creneau_seq")
    private Long id;
    private Date heureDebut;
    private Date heureFin;

    @Builder.Default
    @ManyToMany(mappedBy = "creneaux")
    private List<Module> modules = new ArrayList<Module>();

    @Builder.Default
    @ManyToMany(mappedBy = "creneaux")
    private List<Groupe> groupes = new ArrayList<Groupe>();

    @Builder.Default
    @ManyToMany(mappedBy = "creneaux")
    private List<Professeur> professeurs = new ArrayList<Professeur>();
}
