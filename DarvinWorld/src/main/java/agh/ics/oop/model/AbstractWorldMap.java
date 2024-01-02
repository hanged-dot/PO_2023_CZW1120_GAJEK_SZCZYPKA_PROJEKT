package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap{
    Map<Vector2d, Animal> animals = new HashMap<>();
    ArrayList<MapChangeListener> observers = new ArrayList<>();
    Boundary bounds;

    UUID identifier;
    @Override
    public void place(Animal animal) throws PositionAlreadyOccupiedException{
        if (canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(),animal);
            mapChanged("Zwierzę zostało umieszczone na mapie na pozycji" + animal.getPosition());
        }
        else {
            throw new PositionAlreadyOccupiedException(animal.getPosition());
        }
    }

    @Override
    public void move(Animal animal, MoveDirection direction){
        Vector2d pos1= animal.getPosition();
        animals.remove(animal.getPosition());
        animal.move(direction, this);
        animals.put(animal.getPosition(), animal);
        Vector2d pos2 = animal.getPosition();
        mapChanged("Zwierzę poruszyło się z pozycji "+ pos1 + " na pozycję " +pos2);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (objectAt(position)!=null){return true;}
        return false;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (animals.get(position) != null) return animals.get(position);
        return null;
    }

    @Override
    public Collection <WorldElement> getElements(){
        ArrayList<WorldElement> elements = new ArrayList<>();
        for (WorldElement value: animals.values()){
            elements.add(value);
        }
        return elements;
    }
    @Override
    public Boundary getCurrentBounds(){return bounds;}

    @Override
    public UUID getID() {return identifier;}

    @Override
    public String toString(){
        MapVisualizer map = new MapVisualizer(this);
        return map.draw(this.getCurrentBounds().lowerLeft(), this.getCurrentBounds().upperRight());
    }

    public void addObserver(MapChangeListener observer){
        this.observers.add(observer);
    }
    public void removeObserver(MapChangeListener observer){
        this.observers.remove(observer);
    }

    public void mapChanged(String changeInfo){
        for (int i=0; i <observers.size();i++) {
            observers.get(i).mapChanged(this, changeInfo);
        }
    }

}
