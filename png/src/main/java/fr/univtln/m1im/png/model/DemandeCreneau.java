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
@Table(name="demandes_creneaux")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@NamedQueries({
    @NamedQuery(
        name = "DemandeCreneau.getAllPending",
        query = "SELECT d FROM DemandeCreneau d WHERE d.status = 0"
    )
})
public class DemandeCreneau {
    @Id
    @SequenceGenerator(name = "demande_creneau_seq", sequenceName = "demande_creneau_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "demande_creneau_seq")
    @Builder.Default
    private Long id = null;
    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;
    private String type; //TD CM TP EXAM

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<Module> modules = new ArrayList<Module>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade ={CascadeType.MERGE})
    private List<Groupe> groupes = new ArrayList<Groupe>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "demandes_creneaux", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<Professeur> professeurs = new ArrayList<Professeur>();

    @ManyToOne
    private Salle salle;

    @Builder.Default
    private int status = 0; //0: En attente, 1: accepte, 2 : refuse

    public static DemandeCreneau makeFromCreneau(Creneau c) {
        List<Module> modules = new ArrayList<>();
        for (Module m : c.getModules()) {
            modules.add(m);
        }
        List<Groupe> groupes = new ArrayList<>();
        for (Groupe g : c.getGroupes()) {
            groupes.add(g);
        }
        List<Professeur> professeurs = new ArrayList<>();
        for (Professeur p : c.getProfesseurs()) {
            professeurs.add(p);
        }
        return DemandeCreneau.builder()
                .heureDebut(c.getHeureDebut())
                .heureFin(c.getHeureFin())
                .type(c.getType())
                .modules(modules)
                .professeurs(professeurs)
                .groupes(groupes)
                .salle(c.getSalle())
                .build();
    }
}
