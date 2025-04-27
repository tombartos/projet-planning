package fr.univtln.m1im.png.repositories;

import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Module;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Module} entities.
 * Provides methods for retrieving modules, their codes, and associated
 * creneaux.
 */
public class ModuleRepository extends JpaRepository<Module, Long> {

    /**
     * Constructs a new {@link ModuleRepository}.
     *
     * @param entityManager The {@link EntityManager} used for database operations.
     */
    public ModuleRepository(EntityManager entityManager) {
        super(Module.class, entityManager);
    }

    /**
     * Retrieves all creneaux associated with a specific module.
     *
     * @param moduleCode The code of the module.
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of creneaux per page.
     * @return A list of creneaux associated with the specified module.
     */
    public List<Creneau> getAllCreneaux(String moduleCode, int pageNumber, int pageSize) {
        return em.createNamedQuery("Module.getAllCreneaux", Creneau.class)
                .setParameter("codeModule", moduleCode)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves all module codes with pagination.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of module codes per page.
     * @return A list of module codes for the specified page.
     */
    public List<String> getAllModulesCodes(int pageNumber, int pageSize) {
        return em.createNamedQuery("Module.getAllModulesCodes", String.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Retrieves a module by its code.
     *
     * @param moduleCode The code of the module.
     * @return The module with the specified code.
     */
    public Module getModuleByCode(String moduleCode) {
        return em.createNamedQuery("Module.getModuleByCode", Module.class)
                .setParameter("codeModule", moduleCode)
                .getSingleResult();
    }
}
