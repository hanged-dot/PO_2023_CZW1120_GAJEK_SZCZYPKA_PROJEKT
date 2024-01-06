package agh.ics.oop.model;

import java.util.ArrayList;

public interface WorldMap {

    void removeDeadAnimals();
    void moveEveryAnimal();
    void removeEatenPlants();
    void procreate();
    boolean refreshMap();
    Boundary getCurrentBounds();
    SimulationStatistics getSimulationStatistics();
    ArrayList<Animal> getAnimalsWithDominantGenotype();
    ArrayList<PositionAbundance> getPositionsPreferredByPlants();
}
