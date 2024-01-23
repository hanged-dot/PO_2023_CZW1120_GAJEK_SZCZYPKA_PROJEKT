package WordMap;


import Records.MoveDirection;

public class OptionsParser {
    public static MoveDirection parse(int args) throws IllegalArgumentException{
            return switch (args) {
                case 0 -> MoveDirection.FORWARD;
                case 1 -> MoveDirection.FORWARDRIGHT;
                case 2 -> MoveDirection.RIGHT;
                case 3 -> MoveDirection.BACKWARDRIGHT;
                case 4 -> MoveDirection.BACKWARD;
                case 5 -> MoveDirection.BACKWARDLEFT;
                case 6 -> MoveDirection.LEFT;
                case 7 -> MoveDirection.FORWARDLEFT;
                default -> throw new IllegalStateException("Unexpected value: " + args);
               };
        }

    }

