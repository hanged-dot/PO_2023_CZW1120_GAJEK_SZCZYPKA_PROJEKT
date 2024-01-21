package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    //equals(Object other), toString(), precedes(Vector2d other), follows(Vector2d other), upperRight(Vector2d other), lowerLeft(Vector2d other), add(Vector2d other), subtract(Vector2d other), opposite()
    @Test
    public void testEquals(){
        Vector2d test1 = new Vector2d(1,0);
        assertFalse(test1.equals(new Vector2d(0,1)));
        assertTrue(test1.equals(new Vector2d(1,0)));
        assertFalse(test1.equals("smiec1"));
    }
    @Test
    public void testToString(){
        assertEquals("(1,0)",new Vector2d(1,0).toString());
        assertEquals("(1,1)",new Vector2d(1,1).toString());
    }
    @Test
    public void testPrecedes(){
        assertTrue(new Vector2d(1,0).precedes(new Vector2d(2,2)));
        assertFalse(new Vector2d(1,0).precedes(new Vector2d(0,1)));
    }
    @Test
    public void testFollows(){
        assertTrue(new Vector2d(2,2).follows(new Vector2d(1,0)));
        assertFalse(new Vector2d(1,0).follows(new Vector2d(0,1)));
    }
    @Test
    public void testUpperRight(){
        assertEquals(new Vector2d(2,3), new Vector2d(2,2).upperRight(new Vector2d(1,3)));
    }
    @Test
    public void testLowerLeft(){
        assertEquals(new Vector2d(1,2), new Vector2d(2,2).lowerLeft(new Vector2d(1,3)));
    }
    @Test
    public void testAdd(){
        assertEquals(new Vector2d(2,3), new Vector2d(1,2).add(new Vector2d(1,1)));
    }
    @Test
    public void testSubtract(){
        assertEquals(new Vector2d(4,3), new Vector2d(5,7).subtract(new Vector2d(1,4)));
    }
    @Test
    public void testOpposite(){
        assertEquals(new Vector2d(-1,-2), new Vector2d(1,2).opposite());
    }
}
