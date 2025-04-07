package fr.univtln.m1im.png.model;

import java.util.List;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
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
@NamedQueries({
    @NamedQuery(
        name = "Creneau.getCreneauxDay",
        query = "SELECT c FROM Creneau c WHERE c.status = 0 AND c.heureDebut BETWEEN :firstHour AND :lastHour"
    )
})
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
    @ManyToMany(mappedBy = "creneaux", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<Module> modules = new ArrayList<Module>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "creneaux", fetch = FetchType.LAZY, cascade ={CascadeType.MERGE})
    private List<Groupe> groupes = new ArrayList<Groupe>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "creneaux", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<Professeur> professeurs = new ArrayList<Professeur>();

    @ManyToOne
    private Salle salle;

    @Builder.Default
    private String noteProf = "";

    @Builder.Default
    private int status = 0; //0: actif, 1: annulé

    @OneToMany(mappedBy = "creneau", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @ToString.Exclude
    @Builder.Default
    private List<NotePersonnelle> notesPerso = new ArrayList<NotePersonnelle>();

    public static Creneau makeFromDemandeCreneau(DemandeCreneau demandeCreneau) {
        return Creneau.builder()
                .heureDebut(demandeCreneau.getHeureDebut())
                .heureFin(demandeCreneau.getHeureFin())
                .type(demandeCreneau.getType())
                .modules(demandeCreneau.getModules())
                .groupes(demandeCreneau.getGroupes())
                .professeurs(demandeCreneau.getProfesseurs())
                .salle(demandeCreneau.getSalle())
                .build();
    }
}
