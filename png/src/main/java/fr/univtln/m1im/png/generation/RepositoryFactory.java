package fr.univtln.m1im.png.generation;

import java.util.function.Consumer;

import fr.univtln.m1im.png.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Factory interface for managing repositories and database transactions.
 * Provides implementations for interacting with a database or printing to
 * stdout.
 */
interface RepositoryFactory extends AutoCloseable {

    /**
     * Executes a transaction with the provided repository consumer.
     *
     * @param f The consumer that performs operations on the repository.
     */
    public void transaction(Consumer<Repository> f);

    /**
     * Closes the factory and releases resources.
     */
    @Override
    public void close();

    /**
     * Interface for a repository that provides persistence operations.
     */
    static interface Repository {
        /**
         * Persists an object to the repository.
         *
         * @param o The object to persist.
         */
        public void persist(Object o);
    }

    /**
     * Implementation of {@link RepositoryFactory} for interacting with a database.
     */
    static class Database implements RepositoryFactory {
        private EntityManagerFactory emf = Utils.getEntityManagerFactory();

        static record EntityManagerWrapper(EntityManager em) implements Repository {
            @Override
            public void persist(Object o) {
                this.em.persist(o);
            }
        }

        @Override
        public void transaction(Consumer<Repository> f) {
            try (var em = emf.createEntityManager()) {
                try {
                    em.getTransaction().begin();
                    f.accept(new EntityManagerWrapper(em));
                    em.getTransaction().commit();
                } catch (Throwable e) {
                    em.getTransaction().rollback();
                    throw e;
                }
            }
        }

        @Override
        public void close() {
            emf.close();
        }
    }

    /**
     * Implementation of {@link RepositoryFactory} that prints objects to stdout.
     * Useful for debugging or generating scripts.
     */
    static class Stdout implements RepositoryFactory {
        @Override
        public void transaction(Consumer<Repository> f) {
            f.accept(new Repository() {
                @Override
                public void persist(Object o) {
                    System.out.println(o);
                }
            });
        }

        @Override
        public void close() {
        }
    }
}
