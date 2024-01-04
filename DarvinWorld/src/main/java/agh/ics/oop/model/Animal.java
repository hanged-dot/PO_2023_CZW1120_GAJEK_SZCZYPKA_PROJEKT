package agh.ics.oop.model;


import java.util.ArrayList;
import java.util.Random;

public class Animal{
    private boolean transferedThroughTunnel;
    private int id;
    private MapDirection orientation;
    private Vector2d position;
    private int life;
    private int[] genome;
    private int energy;
    private int plants;
    private ArrayList<Animal> kids;
    private int death;
    private int genLen;
    protected ArrayList<MapChangeListener> observers = new ArrayList<>();

    public Animal(Vector2d position, int energy, int lenght){
        this.id=1; // naprawic zeby dawalo nr zwierzecia z tabeli zwierzat
        this.orientation=MapDirection.NORTH;
        this.position=position;
        this.life=0;
        this.energy=energy;
        this.death=0;
        this.kids= new ArrayList<>();
        this.plants=0;
        this.genLen=lenght;
        this.transferedThroughTunnel=false;


    }
    public MapDirection getOrientation() { return orientation; }  //zwraca orientacje zwierzaka

    public Vector2d getPosition() {
        return position;
    }  //zwraca pozycję zwierzaka

    public int getEnergy() {return energy;} //zwraca informację o energii zwierzaka

    public void setGenome(int[] genome){this.genome=genome;} //przypisuje zwierzakowi listę ruchów

    public void setDeath(int day){this.death=day;} // zwraca dzien smierci zwierzaka

    public void addKid(Animal kid){ this.kids.add(kid);} // dodaje zwierzaka do listy dzieci rodzica zwierzaka

    public int getAge(){return this.life;}; //zwraca dotychczasowa dlugosc zycia
    public void age(){this.life++;} // postarza zwierzaka

    public void eat(int energy){this.energy=this.energy+energy;} //zwerzak je

    public void setTransferredThroughTunnel(boolean value){this.transferedThroughTunnel=value;} //zwierzak przeszedl wlasnie przez tunel, ma nim teraz nie wracac
    public boolean isTransferedThroughTunnel(){return this.transferedThroughTunnel;} //zwraca czy zwierzak wlasnie przeszedl przez tunel


    public int getNumberOfChildren(){
        int children=0;
        for (int i=0; i<this.kids.size();i++){
            children+= this.kids.get(i).getNumberOfChildren()+1;
        }
        return children;
    };

    private int compareEnergy(Animal a){return this.getEnergy()-a.getEnergy();};
    private int compareAge(Animal a){return this.getAge()-a.getAge();};
    private int compareNumberOfChildren(Animal a){return this.getNumberOfChildren()-a.getNumberOfChildren();};


//    Metoda greaterThan sprawdza, czy dany zwierzak spełnia warunki wygranej z innym zwierzakiem;
//    jeśli tak, to zwraca 1; jeśli nie, to zwraca -1; a jeśli zwierzaki wszystkie warunki mają takie same,
//    metoda zwraca 0 i wówczas w konkurencji zwierzaków będzie losowane, który z nich zje/rozmnoży się


    public int greaterThan(Animal a){

        int comparedEnergy = compareEnergy(a);

        if (comparedEnergy > 0){
            return 1;
        } else if (comparedEnergy == 0) {

            int comparedAge = compareAge(a);

            if (comparedAge > 0){
                return 1;
            } else if (comparedAge == 0){

                int comparedNumberOfChildren = compareNumberOfChildren(a);

                if (comparedNumberOfChildren > 0){
                    return 1;
                } else if (comparedNumberOfChildren == 0) {
                    return 0;
                }
            }
            return -1;
        }
        return -1;
    }

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


    public void move(MoveDirection direction, TunnelMap map){
        Vector2d nextPosition = map.getNextPosition(this);
        if (this.transferedThroughTunnel==false) {
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
            this.position=nextPosition;
        }
    }

    public boolean canProcreate(int min){ if (getEnergy()>=min) return true;
    return false;} //zwraca informację czy zwierzak da radę się rozmnożyć

    public Animal procreate(Animal other){
    Animal offspring = new Animal(this.getPosition(),0, this.genLen);
    other.addKid(offspring);
    this.addKid(offspring);
    int[] genes= new int[this.genLen];
    int energySum= this.getEnergy()+other.getEnergy();
    int thisToStart=(new Random()).nextInt(2);
    if (thisToStart==1){
        int i;
        for(i=0; i<(int)Math.floor((this.getEnergy()/energySum)*this.genLen);i++){
            genes[i]=this.genome[i];
        }
        while(i<other.genLen){
            genes[i]=other.genome[i];
            i++;
        }
    }
    else{
        int i;
        for(i=0; i<(int)Math.floor((other.getEnergy()/energySum)*other.genLen);i++){
            genes[i]=other.genome[i];
        }
        while(i<this.genLen){
            genes[i]=this.genome[i];
            i++;
        }
    }
    offspring.setGenome(genes);

return offspring;
}
    public void addObserver(MapChangeListener observer) {
        this.observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        this.observers.remove(observer);
    }
}
