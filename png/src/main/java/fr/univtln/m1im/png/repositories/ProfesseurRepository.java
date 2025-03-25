package fr.univtln.m1im.png.repositories;


import fr.univtln.m1im.png.model.Professeur;
import jakarta.persistence.EntityManager;

public class ProfesseurRepository extends JpaRepository<Professeur, Long> {
    public ProfesseurRepository(EntityManager entityManager){
        super(Professeur.class, entityManager);
    }

    public Professeur getByLogin(String login){
        return em.createNamedQuery("Professeur.getByLogin", Professeur.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
