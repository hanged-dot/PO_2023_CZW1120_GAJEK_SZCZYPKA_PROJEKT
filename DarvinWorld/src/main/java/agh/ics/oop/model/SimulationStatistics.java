package agh.ics.oop.model;

public record SimulationStatistics(

        int aliveAnimalCount,
        int deadAnimalCount,
        int plantCount,
        int freePositionCount,
        int[] dominantGenotype,
        int[] dominantAliveGenotype,
        float meanAnimalLifeSpan,
        float meanAliveAnimalOffspringCount
) {
}