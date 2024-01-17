package agh.ics.oop.model;

public class Plant implements WorldElement{

        private Vector2d position;
        public Plant(Vector2d position){
            this.position=position;
        }

        public Vector2d getPosition(){
            return position;
        }


        public String toString(){
            return "*";
        }

        @Override
        public String getPicture() {
            return "icons/plant.png";
        }
}
