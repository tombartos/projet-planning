package fr.univtln.m1im.png.repositories;


import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.DemandeCreneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import jakarta.persistence.EntityManager;

public class DemandeCreneauRepository extends JpaRepository<DemandeCreneau, Long> {
    public DemandeCreneauRepository(EntityManager entityManager){
        super(DemandeCreneau.class, entityManager);
    }

    public String addDemandeCreneau(DemandeCreneau DemandeCreneau) {
        //All the verifications are done in this method
        //First we get the list of creneaux of the day
        OffsetDateTime day = DemandeCreneau.getHeureDebut().truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime firstHour = day.withHour(8).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime lastHour = day.withHour(22).withMinute(0).withSecond(0).withNano(0);
        CreneauRepository creneauRepository = new CreneauRepository(em);
        //Here we get the list of all creneaux of the day assuming there is a reasonable number of creneaux,
        //We could also get the creneaux of the week by criteria (one request per criteria) if the number of creneaux is too high
        List<Creneau> creneauxDay = creneauRepository.getCreneauxDay(firstHour, lastHour);
        creneauxDay.sort((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()));

        Creneau creneauTmp = Creneau.makeFromDemandeCreneau(DemandeCreneau); //We create a temporary creneau to check the conflicts
        //1: Prof check
        for(Professeur prof : DemandeCreneau.getProfesseurs()) {
            List<Creneau> profCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getProfesseurs().contains(prof)) {
                    profCreneaux.add(c);
                }
            }
            if (!Utils.canInsertCreneau(creneauTmp, profCreneaux)) {
                return ("La demande ne peut pas être effectuée car il y a un conflit avec le professeur " + prof.getNom() + " " + prof.getPrenom());
            }
        }

        //2: Groupe check
        for (Groupe groupe : DemandeCreneau.getGroupes()) {
            List<Creneau> groupeCreneaux = new ArrayList<>();
            for (Creneau c : creneauxDay) {
                if (c.getGroupes().contains(groupe)) {
                    groupeCreneaux.add(c);
                }
            }
            if (!Utils.canInsertCreneau(creneauTmp, groupeCreneaux)) {
                return ("La demande ne peut pas être effectuée car il y a un conflit avec le groupe " + groupe.getNom());
            }
        }

        //3: Salle check
        for (Creneau c : creneauxDay) {
            List<Creneau> salleCreneaux = new ArrayList<>();
            if (c.getSalle().equals(DemandeCreneau.getSalle())) {
                salleCreneaux.add(c);
            }
        }
        if (!Utils.canInsertCreneau(creneauTmp, creneauxDay)) {
            return ("La demande ne peut pas être effectuée car il y a un conflit avec la salle " + DemandeCreneau.getSalle().getCode());
        }
        em.getTransaction().begin();
        //Persist the creneau and make the relations with the modules, groupes and professeurs
        for (Professeur prof : DemandeCreneau.getProfesseurs()) {
            Professeur managedProf = em.merge(prof);
            managedProf.getDemandes_creneaux().add(DemandeCreneau);
        }
        
        em.persist(DemandeCreneau);
        em.getTransaction().commit();
        return ("La demande a été effectuée avec succès");
    }
    //TODO: Etendre plusieurs semaines

    
}
