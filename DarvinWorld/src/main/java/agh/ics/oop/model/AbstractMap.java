package agh.ics.oop.model;

import agh.ics.oop.model.util.PlantPositionGenerator;
import com.sun.scenario.animation.shared.AnimationAccessor;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.round;
import static java.util.Collections.min;


public abstract class AbstractMap implements WorldMap {

    protected final Boundary mapBoundary;
    private final UUID identifier;
    private HashMap<Vector2d, LinkedList<Animal>> animals;
    protected HashMap<Vector2d, ArrayList<Animal>> beforeMoveAnimals;
    protected HashMap<Vector2d, ArrayList<Animal>> afterMoveAnimals;

    protected ArrayList<MapChangeListener> observers = new ArrayList<>();
    private HashMap<Vector2d,Plant> plants;
    private HashSet<Vector2d> plantsToEat;
    private final PlantPositionGenerator plantPositionGenerator;
    private final int dailyPlantCount;
    protected SimulationStatisticsGenerator statisticsGenerator;
    private final AnimalComparator animalComparator;
    int day; // current map day

    private MapChangeListener observer;

    public AbstractMap(MapProperties mapProperties, AnimalProperties animalProperties, ArrayList<MapChangeListener> observers) {

        this.observers = new ArrayList<>();
        this.identifier= UUID.randomUUID();
        statisticsGenerator = new SimulationStatisticsGenerator(mapProperties, animalProperties);
        animalComparator = new AnimalComparator();
        day = 1;

        this.statisticsGenerator = new SimulationStatisticsGenerator(mapProperties);
        this.animalComparator = new AnimalComparator();
        this.day = 1;
        this.mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1, 0, mapProperties.mapHeight() - 1);
        this.animals = new HashMap<>();
        mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1,
                0, mapProperties.mapHeight() - 1);

        beforeMoveAnimals = new HashMap<>();
        afterMoveAnimals = new HashMap<>();

        plants = new HashSet<>();
        plantsToEat = new HashSet<>();
        dailyPlantCount = mapProperties.dailyPlantCount();


        for (int i = 0; i < mapProperties.startAnimalCount(); ++i) {
//            tworzymy nowe zwierzątka, generujemy im genom, umieszczamy na mapie
            Animal animal = new Animal(animalProperties, createRandomPosition(mapBoundary));
            animal.setGenome(createRandomGenome(animalProperties.genomeLength()));
            statisticsGenerator.aliveGenotypeCountUpdate(animal.getGenome(), true);
            place(animal);
        }

        this.dailyPlantCount = mapProperties.dailyPlantCount();

        plants = new HashMap<>();
        plantsToEat = new HashSet<>();

        plantPositionGenerator = new PlantPositionGenerator(mapBoundary);

        beforeMoveAnimals.putAll(afterMoveAnimals);
        afterMoveAnimals.clear();

        createNewPlants(mapProperties.startPlantCount());
    }
    @Override
    public UUID getID() {return identifier;}
    public HashMap<Vector2d,LinkedList<Animal>> getAnimals(){ return this.animals;}

//    Usunięcie martwych zwierzaków z mapy

    @Override
    public void removeDeadAnimals() {

        ArrayList<Vector2d> keysToRemove = new ArrayList<>();

        for (Vector2d key : beforeMoveAnimals.keySet()) {
//            pozyskanie setu zwierzaków na danej pozycji
            ArrayList<Animal> animalList = beforeMoveAnimals.get(key);

            animalList.removeIf(a ->
            {
                if (a.getEnergy() == 0){
                    System.out.println("Somebody dies!");
                    statisticsGenerator.deadAnimalUpdate(a);
                    return true;
                }
                return false;
            });

//            Jeśli lista została pusta, usuwamy ją z hashmapy i update wolnych pozycji na mapie
            if (animalList.isEmpty()) {
                keysToRemove.add(key);
            }
        }

        for (Vector2d key : keysToRemove){
            beforeMoveAnimals.remove(key);
            if (!plants.containsKey(key)){
                statisticsGenerator.freePositionCountUpdate(false);
            }
        }
    }

