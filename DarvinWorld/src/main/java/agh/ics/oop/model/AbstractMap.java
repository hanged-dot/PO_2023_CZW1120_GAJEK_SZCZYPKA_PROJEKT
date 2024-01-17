package agh.ics.oop.model;

import agh.ics.oop.model.util.PlantPositionGenerator;
import com.sun.scenario.animation.shared.AnimationAccessor;

import java.util.*;

import static java.lang.Math.round;
import static java.util.Collections.min;


public abstract class AbstractMap implements WorldMap {

    protected final Boundary mapBoundary;
    protected HashMap<Vector2d, ArrayList<Animal>> beforeMoveAnimals;
    protected HashMap<Vector2d, ArrayList<Animal>> afterMoveAnimals;

    protected ArrayList<MapChangeListener> observers = new ArrayList<>();
    private HashSet<Vector2d> plants;
    private HashSet<Vector2d> plantsToEat;
    private final PlantPositionGenerator plantPositionGenerator;
    private final int dailyPlantCount;
    protected SimulationStatisticsGenerator statisticsGenerator;
    private final AnimalComparator animalComparator;
    int day; // current map day

    private MapChangeListener observer;

    public AbstractMap(MapProperties mapProperties,
                       AnimalProperties animalProperties,
                       MapChangeListener observer) {

        this.observer = observer;

        statisticsGenerator = new SimulationStatisticsGenerator(mapProperties);
        animalComparator = new AnimalComparator();
        day = 1;

        mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1,
                0, mapProperties.mapHeight() - 1);

        beforeMoveAnimals = new HashMap<>();
        afterMoveAnimals = new HashMap<>();

        plants = new HashSet<>();
        plantsToEat = new HashSet<>();
        dailyPlantCount = mapProperties.dailyPlantCount();

        plantPositionGenerator = new PlantPositionGenerator(mapBoundary);

        for (int i = 0; i < mapProperties.startAnimalCount(); ++i) {
//            tworzymy nowe zwierzątka, generujemy im genom, umieszczamy na mapie
            Animal animal = new Animal(animalProperties, createRandomPosition(mapBoundary));
            animal.setGenome(createRandomGenome(animalProperties.genomeLength()));
            place(animal);
        }
        beforeMoveAnimals.putAll(afterMoveAnimals);
        afterMoveAnimals.clear();
        System.out.println(beforeMoveAnimals.size());

        createNewPlants(mapProperties.startPlantCount());
    }

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
            if (!plants.contains(key)){
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

//        Jeśli na nowym miejscu zwierzaka znajduje się roślina, dołączamy ją do listy roślin do zjedzenia w następnym dniu
            if (plants.contains(animalPosition)) {
                plantsToEat.add(animalPosition);
            } else {
//            w przeciwnym wypadku pozycja ta do tej pory była wolna, zatem musimy zmniejszyć licznik wolnych pozycji
                statisticsGenerator.freePositionCountUpdate(false);
            }
        }
    }

    //    Skręt i przemieszczanie każdego zwierzaka
    private void move(Animal animal) {

//        Wyznaczamy docelową pozycję zwierzaka
        Vector2d targetPosition = getNextPosition(animal);
//        Informujemy zwierzaka o zmianie jego położenia
        animal.move(targetPosition);
//        Umieszczamy zwierzaka na odpowiednim miejscu na mapie
        this.place(animal);
//        TODO poinformuj wizualizację że zwierzak zmienił pozycję (fizycznie musi teraz zmienić pozycję)

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
    //             zwracamy najedzonego zwierzaka do listy
            afterMoveAnimals.get(plantPosition).add(eatingAnimal);
            plants.remove(plantPosition);
            statisticsGenerator.plantCountUpdate(false);
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
                competingAnimals.add(animalsAtPosition.remove(i));
            } else {
                break;
            }
        }

        if (competingAnimals.size() == 1) {
            System.out.println("Na pozycji "+position+" wyggrywa zwierz z energią "+animal1.getEnergy());
            return animal1;
        } else {
//            losujemy zwierzaka z listy zwierzaków rywalizujących o jedzenie
            Random random = new Random();
            int temp = random.nextInt(0, competingAnimals.size() - 1);
            for (int i = 0; i < competingAnimals.size(); ++i){
                if (i != temp){
                    animalsAtPosition.add(competingAnimals.get(i));
                }
            }
            System.out.println("Na pozycji "+position+" wyggrywa zwierz z energią "+competingAnimals.get(temp).getEnergy());
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
                    statisticsGenerator.newbornAnimalUpdate(child);
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
        int numberOfAnimals = 0;
        for (List<Animal> list : afterMoveAnimals.values()){
            numberOfAnimals += list.size();
        }
        System.out.println("Zakończono dzień "+day+". Pozostaje zwierzaków: "+numberOfAnimals);

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
            plants.add(position);
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
}