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

/**
 * Represents a room (Salle) in the system.
 * This entity is mapped to the "Salles" table in the database.
 * It includes details such as the room code, description, and capacity.
 * 
 * Named queries:
 * - Salle.getWeekCrenaux: Retrieves time slots (Creneaux) for a specific room
 * within a date range.
 * - Salle.getAll: Retrieves all rooms.
 * - Salle.getByCode: Retrieves a room by its code.
 */
@Entity
@Table(name = "Salles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@NamedQueries({
    @NamedQuery(name = "Salle.getWeekCrenaux", query = "SELECT c FROM Creneau c WHERE c.salle.code = :code AND c.heureDebut BETWEEN :firstDay AND :lastDay"),
    @NamedQuery(name = "Salle.getAll", query = "SELECT s FROM Salle s"),
    @NamedQuery(name = "Salle.getByCode", query = "SELECT s FROM Salle s WHERE s.code = :code")
})
public class Salle {

  /**
   * The unique code identifying the room.
   */
  @Id
  private String code;

  /**
   * A description of the room.
   */
  @Column(nullable = false)
  private String description;

  /**
   * The capacity of the room (number of people it can accommodate).
   */
  @Column(nullable = false)
  private int capacite;
}