//    Rozmieszczanie zwierzaków na mapie (zarówno przy budowaniu nowej mapy jak i po każdym ruchu zwierzęcia)
    private void place(Animal animal) {

        //        Pobieramy pozycję zwierzaka
        Vector2d animalPosition = animal.getPosition();

//        jeśli już są jakieś zwierzęta na tej pozycji, to po prostu dodajemy danego zwierzaka do listy
        if (afterMoveAnimals.containsKey(animalPosition)) {
            afterMoveAnimals.get(animalPosition).add(animal);
        }
//        a jeśli nie, to tworzymy na tej pozycji nową kolekcję
        else {
            ArrayList<Animal> animalList = new ArrayList<>();
            animalList.add(animal);
            afterMoveAnimals.put(animalPosition, animalList);

//        Jeśli na nowym miejscu zwierzaka znajduje się roślina, dołączamy ją do listy roślin do zjedzenia
//        w następnym dniu
        if (plants.containsKey(animalPosition)) {
            plantsToEat.add(animalPosition);
        } else {
//            w przeciwnym wypadku pozycja ta do tej pory była wolna, zatem musimy zmniejszyć licznik wolnych
//            pozycji
            statisticsGenerator.freePositionCountUpdate(false);
            }
        }
        mapChanged("Zwierzę zostało umieszczone na mapie na pozycji" + animal.getPosition());
    }

    //    Skręt i przemieszczanie każdego zwierzaka
    private void move(Animal animal) {

        Vector2d pos1 = animal.getPosition();
//        Wyznaczamy docelową pozycję zwierzaka
        Vector2d targetPosition = getNextPosition(animal);
//        Informujemy zwierzaka o zmianie jego położenia
        animal.move(targetPosition);
//        Umieszczamy zwierzaka na odpowiednim miejscu na mapie
        this.place(animal);
        mapChanged("Zwierzę poruszyło się z pozycji "+ pos1 + " na pozycję " +targetPosition);

    }

    @Override
    public void moveEveryAnimal() {

        for (List<Animal> animalList : beforeMoveAnimals.values()) {
            for (Animal animal : animalList) {
                move(animal);
                animal.age();
            }
        }
        for (List<Animal> animalList: afterMoveAnimals.values()){
            animalList.sort(new AnimalComparator());
        }
    }

//    Wyznaczamy nową pozycję zwierzaka po wykonaniu ruchu:
    protected Vector2d getNextPosition(Animal animal) {

//        Na podstawie genomu zwierzaka wyznaczana jest jego docelowa pozycja:
        Vector2d targetPosition = animal.turn();

        int x = targetPosition.getX();
        int y = targetPosition.getY();

//        Sprawdzamy, czy zwierzak nie wyjdzie poza południową/północną granicę mapy
        if (y < mapBoundary.lowerY()) {
            animal.reverseOrientation();
            ++y;
        } else if (y > mapBoundary.upperY()) {
            animal.reverseOrientation();
            --y;
        }

//        Sprawdzamy, czy zwierzak nie wyjdzie poza zachodnią/wschodnią granicę mapy
        if (x < mapBoundary.leftX()) {
            x = mapBoundary.rightX();
        } else if (x > mapBoundary.rightX()) {
            x = mapBoundary.leftX();
        }

        return new Vector2d(x, y);
    }

