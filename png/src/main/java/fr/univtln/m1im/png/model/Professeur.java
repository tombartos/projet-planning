package fr.univtln.m1im.png.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Getter
@Setter
public class Professeur extends Utilisateur{
    @Builder.Default
    @ManyToMany(mappedBy = "professeurs", fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<Module>();

    @Builder.Default
    @ManyToMany(mappedBy = "professeurs", fetch = FetchType.LAZY)
    private List<Creneau> creneaux = new ArrayList<Creneau>();
}
