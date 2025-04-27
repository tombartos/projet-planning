package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import fr.univtln.m1im.png.model.Module;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repository class for managing {@link Creneau} entities.
 * Provides methods for retrieving, adding, deleting, and modifying time slots
 * (creneaux).
 */
public class CreneauRepository extends JpaRepository<Creneau, Long> {
    private static final Logger log = LoggerFactory.getLogger(CreneauRepository.class);

    public CreneauRepository(EntityManager entityManager) {
        super(Creneau.class, entityManager);
    }

    /**
     * Retrieves all creneaux within a specific day.
     *
     * @param firstHour The start of the day.
     * @param lastHour  The end of the day.
     * @return A list of creneaux within the specified day.
     */
    public List<Creneau> getCreneauxDay(OffsetDateTime firstHour, OffsetDateTime lastHour) {
        return em.createNamedQuery("Creneau.getCreneauxDay", Creneau.class)
                .setParameter("firstHour", firstHour)
                .setParameter("lastHour", lastHour)
                .getResultList();
    }

    /**
     * Retrieves a creneau by its ID.
     *
     * @param id The ID of the creneau.
     * @return The creneau with the specified ID.
     */
    public Creneau getCreneauById(Long id) {
        return em.createNamedQuery("Creneau.getCreneauById", Creneau.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * Adds or modifies a creneau.
     *
     * @param creneau    The creneau to add or modify.
     * @param oldCreneau The existing creneau to modify, or null if adding a new
     *                   creneau.
     * @return A message indicating the result of the operation.
     */
    public String addCreneau(Creneau creneau, Creneau oldCreneau) {
        // All the verifications are done in this method
        // oldCreneau is the creneau to modify, if it is null we are adding a new
        // creneau
        // First we get the list of creneaux of the day
        OffsetDateTime day = creneau.getHeureDebut().truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime firstHour = day.withHour(8).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime lastHour = day.withHour(22).withMinute(0).withSecond(0).withNano(0);
        CreneauRepository creneauRepository = new CreneauRepository(em);
        // Here we get the list of all creneaux of the day assuming there is a
        // reasonable number of creneaux,
        // We could also get the creneaux of the week by criteria (one request per
        // criteria) if the number of creneaux is too high
        List<Creneau> creneauxDay = creneauRepository.getCreneauxDay(firstHour, lastHour);
        creneauxDay.sort((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()));
        if (oldCreneau != null) {
            // We delete the creneau to modify from the list of creneaux of the day
            creneauxDay.remove(oldCreneau);
        }

        // 1: Prof check
        for (Professeur prof : creneau.getProfesseurs()) {
            List<Creneau> profCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getProfesseurs().contains(prof)) {
                    profCreneaux.add(c);
                }
            }
            if (!Utils.canInsertCreneau(creneau, profCreneaux)) {
                return ("Le créneau ne peut pas être inséré car il y a un conflit avec le professeur " + prof.getNom()
                        + " " + prof.getPrenom());
            }
        }

        // 2: Groupe check
        for (Groupe groupe : creneau.getGroupes()) {
            List<Creneau> groupeCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getGroupes().contains(groupe)) {
                    groupeCreneaux.add(c);
                }
            }
            if (!Utils.canInsertCreneau(creneau, groupeCreneaux)) {
                return ("Le créneau ne peut pas être inséré car il y a un conflit avec le groupe " + groupe.getNom());
            }
        }

        // 3: Salle check
        List<Creneau> salleCreneaux = new ArrayList<>();
        for (Creneau c : creneauxDay) {
            if (c.getSalle().equals(creneau.getSalle())) {
                salleCreneaux.add(c);
            }
        }
        if (!Utils.canInsertCreneau(creneau, salleCreneaux)) {
            return ("Le créneau ne peut pas être inséré car il y a un conflit avec la salle "
                    + creneau.getSalle().getCode());
        }
        if (oldCreneau != null)
            creneauRepository.deleteCreneau(oldCreneau);
        em.getTransaction().begin();
        // Persist the creneau and make the relations with the modules, groupes and
        // professeurs
        for (Professeur prof : creneau.getProfesseurs()) {
            Professeur managedProf = em.merge(prof);
            managedProf.getCreneaux().add(creneau);
        }
        for (Groupe groupe : creneau.getGroupes()) {
            Groupe managedGroupe = em.merge(groupe);
            managedGroupe.getCreneaux().add(creneau);
        }
        for (Module module : creneau.getModules()) {
            Module managedModule = em.merge(module);
            managedModule.getCreneaux().add(creneau);
        }
        em.persist(creneau);
        em.getTransaction().commit();

        log.info("Le créneau a été inséré");
        return ("Le créneau a été inséré");
    }

    /**
     * Deletes a creneau from the database.
     *
     * @param creneau The creneau to delete.
     */
    public void deleteCreneau(Creneau creneau) {
        em.getTransaction().begin();
        em.merge(creneau);
        for (Module module : creneau.getModules()) {
            em.merge(module);
            module.getCreneaux().remove(creneau);
        }
        for (Groupe groupe : creneau.getGroupes()) {
            em.merge(groupe);
            groupe.getCreneaux().remove(creneau);
        }
        for (Professeur prof : creneau.getProfesseurs()) {
            em.merge(prof);
            prof.getCreneaux().remove(creneau);
        }
        em.remove(creneau);
        em.getTransaction().commit();
    }

    /**
     * Cancels a creneau by setting its status to "ANNULÉ".
     *
     * @param creneau The creneau to cancel.
     */
    public void annulerCreneau(Creneau creneau) {
        em.getTransaction().begin();
        creneau.setStatus(Creneau.Status.ANNULE);
        em.merge(creneau);
        em.getTransaction().commit();
    }

    /**
     * Restores a canceled creneau by setting its status to "ACTIF".
     *
     * @param creneau The creneau to restore.
     */
    public void restoreCreneau(Creneau creneau) {
        em.getTransaction().begin();
        creneau.setStatus(Creneau.Status.ACTIF);
        em.merge(creneau);
        em.getTransaction().commit();
    }
}
