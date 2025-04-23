package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import jakarta.persistence.EntityManager;

public class EtudiantRepository extends JpaRepository<Etudiant, Long> {
    public EtudiantRepository(EntityManager entityManager) {
        super(Etudiant.class, entityManager);
    }

    public List<Creneau> getAllCreneaux(Long etudiantId, int pageNumber, int pageSize) {
        return em.createNamedQuery("Etudiant.getAllCreneaux", Creneau.class)
                .setParameter("etudiantId", etudiantId)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Creneau> getWeekCreneaux(Long etudiantId, int weekNumber, int year, int pageNumber, int pageSize) {
        List<OffsetDateTime> weekdays = Utils.getFirstLastDayOfWeek(weekNumber, year);
        return em.createNamedQuery("Etudiant.getWeekCrenaux", Creneau.class)
                .setParameter("etudiantId", etudiantId)
                .setParameter("firstDay", weekdays.getFirst())
                .setParameter("lastDay", weekdays.getLast())
                .setFirstResult(0)
                .getResultList();
    }

    public Etudiant getByLogin(String login) {
        return em.createNamedQuery("Etudiant.getByLogin", Etudiant.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
