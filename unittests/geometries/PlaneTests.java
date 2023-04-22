package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Unit tests for geometries.Plane class
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}
     */
    @Test
    public void testConstructor() {
        // =============== Boundary Values Tests ==================
        // BVA1: two points are the same
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 1, 0)),
                "Constructed a plane with two points that are the same");
        // BVA2: all point on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 2), new Point(0, 0, 3)),
                "Constructed a plane with all point on the same line");

    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP1: test normal vector
        Plane plane = new Plane(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));
        double n = Math.sqrt(1d / 3);
        Vector result = plane.getNormal(new Point(0, 0, 1));
        //ensure the result is correct
        assertTrue(result.equals(new Vector(n, n, n))
                        || result.scale(-1).equals(new Vector(n, n, n)),
                "Wrong normal to plane");
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001,
                "Plane's normal is not a unit vector");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(Ray)}
     */
    @Test
    public void testFindIntersections() {
        Plane plane = new Plane(new Point(1, 1, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        // ============ Equivalence Partitions Tests ==============
        // EP1: Neither parallel nor orthogonal ray, starts outside and intersects the plane
        Ray ray = new Ray(new Point(1, 0, 0), new Vector(1, 1, 1));
        assertEquals(List.of(new Point(2, 1, 1)), plane.findIntersections(ray),
                "Neither parallel nor orthogonal ray failed to intersect");
        // EP2: Neither parallel nor orthogonal ray, starts outside and doesn't intersect the plane
        ray = new Ray(new Point(1, 0, 0), new Vector(-1, -1, -1));
        assertNull(plane.findIntersections(ray), "Neither parallel nor orthogonal ray failed to NOT intersect");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        // BVA1: Ray included in the plane
        ray = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "Parallel ray included in plane failed to NOT intersect");
        // BVA2: Ray not included in the plane
        ray = new Ray(new Point(1, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "Parallel ray not included in plane failed to NOT intersect");

        // **** Group: Ray is orthogonal to the plane
        // BVA3: Point p0 is before the plane
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(1, 0, 1)), plane.findIntersections(ray),
                "Orthogonal ray with p0 before plane failed to intersect");
        // BVA4: Point p0 is in the plane
        ray = new Ray(new Point(1, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "Orthogonal ray with p0 in plane failed to NOT intersect");
        // BVA5: Point p0 is after the plane
        ray = new Ray(new Point(1, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "Orthogonal ray with p0 after plane failed to NOT intersect");

        // **** Group: Ray is neither parallel nor orthogonal to the plane
        // BVA6: Ray begins at the plane (p0 is in the plane, but not the ray)
        ray = new Ray(new Point(0, 1, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray),
                "Neither parallel nor orthogonal ray, ray begins at plane, failed to NOT intersect");
        // BVA7: Ray begins in the same point which appears as reference point in the plane (Q)
        ray = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray),
                "Neither parallel nor orthogonal ray, ray begins at reference point, failed to NOT intersect");

    }
}