package fr.univtln.m1im.png.repositories;


import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Module;
import jakarta.persistence.EntityManager;

public class ModuleRepository extends JpaRepository<Module, Long> {
    public ModuleRepository(EntityManager entityManager){
        super(Module.class, entityManager);
    }
    
    public List<Creneau> getAllCreneaux(String moduleCode, int pageNumber, int pageSize){
        return em.createNamedQuery("Module.getAllCreneaux", Creneau.class)
                .setParameter("codeModule", moduleCode)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<String> getAllModulesCodes(int pageNumber, int pageSize){
        return em.createNamedQuery("Module.getAllModulesCodes", String.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Module getModuleByCode(String moduleCode){
        return em.createNamedQuery("Module.getModuleByCode", Module.class)
                .setParameter("codeModule", moduleCode)
                .getSingleResult();
    }
}
