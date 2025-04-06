package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="Responsables")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@NamedQueries({
    @NamedQuery(
        name = "Responsable.getByLogin",
        query = "SELECT r FROM Responsable r WHERE r.login = :login")
})
public class Responsable extends Utilisateur {
    String UFR; // UFR affected to the responsible

}
