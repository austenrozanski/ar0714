package helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class DateHelper {

    public static boolean isDateInMonth(LocalDate date, Month month)
    {
        return date.getMonth().equals(month);
    }

    public static boolean isDateOnDayOfWeek(LocalDate date, DayOfWeek dayofWeek)
    {
        return date.getDayOfWeek().equals(dayofWeek);
    }

    public static boolean isDateOnWeekend(LocalDate date)
    {
        return isDateOnDayOfWeek(date, DayOfWeek.SATURDAY) || isDateOnDayOfWeek(date, DayOfWeek.SUNDAY);
    }

    public static boolean isDateOnWeekday(LocalDate date)
    {
        return !isDateOnWeekend(date);
    }
}
