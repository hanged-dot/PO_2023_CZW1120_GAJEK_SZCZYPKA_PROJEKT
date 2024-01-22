package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void testNext() {
        assertEquals(MapDirection.NORTH,MapDirection.NORTHWEST.next());
        assertEquals(MapDirection.NORTHWEST,MapDirection.WEST.next());
        assertEquals(MapDirection.WEST,MapDirection.SOUTHWEST.next());
        assertEquals(MapDirection.SOUTHWEST,MapDirection.SOUTH.next());
        assertEquals(MapDirection.SOUTH,MapDirection.SOUTHEAST.next());
        assertEquals(MapDirection.SOUTHEAST,MapDirection.EAST.next());
        assertEquals(MapDirection.EAST,MapDirection.NORTHEAST.next());
        assertEquals(MapDirection.NORTHEAST,MapDirection.NORTH.next());
    }
    @Test
    public void testPrevious(){
        assertEquals(MapDirection.NORTH,MapDirection.NORTHEAST.previous());
        assertEquals(MapDirection.NORTHEAST,MapDirection.EAST.previous());
        assertEquals(MapDirection.EAST,MapDirection.SOUTHEAST.previous());
        assertEquals(MapDirection.SOUTHEAST,MapDirection.SOUTH.previous());
        assertEquals(MapDirection.SOUTH,MapDirection.SOUTHWEST.previous());
        assertEquals(MapDirection.SOUTHWEST,MapDirection.WEST.previous());
        assertEquals(MapDirection.WEST,MapDirection.NORTHWEST.previous());
        assertEquals(MapDirection.NORTHWEST,MapDirection.NORTH.previous());
    }
}
