package fr.univtln.m1im.png.repositories;


import fr.univtln.m1im.png.model.Groupe;
import jakarta.persistence.EntityManager;

public class GroupeRepository extends JpaRepository<Groupe, Long> {
    public GroupeRepository(EntityManager entityManager){
        super(Groupe.class, entityManager);
    }
}
