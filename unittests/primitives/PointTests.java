package primitives;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for primitives.Point class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */

public class PointTests {

    /**
     *  Test method for {@link Point#add(Vector)}.
     *  Checks addition
     */
    @Test
    public void testAdd() {
        Point p0 = new Point(0,0,0);
        Vector v1 = new Vector(1,1,1);
        // ============ Equivalence Partitions Tests ==============
        // EP1: Addition with a positive result
        assertEquals(new Point (1,1,1), p0.add(v1), "Incorrect addition");

        // =============== Boundary Values Tests ==================
        // BVA1: Addition with a negative result
        assertThrows(IllegalArgumentException.class, ()->p0.add(new Vector(0,0,0)), "impossible addition");
    }

    /**
     *  Test method for {@link Point#subtract(Point)}.
     *  Checks subtraction
     */
    @Test
    public void testSubtract() {
        Point p1 = new Point(2, 4, 3);
        Point p2 = new Point(1,1,1);
        Point p3 = new Point (-5,-6,-7);

        // =============== Boundary Values Tests ==================
        // BVA1: Subtraction results in vector 0
        assertThrows(IllegalArgumentException.class, ()->p1.subtract(p1), "Subtraction results in vector 0");

        // ============ Equivalence Partitions Tests ==============
        // EP1: Result is positive
        Assertions.assertEquals(new Vector(1,3,2),p1.subtract(p2),"Vector subtraction with positive result failed");
        // EP2: Result is negative
        assertEquals(new Vector(-1,-3,-2),p2.subtract(p1),"Vector subtraction with negative result failed");
        // EP3: Subtracting a negative number
        assertEquals(new Vector(7,10,10),p1.subtract(p3),"Vector subtraction with negative vector failed");
    }

    /**
     *  Test method for {@link Point#distance(Point)}.
     *  Checks distance calculation
     */
    @Test
    public void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // EP1: Distance between two points is calculated correctly
        assertEquals(9, new Point (0,0,0).distance(new Point (0,0,9)), "Problem with distance calculation");
    }

    /**
     *  Test method for {@link Point#distanceSquared(Point)}.
     *  Checks distance (squared) calculation - sum of squared coordinates
     */
    @Test
    public void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // EP1: Squared distance between two points is calculated correctly
        assertEquals(3, new Point (0,0,0).distanceSquared(new Point (1,1,1)), "Problem with distance calculation");
    }
}