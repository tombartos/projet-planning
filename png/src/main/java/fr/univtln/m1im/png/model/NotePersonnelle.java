package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;

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
public class NotePersonnelle {
    @Id
    @SequenceGenerator(name = "note_perso_seq", sequenceName = "note_perso_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "note_perso_seq")
    @Builder.Default
    private Long id = null;

    @Builder.Default
    private String notePerso = "";

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Creneau creneau;
}
