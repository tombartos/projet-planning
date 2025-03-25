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

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private static EntityManagerFactory emf;
    
    static public void initconnection(String user, String password){
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

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    private static final class DatabaseConfig {
        private static final String PERSISTENCE_UNIT = "png";
    }

    static public int getWeekNumber(OffsetDateTime dateTime) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return dateTime.get(weekFields.weekOfYear());
    }

    /**
     * Returns a list containing the first and last day of the week
     * @param weekNumber
     * @param year
     */
    static public List<OffsetDateTime> getFirstLastDayOfWeek(int weekNumber, int year){
        OffsetDateTime firstDayOfWeek = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());
        //Searching for monday
        while(firstDayOfWeek.getDayOfWeek().getValue() != 1){
            firstDayOfWeek = firstDayOfWeek.plusDays(1);
        }
        firstDayOfWeek = firstDayOfWeek.plusWeeks(weekNumber - Utils.getWeekNumber(firstDayOfWeek));
        OffsetDateTime lastDayOfWeek = firstDayOfWeek.plusDays(6);
        ArrayList<OffsetDateTime> res = new ArrayList<>();
        res.add(firstDayOfWeek);
        res.add(lastDayOfWeek);
        return res;
    }
    
}
