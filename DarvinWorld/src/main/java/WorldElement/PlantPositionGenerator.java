package WorldElement;



import Records.Boundary;
import WordMap.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

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

        positions = new ArrayList<>();

        Random random = new Random();

        int equatorCtr = 0;
        int northCtr = 0;
        int southCtr = 0;

        Collections.shuffle(equatorPositions);
        Collections.shuffle(northernSteppePositions);
        Collections.shuffle(southernSteppePositions);

//        jeśli na mapie nie ma wystarczająco dużo wolnych miejsc, zajmiemy tylko taką ilość miejsc, jaka jest dostępna
        positionCount = min(positionCount,
                sum((sum(equatorPositions.size(), northernSteppePositions.size())), southernSteppePositions.size()) - takenPositions.size());


        while (positionCount > 0){

            int temp;
            if (equatorCtr < equatorPositions.size()){
                temp = random.nextInt(0, 10);
            } else {
                temp = random.nextInt(8, 10);
            }

            if (temp <= 7){

                while (equatorCtr < equatorPositions.size() && takenPositions.containsKey(equatorPositions.get(equatorCtr))){
                    ++equatorCtr;
                }
                if (equatorCtr < equatorPositions.size()) {
                    positions.add(equatorPositions.get(equatorCtr));
                    --positionCount;
                    ++equatorCtr;
                }

            } else if (temp == 8 && northCtr < northernSteppePositions.size()){

                while (northCtr < northernSteppePositions.size() && takenPositions.containsKey(northernSteppePositions.get(northCtr))){
                    ++northCtr;
                }
                if (northCtr < northernSteppePositions.size()) {
                    positions.add(northernSteppePositions.get(northCtr));
                    --positionCount;
                    ++northCtr;
                }


            } else if (temp == 9 && southCtr < southernSteppePositions.size()) {

                while (southCtr < southernSteppePositions.size() && takenPositions.containsKey(southernSteppePositions.get(southCtr))){
                    ++southCtr;
                }
                if (southCtr < southernSteppePositions.size()) {
                    positions.add(southernSteppePositions.get(southCtr));
                    --positionCount;
                    ++southCtr;
                }

            }
            if (equatorCtr >= equatorPositions.size() && northCtr >= northernSteppePositions.size() && southCtr >= southernSteppePositions.size()){
                break;
            }

        }
    }

    public ArrayList<Vector2d> getPositions(HashMap<Vector2d,Plant> takenPositions, int positionCount){
        generatePositions(takenPositions, positionCount);
        return positions;
    }

    public Boundary getEquatorBoundary() {
        return equatorBoundary;
    }
}
