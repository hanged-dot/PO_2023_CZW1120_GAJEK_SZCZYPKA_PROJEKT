package agh.ics.oop.model.util;

import java.util.Random;

public class RandomNumberGenerator {
private int[] genome;
    public RandomNumberGenerator(int lenght){
        genome= new int[lenght];
        for (int i=0; i<lenght;i++){
            genome[i]=(new Random()).nextInt(8);
        }
    }
}
