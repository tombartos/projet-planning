package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Etudiant} entities.
 * Provides methods for retrieving students by login and fetching their
 * schedules.
 */
public class EtudiantRepository extends JpaRepository<Etudiant, Long> {
    public EtudiantRepository(EntityManager entityManager) {
        super(Etudiant.class, entityManager);
    }

    /**
     * Retrieves all creneaux for a specific student.
     *
     * @param etudiantId The ID of the student.
     * @param pageNumber The page number.
     * @param pageSize   The page size.
     * @return A list of creneaux for the student.
     */
    public List<Creneau> getAllCreneaux(Long etudiantId, int pageNumber, int pageSize) {
        return em.createNamedQuery("Etudiant.getAllCreneaux", Creneau.class)
                .setParameter("etudiantId", etudiantId)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves the weekly creneaux for a specific student.
     *
     * @param etudiantId The ID of the student.
     * @param weekNumber The week number.
     * @param year       The year.
     * @param pageNumber The page number.
     * @param pageSize   The page size.
     * @return A list of weekly creneaux for the student.
     */
    public List<Creneau> getWeekCreneaux(Long etudiantId, int weekNumber, int year, int pageNumber, int pageSize) {
        List<OffsetDateTime> weekdays = Utils.getFirstLastDayOfWeek(weekNumber, year);
        return em.createNamedQuery("Etudiant.getWeekCrenaux", Creneau.class)
                .setParameter("etudiantId", etudiantId)
                .setParameter("firstDay", weekdays.getFirst())
                .setParameter("lastDay", weekdays.getLast())
                .setFirstResult(0)
                .getResultList();
    }

    /**
     * Retrieves a student by their login.
     *
     * @param login The login of the student.
     * @return The student with the specified login.
     */
    public Etudiant getByLogin(String login) {
        return em.createNamedQuery("Etudiant.getByLogin", Etudiant.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
