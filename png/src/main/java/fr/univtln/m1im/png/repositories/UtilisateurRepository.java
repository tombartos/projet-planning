package fr.univtln.m1im.png.repositories;


import fr.univtln.m1im.png.model.Utilisateur;
import jakarta.persistence.EntityManager;

public class UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    public UtilisateurRepository(EntityManager entityManager){
        super(Utilisateur.class, entityManager);
    }
}
