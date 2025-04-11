package fr.univtln.m1im.png.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Salles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@NamedQueries({
    @NamedQuery(
      name = "Salle.getWeekCrenaux",
      query = "SELECT c FROM Creneau c WHERE c.salle.code = :code AND c.heureDebut BETWEEN :firstDay AND :lastDay"
    ),
    @NamedQuery(
      name = "Salle.getAll",
      query = "SELECT s FROM Salle s"
    ),
    @NamedQuery(
      name = "Salle.getByCode",
      query = "SELECT s FROM Salle s WHERE s.code = :code"
    )
  })
public class Salle {
    @Id
    private String code;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int capacite;
}