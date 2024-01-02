package agh.ics.oop.model;

import java.util.UUID;



public class RectangularMap extends AbstractWorldMap{

    public RectangularMap(int height, int width){
        bounds= new Boundary(new Vector2d(0,0), new Vector2d(width,height));
        identifier= UUID.randomUUID();
    }

    /*
    public int getHeight(){return height;}
    public int getWidth(){return width;}
*/

    @Override
    public boolean canMoveTo(Vector2d position) {
        if (position.getY()<=bounds.upperRight().getY() && position.getY()>=(this.bounds.lowerLeft().getY())){return true;}
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
