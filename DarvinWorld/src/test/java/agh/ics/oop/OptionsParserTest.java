package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class OptionsParserTest {
    @Test
    public void testOptionsPars(){
        assertEquals(MoveDirection.FORWARD,OptionsParser.parse(0));
        assertEquals(MoveDirection.FORWARDRIGHT,OptionsParser.parse(1));
        assertEquals(MoveDirection.RIGHT,OptionsParser.parse(2));
        assertEquals(MoveDirection.BACKWARD,OptionsParser.parse(3));
        assertEquals(MoveDirection.BACKWARDRIGHT,OptionsParser.parse(4));
        assertThrows(IllegalArgumentException.class ,() -> OptionsParser.parse(8));
    }
}
