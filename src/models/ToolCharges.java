package models;

import enums.ToolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ToolCharges {
    private ToolType type;
    private BigDecimal dailyCharges;
    private boolean chargedOnWeekdays;
    private boolean chargedOnWeekends;
    private boolean chargedOnHolidays;
}
