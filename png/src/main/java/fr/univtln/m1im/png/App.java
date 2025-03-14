package fr.univtln.m1im.png;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 */
@Slf4j
public final class App {
    private static final EntityManagerFactory emf;

    static {
    EntityManagerFactory tryEmf = null;

    try {
      // Create EntityManagerFactory
      tryEmf = Persistence.createEntityManagerFactory(DatabaseConfig.PERSISTENCE_UNIT);

    } catch (Exception e) {
      log.error("Failed to create EntityManagerFactory", e);
      System.exit(0);
    }
    emf = tryEmf;

    //TODO: Remove this + continue with the rest of the code
    // Stop the H2 database server on shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      emf.close();
      log.info("EMF closed");
    }));
  }

    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    private static final class DatabaseConfig {
        private static final String[] DB_ARGS_TCP = {"-tcpAllowOthers", "-ifNotExists"};
        private static final String[] DB_ARGS_WEB = {"-webAllowOthers"};
    
        private static final String PERSISTENCE_UNIT = "png";
      }
}
