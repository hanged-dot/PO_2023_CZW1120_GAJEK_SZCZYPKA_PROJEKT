package agh.ics.oop.model;

public interface SimulationChangeListener {

    void aliveAnimalCountUpdate(boolean up);
    void deadAnimalCountUpdate(int age);
    void plantCountUpdate(boolean up);
    void freePositionCountUpdate(int count);
    void allGenotypeCountUpdate(int[] genotype, boolean up);
    void aliveGenotypeCountUpdate(int[] genotype, boolean up);
    void totalKidsCountUpdate(int kids);

}
