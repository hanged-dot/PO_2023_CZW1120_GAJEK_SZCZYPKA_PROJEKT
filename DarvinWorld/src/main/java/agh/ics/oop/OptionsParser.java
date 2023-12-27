package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] args) throws IllegalArgumentException{
        int len=0;
        List<MoveDirection> direct = new ArrayList<>(len);
        for (String argument :args){
            switch (argument) {
                case "0" -> direct.add(MoveDirection.FORWARD);
                case "1" -> direct.add(MoveDirection.FORWARDRIGHT);
                case "2" -> direct.add(MoveDirection.RIGHT);
                case "3" -> direct.add(MoveDirection.BACKWARDRIGHT);
                case "4" -> direct.add(MoveDirection.BACKWARD);
                case "5" -> direct.add(MoveDirection.BACKWARDLEFT);
                case "6" -> direct.add(MoveDirection.LEFT);
                case "7" -> direct.add(MoveDirection.FORWARDLEFT);
                default -> throw new IllegalArgumentException(argument + " is not legal move specification");
               };

        }


        return direct;
        }

    }

