package model;

public record MapProperties(

        int mapWidth,
        int mapHeight,
        int startPlantCount,
        int dailyPlantCount,
        int startAnimalCount,
        boolean tunnelMode,
        int tunnelCount) {
}
