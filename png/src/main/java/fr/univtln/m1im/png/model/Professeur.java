package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
@Setter
@ToString
@NamedQueries({
    @NamedQuery(
      name = "Professeur.getByLogin",
      query = "SELECT p FROM Professeur p WHERE p.login = :login"
    ),
    @NamedQuery(
      name = "Professeur.getWeekCrenaux",
      query = "SELECT c FROM Professeur p JOIN p.creneaux c WHERE p.id = :professeurId AND c.heureDebut BETWEEN :firstDay AND :lastDay"
    )
  })
public class Professeur extends Utilisateur {
    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<Module>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Creneau> creneaux = new ArrayList<Creneau>();

}
