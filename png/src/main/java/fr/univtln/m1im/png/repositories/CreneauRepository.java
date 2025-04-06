package fr.univtln.m1im.png.repositories;


import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import jakarta.persistence.EntityManager;

public class CreneauRepository extends JpaRepository<Creneau, Long> {
    public CreneauRepository(EntityManager entityManager){
        super(Creneau.class, entityManager);
    }

    public List<Creneau> getCreneauxDay(OffsetDateTime firstHour, OffsetDateTime lastHour) {
        return em.createNamedQuery("Creneau.getCreneauxDay", Creneau.class)
                .setParameter("firstHour", firstHour)
                .setParameter("lastHour", lastHour)
                .getResultList();
    }
}