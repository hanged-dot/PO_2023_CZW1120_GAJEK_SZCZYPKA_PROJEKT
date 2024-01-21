package agh.ics.oop.model;

import agh.ics.oop.model.*;

import javax.management.AttributeChangeNotification;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class SimulationStatisticsGenerator implements SimulationChangeListener {

    private int aliveAnimalCount;
    private int deadAnimalCount;
    private int deadAnimalLifeSpan;
    private int plantCount;
    private int allPositionCount;
    private int freePositionCount;
    private final int totalPositionCount;
    private int totalAliveAnimalOffspringCount;
    private int totalEnergy;
    private int energyFromPlant;
    private HashMap<int[], Integer> allGenotypes;
    private HashMap<int[], Integer> aliveGenotypes;
    private HashMap<Vector2d, Integer> plantHistory;

    public SimulationStatisticsGenerator(MapProperties properties, AnimalProperties animalProperties){
        aliveAnimalCount = properties.startAnimalCount();
        deadAnimalCount = 0;
        deadAnimalLifeSpan = 0;
        plantCount = 0;
        allPositionCount = properties.mapWidth()*properties.mapHeight();
        freePositionCount = allPositionCount;
        totalPositionCount = properties.mapWidth()*properties.mapHeight();
        totalAliveAnimalOffspringCount = 0;
        allGenotypes = new HashMap<>();
        aliveGenotypes = new HashMap<>();
        plantHistory = new HashMap<>();
        totalEnergy = aliveAnimalCount*animalProperties.startAnimalEnergy();
        energyFromPlant = animalProperties.energyFromPlant();
    }

    @Override
    public void aliveAnimalCountUpdate(boolean up) {
        if(up) ++aliveAnimalCount;
        else --aliveAnimalCount;
    }

    @Override
    public void deadAnimalCountUpdate(int age) {
        ++deadAnimalCount;
        deadAnimalLifeSpan += age;
        aliveAnimalCountUpdate(false);
    }

    @Override
    public void plantCountUpdate(boolean up) {
        if (up) ++plantCount;
        else --plantCount;
    }

    @Override
    public void freePositionCountUpdate(int count) {
        this.freePositionCount = allPositionCount - count;
    }

    public void plantHistoryUpdate(Vector2d position){
        if (!plantHistory.containsKey(position)){
            plantHistory.put(position, 1);
        }
        else {
            Integer recentPlantCount = plantHistory.remove(position);
            plantHistory.put(position, recentPlantCount + 1);
        }
    }

    public void totalEnergyUpdate(boolean up){

        if (up){
            totalEnergy += energyFromPlant;
        } else {
            totalEnergy -= aliveAnimalCount;
        }
    }

    public ArrayList<PositionAbundance> generatePreferredPlantPositions(){

        int preferredPositionsCount = min(plantHistory.size(),
                (int)(0.1 * (float)totalPositionCount));

        ArrayList<PositionAbundance> positionAbundanceArrayList = new ArrayList<>();

//        iterujemy po wszystkich polach na których rosły kiedykolwiek rośliny
        for (var plantHist : plantHistory.entrySet())
        {
//            tworzymy rekord zawierający pozycję i ilość roślin, które na niej wyrosły
            var plantPositionAbundance = new PositionAbundance(plantHist.getKey(), plantHist.getValue());

//            jeśli w liście są jeszcze miejsca na nowe pozycje, zapełniamy ją
            if(positionAbundanceArrayList.size() < preferredPositionsCount)
            {
                positionAbundanceArrayList.add(plantPositionAbundance);
            }
//            w przeciwnym wypadku sprawdzamy, czy dana pozycja jest bardziej preferowana niz któraś z pozycji na liście
            else
            {
//                w lowestIndex szukamy najmniej preferowanej pozycji z naszej wynikowej listy
                int lowestIndex = 0;

                for(int i = lowestIndex + 1; i < preferredPositionsCount; ++i)
                {
                    if(positionAbundanceArrayList.get(lowestIndex).numberOfPlants() >
                            positionAbundanceArrayList.get(i).numberOfPlants())
                    {
                        lowestIndex = i;
                    }
                }

                if(plantHist.getValue() > positionAbundanceArrayList.get(lowestIndex).numberOfPlants()) {
                    positionAbundanceArrayList.remove(lowestIndex);
                    positionAbundanceArrayList.add(plantPositionAbundance);
                }
            }
        }
        return positionAbundanceArrayList;
    }

    @Override
    public void allGenotypeCountUpdate(int[] genotype, boolean up) {
        genotypeCountUpdate(genotype, allGenotypes, up);
        genotypeCountUpdate(genotype, aliveGenotypes, up);
    }
    public void aliveGenotypeCountUpdate(int[] genotype, boolean up){
        genotypeCountUpdate(genotype, aliveGenotypes, up);
    }

    private void genotypeCountUpdate(int[] genotype, HashMap<int[], Integer> genotypes, boolean up) {
        if (up){
            if (genotypes.containsKey(genotype)){
                int n = genotypes.get(genotype);
                genotypes.put(genotype, n + 1);
            } else {
                genotypes.put(genotype, 1);
            }
        } else {
            int n = genotypes.get(genotype);
            genotypes.put(genotype, n - 1);
        }

    }

    @Override
    public void totalKidsCountUpdate(int kidsCount) {
        totalAliveAnimalOffspringCount = max(0, totalAliveAnimalOffspringCount + kidsCount);
    }

    public void newbornAnimalUpdate(Animal newbornAnimal){
        totalKidsCountUpdate(2);
        aliveAnimalCountUpdate(true);
        allGenotypeCountUpdate(newbornAnimal.getGenome(), true);
    }

    public void deadAnimalUpdate(Animal deadAnimal){
        deadAnimalCountUpdate(deadAnimal.getAge());
        aliveGenotypeCountUpdate(deadAnimal.getGenome(), false);
        totalKidsCountUpdate((-1)*deadAnimal.getNumberOfChildren());
    }

    private float getMeanLifeSpan(){
        if (deadAnimalCount != 0){
            return (float) deadAnimalLifeSpan / deadAnimalCount;
        } else {
            return 0;
        }
    }

    private int[] getDominantGenotype(HashMap<int[], Integer> genotypes){

        int[] genotype = new int[0];
        int maxCount = 0;

        for (int[] g: genotypes.keySet()){
            if (genotypes.get(g) > maxCount){
                genotype = g;
            }
        }
        return genotype;
    }

    private float getMeanAliveAnimalOffspringCount(){
        return (float) totalAliveAnimalOffspringCount/aliveAnimalCount;
    }

    private float getMeanAliveEnergy(){
        return (float)this.totalEnergy/(float)this.aliveAnimalCount;
    }

    public SimulationStatistics generateSimulationStatics(){

        return new SimulationStatistics(
                aliveAnimalCount,
                deadAnimalCount,
                plantCount,
                freePositionCount,
                getDominantGenotype(allGenotypes),
                getDominantGenotype(aliveGenotypes),
                getMeanAliveEnergy(),
                getMeanLifeSpan(),
                getMeanAliveAnimalOffspringCount()
        );
    }
}
