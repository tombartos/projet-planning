package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
@Setter
public class Professeur extends Utilisateur {
    @Builder.Default
    @ManyToMany
    private List<Module> modules = new ArrayList<Module>();

    @Builder.Default
    @ManyToMany
    private List<Creneau> creneaux = new ArrayList<Creneau>();

    // public Professeur(Long id, String nom, String prenom, String login, String email, String password, Date dateNaissance) {
    //     super(id, nom, prenom, login, email, password, dateNaissance);
    // }
}
