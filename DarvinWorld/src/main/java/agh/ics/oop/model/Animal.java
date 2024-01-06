package agh.ics.oop.model;


import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Animal{

    private final AnimalProperties properties;
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

    public Animal(AnimalProperties properties, Vector2d position){

        this.properties=properties;
        this.genLen=properties.genomeLength();
        this.energy=properties.startAnimalEnergy();
        this.orientation=MapDirection.NORTH;
        this.position=position;
        this.life=0;
        this.death=0;
        this.kids= new ArrayList<>();
        this.plants=0;
        this.transferedThroughTunnel=false;

    }
    public MapDirection getOrientation() { return orientation; }  //zwraca orientacje zwierzaka
    public Vector2d getPosition() {
        return position;
    }  //zwraca pozycję zwierzaka
    public int getEnergy() {return energy;} //zwraca informację o energii zwierzaka
    public void setGenome(int[] genome){this.genome=genome;} //przypisuje zwierzakowi listę ruchów
    public int[] getGenome(){return this.genome;}
    public void setDeath(int day){this.death=day;} // zwraca dzien smierci zwierzaka
    public void addKid(Animal kid){ this.kids.add(kid);} // dodaje zwierzaka do listy dzieci rodzica zwierzaka
    public int getAge(){return this.life;}; //zwraca dotychczasowa dlugosc zycia
    public void age(){this.life++;} // postarza zwierzaka
    public void eat(){this.energy=this.energy+properties.energyFromPlant();} //zwerzak je
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
        MapDirection w = this.getOrientation();
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

// ANIMAL TURNING & MOVING

    private MoveDirection getNextDirection(){

        return switch (genome[life%genLen]){
            case 0 -> MoveDirection.FORWARD;
            case 1 -> MoveDirection.FORWARDRIGHT;
            case 2 -> MoveDirection.RIGHT;
            case 3 -> MoveDirection.BACKWARDRIGHT;
            case 4 -> MoveDirection.BACKWARD;
            case 5 -> MoveDirection.BACKWARDLEFT;
            case 6 -> MoveDirection.LEFT;
            case 7 -> MoveDirection.FORWARDLEFT;
            default -> throw new IllegalStateException("Unexpected value: " + genome[life % genLen]);
        };
    }

    public void move(Vector2d targetPosition){
        this.position = targetPosition;
    }
    public Vector2d turn(){

        MoveDirection direction = getNextDirection();

        switch (direction) {
            case LEFT -> this.orientation = this.orientation.previous().previous();
            case BACKWARDLEFT -> this.orientation = this.orientation.previous().previous().previous();
            case FORWARDLEFT -> this.orientation = this.orientation.previous();
            case RIGHT -> this.orientation = this.orientation.next().next();
            case FORWARDRIGHT -> this.orientation = this.orientation.next();
            case FORWARD -> {}
            case BACKWARD -> this.orientation = this.orientation.next().next().next().next();
            case BACKWARDRIGHT -> this.orientation = this.orientation.next().next().next();
        }

        Vector2d movement = this.orientation.toUnitVector();
//        Zwracamy docelową pozycję zwierzaka wynikającą z jego genomu
        return this.position.add(movement);
    }

    public void reverseOrientation(){
        this.orientation = this.orientation.previous().previous().previous().previous();
    }
//jest ok
    public boolean canProcreate(int min){ if (getEnergy()>=min) return true;
    return false;} //zwraca informację czy zwierzak da radę się rozmnożyć
// jest ok chyba (MG)
public Animal procreate(Animal other){

    Animal offspring = new Animal(this.properties, this.getPosition());

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
        for(i=0; i<(int)Math.floor(((double) other.getEnergy() /energySum)*other.genLen); i++){
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

//nie jest ok dodac do observatora animala
    public void addObserver(MapChangeListener observer) {
        this.observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        this.observers.remove(observer);
    }
}
