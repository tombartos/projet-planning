package fr.univtln.m1im.png.repositories;


import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import jakarta.persistence.EntityManager;

public class EtudiantRepository extends JpaRepository<Etudiant, Long> {
    public EtudiantRepository(EntityManager entityManager){
        super(Etudiant.class, entityManager);
    }

    public List<Creneau> getCreneaux(Long etudiantId, int pageNumber, int pageSize) {
        return em.createNamedQuery("Etudiant.getCreneaux", Creneau.class)
                .setParameter("etudiantId", etudiantId)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
