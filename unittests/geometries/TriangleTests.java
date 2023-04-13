package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Triangle class
 */
public class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test normal vector
        Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));
        double n = Math.sqrt(1d / 3);
        Vector result = triangle.getNormal(new Point(0, 0, 1));
        //ensure the result is correct
        assertTrue(result.equals(new Vector(n, n, n))
                        || result.scale(-1).equals(new Vector(n, n, n)),
                "Bad normal to triangle");
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001,
                "Triangle's normal is not a unit vector");
    }

}