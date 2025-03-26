package fr.univtln.m1im.png.repositories;


import fr.univtln.m1im.png.model.Module;
import jakarta.persistence.EntityManager;

public class ModuleRepository extends JpaRepository<Module, Long> {
    public ModuleRepository(EntityManager entityManager){
        super(Module.class, entityManager);
    }
}