//    Konsumpcja roślin, na których pola weszły zwierzaki

    public void removeEatenPlants() {

        for (Vector2d plantPosition : plantsToEat) {

            Animal eatingAnimal = chooseEatingAnimal(plantPosition);
            eatingAnimal.eat();
            statisticsGenerator.totalEnergyUpdate(true);    // informujemy statystyki, że wzrosła energia jednego zwierzaka
            statisticsGenerator.plantCountUpdate(false);    // informujemy statystyki, że zmniejszyła się liczba roślin
            //             zwracamy najedzonego zwierzaka do listy
            afterMoveAnimals.get(plantPosition).add(eatingAnimal);
            plants.remove(plantPosition);
            }

        plantsToEat = new HashSet<>();
    }

    private Animal chooseEatingAnimal(Vector2d position) {

        List<Animal> animalsAtPosition = afterMoveAnimals.get(position);

        if (animalsAtPosition.size()==1){
            return animalsAtPosition.remove(0);
        }

        int index = animalsAtPosition.size() - 1;
        Animal animal1 = animalsAtPosition.remove(index);
        ArrayList<Animal> competingAnimals = new ArrayList<>();
        competingAnimals.add(animal1);

//        jeśli w kolejce są zwierzaki o takiej samej energii, wieku i ilości dzieci, tworzymy listę tych zwierząt
        for (int i = index - 1; i >= 0; --i) {
            Animal animal2 = animalsAtPosition.get(i);
            if (animal1.greaterThan(animal2) == 0) {
                competingAnimals.add(animalsAtPosition.get(i));
            } else {
                break;
            }
        }

        if (competingAnimals.size() == 1) {
            animalsAtPosition.remove(animal1);
            return animal1;
        } else {
//            losujemy zwierzaka z listy zwierzaków rywalizujących o jedzenie
            Random random = new Random();
            int temp = random.nextInt(0, competingAnimals.size());
            animalsAtPosition.remove(competingAnimals.get(temp));
            return competingAnimals.get(temp);
        }
    }

//Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.

    public void procreate() {

        int numberOfAnimals = 0;
        for (List<Animal> list : afterMoveAnimals.values()){
            numberOfAnimals += list.size();
        }

        for (Vector2d position : afterMoveAnimals.keySet()) {

            List<Animal> animalList = afterMoveAnimals.get(position);

            if (animalList.size() > 1){

                ArrayList<Animal> procreatingAnimals = new ArrayList<>();
                ArrayList<Animal> children = new ArrayList<>();

                for (int i = animalList.size()  - 1; i >= 0; --i){
                    if (animalList.get(i).canProcreate()) {
                        Animal animal = animalList.get(i);
                        procreatingAnimals.add(animal);
                    } else {
                        break;
                    }
                }

                for (int i = procreatingAnimals.size() - 1; i > 0; i = i - 2) {

                    Animal child = procreatingAnimals.get(i).procreate(procreatingAnimals.get(i - 1));
                    children.add(child);
                    statisticsGenerator.newbornAnimalUpdate(child);     // informujemy statystyki, że doszło nowe zwierzę z nowym genomem
                }

                for (Animal animal : children) {
                    afterMoveAnimals.get(position).add(animal);
                }
            }
        }
    }


    @Override
    public boolean refreshMap () {

//        TODO: wyświetlić pojawiające się roślinki
        if (afterMoveAnimals.isEmpty()){
            return false;
        }

        statisticsGenerator.totalEnergyUpdate(false);       // informujemy statystyki, że energia wszystkich zwierząt spada o 1
        printStatisticsPlease();

        // Rozpoczęcie nowego dnia:

        ++day;

        createNewPlants(dailyPlantCount);

        for (Vector2d key : afterMoveAnimals.keySet()) {

            ArrayList<Animal> animalList = afterMoveAnimals.get(key);

            if (animalList.isEmpty()){
                afterMoveAnimals.remove(key);
            }
            else {
                animalList.sort(animalComparator);
            }
        }

        beforeMoveAnimals.clear();
        beforeMoveAnimals.putAll(afterMoveAnimals);
        afterMoveAnimals.clear();

        return true;
    }

    //        Wzrastanie nowych roślin na wybranych polach mapy

    public void createNewPlants (int plantCount){
//       parametr plantCount to albo startowa ilość roślin, albo ilość roślin wyrastająca w 1 dzień
        ArrayList<Vector2d> positions = plantPositionGenerator.getPositions(plants, plantCount);

        for (Vector2d position : positions) {
            Plant p = new Plant(position);
            plants.put(position,p);

//  Na każdym polu, na którym wyrasta roślina, zwiększamy licznik roślin
            statisticsGenerator.plantHistoryUpdate(position);
//  Przekazujemy do statystyk informację o pojawieniu się kolejnej rośliny
            statisticsGenerator.plantCountUpdate(true);
//            Jeśli na tym polu nie było do tej pory żadnego zwierzęcia, to przekazujemy również do statystyk
//            informację, że zmniejszyła się ilość pustych pól
            if (!afterMoveAnimals.containsKey(position)){
                statisticsGenerator.freePositionCountUpdate(false);
            }
        }
    }

    @Override
    public SimulationStatistics getSimulationStatistics(){
        return statisticsGenerator.generateSimulationStatics();
    }

    protected Vector2d createRandomPosition (Boundary boundary){

        Random random = new Random();
        return new Vector2d(random.nextInt(boundary.leftX(), boundary.rightX()),
                random.nextInt(boundary.lowerY(), boundary.upperY()));
    }

    protected int[] createRandomGenome(int genomeLen){
        Random random = new Random();
        int[] genome = new int[genomeLen];
        for (int i = 0; i < genomeLen; ++i){
            genome[i] = random.nextInt(0, 8);
        }
        return genome;
    }

    public Boundary getCurrentBounds () {
        return mapBoundary;
    }

