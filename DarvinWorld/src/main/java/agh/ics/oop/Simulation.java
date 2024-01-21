package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Simulation implements Runnable{

    protected WorldMap map;
    private List<Animal> animals;
    boolean isPaused=false;

// w konstruktorze przekazujemy wszystkie parametry zczytane z okna startowego
    public Simulation(SimulationProperties simulationProperties,
                      MapChangeListener observers) {

        if (simulationProperties.hasTunnels()){
            this.map = new TunnelMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), observers);
        } else {
            this.map = new GlobeMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), observers);
        }

        animals = new ArrayList<>();

    }

    public Animal getAnimal(int x){ return this.animals.get(x); }
    public void setPause(){ this.isPaused= !isPaused;}
    public boolean getPause() { return this.isPaused;}
    public void run() {
        Boolean continuation=true;
        do {
//            TODO Update statystyk symulacji - trzeba ogarnąć ich wyświetlanie
            if (!getPause()) {
                dailyCycle();
                continuation=map.refreshMap();
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (continuation); // Wzrastanie nowych roślin na wybranych polach mapy. + sprawdzenie czy są jakieś zwierzęta (inaczej symulacja się kończy) + usunięcie martwych zwierząt

        System.out.println("Symulacja over");

    }

    protected void dailyCycle(){
        map.moveEveryAnimal();
        map.removeEatenPlants();
        map.procreate();
    }

//    tu pewnie będzie konieczna zmiana nazwy - metoda, która ma się wywoałać, kiedy po zatrzymaniu
//    symulacji użytkownik naciśnie odpowiedni przycisk
    public void highlightAnimalsWithMostPopularGenome(){
        ArrayList<Animal> animals = map.getAnimalsWithDominantGenotype();
//        TODO highlighting animals
    }

//    j.w. ale tym razem jak użytkownik zażyczy sobie podświetlenie pól najchętniej zarastanych
//    przez rośliny

    public void highlightPositionsPreferredByPlants(){

        ArrayList<PositionAbundance> positionsWithPlantCount = map.getPositionsPreferredByPlants();

        ArrayList<Vector2d> positions = new ArrayList<>();
        for (PositionAbundance p : positionsWithPlantCount){
            positions.add(p.position());
//            TODO highlight position (może nawet nie potrzeba tej arraylisty wektorów)
        }
    }

}

