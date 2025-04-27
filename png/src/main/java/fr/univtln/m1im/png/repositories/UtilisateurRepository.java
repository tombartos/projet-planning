package fr.univtln.m1im.png.repositories;

import fr.univtln.m1im.png.model.Utilisateur;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Utilisateur} entities.
 * Provides basic CRUD operations for utilisateurs.
 */
public class UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    /**
     * Constructs a new {@link UtilisateurRepository}.
     *
     * @param entityManager The {@link EntityManager} used for database operations.
     */
    public UtilisateurRepository(EntityManager entityManager) {
        super(Utilisateur.class, entityManager);
    }
}
