package agh.ics.oop.model;

import agh.ics.oop.model.util.PlantPositionGenerator;

import java.util.*;


public abstract class AbstractMap implements WorldMap {

    protected final Boundary mapBoundary;
    protected HashMap<Vector2d, PriorityQueue<Animal>> animals;
    protected ArrayList<MapChangeListener> observers = new ArrayList<>();

    //    Rośliny nie mają żadnych cech poza położeniem, dlatego wystarczy przechowywać je jako
//    ich współrzędne na mapie
    private HashSet<Vector2d> plants;

    //    a tutaj na bieżąco dodajemy trawę do zjedzenia przez zwierzę
    private HashSet<Vector2d> plantsToEat;
    private final PlantPositionGenerator plantPositionGenerator;
    private int dailyPlantCount;

    public AbstractMap(MapProperties mapProperties, AnimalProperties animalProperties) {

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
            PriorityQueue<Animal> animalQueue = animals.get(key);

//            Usuwanie martwych zwierząt

            animalQueue.removeIf(animal -> animal.getEnergy() == 0);


//            Jeśli set został pusty, usuwamy go z hashmapy
            if (animalQueue.isEmpty()) {
                animals.remove(key);
            }
        }
    }

    private void place(Animal animal) {

        Vector2d animalPosition = animal.getPosition();
//        jeśli już są jakieś zwierzęta na tej pozycji, to po prostu dodajemy danegp zwierzaka do setu
        if (animals.containsKey(animalPosition)) {
            animals.get(animalPosition).add(animal);
        }
//        a jeśli nie, to tworzymy na tej pozycji nową kolekcję
        else {
            PriorityQueue<Animal> animalQueue = new PriorityQueue<>(new AnimalComparator());
            animalQueue.add(animal);
            animals.put(animalPosition, animalQueue);
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

        for (PriorityQueue<Animal> animals : animals.values()) {
            for (Animal animal : animals) {
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
        }
        plantsToEat = new HashSet<>();
    }

    private Animal chooseEatingAnimal(Vector2d position) {

        PriorityQueue<Animal> animalsAtPosition = animals.get(position);
        Animal animal1 = animalsAtPosition.poll();
        ArrayList<Animal> competingAnimals = new ArrayList<>();
        competingAnimals.add(animal1);

//        jeśli w kolejce są zwierzaki o takiej samej energii, wieku i ilości dzieci, tworzymy listę
//        tych zwierząt
        while (!animalsAtPosition.isEmpty()) {
            Animal animal2 = animalsAtPosition.poll();
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

//            a resztę zwierzaków zwracamy do kolejki zwierzaków
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
            while (!animals.get(position).isEmpty()) {
                Animal animal = animals.get(position).poll();
                if (!animal.canProcreate()) {
                    break;
                } else {
                    procreatingAnimals.add(animal);
                }
            }

            for (int i = 0; i < procreatingAnimals.size() - 1; i += 2) {
                children.add(procreatingAnimals.get(i).procreate(procreatingAnimals.get(i + 1)));
            }

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
    public void growPlants() {
        createNewPlants(dailyPlantCount);
    }

    public void createNewPlants(int plantCount) {

//        parametr plantCount to albo startowa ilość roślin, albo ilość roślin wyrastająca w 1 dzień

        plants.addAll(plantPositionGenerator.getPositions(plants, plantCount));

    }

    protected Vector2d createRandomPosition(Boundary boundary) {

        Random random = new Random();
        return new Vector2d(random.nextInt(boundary.leftX(), boundary.rightX()),
                random.nextInt(boundary.lowerY(), boundary.upperY()));
    }

    @Override

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