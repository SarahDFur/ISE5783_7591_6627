package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for geometries.Plane class
 */
public class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}
     */
    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        //TC01: two points are the same
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 1, 0)),
                "Constructed a plane with two points that are the same");
        //TC02: all point on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 2), new Point(0, 0, 3)),
                "Constructed a plane with all point on the same line");

    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test normal vector
        Plane plane = new Plane(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));
        double n = Math.sqrt(1d / 3);
        Vector result = plane.getNormal(new Point(0, 0, 1));
        //ensure the result is correct
        assertTrue(result.equals(new Vector(n, n, n))
                        || result.scale(-1).equals(new Vector(n, n, n)),
                "Bad normal to plane");
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001,
                "Plane's normal is not a unit vector");
    }

}