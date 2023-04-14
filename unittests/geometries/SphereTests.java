package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test normal vector
        Sphere sphere = new Sphere(1, new Point(0, 0, 0));
        Vector result = sphere.getNormal(new Point(0, 0, 1));
        //ensure the result is correct
        assertEquals(result, new Vector(0, 0, 1), "Bad normal to sphere");
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001,
                "Sphere's normal is not a unit vector");
    }
}