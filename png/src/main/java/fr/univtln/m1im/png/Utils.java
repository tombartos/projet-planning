package fr.univtln.m1im.png;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

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
