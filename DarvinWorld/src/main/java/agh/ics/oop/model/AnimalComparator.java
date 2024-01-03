package agh.ics.oop.model;

import java.util.Comparator;

public class AnimalComparator implements Comparator<model.Animal> {

    @Override
    public int compare(model.Animal a1, Animal a2) {
        return a1.greaterThan(a2);
    }
}
