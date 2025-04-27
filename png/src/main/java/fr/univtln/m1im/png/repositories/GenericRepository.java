package fr.univtln.m1im.png.repositories;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for managing entities.
 * Provides basic CRUD operations and pagination support.
 *
 * @param <T>  The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
public interface GenericRepository<T, ID> {

    /**
     * Finds an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return An {@link Optional} containing the entity if found, or empty if not.
     */
    Optional<T> findById(ID id);

    /**
     * Retrieves all entities with pagination.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize   The number of entities per page.
     * @return A list of entities for the specified page.
     */
    List<T> findAll(int pageNumber, int pageSize);

    /**
     * Saves an entity to the repository.
     * If the entity already exists, it will be updated.
     *
     * @param entity The entity to save.
     * @return The saved entity.
     */
    T save(T entity);

    /**
     * Deletes an entity from the repository.
     *
     * @param entity The entity to delete.
     */
    void delete(T entity);
}