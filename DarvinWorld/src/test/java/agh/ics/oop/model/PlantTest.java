package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantTest {

        @Test
        public void testGetPosition(){
            Plant g = new Plant(new Vector2d(2,2));
            assertEquals(new Vector2d(2,2),g.getPosition());
        }
        @Test
        public void testToString(){
            Plant g = new Plant(new Vector2d(2,2));
            assertEquals("*",g.toString());
        }
}
