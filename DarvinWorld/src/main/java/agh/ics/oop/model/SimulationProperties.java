package agh.ics.oop.model;

import java.io.Serializable;

public record SimulationProperties (
        MapProperties mapProperties,
        AnimalProperties animalProperties,
        boolean hasTunnels,
        boolean hasLightMutationCorrect,
        boolean saveStatistics
) implements Serializable{
}
