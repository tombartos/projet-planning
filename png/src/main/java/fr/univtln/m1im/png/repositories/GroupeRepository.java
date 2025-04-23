package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;
import jakarta.persistence.EntityManager;

public class GroupeRepository extends JpaRepository<Groupe, Long> {
    public GroupeRepository(EntityManager entityManager) {
        super(Groupe.class, entityManager);
    }

    public List<Groupe> getAll(int pageNumber, int pageSize) {
        return em.createNamedQuery("Groupe.getAll", Groupe.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<GroupeDTO> getAllDTO(int pageNumber, int pageSize) {
        return em.createNamedQuery("Groupe.getAllDTO", GroupeDTO.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

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

    public Groupe getByCode(String code) {
        return em.createNamedQuery("Groupe.getByCode", Groupe.class)
                .setParameter("code", code)
                .getSingleResult();
    }
}
