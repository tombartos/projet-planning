package fr.univtln.m1im.png;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    void createUser(String username, String password) {
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
            entityManager.getTransaction().begin();

            // We can't use the classic JPA way to create a user because it's not supported by the JPA standard
            String createUserQuery = String.format("CREATE USER %s WITH PASSWORD '%s';", username, password);
            entityManager.createNativeQuery(createUserQuery).executeUpdate();

            // Grant privileges
            String grantConnectQuery = String.format("GRANT CONNECT ON DATABASE postgres TO %s;", username);
            entityManager.createNativeQuery(grantConnectQuery).executeUpdate();

            String grantUsageQuery = String.format("GRANT USAGE ON SCHEMA public TO %s;", username);
            entityManager.createNativeQuery(grantUsageQuery).executeUpdate();

            String grantSelectQuery = String.format("GRANT SELECT ON ALL TABLES IN SCHEMA public TO %s;", username);
            entityManager.createNativeQuery(grantSelectQuery).executeUpdate();

            entityManager.getTransaction().commit();
            log.info("User created successfully: {}", username);
        } catch (Exception e) {
            log.error("Failed to create user", e);
        }
    }

    void tryCreateUser(Utilisateur user, String password){
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
            String checkUserQuery = String.format("SELECT COUNT(*) FROM pg_roles WHERE rolname='%s';", user.getLogin());
            //String checkUserQuery = "SELECT COUNT(*) FROM pg_roles";
            log.info(checkUserQuery);
            int userExists = ((Number) entityManager.createNativeQuery(checkUserQuery).getSingleResult()).intValue();
            if (userExists == 0) {
            createUser(user.getLogin(), password);
            } else {
            log.info("User already exists: {}", user.getLogin());
            }
        } catch (Exception e) {
            log.error("Failed to check or create user", e);
        }
    }
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        assertEquals(1, 1);
    }
}
