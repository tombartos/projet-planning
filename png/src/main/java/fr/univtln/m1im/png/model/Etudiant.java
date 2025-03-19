package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.SuperBuilder;

import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString
@NamedQueries({
    @NamedQuery(
      name = "Etudiant.getCreneaux",
      query = "SELECT c FROM Etudiant e JOIN e.groupes g JOIN g.creneaux c WHERE e.id = :etudiantId"
    )
  })
public class Etudiant extends Utilisateur {
    @ToString.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "etudiants", fetch = FetchType.LAZY)
    private List<Groupe> groupes = new ArrayList<Groupe>();
}
