package Records;

public record SimulationStatistics(

        int aliveAnimalCount,
        int deadAnimalCount,
        int plantCount,
        int freePositionCount,
        int[] dominantGenotype,
        int[] dominantAliveGenotype,
        float meanAliveAnimalEnergy,
        float meanAnimalLifeSpan,
        float meanAliveAnimalOffspringCount
) {
}
