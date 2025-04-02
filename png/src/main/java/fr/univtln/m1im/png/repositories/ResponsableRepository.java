package fr.univtln.m1im.png.repositories;

import fr.univtln.m1im.png.model.Responsable;
import jakarta.persistence.EntityManager;

public class ResponsableRepository extends JpaRepository<Responsable, Long> {
    public ResponsableRepository(EntityManager entityManager) {
        super(Responsable.class, entityManager);
    }

    public Responsable getByLogin(String login) {
        return em.createNamedQuery("Responsable.getByLogin", Responsable.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
