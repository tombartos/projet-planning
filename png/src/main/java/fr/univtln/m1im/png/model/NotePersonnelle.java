package fr.univtln.m1im.png.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "notes_perso")

@NamedQueries({
        @NamedQuery(name = "NotePersonnelle.getByCreneauUtilisateur", query = "SELECT n FROM NotePersonnelle n WHERE n.utilisateur.id = :idUtilisateur AND n.creneau.id = :idCreneau")
})
public class NotePersonnelle {
    @Id
    @SequenceGenerator(name = "note_perso_seq", sequenceName = "note_perso_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "note_perso_seq")
    private Long id;

    @Builder.Default
    @Column(nullable = false)
    private String notePerso = "";

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Creneau creneau;
}
