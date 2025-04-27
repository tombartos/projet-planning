package fr.univtln.m1im.png.repositories;

import fr.univtln.m1im.png.model.Responsable;
import jakarta.persistence.EntityManager;

/**
 * Repository class for managing {@link Responsable} entities.
 * Provides methods for retrieving responsables by login.
 */
public class ResponsableRepository extends JpaRepository<Responsable, Long> {

    /**
     * Constructs a new {@link ResponsableRepository}.
     *
     * @param entityManager The {@link EntityManager} used for database operations.
     */
    public ResponsableRepository(EntityManager entityManager) {
        super(Responsable.class, entityManager);
    }

    /**
     * Retrieves a responsable by their login.
     *
     * @param login The login of the responsable.
     * @return The responsable with the specified login.
     */
    public Responsable getByLogin(String login) {
        return em.createNamedQuery("Responsable.getByLogin", Responsable.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    // TODO: Etendre plusieurs semaines

}
