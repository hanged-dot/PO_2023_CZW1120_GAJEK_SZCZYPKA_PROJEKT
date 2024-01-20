package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimulationStatisticsSaver {

    public void save(WorldMap worldMap, SimulationStatistics stats) {

        String message = constructMessage(stats);

        File file = new File("src/main/resources/statistics/map_"+worldMap.getID()+".csv");
        if(!file.exists() && !file.isDirectory()) {
            try{
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file,true);
                BufferedWriter fileWriterBuffer = new BufferedWriter(fileWriter);
                fileWriterBuffer.append("aliveAnimalCount,deadAnimalCount,plantCount,freePositionCount,meanAliveAnimalEnergy,meanAnimalLifeSpan,meanAliveAnimalOffspringCount,dominantGenotype,dominantAliveGenotype");
                fileWriterBuffer.newLine();
                fileWriterBuffer.flush();
                fileWriterBuffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter fileWriterBuffer = new BufferedWriter(fileWriter);
            fileWriterBuffer.append(message);
            fileWriterBuffer.newLine();
            fileWriterBuffer.flush();
            fileWriterBuffer.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        };
        System.out.println("zapisao");
    }

    private String constructMessage(SimulationStatistics statistics){

        String dominantGenotypeOfAliveAnimals = genomeToString(statistics.dominantAliveGenotype());
        String dominantGenotypeOfAllAnimals = genomeToString(statistics.dominantGenotype());

        return "%d,%d,%d,%d,%f,%f,%f,%s,%s".formatted(statistics.aliveAnimalCount(), statistics.deadAnimalCount(), statistics.plantCount(), statistics.freePositionCount(),
                statistics.meanAliveAnimalEnergy(), statistics.meanAnimalLifeSpan(), statistics.meanAliveAnimalOffspringCount(),  dominantGenotypeOfAliveAnimals, dominantGenotypeOfAllAnimals);
    }

    private String genomeToString(int[] genome){

        StringBuilder genomeString = new StringBuilder();

        for(int i : genome){
            genomeString.append("%d".formatted(i));
        }
        return genomeString.toString();
    }
}




