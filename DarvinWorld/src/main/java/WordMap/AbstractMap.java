package WordMap;


import Presenter.MapChangeListener;
import Presenter.SimulationPresenter;
import Records.*;
import WorldElement.Animal;
import WorldElement.AnimalComparator;
import WorldElement.Plant;
import WorldElement.PlantPositionGenerator;
import WorldElement.WorldElement;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;


public abstract class AbstractMap implements WorldMap {

    protected final Boundary mapBoundary;
    private final UUID identifier;
    private HashMap<Vector2d, LinkedList<Animal>> animals;
    protected HashMap<Vector2d, ArrayList<Animal>> beforeMoveAnimals;
    protected HashMap<Vector2d, ArrayList<Animal>> afterMoveAnimals;

    protected ArrayList<MapChangeListener> observers = new ArrayList<>();
    private HashMap<Vector2d, Plant> plants;
    private HashSet<Vector2d> plantsToEat;
    private final PlantPositionGenerator plantPositionGenerator;
    private final int dailyPlantCount;
    private final AnimalComparator animalComparator;
    private Animal ObservedAnimal;
    SimulationPresenter simulationPresenter;

//    Statistics:

    int day;
    private int aliveAnimalCount;
    private int deadAnimalCount;
    private int deadAnimalLifeSpan;
    private final int allPositionCount;
    private final int totalPositionCount;
    private int totalAliveAnimalOffspringCount;
    private int totalEnergy;
    private int energyFromPlant;
    private HashMap<int[], Integer> allGenotypes;
    private HashMap<int[], Integer> aliveGenotypes;
    private HashMap<Vector2d, Integer> plantHistory;

    private SimulationStatistics simulationStatistics;

    public AbstractMap(MapProperties mapProperties, AnimalProperties animalProperties, SimulationPresenter simulationPresenter) {

        this.simulationPresenter = simulationPresenter;
        this.ObservedAnimal=null;
        this.identifier= UUID.randomUUID();

        this.animalComparator = new AnimalComparator();
        this.mapBoundary = new Boundary(0, mapProperties.mapWidth() - 1, 0, mapProperties.mapHeight() - 1);
        this.animals = new HashMap<>();

        beforeMoveAnimals = new HashMap<>();
        afterMoveAnimals = new HashMap<>();

        this.dailyPlantCount = mapProperties.dailyPlantCount();

        plants = new HashMap<>();
        plantsToEat = new HashSet<>();

        this.plantPositionGenerator = new PlantPositionGenerator(mapBoundary);

        this.day = 0;
        this.aliveAnimalCount = mapProperties.startAnimalCount();
        this.deadAnimalCount = 0;
        this.deadAnimalLifeSpan = 0;
        this.allPositionCount = mapProperties.mapWidth()*mapProperties.mapHeight();
        this.totalPositionCount = allPositionCount;
        this.totalAliveAnimalOffspringCount = 0;
        this.allGenotypes = new HashMap<>();
        this.aliveGenotypes = new HashMap<>();
        this.plantHistory = new HashMap<>();
        this.totalEnergy = aliveAnimalCount * animalProperties.startAnimalEnergy();
        this.energyFromPlant = animalProperties.energyFromPlant();


        for (int i = 0; i < mapProperties.startAnimalCount(); ++i) {
//            tworzymy nowe zwierzątka, generujemy im genom, umieszczamy na mapie
            Animal animal = new Animal(animalProperties, createRandomPosition(mapBoundary));
            animal.setGenome(createRandomGenome(animalProperties.genomeLength()));
            allGenotypeCountUpdate(animal.getGenome(), true);
            place(animal);
        }

        beforeMoveAnimals.putAll(afterMoveAnimals);
        afterMoveAnimals.clear();
        createNewPlants(mapProperties.startPlantCount());

        simulationStatistics = generateSimulationStatistics();

        this.simulationPresenter.setWorldMap(this);

        mapChanged("New map created");
    }
    @Override
    public UUID getID() {
        return identifier;
    }

    @Override
    public int getDay() {return this.day;}
    public HashMap<Vector2d,LinkedList<Animal>> getAnimals(){
        return this.animals;
    }

//    Usunięcie martwych zwierzaków z mapy

    @Override
    public ArrayList<Vector2d> getDeadAnimals() {

        ArrayList<Vector2d> keysToRemove = new ArrayList<>();

        for (Vector2d key : beforeMoveAnimals.keySet()) {
//            pozyskanie listy zwierzaków na danej pozycji
            ArrayList<Animal> animalList = beforeMoveAnimals.get(key);

            animalList.removeIf(a ->
            {
                if (a.getEnergy() == 0){
                    a.setDeath(this.day);
                    deadAnimalCount++;
                    deadAnimalLifeSpan += a.getAge();
                    aliveGenotypeCountUpdate(a.getGenome(), false);
                    return true;
                }
                return false;
            });

//            Jeśli lista została pusta, usuwamy ją z hashmapy i update wolnych pozycji na mapie
            if (animalList.isEmpty()) {
                keysToRemove.add(key);
            }
        }

        return keysToRemove;
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
    }

