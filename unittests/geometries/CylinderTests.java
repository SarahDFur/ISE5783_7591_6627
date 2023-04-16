package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}
     */
    @Test
    public void testGetNormal() {
        Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);
        // ============ Equivalence Partitions Tests ==============
        //TC01: test normal vector using point on top base of cylinder
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(0.5, 0.5, 1)),
                "Bad normal to Cylinder - top base");
        //TC02: test normal vector using point on bottom base of cylinder
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0.5, 0.5, 0)),
                "Bad normal to Cylinder - bottom base");
        //TC03: test normal vector using point on side of cylinder
        assertEquals(new Vector(0, 1, 0), cylinder.getNormal(new Point(0, 1, 0.5)),
                "Bad normal to Cylinder - side");

        // =============== Boundary Values Tests ==================
        //TC11: test normal vector using point at center of top base of cylinder
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(0, 0, 1)),
                "Bad normal to Cylinder - center of top base");
        //TC12: test normal vector using point at center of bottom base of cylinder
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0, 0, 0)),
                "Bad normal to Cylinder - center of bottom base");
    }

    @Test
    public void testFindIntersections() {

    }
}