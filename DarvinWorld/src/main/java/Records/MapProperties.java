package Records;

import java.io.Serializable;

public record MapProperties(
        int mapWidth,
        int mapHeight,
        int startPlantCount,
        int dailyPlantCount,
        int startAnimalCount,
        boolean tunnelMode,
        int tunnelCount) implements Serializable {
}
