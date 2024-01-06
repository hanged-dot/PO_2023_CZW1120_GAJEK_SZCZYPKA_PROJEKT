package agh.ics.oop.model;

public interface WorldMap {

    void removeDeadAnimals();
    void moveEveryAnimal();
    void removeEatenPlants();
    void procreate();
    boolean refreshMap();
    Boundary getCurrentBounds();
}
