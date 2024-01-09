package agh.ics.oop.model;

public record SimulationProperties(
        MapProperties mapProperties,
        AnimalProperties animalProperties,
        boolean hasTunnels,
        boolean hasLightMutationCorrect,
        boolean saveStatistics
) {
}
