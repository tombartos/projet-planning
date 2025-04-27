package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Groupe} entities.
 * Provides methods for retrieving groups, their DTOs, and their weekly
 * schedules.
 */
public class GroupeRepository extends JpaRepository<Groupe, Long> {

    /**
     * Constructs a new {@link GroupeRepository}.
     *
     * @param entityManager The {@link EntityManager} used for database operations.
     */
    public GroupeRepository(EntityManager entityManager) {
        super(Groupe.class, entityManager);
    }

    /**
     * Retrieves all groups with pagination.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of groups per page.
     * @return A list of groups for the specified page.
     */
    public List<Groupe> getAll(int pageNumber, int pageSize) {
        return em.createNamedQuery("Groupe.getAll", Groupe.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves all group DTOs with pagination.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of group DTOs per page.
     * @return A list of group DTOs for the specified page.
     */
    public List<GroupeDTO> getAllDTO(int pageNumber, int pageSize) {
        return em.createNamedQuery("Groupe.getAllDTO", GroupeDTO.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves the weekly schedule (creneaux) for a specific group.
     *
     * @param code       The code of the group.
     * @param weeknumber The week number.
     * @param year       The year.
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of creneaux per page.
     * @return A list of creneaux for the specified group and week.
     */
    public List<Creneau> getWeekCreneaux(String code, int weeknumber, int year, int pageNumber, int pageSize) {
        // TODO: Recuperer aussi les creneaux des sous groupes
        List<OffsetDateTime> weekdays = Utils.getFirstLastDayOfWeek(weeknumber, year);
        return em.createNamedQuery("Groupe.getWeekCreneaux", Creneau.class)
                .setParameter("code", code)
                .setParameter("firstDay", weekdays.getFirst())
                .setParameter("lastDay", weekdays.getLast())
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves a group by its code.
     *
     * @param code The code of the group.
     * @return The group with the specified code.
     */
    public Groupe getByCode(String code) {
        return em.createNamedQuery("Groupe.getByCode", Groupe.class)
                .setParameter("code", code)
                .getSingleResult();
    }
}
