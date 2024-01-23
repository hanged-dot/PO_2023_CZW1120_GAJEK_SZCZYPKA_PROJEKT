package WorldElement;


import Presenter.MapChangeListener;
import Records.AnimalProperties;
import Records.MapDirection;
import Records.MoveDirection;
import WordMap.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static WordMap.OptionsParser.parse;


public class Animal implements WorldElement{

    private final AnimalProperties properties;
    private boolean transferedThroughTunnel;
    private int id;
    private MapDirection orientation;
    private Vector2d position;
    private int life;
    private int[] genome;
    private List<MoveDirection> genomeMoves;
    private int energy;
    private int plants;
    private ArrayList<Animal> kids;
    private int death;
    private int genLen;
    private int shift;
    protected ArrayList<MapChangeListener> observers = new ArrayList<>();

    public Animal(AnimalProperties properties, Vector2d position){
        this.properties=properties;
        this.genLen=properties.genomeLength();
        this.energy=properties.startAnimalEnergy();
        this.orientation=newRandomOrientation();
        this.position=position;
        this.life=0;
        this.death=0;
        this.kids= new ArrayList<>();
        this.plants=0;
        this.transferedThroughTunnel=false;
        this.shift=(new Random()).nextInt(genLen);
    }
    public AnimalProperties getProperties() {return this.properties;}
    public MapDirection getOrientation() { return orientation; }  //zwraca orientacje zwierzaka
    public Vector2d getPosition() {
        return position;
    }  //zwraca pozycję zwierzaka
    public int getEnergy() {return energy;} //zwraca informację o energii zwierzaka
    public void setGenome(int[] genome){this.genome=genome;} //przypisuje zwierzakowi listę ruchów
    public int[] getGenome(){return this.genome;}
    public void setDeath(int day){this.death=day;} // ustala dzien smierci zwierzaka
    public int getDeath(){return this.death;}
    public void addKid(Animal kid){ this.kids.add(kid);} // dodaje zwierzaka do listy dzieci rodzica zwierzaka
    public int getAge(){return this.life;}; //zwraca dotychczasowa dlugosc zycia
    public int getGenomePart(){ return (life+shift)%genLen;}
    public int getPlants(){ return this.plants;}
    public int getKidsLen(){return this.kids.size();}
    public void age(){
        this.life++;
        this.energy--;} // postarza zwierzaka
    public void eat(){
        this.energy=this.energy+properties.energyFromPlant();
        this.plants+=1;
    } //zwerzak je
    public void setTransferredThroughTunnel(boolean value){this.transferedThroughTunnel=value;} //zwierzak przeszedl wlasnie przez tunel, ma nim teraz nie wracac
    public boolean isTransferedThroughTunnel(){return this.transferedThroughTunnel;} //zwraca czy zwierzak wlasnie przeszedl przez tunel

    public int getNumberOfChildren(){
        int children=0;
        for (int i=0; i<this.kids.size();i++){
            children+= this.kids.get(i).getNumberOfChildren()+1;
        }
        return children;
    };
    public MapDirection newRandomOrientation(){
        int orientationNumber = (new Random()).nextInt(8);
        return switch(orientationNumber){
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTHEAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTHEAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTHWEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTHWEST;
            default -> MapDirection.NORTH;
        };
    }

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

    @Override
    public String getPicture() {

            if(0==this.getEnergy()){return "icons/grey_paw.png";}
            if(0<this.getEnergy() && this.getEnergy()<this.properties.minProcreateEnergy() ){ return "icons/orange_paw.png";}
            if(this.properties.minProcreateEnergy()<=this.getEnergy() && this.getEnergy()< properties.startAnimalEnergy()){return "icons/green_paw.png";}
            else{return "icons/blue_paw.png";}
    }

    public boolean isAt (Vector2d position){
        if (this.position.equals(position)){ return true; }
        return false;
    }

// ANIMAL TURNING & MOVING

    private MoveDirection getNextDirection(){
    return parse(genome[(life+shift)%genLen]);
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
    public boolean canProcreate(){
        return getEnergy() >= properties.minProcreateEnergy();
    } //zwraca informację czy zwierzak da radę się rozmnożyć
// jest ok chyba (MG)
public Animal procreate(Animal other){
    AnimalProperties offspringProperties = new AnimalProperties(this.properties.procreateEnergy()+other.properties.procreateEnergy(),this.properties.energyFromPlant(),this.properties.minProcreateEnergy(),this.properties.procreateEnergy(),this.properties.minMutationCount(),this.properties.maxMutationCount(),this.genLen,this.properties.withLightMutationCorrect());
    Animal offspring = new Animal(offspringProperties, this.getPosition());

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
    offspring.mutate(genes);

    return offspring;
}
public void mutate(int[] genes){
        if(this.properties.maxMutationCount() == 0){
            return;
        }

        int maxMutation = this.properties.maxMutationCount();
        int minMutation = this.properties.minMutationCount();
        int mutationCount= (new Random()).nextInt(maxMutation-minMutation) + minMutation;
        ArrayList<Integer> positionsInGenome = new ArrayList<>();
        for (int i=0;i<genLen;i++){positionsInGenome.add(i);}
        Collections.shuffle(positionsInGenome);
        if(!this.properties.withLightMutationCorrect()) {
            for (int i = 0; i < mutationCount; i++) {genes[positionsInGenome.get(i)] = (new Random()).nextInt(8);}
        }
        else{
            for (int i = 0; i < mutationCount; i++) {genes[positionsInGenome.get(i)] = (genes[positionsInGenome.get(i)]+((int)Math.pow((-1),(new Random()).nextInt(2))))%8;}
        }
        this.setGenome(genes);
}



}
