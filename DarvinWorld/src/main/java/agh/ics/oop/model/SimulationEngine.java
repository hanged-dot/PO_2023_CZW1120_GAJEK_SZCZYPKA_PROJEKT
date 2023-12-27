package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine{
    private ArrayList<Simulation> sims;
    List<Thread> tasks = new ArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    public SimulationEngine (ArrayList<Simulation> simulations){
        this.sims=simulations;
    }
    public void runSync(){
        for( int i=0; i<sims.size();i++){
            sims.get(i).run();
        }
    }

    public void runAsync(){
        for( int i=0; i<sims.size();i++){
            tasks.add((new Thread(sims.get(i))));
            tasks.get(i).start();

        }

    }

    public void awaitSimulationsEnd() {
        for (Thread thread : tasks){
            try{thread.join();} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        }
        executorService.shutdown();
        try {executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void runAsyncInThreadPool(){

        for( int i=0; i<sims.size();i++){
            executorService.submit(sims.get(i));
        }



    }

}
