package model;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal a1, Animal a2) {
        return a1.greaterThan(a2);
    }
}
