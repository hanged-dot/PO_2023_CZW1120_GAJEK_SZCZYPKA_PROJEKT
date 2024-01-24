package Simulation;



import Presenter.SimulationPresenter;
import Records.SimulationProperties;
import WordMap.GlobeMap;
import WordMap.TunnelMap;
import WordMap.WorldMap;
import WorldElement.Animal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Simulation implements Runnable{

    protected WorldMap map;
    private List<Animal> animals;
    boolean isPaused=false;
    int executorServiceThreadID;
    boolean simulating;

    SimulationPresenter simPresenter;

// w konstruktorze przekazujemy wszystkie parametry czytane z okna startowego
    public Simulation(SimulationProperties simulationProperties){

        simulating = true;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationpresenter.fxml"));

        BorderPane viewRoot;
        try {
            viewRoot = loader.load();
            var scene = new Scene(viewRoot);
            stage.setScene(scene);
            stage.setTitle("Simulation app");
            stage.minWidthProperty().bind(viewRoot.minWidthProperty());
            stage.minHeightProperty().bind(viewRoot.minHeightProperty());
            simPresenter = loader.getController();
            simPresenter.setSimulation(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    public void terminateAndClose(){
        simPresenter.closeSimulation();
    }
}

