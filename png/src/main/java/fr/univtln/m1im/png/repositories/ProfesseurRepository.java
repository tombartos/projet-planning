package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Professeur;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Professeur} entities.
 * Provides methods for retrieving professors by login, fetching weekly
 * schedules,
 * and retrieving all professors or their DTOs.
 */
public class ProfesseurRepository extends JpaRepository<Professeur, Long> {
    public ProfesseurRepository(EntityManager entityManager) {
        super(Professeur.class, entityManager);
    }

    public Professeur getByLogin(String login) {
        return em.createNamedQuery("Professeur.getByLogin", Professeur.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    public List<Creneau> getWeekCrenaux(long professeurId, int weekNumber, int year, int pageNumber, int pageSize) {
        List<OffsetDateTime> weekdays = Utils.getFirstLastDayOfWeek(weekNumber, year);
        return em.createNamedQuery("Professeur.getWeekCrenaux", Creneau.class)
                .setParameter("professeurId", professeurId)
                .setParameter("firstDay", weekdays.getFirst())
                .setParameter("lastDay", weekdays.getLast())
                .getResultList();
    }

    public List<Professeur> getAll(int pageNumber, int pageSize) {
        return em.createNamedQuery("Professeur.getAll", Professeur.class)
                .setFirstResult(pageSize * pageNumber)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<ProfesseurDTO> getAllDTO(int pageNumber, int pageSize) {
        return em.createNamedQuery("ProfesseurDTO.getAllDTO", ProfesseurDTO.class)
                .setFirstResult(pageSize * pageNumber)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
