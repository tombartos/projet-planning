package fr.univtln.m1im.png.repositories;


import fr.univtln.m1im.png.model.Creneau;
import jakarta.persistence.EntityManager;

public class CreneauRepository extends JpaRepository<Creneau, Long> {
    public CreneauRepository(EntityManager entityManager){
        super(Creneau.class, entityManager);
    }
}
