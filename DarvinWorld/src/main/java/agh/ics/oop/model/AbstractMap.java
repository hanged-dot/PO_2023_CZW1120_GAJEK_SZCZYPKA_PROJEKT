package agh.ics.oop.model;

import agh.ics.oop.model.util.PlantPositionGenerator;
import agh.ics.oop.presenter.SimulationStatisticsGenerator;

import java.util.*;


public abstract class AbstractMap implements WorldMap {

    protected final Boundary mapBoundary;
    protected HashMap<Vector2d, ArrayList<Animal>> animals;
    protected ArrayList<MapChangeListener> observers = new ArrayList<>();

    //    Rośliny nie mają żadnych cech poza położeniem, dlatego wystarczy przechowywać je jako
//    ich współrzędne na mapie
    private HashSet<Vector2d> plants;
    private HashSet<Vector2d> plantsToEat;
    private final PlantPositionGenerator plantPositionGenerator;
    private int dailyPlantCount;
    protected ArrayList<MapChangeListener> observers = new ArrayList<>();
    protected SimulationStatisticsGenerator statisticsGenerator;

    public AbstractMap(MapProperties mapProperties, AnimalProperties animalProperties) {

        statisticsGenerator = new SimulationStatisticsGenerator(mapProperties);

        mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1,
                0, mapProperties.mapHeight() - 1);

        animals = new HashMap<>();

        for (int i = 0; i < mapProperties.startAnimalCount(); ++i) {
//            do uzupełnienia parametry przekazywane do konstruktora zwierzaka,
//            zapewne wszystkie AnimalProperties
            place(new Animal());
        }

        dailyPlantCount = mapProperties.dailyPlantCount();

        plants = new HashSet<>();
        plantsToEat = new HashSet<>();

        plantPositionGenerator = new PlantPositionGenerator(mapBoundary);

        createNewPlants(mapProperties.startPlantCount());
    }

//    Usunięcie martwych zwierzaków z mapy

    @Override
    public void removeDeadAnimals() {

        for (Vector2d key : animals.keySet()) {

//            pozyskanie setu zwierzaków na danej pozycji
            ArrayList<Animal> animalArrayList = animals.get(key);

//            Usuwanie martwych zwierząt
            for (Animal animal:animalArrayList){
                if (animal.getEnergy()==0){
                    // usuniecie zwierzaka
                    statisticsGenerator.deadAnimalCountUpdate(animal.getAge());
                    statisticsGenerator.aliveGenotypeCountUpdate(animal.getGenome(), false);
                }
            }
            animalArrayList.removeIf(animal -> animal.getEnergy() == 0);


//            Jeśli set został pusty, usuwamy go z hashmapy
            if (animalArrayList.isEmpty()) {
                animals.remove(key);
            }
        }
    }

    private void place(Animal animal) {

        Vector2d animalPosition = animal.getPosition();
//        jeśli już są jakieś zwierzęta na tej pozycji, to po prostu dodajemy danego zwierzaka do setu
        if (animals.containsKey(animalPosition)) {
            animals.get(animalPosition).add(animal);
        }
//        a jeśli nie, to tworzymy na tej pozycji nową kolekcję
        else {
            ArrayList<Animal> animalArrayList = new ArrayList<>();
            animalArrayList.add(animal);
            animals.put(animalPosition, animalArrayList);
        }
    }

    //    Skręt i przemieszczanie każdego zwierzaka
    private void move(Animal animal) {

        Vector2d animalPosition = animal.getPosition();
        Vector2d targetPosition = getNextPosition(animal);

        if (!targetPosition.equals(animalPosition)) {
//            animal.move();
            animals.get(animalPosition).remove(animal);
            this.place(animal);

            if (plants.contains(targetPosition)) {
                plantsToEat.add(targetPosition);
            }
        }
    }

    @Override
    public void moveEveryAnimal() {

        for (ArrayList<Animal> animalArrayList : animals.values()) {
            for (Animal animal : animalArrayList) {
                move(animal);
            }
        }
    }


    protected Vector2d getNextPosition(Animal animal) {

//        tutaj trzeba będzie pobrać kolejną pozycję zwierzaka
        Vector2d targetPosition;

        int x = targetPosition.getX();
        int y = targetPosition.getY();

        if (y < mapBoundary.lowerY()) {
//            TODO: zmiana kierunku zwierzaka na przeciwny
            ++y;
        } else if (y > mapBoundary.upperY()) {
//            TODO: zmiana kierunku zwierzaka na przeciwny
            --y;
        }

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
//            wybór zwierzaka, który zje roślinę:

            Animal eatingAnimal = chooseEatingAnimal(plantPosition);
            eatingAnimal.eat();
//             zwracamy najedzonego zwierzaka do listy
            animals.get(plantPosition).add(eatingAnimal);

            plantsToEat.remove(plantPosition);
            statisticsGenerator.plantCountUpdate(false);
        }
        plantsToEat = new HashSet<>();
    }

    private Animal chooseEatingAnimal(Vector2d position) {

        ArrayList<Animal> animalsAtPosition = animals.get(position);
        int index = animalsAtPosition.size() - 1;
        Animal animal1 = animalsAtPosition.get(index);
        ArrayList<Animal> competingAnimals = new ArrayList<>();
        competingAnimals.add(animal1);

//        jeśli w kolejce są zwierzaki o takiej samej energii, wieku i ilości dzieci, tworzymy listę
//        tych zwierząt
        for (int i = index; i >= 0; --i){
            Animal animal2 = animalsAtPosition.get(i);
            if (animal1.greaterThan(animal2) == 0) {
                competingAnimals.add(animal2);
            } else {
                break;
            }
        }

//        jeśli lista zawiera tylko jednego najsilniejszego zwierzaka, zwracamy go od razu
        if (competingAnimals.size() == 1) {
            return animal1;
        } else {
//            w przeciwnym wypadku losujemy zweirzaka z listy
            Random random = new Random();
            int temp = random.nextInt(0, competingAnimals.size() - 1);
//            a resztę zwierzaków zwracamy do kolejki zwierzaków (lądują na końcu listy, więc nadal jest posortowana)
            for (int i = 0; i < competingAnimals.size(); ++i) {
                if (i != temp) {
                    animals.get(position).add(competingAnimals.get(i));
                }
            }
//            i zwracamy wylosowanego zwierzaka
            return competingAnimals.get(temp);
        }
    }

//Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.

    public void procreate() {

        for (Vector2d position : animals.keySet()) {

            ArrayList<Animal> procreatingAnimals = new ArrayList<>();
            ArrayList<Animal> children = new ArrayList<>();

//            na początek tworzymy listę ze wszystkimi zwierzętami zdolnymi do prokreacji,
//            ponieważ są na bieżąco wybierane z kolejki, są od razu ułożone w malejącej kolejności
            ArrayList<Animal> animalArrayList = animals.get(position);
            int index = animalArrayList.size();
            while (!animalArrayList.isEmpty()) {
                Animal animal = animals.get(position).get(index);
                --index;
                if (!animal.canProcreate()) {
                    break;
                } else {
                    procreatingAnimals.add(animal);
                }
            }

            for (int i = procreatingAnimals.size() - 1; i <= 0; i = i - 2) {
                Animal kid = procreatingAnimals.get(i).procreate(procreatingAnimals.get(i + 1));
                children.add(kid);
                statisticsGenerator.aliveAnimalCountUpdate(true);
                statisticsGenerator.allGenotypeCountUpdate(kid.getGenome(), true);

            for (Animal animal : procreatingAnimals) {
                animals.get(position).add(animal);
            }
            for (Animal animal : children) {
                animals.get(position).add(animal);
            }
        }

    }

    //        Wzrastanie nowych roślin na wybranych polach mapy

    @Override
    public void refreshMap() {

        createNewPlants(dailyPlantCount);

        for (ArrayList<Animal> animalArrayList: animals.values()){
            animalArrayList.sort(new AnimalComparator());
        }
    }

    public void createNewPlants(int plantCount) {

//        parametr plantCount to albo startowa ilość roślin, albo ilość roślin wyrastająca w 1 dzień
        ArrayList<Vector2d> positions = plantPositionGenerator.getPositions(plants, plantCount);
        for (Vector2d position: positions){
            plants.add(position);
            statisticsGenerator.plantCountUpdate(true);
        }
    }

    protected Vector2d createRandomPosition(Boundary boundary) {

        Random random = new Random();
        return new Vector2d(random.nextInt(boundary.leftX(), boundary.rightX()),
                random.nextInt(boundary.lowerY(), boundary.upperY()));
    }


    public Boundary getCurrentBounds() {
        return mapBoundary;
    }

    public void mapChanged(String changeInfo) {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).mapChanged(this, changeInfo);
        }
    }

    public void addObserver(MapChangeListener observer) {
        this.observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        this.observers.remove(observer);
    }

    public boolean canMoveTo(Vector2d position) {
        if (position.getY() <= mapBoundary.upperY() && position.getY() >= (this.mapBoundary.lowerY())) {
            return true;
        }
        return false;

    }
    public boolean isOccupied(Vector2d position){

    };
}