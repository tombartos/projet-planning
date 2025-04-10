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

    public Creneau getCreneauById(Long id) {
        return em.createNamedQuery("Creneau.getCreneauById", Creneau.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public String addCreneau(Creneau creneau, Creneau oldCreneau) {
        //All the verifications are done in this method
        //oldCreneau is the creneau to modify, if it is null we are adding a new creneau
        //First we get the list of creneaux of the day
        OffsetDateTime day = creneau.getHeureDebut().truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime firstHour = day.withHour(8).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime lastHour = day.withHour(22).withMinute(0).withSecond(0).withNano(0);
        CreneauRepository creneauRepository = new CreneauRepository(em);
        //Here we get the list of all creneaux of the day assuming there is a reasonable number of creneaux,
        //We could also get the creneaux of the week by criteria (one request per criteria) if the number of creneaux is too high
        List<Creneau> creneauxDay = creneauRepository.getCreneauxDay(firstHour, lastHour);
        creneauxDay.sort((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()));
        if (oldCreneau != null){
            //We delete the creneau to modify from the list of creneaux of the day
            creneauxDay.remove(oldCreneau);
        }
        
        //1: Prof check
        for(Professeur prof : creneau.getProfesseurs()) {
            List<Creneau> profCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getProfesseurs().contains(prof)) {
                    profCreneaux.add(c);
                }
            }
            if (!Utils.canInsertCreneau(creneau, profCreneaux)) {
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
            if (!Utils.canInsertCreneau(creneau, groupeCreneaux)) {
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
        if (!Utils.canInsertCreneau(creneau, creneauxDay)) {
            return ("Le créneau ne peut pas être inséré car il y a un conflit avec la salle " + creneau.getSalle().getCode());
        }
        if (oldCreneau != null)
            creneauRepository.deleteCreneau(oldCreneau);
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

    public void deleteCreneau(Creneau creneau) {
        em.getTransaction().begin();
        em.merge(creneau);
        for(Module module : creneau.getModules()) {
            em.merge(module);
            module.getCreneaux().remove(creneau);
        }
        for(Groupe groupe : creneau.getGroupes()) {
            em.merge(groupe);
            groupe.getCreneaux().remove(creneau);
        }
        for(Professeur prof : creneau.getProfesseurs()) {
            em.merge(prof);
            prof.getCreneaux().remove(creneau);
        }
        em.remove(creneau);
        // em.createQuery("DELETE FROM Creneau c WHERE c.id = :id")
        //         .setParameter("id", creneau.getId())
        //         .executeUpdate();
        em.getTransaction().commit();
    }
}
