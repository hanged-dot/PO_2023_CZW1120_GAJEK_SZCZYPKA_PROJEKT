package agh.ics.oop.model;

public record MapProperties(

        int mapWidth,
        int mapHeight,
        int startPlantCount,
        int dailyPlantCount,
        int energyFromPlant,
        int startAnimalCount,
        boolean tunnelMode,
        int tunnelCount) {
}
