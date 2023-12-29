package model;

import java.util.*;


public abstract class AbstractMap {

    protected Boundary mapBoundary;

    protected HashMap<Vector2d, TreeSet<Animal>> animals;

//    Rośliny nie mają żadnych cech poza położeniem, dlatego wystarczy przechowywać je jako
//    ich współrzędne na mapie
    private HashSet<Vector2d> plants;

//    a tutaj na bieżąco dodajemy trawę do zjedzenia przez zwierzę
    private HashSet<Vector2d> plantsToEat;

    private PlantPositionGenerator plantPositionGenerator;

    public AbstractMap(MapProperties mapProperties, AnimalProperties animalProperties){

        mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1,
                0, mapProperties.mapHeight() - 1 );

        animals = new HashMap<>();

        for (int i = 0; i < mapProperties.startAnimalCount(); ++i) {
//            do uzupełnienia parametry przekazywane do konstruktora zwierzaka,
//            zapewne wszystkie AnimalProperties
            place(new Animal());
        }

        plants = new HashSet<>();
        plantsToEat = new HashSet<>();

        plantPositionGenerator = new PlantPositionGenerator(mapBoundary);

        createNewPlants(mapProperties.startPlantCount());
    }

//    Usunięcie martwych zwierzaków z mapy

    public void removeDeadAnimals(){

        for (Vector2d key : animals.keySet()){

//            pozyskanie setu zwierzaków na danej pozycji
            TreeSet<Animal> animalSet = animals.get(key);

//            Usuwanie martwych zwierząt
            animalSet.removeIf(animal -> animal.getEnergy() == 0);

//            Jeśli set został pusty, usuwamy go z hashmapy
            if (animalSet.isEmpty()){
                animals.remove(key);
            }
        }
    }

    public void place(Animal animal) {

        Vector2d animalPosition = animal.getPosition();
//        jeśli już są jakieś zwierzęta na tej pozycji, to po prostu dodajemy danegp zwierzaka do setu
        if (animals.containsKey(animalPosition)) {
            animals.get(animalPosition).add(animal);
        }
//        a jeśli nie, to tworzymy na tej pozycji nowy set
        else{
            TreeSet<Animal> animalSet = new TreeSet<>(new AnimalComparator());
            animalSet.add(animal);
            animals.put(animalPosition, animalSet);
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

            if (plants.contains(targetPosition)){
                plantsToEat.add(targetPosition);
            }
        }
    }

    public void moveEveryAnimal(){

        for (TreeSet<Animal> animals : animals.values()){
            for (Animal animal : animals){
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
        } else if ( x > mapBoundary.rightX()) {
            x = mapBoundary.leftX();
        }

        return new Vector2d(x, y);
    }


//    Konsumpcja roślin, na których pola weszły zwierzaki

    public void removeEatenPlants(){

        for (Vector2d plantPosition : plantsToEat){
//            wybór zwierzaka, który zje roślinę:
            animals.get(plantPosition).last().eat();
            plantsToEat.remove(plantPosition);
            }
        plantsToEat = new HashSet<>();
    }


//Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.

    public void procreate() {

        for (Vector2d position : animals.keySet()) {

            Iterator<Animal> animalIterator = animals.get(position).descendingIterator();

            Animal animal = animalIterator.next();

            do {
                Animal nextAnimal = animalIterator.next();

                if(nextAnimal == null || !nextAnimal.canProcreate()) {
                    break;
                }
                else {
                    animals.get(position).add(animal.procreate(nextAnimal));
                }

                if (!animalIterator.hasNext()){
                    break;
                }

                animal = animalIterator.next();

            } while (true);

        }

    }

    //        Wzrastanie nowych roślin na wybranych polach mapy

    public void createNewPlants(int plantCount) {

//        parametr plantCount to albo startowa ilość roślin, albo ilość roślin wyrastająca w 1 dzień

        plants.addAll(plantPositionGenerator.getPositions(plants, plantCount));

    }

    protected Vector2d createRandomPosition(Boundary boundary) {

        Random random = new Random();
        return new Vector2d(random.nextInt(boundary.leftX(), boundary.rightX()),
                random.nextInt(boundary.lowerY(), boundary.upperY()));
    }
}