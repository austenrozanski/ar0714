package models;

import enums.ToolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tool {
    private String code;
    private ToolType type;
    private String brand;
}
