package agh.ics.oop.model;

public record AnimalProperties(
        int startAnimalEnergy,
        int energyFromPlant,
        int minProcreateEnergy,
        int procreateEnergy,
        int minMutationCount,
        int maxMutationCount,
        int genomeLength,
        boolean withLightMutationCorrect

) {
}
