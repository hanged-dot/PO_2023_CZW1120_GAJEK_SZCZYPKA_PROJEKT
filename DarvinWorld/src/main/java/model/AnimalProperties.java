package model;

public record AnimalProperties(
        int startAnimalEnergy,
        int minProcreateEnergy,
        int procreateEnergy,
        int minMutationCount,
        int maxMutationCount,
        int genomeLength,
        boolean withLightMutationCorrect
) {
}
