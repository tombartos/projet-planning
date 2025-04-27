package fr.univtln.m1im.png.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Responsables")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "Responsable.getByLogin", query = "SELECT r FROM Responsable r WHERE r.login = :login")
})
/**
 * Represents a responsible user in the system.
 * A responsible user is associated with a specific UFR (Unité de Formation et
 * de Recherche).
 */
public class Responsable extends Utilisateur {
    @Column(nullable = false)
    String UFR; // UFR affected to the responsible

}
