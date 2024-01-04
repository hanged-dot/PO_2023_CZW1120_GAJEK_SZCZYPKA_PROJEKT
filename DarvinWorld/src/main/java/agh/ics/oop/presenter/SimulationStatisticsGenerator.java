package agh.ics.oop.presenter;

import agh.ics.oop.model.MapProperties;
import agh.ics.oop.model.SimulationChangeListener;
import agh.ics.oop.model.SimulationStatistics;

import javax.management.AttributeChangeNotification;
import java.util.HashMap;

import static java.lang.Math.max;

public class SimulationStatisticsGenerator implements SimulationChangeListener {

    private int aliveAnimalCount;
    private int deadAnimalCount;
    private int deadAnimalLifeSpan;
    private int plantCount;
    private int freePositionCount;
    private int totalAliveAnimalOffspringCount;
    private HashMap<int[], Integer> allGenotypes;
    private HashMap<int[], Integer> aliveGenotypes;

    public SimulationStatisticsGenerator(MapProperties properties){
        aliveAnimalCount = properties.startAnimalCount();
        deadAnimalCount = 0;
        deadAnimalLifeSpan = 0;
        plantCount = 0;
        freePositionCount = properties.mapWidth()*properties.mapHeight();
        totalAliveAnimalOffspringCount = 0;
        allGenotypes = new HashMap<>();
        aliveGenotypes = new HashMap<>();
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
    public void freePositionCountUpdate(boolean up) {
        if (up) ++ freePositionCount;
        else --freePositionCount;
    }

    @Override
    public void allGenotypeCountUpdate(int[] genotype, boolean up) {
        genotypeCountUpdate(genotype, allGenotypes, up);
        genotypeCountUpdate(genotype, aliveGenotypes, up);
    }

    // na razie bierze pod uwagę wszystkie genotypy, dorobię jeszcz osobno dla żyjących genotypów
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
//        WAŻNE PYTANIE: liczymy dla każdego zwierzaka liczbę dzieci, czy jedno dziecko na parę?
        totalAliveAnimalOffspringCount = max(0, totalAliveAnimalOffspringCount + kidsCount);
    }

    private float getMeanLifeSpan(){
        if (deadAnimalCount != 0){
            return (float) deadAnimalLifeSpan /deadAnimalCount;
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

    public SimulationStatistics generateSimulationStatics(){

        return new SimulationStatistics(
                aliveAnimalCount,
                deadAnimalCount,
                plantCount,
                freePositionCount,
                getDominantGenotype(allGenotypes),
                getDominantGenotype(aliveGenotypes),
                getMeanLifeSpan(),
                getMeanAliveAnimalOffspringCount()
        );

    }

}