//    Metoda generująca zwierzaki z dominującym genotypem
    public ArrayList<Animal> getAnimalsWithDominantGenotype(){

        ArrayList<Animal> animalsWithDominantGenotype = new ArrayList<>();
        int[] dominantGenotype = statisticsGenerator.generateSimulationStatics().dominantGenotype();

        for (ArrayList<Animal> animalList : afterMoveAnimals.values()){
            for (Animal animal : animalList){
                if (Arrays.equals(dominantGenotype, animal.getGenome())){
                    animalsWithDominantGenotype.add(animal);
                }
            }
        }

        return animalsWithDominantGenotype;
    }

    public ArrayList<PositionAbundance> getPositionsPreferredByPlants(){
        return statisticsGenerator.generatePreferredPlantPositions();
    }


    public void mapChanged (String changeInfo){
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).mapChanged(this, changeInfo);
        }
    }

    public WorldElement getStrongest(Vector2d position){
        LinkedList<Animal> pos1 = this.animals.get(position);
        int energy=0;
        Animal strongest = null;
        for (Animal a : pos1){
            if(a.getEnergy()>energy && a.getDeath()!=0){
                energy=a.getEnergy();
                strongest=a;
            }
        }
        return strongest;
    }

    public WorldElement getPlant(Vector2d position){
        if(this.plants.containsKey(position)) return this.plants.get(position);
        else return null;
    }

    public void addObserver (MapChangeListener observer){
        this.observers.add(observer);
    }

    public void removeObserver (MapChangeListener observer){
        this.observers.remove(observer);
    }

    public void printStatisticsPlease(){
        System.out.println("Wypisujemy statystyki z dnia nr "+day);
        SimulationStatistics stats = statisticsGenerator.generateSimulationStatics();

        System.out.println("Ilość żywych zwierząt: " + stats.aliveAnimalCount());
        System.out.println("Ilość martwych zwierząt: "+stats.deadAnimalCount());
        System.out.println("Ilość roślin: "+ stats.plantCount());
        System.out.println("Ilość wolnych pozycji: "+stats.freePositionCount());
        System.out.print("Średnia energia żywych zwierzaków: "+stats.meanAliveAnimalEnergy());
        System.out.println("Dominujący genotyp: ");
        for (int i : stats.dominantGenotype()){
            System.out.print(i);
        }
        System.out.print("\n");
        System.out.println("Dominujący żyjący genotyp: ");
        for (int i : stats.dominantAliveGenotype()){
            System.out.print(i);
        }
        System.out.print("\n");
        System.out.println("Średnia długość życia zwierząt: "+stats.meanAnimalLifeSpan());
        System.out.println("Średnia ilość potomstwa: "+stats.meanAliveAnimalOffspringCount());

    }
}