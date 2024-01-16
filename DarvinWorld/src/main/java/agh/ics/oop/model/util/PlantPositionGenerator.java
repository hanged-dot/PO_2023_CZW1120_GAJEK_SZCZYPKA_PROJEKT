package agh.ics.oop.model.util;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Plant;
import agh.ics.oop.model.Vector2d;

import java.util.*;

import static java.lang.Integer.sum;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class PlantPositionGenerator {

    private Boundary equatorBoundary;
    private ArrayList<Vector2d> equatorPositions;
    private Boundary northernSteppeBoundary;
    private ArrayList<Vector2d> northernSteppePositions;
    private Boundary southernSteppeBoundary;
    private ArrayList<Vector2d> southernSteppePositions;
    ArrayList<Vector2d> positions = new ArrayList<>();
    public PlantPositionGenerator(Boundary boundary){

        equatorBoundary = new Boundary(boundary.leftX(), boundary.rightX(),
                (int) round(boundary.upperY()*0.4), (int) round(boundary.upperY()*0.6));

        northernSteppeBoundary = new Boundary(boundary.leftX(), boundary.rightX(),
                equatorBoundary.upperY() + 1, boundary.upperY());

        southernSteppeBoundary = new Boundary(boundary.leftX(), boundary.rightX(),
                0, equatorBoundary.lowerY() - 1);


        equatorPositions = new ArrayList<>();
        northernSteppePositions = new ArrayList<>();
        southernSteppePositions = new ArrayList<>();

        for (int i = boundary.leftX(); i <= boundary.rightX(); i++){

            for (int j = equatorBoundary.lowerY(); j <= equatorBoundary.upperY(); j++){
                equatorPositions.add(new Vector2d(i, j));
            }

            for (int j = northernSteppeBoundary.lowerY(); j <= northernSteppeBoundary.upperY(); j++){
                northernSteppePositions.add(new Vector2d(i, j));
            }

            for (int j = southernSteppeBoundary.lowerY(); j <= southernSteppeBoundary.upperY(); j++){
                southernSteppePositions.add(new Vector2d(i, j));
            }
        }
    }
//      zmiana hashsetu z typu vector na plant
    private void generatePositions(HashMap<Vector2d,Plant> takenPositions, int positionCount){

        Random random = new Random();

        int equatorCtr = 0;
        int northCtr = 0;
        int southCtr = 0;

        Collections.shuffle(equatorPositions);
        Collections.shuffle(northernSteppePositions);
        Collections.shuffle(southernSteppePositions);

//        jeśli na mapie nie ma wystarczająco dużo wolnych miejsc, zajmiemy tylko taką ilość miejsc, jaka jest dostępna
        positionCount = min(positionCount,
                sum(sum(equatorPositions.size(), northernSteppePositions.size()), southernSteppePositions.size()) -
                takenPositions.size());

        while (positionCount > 0){
//nie wiem czy to bedzie dizalac
            int temp = random.nextInt(0,9);
            if (temp <= 7 && equatorCtr < equatorPositions.size()){
                if (!takenPositions.containsKey(equatorPositions.get(equatorCtr))) {
                    positions.add(equatorPositions.get(equatorCtr));
                    ++equatorCtr;
                }
            } else if (temp == 8 && northCtr < northernSteppePositions.size()){
                if (!takenPositions.containsKey(northernSteppePositions.get(northCtr))){
                    positions.add(northernSteppePositions.get(northCtr));
                    ++northCtr;
                }
            } else if (temp == 9 && southCtr < southernSteppePositions.size()) {
                if (!takenPositions.containsKey(southernSteppePositions.get(southCtr))){
                    positions.add(southernSteppePositions.get(southCtr));
                    ++southCtr;
                }

            }

        }

    }

    public ArrayList<Vector2d> getPositions(HashMap<Vector2d,Plant> takenPositions, int positionCount){
        generatePositions(takenPositions, positionCount);
        return positions;
    }

}
