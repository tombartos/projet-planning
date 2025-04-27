package fr.univtln.m1im.png.dto;

/**
 * Data Transfer Object (DTO) for the {@link fr.univtln.m1im.png.model.Professeur} class.
 * Used to transfer professor data between layers.
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfesseurDTO {
    long id;
    String nom;
    String prenom;
}
