package fr.univtln.m1im.png.generation;

import java.util.function.Consumer;

import fr.univtln.m1im.png.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

interface RepositoryFactory extends AutoCloseable {
    public void transaction(Consumer<Repository> f);

    // AutoCloseable.close may throw an Exception, though EntityManagerFactory.close
    // doesn't. Behave like EntityManagerFactory.
    public void close();

    static interface Repository {
        public void persist(Object o);
    }

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

    // Right now this just stupidly prints stuff to stdout for debugging, but we
    // could have it print a script.
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
		public void close() {}
    }
}
