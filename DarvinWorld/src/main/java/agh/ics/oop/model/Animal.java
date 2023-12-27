package agh.ics.oop.model;


import java.util.ArrayList;

public class Animal implements WorldElement{
    private int id;
    private MapDirection orientation;
    private Vector2d position;
    private int life;
    private int[] genome;
    private int energy;
    private int plants;
    private ArrayList<Integer> kids;
    private int death;

    public Animal(Vector2d position, int energy,int[] genome){
        this.id=1; // naprawic zeby dawalo nr zwierzecia z tabeli zwierzat
        this.orientation=MapDirection.NORTH;
        this.position=position;
        this.life=0;
        this.energy=energy;
        this.death=0;
        this.kids= new ArrayList<>();
        this.plants=0;
        this.genome=genome;

    }
    public MapDirection getOrientation() { return orientation; }

    public Vector2d getPosition() {
        return position;
    }

    public int getLife(){return life;}
    public void age(){this.life++;}


    @Override
    public String toString() {
        MapDirection w= this.getOrientation();
        String b = switch (w) {
            case NORTH ->"N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH ->"S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";
        };
        return b;
    }

    public boolean isAt (Vector2d position){
        if (this.position.equals(position)){ return true; }
        return false;
    }

    public Vector2d onTunel(MoveValidator map){
        return null;
    }


    public void move(MoveDirection direction, WorldMap map){
        if (this.onTunel(map)==null) {
            MapDirection original= this.orientation;
            switch (direction) {
                case LEFT -> this.orientation = this.orientation.previous().previous();
                case BACKWARDLEFT -> this.orientation = this.orientation.previous().previous().previous();
                case FORWARDLEFT -> this.orientation = this.orientation.previous();
                case RIGHT -> this.orientation = this.orientation.next().next();
                case FORWARDRIGHT -> this.orientation = this.orientation.next();
                case FORWARD -> {}
                case BACKWARD -> this.orientation = this.orientation.next().next().next().next();
                case BACKWARDRIGHT -> this.orientation = this.orientation.next().next().next();
            };
            if (!map.canMoveTo(this.position.add(this.orientation.toUnitVector()))){
                this.orientation=original.next().next().next().next();
            }
            else{this.position=this.position.add(this.orientation.toUnitVector());}
            if(this.position.getX()>map.getCurrentBounds().upperRight().getX()){
                this.position=new Vector2d(map.getCurrentBounds().lowerLeft().getX(),this.position.getY());
            }
            if(this.position.getX()<map.getCurrentBounds().lowerLeft().getX()){
                this.position=new Vector2d(map.getCurrentBounds().upperRight().getX(),this.position.getY());
            }

        }
        else{
            this.position=this.onTunel(map);
        }
    }
/*
        if (direction==MoveDirection.RIGHT){ this.orientation=this.orientation.next();}
        if (direction==MoveDirection.FORWARD && map.canMoveTo(this.position.add(this.orientation.toUnitVector()))){
            this.position=this.position.add(this.orientation.toUnitVector());
        }
        if (direction==MoveDirection.BACKWARD && map.canMoveTo(this.position.subtract(this.orientation.toUnitVector()))){
            this.position=this.position.subtract(this.orientation.toUnitVector());}
        */


}
