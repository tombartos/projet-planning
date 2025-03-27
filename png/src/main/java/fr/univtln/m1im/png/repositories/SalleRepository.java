package fr.univtln.m1im.png.repositories;


import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Salle;
import jakarta.persistence.EntityManager;

public class SalleRepository extends JpaRepository<Salle, Long> {
    public SalleRepository(EntityManager entityManager){
        super(Salle.class, entityManager);
    }

    public List<Creneau> getWeekCrenaux(String code, int weekNumber, int year, int pageNumber, int pageSize){
        List<OffsetDateTime> weekdays = Utils.getFirstLastDayOfWeek(weekNumber, year);
        return em.createNamedQuery("Salle.getWeekCrenaux", Creneau.class)
                .setParameter("code", code)
                .setParameter("firstDay", weekdays.getFirst())
                .setParameter("lastDay", weekdays.getLast())
                .getResultList();
    }
}
