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

}
