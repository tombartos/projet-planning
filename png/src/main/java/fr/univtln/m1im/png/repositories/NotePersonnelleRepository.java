package fr.univtln.m1im.png.repositories;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.NotePersonnelle;
import fr.univtln.m1im.png.model.Utilisateur;
import jakarta.persistence.EntityManager;

public class NotePersonnelleRepository extends JpaRepository<NotePersonnelle, Long> {
    public NotePersonnelleRepository(EntityManager entityManager) {
        super(NotePersonnelle.class, entityManager);
    }

    public NotePersonnelle getByCreneauUtilisateur(long utilisateurId, long creneauId) {
        try {
            return em.createNamedQuery("NotePersonnelle.getByCreneauUtilisateur", NotePersonnelle.class)
                    .setParameter("idUtilisateur", utilisateurId)
                    .setParameter("idCreneau", creneauId)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    public void modify(NotePersonnelle note, String text, Creneau creneau, Utilisateur utilisateur) {
        if (note != null) {
            em.getTransaction().begin(); // We try without following the managedobject pattern like in the other
                                         // repositories
            em.merge(note);
            note.setNotePerso(text);
            em.getTransaction().commit();
        } else {
            note = NotePersonnelle.builder()
                    .notePerso(text)
                    .creneau(creneau)
                    .utilisateur(utilisateur)
                    .build();
            em.getTransaction().begin();
            em.persist(note);
            em.merge(utilisateur);
            em.merge(creneau);
            utilisateur.getNotesPerso().add(note);
            creneau.getNotesPerso().add(note);
            em.getTransaction().commit();
        }
    }
}
