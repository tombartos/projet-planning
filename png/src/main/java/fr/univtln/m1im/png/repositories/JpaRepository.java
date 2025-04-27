package fr.univtln.m1im.png.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.*;

/**
 * Abstract base class for JPA-based repositories.
 * Provides common CRUD operations and pagination support.
 *
 * @param <T>  The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
public abstract class JpaRepository<T, ID> implements GenericRepository<T, ID> {
    /**
     * The {@link EntityManager} used for database operations.
     */
    protected EntityManager em;

    /**
     * The class type of the entity managed by this repository.
     */
    private final Class<T> entityClass;

    /**
     * Constructs a new {@link JpaRepository}.
     *
     * @param entityClass   The class type of the entity.
     * @param entityManager The {@link EntityManager} used for database operations.
     */
    protected JpaRepository(Class<T> entityClass, EntityManager entityManager) {
        this.em = entityManager;
        this.entityClass = entityClass;
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return An {@link Optional} containing the entity if found, or empty if not.
     */
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    /**
     * Retrieves all entities with pagination.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of entities per page.
     * @return A list of entities for the specified page.
     */
    @Override
    public List<T> findAll(int pageNumber, int pageSize) {
        // Utiliser des named queries ou la criteria API
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e ORDER BY e.id";
        return em.createQuery(jpql, entityClass)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Saves an entity to the repository.
     * If the entity already exists, it will be updated.
     *
     * @param entity The entity to save.
     * @return The saved entity.
     */
    @Override
    public T save(T entity) {
        em.getTransaction().begin();
        if (em.contains(entity)) {
            entity = em.merge(entity);
        } else {
            em.persist(entity);
        }
        em.getTransaction().commit();
        return entity;
    }

    /**
     * Deletes an entity from the repository.
     *
     * @param entity The entity to delete.
     */
    @Override
    public void delete(T entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }
}