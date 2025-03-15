package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Etudiant extends Utilisateur {
    @Builder.Default
    @ManyToMany(mappedBy = "etudiants")
    private List<Groupe> groupes = new ArrayList<Groupe>();
}
