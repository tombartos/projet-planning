package fr.univtln.m1im.png.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for the {@link Groupe} class.
 * Used to transfer group data between layers.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GroupeDTO {
    String code;
    String nom;
    String formation;
}
