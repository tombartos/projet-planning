package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="Utilisateurs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
@NamedQueries({
    @NamedQuery(
        name = "Utilisateur.getNotePerso",
        query = "SELECT n FROM NotePersonnelle n WHERE n.utilisateur.id = :idUtilisateur AND n.creneau.id = :idCreneau"
    )
})

public abstract class Utilisateur {
    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;
    private String nom;
    private String prenom;
    private String login; //Username
    private String email;
    private LocalDate dateNaissance;

    @OneToMany(mappedBy = "utilisateur")
    @ToString.Exclude
    @Builder.Default
    private List<NotePersonnelle> notesPerso = new ArrayList<NotePersonnelle>();
}
