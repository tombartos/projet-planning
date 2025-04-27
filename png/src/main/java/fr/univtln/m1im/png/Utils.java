package fr.univtln.m1im.png;

import java.time.OffsetDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.univtln.m1im.png.model.Creneau;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class providing helper methods for database connection, date
 * manipulation,
 * and creneau validation.
 */
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private static EntityManagerFactory emf;

    /**
     * Initializes the {@link EntityManagerFactory} with the provided user
     * credentials.
     *
     * @param user     The database username.
     * @param password The database password.
     */
    static public void initconnection(String user, String password) {
        log.info("Starting EntityManagerFactory initialization");
        EntityManagerFactory tryEmf = null;

        try {
            log.info("Initializing EntityManagerFactory");
            Map<String, String> properties = new HashMap<>();
            properties.put("jakarta.persistence.jdbc.user", user);
            properties.put("jakarta.persistence.jdbc.password", password);
            tryEmf = Persistence.createEntityManagerFactory(DatabaseConfig.PERSISTENCE_UNIT, properties);
            log.info("EntityManagerFactory initialized successfully");
        } catch (Exception e) {
            log.error("Failed to create EntityManagerFactory", e);
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
            // System.exit(0);
        }
        emf = tryEmf;
    }

    /**
     * Retrieves the {@link EntityManagerFactory}.
     *
     * @return The {@link EntityManagerFactory} instance.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    private static final class DatabaseConfig {
        private static final String PERSISTENCE_UNIT = "png";
    }

    /**
     * Retrieves the week number for a given {@link OffsetDateTime}.
     *
     * @param dateTime The date and time to calculate the week number for.
     * @return The week number of the year.
     */
    static public int getWeekNumber(OffsetDateTime dateTime) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return dateTime.get(weekFields.weekOfYear());
    }

    /**
     * Returns a list containing the first and last day of the specified week.
     *
     * @param weekNumber The week number.
     * @param year       The year.
     * @return A list with the first and last day of the week as
     *         {@link OffsetDateTime}.
     */
    static public List<OffsetDateTime> getFirstLastDayOfWeek(int weekNumber, int year) {
        OffsetDateTime firstDayOfWeek = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());
        // Searching for monday
        while (firstDayOfWeek.getDayOfWeek().getValue() != 1) {
            firstDayOfWeek = firstDayOfWeek.plusDays(1);
        }
        firstDayOfWeek = firstDayOfWeek.plusWeeks(weekNumber - Utils.getWeekNumber(firstDayOfWeek));
        OffsetDateTime lastDayOfWeek = firstDayOfWeek.plusDays(6);
        ArrayList<OffsetDateTime> res = new ArrayList<>();
        res.add(firstDayOfWeek);
        res.add(lastDayOfWeek);
        return res;
    }

    /**
     * Checks if a creneau can be inserted into a list of creneaux for a day.
     *
     * @param creneau     The creneau to check.
     * @param creneauxDay The list of creneaux for the day, sorted by time.
     * @return {@code true} if the creneau can be inserted, {@code false} otherwise.
     */
    public static Boolean canInsertCreneau(Creneau creneau, List<Creneau> creneauxDay) {
        // We want to know if the creneau can be inserted in the already sorted list of
        // creneaux of the day
        // Check empty
        if (creneauxDay.isEmpty()) {
            return true;
        }
        // Check if the same hours are already taken
        for (Creneau c : creneauxDay) {
            if (c.getHeureDebut().isEqual(creneau.getHeureDebut()) || c.getHeureFin().isEqual(creneau.getHeureFin())) {
                return false;
            }
        }
        // Check if the creneau is before the first or after the last
        if (creneauxDay.getFirst().getHeureDebut().isAfter(creneau.getHeureFin())) {
            return true;
        }
        if (creneauxDay.getLast().getHeureFin().isBefore(creneau.getHeureDebut())) {
            return true;
        }
        // Check if the creneau is between two creneaux
        for (int i = 0; i < creneauxDay.size() - 1; i++) {
            Creneau c1 = creneauxDay.get(i);
            Creneau c2 = creneauxDay.get(i + 1);
            if (c1.getHeureFin().isBefore(creneau.getHeureDebut())
                    && c2.getHeureDebut().isAfter(creneau.getHeureFin())) {
                return true;
            }
        }
        return false;
    }
}
