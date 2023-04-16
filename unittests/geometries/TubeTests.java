package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class TubeTests {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}
     */
    @Test
    public void testGetNormal() {
        Tube tube = new Tube(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        // ============ Equivalence Partitions Tests ==============
        //TC01: test normal vector
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(0, 1, 1)),
                "Bad normal to tube");

        // =============== Boundary Values Tests ==================
        //TC11: point forms a right angle with ray's head
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(0, 1, 0)),
                "Bad normal to tube - point forms a right angle with ray's head");
    }

    @Test
    public void testFindIntersections() {

    }
}