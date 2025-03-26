package fr.univtln.m1im.png.repositories;


import fr.univtln.m1im.png.model.Salle;
import jakarta.persistence.EntityManager;

public class SalleRepository extends JpaRepository<Salle, Long> {
    public SalleRepository(EntityManager entityManager){
        super(Salle.class, entityManager);
    }
}
