package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Simulation implements Runnable{

    protected WorldMap map;
    private List<Animal> animals;

// w konstruktorze przekazujemy wszystkie parametry czytane z okna startowego
    public Simulation(SimulationProperties simulationProperties){

        if (simulationProperties.hasTunnels()){
            this.map = new TunnelMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties());
        } else {
            this.map = new GlobeMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties());
        }
        animals = new ArrayList<>();
    }

    public Animal getAnimal(int x){
        return this.animals.get(x);
    }

    public void run() {

//        Codziennie będzie wywoływana metoda map.refreshMap(), która zwraca true, jeśli symulacja może być kontynuowana, albo false, jeśli się okaże, że nie ma już żadnych zwierząt

        do {
            dailyCycle();
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (map.refreshMap()); // Wzrastanie nowych roślin na wybranych polach mapy. + sprawdzenie czy są jakieś zwierzęta (inaczej symulacja się kończy) + usunięcie martwych zwierząt

        System.out.println("Simulation over");

    }

    protected void dailyCycle(){
        map.moveEveryAnimal();
        map.removeEatenPlants();
        map.procreate();
    }

}

