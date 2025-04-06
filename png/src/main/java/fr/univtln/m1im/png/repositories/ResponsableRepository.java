package fr.univtln.m1im.png.repositories;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Responsable;
import fr.univtln.m1im.png.model.Module;
import jakarta.persistence.EntityManager;

public class ResponsableRepository extends JpaRepository<Responsable, Long> {
    private static final Logger log = Logger.getLogger(ResponsableRepository.class.getName());
    public ResponsableRepository(EntityManager entityManager) {
        super(Responsable.class, entityManager);
    }

    private Boolean canInsertCreneau(Creneau creneau, List<Creneau> creneauxDay) {
        //We want to know if the creneau can be inserted in the already sorted list of creneaux of the day
        //Check empty
        if (creneauxDay.isEmpty()) {
            return true;
        }
        //Check if the same hours are already taken
        for (Creneau c : creneauxDay) {
            if (c.getHeureDebut().isEqual(creneau.getHeureDebut()) || c.getHeureFin().isEqual(creneau.getHeureFin())) {
                return false;
            }
        }
        //Check if the creneau is before the first or after the last
        if (creneauxDay.getFirst().getHeureDebut().isAfter(creneau.getHeureFin())) {
            return true;
        }
        if (creneauxDay.getLast().getHeureFin().isBefore(creneau.getHeureDebut())) {
            return true;
        }
        //Check if the creneau is between two creneaux
        for (int i = 0; i < creneauxDay.size() - 1; i++) {
            Creneau c1 = creneauxDay.get(i);
            Creneau c2 = creneauxDay.get(i + 1);
            if (c1.getHeureFin().isBefore(creneau.getHeureDebut()) && c2.getHeureDebut().isAfter(creneau.getHeureFin())) {
                return true;
            }
        }
        return false;
    }

    public Responsable getByLogin(String login) {
        return em.createNamedQuery("Responsable.getByLogin", Responsable.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    public String addCreneau(Creneau creneau) {
        //All the verifications are done in this method
        //First we get the list of creneaux of the day
        OffsetDateTime day = creneau.getHeureDebut().truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime firstHour = day.withHour(8).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime lastHour = day.withHour(22).withMinute(0).withSecond(0).withNano(0);
        CreneauRepository creneauRepository = new CreneauRepository(em);
        //Here we get the list of all creneaux of the day assuming there is a reasonable number of creneaux,
        //We could also get the creneaux of the week by criteria (one request per criteria) if the number of creneaux is too high
        List<Creneau> creneauxDay = creneauRepository.getCreneauxDay(firstHour, lastHour);
        creneauxDay.sort((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()));
        
        //1: Prof check
        for(Professeur prof : creneau.getProfesseurs()) {
            List<Creneau> profCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getProfesseurs().contains(prof)) {
                    profCreneaux.add(c);
                }
            }
            if (!canInsertCreneau(creneau, profCreneaux)) {
                return ("Le créneau ne peut pas être inséré car il y a un conflit avec le professeur " + prof.getNom() + " " + prof.getPrenom());
            }
        }

        //2: Groupe check
        for (Groupe groupe : creneau.getGroupes()) {
            List<Creneau> groupeCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getGroupes().contains(groupe)) {
                    groupeCreneaux.add(c);
                }
            }
            if (!canInsertCreneau(creneau, groupeCreneaux)) {
                return ("Le créneau ne peut pas être inséré car il y a un conflit avec le groupe " + groupe.getNom());
            }
        }

        //3: Salle check
        for (Creneau c : creneauxDay) {
            List<Creneau> salleCreneaux = new ArrayList<>();
            if (c.getSalle().equals(creneau.getSalle())) {
                salleCreneaux.add(c);
            }
        }
        if (!canInsertCreneau(creneau, creneauxDay)) {
            return ("Le créneau ne peut pas être inséré car il y a un conflit avec la salle " + creneau.getSalle().getCode());
        }
        em.getTransaction().begin();
        //Persist the creneau and make the relations with the modules, groupes and professeurs
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
        return ("Le créneau a été inséré");
    }
    //TODO: Etendre plusieurs semaines
        
}