package WordMap;

import Records.Boundary;
import Records.PositionAbundance;
import Records.SimulationStatistics;
import WorldElement.Animal;
import WorldElement.WorldElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public interface WorldMap {
    HashMap<Vector2d, LinkedList<Animal>> getAnimals();
    void removeDeadAnimals();
    void moveEveryAnimal();
    void removeEatenPlants();
    void procreate();
    boolean refreshMap();
    Boundary getCurrentBounds();
    SimulationStatistics getSimulationStatistics();
    ArrayList<Animal> getAnimalsWithDominantGenotype();
    ArrayList<PositionAbundance> getPositionsPreferredByPlants();
    public WorldElement getStrongest(Vector2d position);
    public WorldElement getPlant(Vector2d position);
    UUID getID();
    int getDay();
    void addAnimalObserver(Animal animal);
}
