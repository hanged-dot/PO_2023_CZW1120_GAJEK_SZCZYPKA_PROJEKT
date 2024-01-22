package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.presenter.SimulationPresenter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Simulation implements Runnable{

    protected WorldMap map;
    private List<Animal> animals;
    boolean isPaused=false;
    boolean end =false;

    int executorServiceThreadID;

    boolean isPaused;
    boolean simulating;

    SimulationPresenter simPresenter;

// w konstruktorze przekazujemy wszystkie parametry czytane z okna startowego
    public Simulation(SimulationProperties simulationProperties){

        simulating = true;
        simPresenter = new SimulationPresenter(this); // because java doesn't support basic objective programming language tools we have to make code more dirty in order to simply notify Simulations of happening things instead of implementing observator for every button action

        if (simulationProperties.hasTunnels()){
            this.map = new TunnelMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), simPresenter);
        } else {
            this.map = new GlobeMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), simPresenter);
        }

        animals = new ArrayList<>();
    }

    public Animal getAnimal(int x){
        return this.animals.get(x);
    }

    public void run() {

//        Codziennie będzie wywoływana metoda map.refreshMap(), która zwraca true, jeśli symulacja może być kontynuowana, albo false, jeśli się okaże, że nie ma już żadnych zwierząt

        while(simulating) {

            if(!isPaused) {
                do {
                    dailyCycle();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (map.refreshMap() && !isPaused && simulating); // Wzrastanie nowych roślin na wybranych polach mapy. + sprawdzenie czy są jakieś zwierzęta (inaczej symulacja się kończy) + usunięcie martwych zwierząt
            }
            else {
                try {
                    sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        SimulationEngine.getInstance().threadFinished(getThreadId());
        System.out.println("Simulation over");

    }

    protected void dailyCycle(){
        map.moveEveryAnimal();
        map.removeEatenPlants();
        map.procreate();
    }

    public void setThreadID(int ID) {
        executorServiceThreadID = ID;
    }

    public int getThreadId(){
        return executorServiceThreadID;
    }

    public void pause() {
        if(!isPaused) {
            simPresenter.pauseUpdate();

            isPaused = true;
        }
    }

    public void resume() {
        if(isPaused) {
            simPresenter.resumeUpdate();

            isPaused = false;
        }
    }

    public void terminate() {
        simulating = false;
    }
}

