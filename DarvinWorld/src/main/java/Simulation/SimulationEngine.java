package Simulation;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private static ArrayList<Simulation> sims;
    ExecutorService executorService;

    private static SimulationEngine instance;

    public static SimulationEngine getInstance()
    {
        if(instance == null){
            sims = new ArrayList<>();
            instance = new SimulationEngine(4);
        }

        return instance;
    }

    private SimulationEngine(int threadPoolCount)
    {
        executorService = Executors.newFixedThreadPool(threadPoolCount);
    }

    public void runSimAsyncInThreadPool(Simulation sim){

        sim.setThreadID(sims.size());

        executorService.submit(sim);
        sims.add(sim);
    }

    public void threadFinished(int threadId) {
        sims.remove(threadId);
    }
}
