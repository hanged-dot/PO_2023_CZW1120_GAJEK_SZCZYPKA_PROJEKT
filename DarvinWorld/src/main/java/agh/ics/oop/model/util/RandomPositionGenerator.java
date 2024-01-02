package agh.ics.oop.model.util;


import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {
    private final ArrayList<Vector2d> result = new ArrayList<>();
    private int index=0;
    public RandomPositionGenerator(double width,double height, int n){
        ArrayList<Vector2d> arr = new ArrayList<>();
        for (int i = 0; i <width; i++) {
            for (int j =0;j<height;j++){
                arr.add(new Vector2d(i,j));
            }
        }
        Collections.shuffle(arr);

        for (int i = 0; i<n;i++){
            result.add(arr.get(i));
        }
    }

    @Override
    public Iterator<Vector2d> iterator() { return this; }

    @Override
    public boolean hasNext() {
        if (index< result.size()){return true;}
        return false;
    }

    @Override
    public Vector2d next() {
        index+=1;
        return result.get(index-1);
    }
}
