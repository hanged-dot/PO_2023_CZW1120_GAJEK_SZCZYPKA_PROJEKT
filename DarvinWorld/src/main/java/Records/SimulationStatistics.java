package Records;

public record SimulationStatistics(
        int day,
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
