package model;

public class Animal{

    private boolean transferredThroughTunnel;

    public Vector2d getPosition(){
//        zwraca pozycję zwierzaka
    }

    public int getEnergy(){
//        zwraca informację o energii zwierzaka
    }

    public void eat(){
//
    }

    public void move(){}

    public boolean canProcreate(){
//        zwraca informację czy zwierzak da radę się rozmnożyć
    }

    public Animal procreate(Animal partner){
//        tu proponuję żeby metoda przyjmowała zwierzę będące partnerem i zwracała potomka
    }

    public boolean isTransferredThroughTunnel(){
        return transferredThroughTunnel;
    }

    public void setTransferredThroughTunnel(boolean transferredThroughTunnel){
        this.transferredThroughTunnel = transferredThroughTunnel;
    }

    public int getAge();
    public int getNumberOfChildren();

    private int compareEnergy(Animal a);
    private int compareAge(Animal a);
    private int compareNumberOfChildren(Animal a);


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


}
