package agh.ics.oop.model;

import agh.ics.oop.model.util.PlantPositionGenerator;
import agh.ics.oop.presenter.SimulationStatisticsGenerator;

import java.util.*;


public abstract class AbstractMap implements WorldMap {

    protected final Boundary mapBoundary;
    protected HashMap<Vector2d, LinkedList<Animal>> animals;
    protected ArrayList<MapChangeListener> observers = new ArrayList<>();
    private HashSet<Vector2d> plants;
    private HashSet<Vector2d> plantsToEat;
    private final PlantPositionGenerator plantPositionGenerator;
    private final int dailyPlantCount;
    protected SimulationStatisticsGenerator statisticsGenerator;
    private final AnimalComparator animalComparator;
    int day; // current map day

    public AbstractMap(MapProperties mapProperties, AnimalProperties animalProperties) {

        statisticsGenerator = new SimulationStatisticsGenerator(mapProperties);
        animalComparator = new AnimalComparator();
        day = 1;

        mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1,
                0, mapProperties.mapHeight() - 1);

        animals = new HashMap<>();

        for (int i = 0; i < mapProperties.startAnimalCount(); ++i) {

            place(new Animal(createRandomPosition(mapBoundary),
                    animalProperties.startAnimalEnergy(),
                    animalProperties.genomeLength()));
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
            List<Animal> animalList = animals.get(key);

//            Usuwanie martwych zwierząt
            while (animalList.get(0).getEnergy()==0){
                Animal deadAnimal = animalList.remove(0);
                statisticsGenerator.deadAnimalUpdate(deadAnimal);
//                TODO: usunięcie zwierzaka z wizualizacji
            }

//            Jeśli lista została pusta, usuwamy ją z hashmapy i update wolnych pozycji na mapie
            if (animalList.isEmpty()) {
                animals.remove(key);
                if (!plants.contains(key)){
                    statisticsGenerator.freePositionCountUpdate(false);
                }
            }
        }
    }

    private void place(Animal animal) {

        Vector2d animalPosition = animal.getPosition();
//        jeśli już są jakieś zwierzęta na tej pozycji, to po prostu dodajemy danego zwierzaka do listy
        if (animals.containsKey(animalPosition)) {
            animals.get(animalPosition).add(animal);
        }
//        a jeśli nie, to tworzymy na tej pozycji nową kolekcję
        else {
            LinkedList<Animal> animalList = new LinkedList<>();
            animalList.add(animal);
            animals.put(animalPosition, animalList);

            if (!plants.contains(animalPosition)){
                statisticsGenerator.freePositionCountUpdate(true);
            }
        }
    }

    //    Skręt i przemieszczanie każdego zwierzaka
    private void move(Animal animal) {

        Vector2d targetPosition = getNextPosition(animal);
//            animal.move();
        this.place(animal);
//        TODO poinformuj wizualizację że zwierzak zmienił pozycję (fizycznie musi teraz zmienić pozycję)

        if (plants.contains(targetPosition)) {
            plantsToEat.add(targetPosition);
        }
    }

    @Override
    public void moveEveryAnimal() {

        for (List<Animal> animalList : animals.values()) {

            while (!animalList.isEmpty()){
                //usuwamy zwierzaka z listy, bo najprawdopodobniej i tak zmieni pozycję
                move(animalList.remove(0));
            }
        }
    }

    protected Vector2d getNextPosition(Animal animal) {

//        tutaj trzeba będzie pobrać kolejną pozycję zwierzaka

        Vector2d targetPosition = animal.

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

        List<Animal> animalsAtPosition = animals.get(position);
        int index = animalsAtPosition.size() - 1;
        Animal animal1 = animalsAtPosition.remove(index);
        ArrayList<Animal> competingAnimals = new ArrayList<>();
        competingAnimals.add(animal1);

//        jeśli w kolejce są zwierzaki o takiej samej energii, wieku i ilości dzieci, tworzymy listę
//        tych zwierząt
        for (int i = index - 1; i >= 0; --i) {
            Animal animal2 = animalsAtPosition.remove(i);
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
            List<Animal> animalList = animals.get(position);
            int index = animalList.size() - 1;

            while (!animalList.isEmpty()) {

                if (animalList.get(index).canProcreate()) {
                    Animal animal = animalList.remove(index);
                    procreatingAnimals.add(animal);
                } else {
                    break;
                }
                --index;
            }

            for (int i = procreatingAnimals.size() - 1; i <= 0; i = i - 2) {
                Animal kid = procreatingAnimals.get(i).procreate(procreatingAnimals.get(i + 1));
                children.add(kid);
                statisticsGenerator.newbornAnimalUpdate(kid);

                for (Animal animal : procreatingAnimals) {
                    animals.get(position).add(animal);
                }

                for (Animal animal : children) {
                    animals.get(position).add(animal);
                }
            }
        }
    }


        //        Wzrastanie nowych roślin na wybranych polach mapy

    @Override
    public void refreshMap () {
        ++day; // TODO zastanowić się czy nie przełożyć tego do symulacji
        createNewPlants(dailyPlantCount);
        for (List<Animal> animalList : animals.values()) {
            animalList.sort(animalComparator);
        }
    }

    public void createNewPlants ( int plantCount){
//       parametr plantCount to albo startowa ilość roślin, albo ilość roślin wyrastająca w 1 dzień
        ArrayList<Vector2d> positions = plantPositionGenerator.getPositions(plants, plantCount);

        for (Vector2d position : positions) {
            plants.add(position);
            statisticsGenerator.plantCountUpdate(true);
            if (!animals.containsKey(position)){
                statisticsGenerator.freePositionCountUpdate(false);
            }
        }
    }

    protected Vector2d createRandomPosition (Boundary boundary){

        Random random = new Random();
        return new Vector2d(random.nextInt(boundary.leftX(), boundary.rightX()),
                random.nextInt(boundary.lowerY(), boundary.upperY()));
    }

    public Boundary getCurrentBounds () {
        return mapBoundary;
    }

    public void mapChanged (String changeInfo){
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).mapChanged(this, changeInfo);
        }
    }

    public void addObserver (MapChangeListener observer){
        this.observers.add(observer);
    }

    public void removeObserver (MapChangeListener observer){
        this.observers.remove(observer);
    }

}