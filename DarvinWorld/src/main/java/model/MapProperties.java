package model;

public record MapProperties(

        boolean tunnelMode,
        int mapWidth,
        int mapHeight,
        int startPlantCount,
        int dailyPlantCount,
        int startAnimalCount) {
}
