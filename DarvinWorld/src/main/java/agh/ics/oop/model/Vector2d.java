package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;
    public Vector2d(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(%d,%d)".formatted(x,y);
    }

    public boolean follows(Vector2d other){
        if(this.x>=other.x && this.y>=other.y)
            return true;
        return false;
    }
    public boolean precedes(Vector2d other) {
        if (this.x <= other.getX() && this.y <= other.getY())
            return true;
        return false;
    }
    public Vector2d add(Vector2d other){
        return new Vector2d(this.x+other.getX(),this.y+ other.getY());

    }
    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x-other.getX(),this.y- other.getY());
    }
    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x,other.getX()),Math.max(this.y,other.getY()));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x,other.getX()),Math.min(this.y,other.getY()));
    }
    public Vector2d opposite(){
        return new Vector2d(-1*this.x,-1*this.y);
    }
    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (x==that.getX() && y==that.getY());
    }
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }

}
