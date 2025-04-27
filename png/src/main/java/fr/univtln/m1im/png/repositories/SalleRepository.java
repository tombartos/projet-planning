package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Salle;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Salle} entities.
 * Provides methods for retrieving salles, their weekly schedules, and paginated
 * lists.
 */
public class SalleRepository extends JpaRepository<Salle, Long> {

    /**
     * Constructs a new {@link SalleRepository}.
     *
     * @param entityManager The {@link EntityManager} used for database operations.
     */
    public SalleRepository(EntityManager entityManager) {
        super(Salle.class, entityManager);
    }

    /**
     * Retrieves the weekly schedule (creneaux) for a specific salle.
     *
     * @param code       The code of the salle.
     * @param weekNumber The week number.
     * @param year       The year.
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of creneaux per page.
     * @return A list of creneaux for the specified salle and week.
     */
    public List<Creneau> getWeekCrenaux(String code, int weekNumber, int year, int pageNumber, int pageSize) {
        List<OffsetDateTime> weekdays = Utils.getFirstLastDayOfWeek(weekNumber, year);
        return em.createNamedQuery("Salle.getWeekCrenaux", Creneau.class)
                .setParameter("code", code)
                .setParameter("firstDay", weekdays.getFirst())
                .setParameter("lastDay", weekdays.getLast())
                .getResultList();
    }

    /**
     * Retrieves all salles with pagination.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of salles per page.
     * @return A list of salles for the specified page.
     */
    public List<Salle> getAll(int pageNumber, int pageSize) {
        return em.createNamedQuery("Salle.getAll", Salle.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves a salle by its code.
     *
     * @param code The code of the salle.
     * @return The salle with the specified code.
     */
    public Salle getByCode(String code) {
        return em.createNamedQuery("Salle.getByCode", Salle.class)
                .setParameter("code", code)
                .getSingleResult();
    }
}
