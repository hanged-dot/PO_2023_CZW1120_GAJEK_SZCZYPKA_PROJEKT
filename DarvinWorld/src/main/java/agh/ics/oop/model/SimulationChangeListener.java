package agh.ics.oop.model;

public interface SimulationChangeListener {

    public void aliveAnimalCountUpdate(boolean up);
    public void deadAnimalCountUpdate(int age);
    void plantCountUpdate(boolean up);
    public void freePositionCountUpdate(int count);
    public void allGenotypeCountUpdate(int[] genotype, boolean up);
    public void aliveGenotypeCountUpdate(int[] genotype, boolean up);
    public void totalKidsCountUpdate(int kids);

}
