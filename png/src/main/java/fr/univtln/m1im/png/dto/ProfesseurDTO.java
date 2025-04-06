package fr.univtln.m1im.png.dto;

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