    @Override
    public void moveEveryAnimal() {

        for (List<Animal> animalList : beforeMoveAnimals.values()) {

            for (Animal animal : animalList) {
                move(animal);
                animal.age();
            }
        }
        var comparator = new AnimalComparator();
        for (List<Animal> animalList: afterMoveAnimals.values()){
            animalList.sort(comparator);
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

                for (int i = procreatingAnimals.size() - 1; i > 0; i-=2) {

                    Animal child = procreatingAnimals.get(i).procreate(procreatingAnimals.get(i - 1));
                    children.add(child);
                    aliveAnimalCount++;
                    totalKidsCountUpdate(1);
                    allGenotypeCountUpdate(child.getGenome(), true);
                }

                for (Animal animal : children) {
                    afterMoveAnimals.get(position).add(animal);
                }
            }
        }
    }

    @Override
    public boolean refreshMap () {

        if (afterMoveAnimals.isEmpty()){
            simulationStatistics = generateSimulationStatistics();
            return false;
        }

        // Rozpoczęcie nowego dnia:

        createNewPlants(dailyPlantCount);

        ++day;


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

        var animalsToRemove = getDeadAnimals();
        for (var animalToRemove : animalsToRemove){
            beforeMoveAnimals.remove(animalToRemove);
        }
        animalsToRemove.clear();

        if (beforeMoveAnimals.isEmpty()){
            return false;
        }

        totalEnergy -= getAliveAnimalCount();

        mapChanged("It's a new day! update");
        simulationStatistics = generateSimulationStatistics();
        return true;
    }

    //        Wzrastanie nowych roślin na wybranych polach mapy

    private void createNewPlants (int numOfPlants){

//       parametr plantCount to albo startowa ilość roślin, albo ilość roślin wyrastająca w 1 dzień
        ArrayList<Vector2d> positions = plantPositionGenerator.getPositions(plants, numOfPlants);
        for (Vector2d position : positions) {
            Plant p = new Plant(position);
            plants.put(position, p);

//  Na każdym polu, na którym wyrasta roślina, zwiększamy licznik roślin
            plantHistoryUpdate(position);
        }
    }

    private void plantHistoryUpdate(Vector2d position){

        if (!plantHistory.containsKey(position)){
            plantHistory.put(position, 1);
        }
        else {
            Integer recentPlantCount = plantHistory.remove(position);
            plantHistory.put(position, recentPlantCount + 1);
        }
    }

    @Override
    public SimulationStatistics getSimulationStatistics(){
        return this.simulationStatistics;
    }

    private SimulationStatistics generateSimulationStatistics(){

        return new SimulationStatistics(
                day,
                getAliveAnimalCount(),
                deadAnimalCount,
                plants.size(),
                getFreePositionCount(),
                getDominantGenotype(aliveGenotypes),
                getDominantGenotype(allGenotypes),
                getMeanAliveEnergy(),
                getMeanLifeSpan(),
                getMeanAliveAnimalOffspringCount()
        );
    }

    private int getTotalEnergy(){
        int totalEnergy = 0;
        for (List<Animal> animalList : beforeMoveAnimals.values()){
            for (Animal a : animalList){
                totalEnergy += a.getEnergy();
            }
        }
        return totalEnergy;
    }

    private int getFreePositionCount(){
        int ctr = allPositionCount - getAliveAnimalCount() - plants.size();
        for (Vector2d v : plants.keySet()){
            if (beforeMoveAnimals.containsKey(v)){
                ctr++;
            }
        }
        return ctr;
    }

    private int getAliveAnimalCount(){

        int ctr = 0;
        for (List<Animal> animalList: beforeMoveAnimals.values()){
            ctr += animalList.size();
        }
        return ctr;
    }

    private int[] getDominantGenotype(HashMap<int[], Integer> genotypes){

        if (genotypes == null){
            return new int[0];
        }

        int[] genotype = new int[0];
        int maxCount = 0;

        var genotypesKS = genotypes.keySet();
        for (int[] g: genotypesKS){
            if (genotypes.get(g) > maxCount){
                genotype = g;
            }
        }
        return genotype;
    }

    private float getMeanAliveEnergy(){
        int aliveAnimalCount = getAliveAnimalCount();
        if(aliveAnimalCount > 0) {
            return (float) getTotalEnergy() / (float) aliveAnimalCount;
        }
        return 0;
    }

    private float getMeanLifeSpan(){
        if (deadAnimalCount != 0){
            return (float) deadAnimalLifeSpan / deadAnimalCount;
        } else {
            return 0;
        }
    }

    public void allGenotypeCountUpdate(int[] genotype, boolean up) {
        genotypeCountUpdate(genotype, allGenotypes, up);
        genotypeCountUpdate(genotype, aliveGenotypes, up);
    }
    public void aliveGenotypeCountUpdate(int[] genotype, boolean up){
        genotypeCountUpdate(genotype, aliveGenotypes, up);
    }

    private void genotypeCountUpdate(int[] genotype, HashMap<int[], Integer> genotypes, boolean up) {

        if (up){
            if (genotypes.containsKey(genotype)){
                int n = genotypes.get(genotype);
                genotypes.put(genotype, n + 1);
            } else {
                genotypes.put(genotype, 1);
            }
        } else {
            int n = genotypes.get(genotype);
            genotypes.put(genotype, n - 1);
        }
    }

    public void totalKidsCountUpdate(int kidsCount) {
        totalAliveAnimalOffspringCount = max(0, totalAliveAnimalOffspringCount + kidsCount);
    }

    private float getMeanAliveAnimalOffspringCount(){
        if(aliveAnimalCount > 0) {
            return (float) totalAliveAnimalOffspringCount / aliveAnimalCount;
        }
        return 0;
    }

    protected Vector2d createRandomPosition (Boundary boundary){

        Random random = new Random();
        return new Vector2d(random.nextInt(boundary.leftX(), boundary.rightX()+1),
                random.nextInt(boundary.lowerY(), boundary.upperY()+1));
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
        int[] dominantGenotype = getSimulationStatistics().dominantGenotype();

        for (ArrayList<Animal> animalList : beforeMoveAnimals.values()){
            for (Animal animal : animalList){
                if (Arrays.equals(dominantGenotype, animal.getGenome())){
                    animalsWithDominantGenotype.add(animal);
                }
            }
        }

        return animalsWithDominantGenotype;
    }

    public ArrayList<PositionAbundance> getPositionsPreferredByPlants(){

        int preferredPositionsCount = min(plantHistory.size(),
                (int)(0.1 * (float)totalPositionCount));

        ArrayList<PositionAbundance> positionAbundanceArrayList = new ArrayList<>();

//        iterujemy po wszystkich polach na których rosły kiedykolwiek rośliny
        for (var plantHist : plantHistory.entrySet())
        {
//            tworzymy rekord zawierający pozycję i ilość roślin, które na niej wyrosły
            var plantPositionAbundance = new PositionAbundance(plantHist.getKey(), plantHist.getValue());

//            jeśli w liście są jeszcze miejsca na nowe pozycje, zapełniamy ją
            if(positionAbundanceArrayList.size() < preferredPositionsCount)
            {
                positionAbundanceArrayList.add(plantPositionAbundance);
            }
//            w przeciwnym wypadku sprawdzamy, czy dana pozycja jest bardziej preferowana niz któraś z pozycji na liście
            else
            {
//                w lowestIndex szukamy najmniej preferowanej pozycji z naszej wynikowej listy
                int lowestIndex = 0;

                for(int i = lowestIndex + 1; i < preferredPositionsCount; ++i)
                {
                    if(positionAbundanceArrayList.get(lowestIndex).numberOfPlants() >
                            positionAbundanceArrayList.get(i).numberOfPlants())
                    {
                        lowestIndex = i;
                    }
                }

                if(plantHist.getValue() > positionAbundanceArrayList.get(lowestIndex).numberOfPlants()) {
                    positionAbundanceArrayList.remove(lowestIndex);
                    positionAbundanceArrayList.add(plantPositionAbundance);
                }
            }
        }
        return positionAbundanceArrayList;
    }


    public void mapChanged (String changeInfo){

        simulationPresenter.mapChanged(this, changeInfo);
        if (ObservedAnimal!=null) simulationPresenter.animalChanged(this.ObservedAnimal,this);
    }

    public WorldElement getStrongest(Vector2d position){
        ArrayList<Animal> pos1 = this.beforeMoveAnimals.get(position);
        int energy=0;
        WorldElement strongest = null;
        if(pos1==null){return strongest;}
        for (Animal a : pos1){
            if(a.getEnergy()>energy && a.getDeath()==0){
                energy=a.getEnergy();
                strongest= (WorldElement) a;
            }
        }
        return strongest;
    }

    public WorldElement getPlant(Vector2d position){
        if(this.plants.containsKey(position)) return this.plants.get(position);
        else return null;
    }

    public void addAnimalObserver (Animal animal){
        this.ObservedAnimal = animal;

    }
    public void removeAnimalObserver (Animal animal) {
        this.ObservedAnimal = null;
    }

}