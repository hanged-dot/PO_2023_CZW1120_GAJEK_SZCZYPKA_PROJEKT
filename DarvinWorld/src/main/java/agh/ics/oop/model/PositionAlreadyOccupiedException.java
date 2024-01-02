package agh.ics.oop.model;

public class PositionAlreadyOccupiedException extends Exception{
    public PositionAlreadyOccupiedException(Vector2d position){
        super("Position "+ position.toString()+ " is already occupied.");
    }
}
