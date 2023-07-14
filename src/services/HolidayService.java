package services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static helpers.DateHelper.isDateInMonth;
import static helpers.DateHelper.isDateOnDayOfWeek;

public class HolidayService {

    public boolean isObservedHoliday(LocalDate date)
    {
        boolean isIndependenceDay = isObservedIndependenceDay(date);
        if (isIndependenceDay) return true;

        boolean isLaborDay = isLaborDay(date);
        if (isLaborDay) return true;

        return false;
    }

    public boolean isObservedIndependenceDay(LocalDate date)
    {
        // Observed Independence Day must be in July and on the 3rd, 4th, or 5th
        if (!isDateInMonth(date, Month.JULY)) return false;
        if (date.getDayOfMonth() != 3 && date.getDayOfMonth() != 4 && date.getDayOfMonth() != 5) return false;

        LocalDate independenceDay = LocalDate.of(date.getYear(), Month.JULY, 4);

        int observedDayOfMonth = 4;

        // If Independence Day falls on Saturday, observe the holiday on the 3rd (Friday)
        if (isDateOnDayOfWeek(independenceDay, DayOfWeek.SATURDAY))
        {
            observedDayOfMonth = 3;
        }

        // If Independence Day falls on Sunday, observe the holiday on the 5th (Monday)
        if (isDateOnDayOfWeek(independenceDay, DayOfWeek.SUNDAY))
        {
            observedDayOfMonth = 5;
        }

        return date.getDayOfMonth() == observedDayOfMonth;
    }

    public boolean isLaborDay(LocalDate date)
    {
        boolean isSeptember = isDateInMonth(date, Month.SEPTEMBER);
        boolean isFirstMondayOfMonth = isDateOnDayOfWeek(date, DayOfWeek.MONDAY) && date.getDayOfMonth() <= 7;

        return isSeptember && isFirstMondayOfMonth;
    }
}
