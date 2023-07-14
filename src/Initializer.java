import enums.ToolType;
import models.Tool;
import models.ToolCharges;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Initializer {
    public static List<Tool> getTools()
    {
        List<Tool> tools = new ArrayList<>() {
            {
                add(new Tool("CHNS", ToolType.CHAINSAW, "Stihl"));
                add(new Tool("LADW", ToolType.LADDER, "Werner"));
                add(new Tool("JAKD", ToolType.JACKHAMMER, "DeWalt"));
                add(new Tool("JAKR", ToolType.JACKHAMMER, "Rigid"));
            }
        };

        return tools;
    }

    public static List<ToolCharges> getToolCharges()
    {
        List<ToolCharges> toolCharges = new ArrayList<>() {
            {
                add(new ToolCharges(ToolType.LADDER, BigDecimal.valueOf(1.99), true, true, false));
                add(new ToolCharges(ToolType.CHAINSAW, BigDecimal.valueOf(1.49), true, false, true));
                add(new ToolCharges(ToolType.JACKHAMMER, BigDecimal.valueOf(2.99), true, false, false));
            }
        };

        return toolCharges;
    }
}
