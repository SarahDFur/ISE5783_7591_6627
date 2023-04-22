package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Triangle class
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP1: test normal vector
        Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));
        double n = Math.sqrt(1d / 3);
        Vector result = triangle.getNormal(new Point(0, 0, 1));
        //ensure the result is correct
        assertTrue(result.equals(new Vector(n, n, n))
                        || result.scale(-1).equals(new Vector(n, n, n)),
                "Wrong normal to triangle");
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001,
                "Triangle's normal is not a unit vector");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(Ray)}
     */
    @Test
    public void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 1, 0), new Point(-1, 0, 0), new Point(1, -1, 0));
        // ============ Equivalence Partitions Tests ==============
        // EP1: Ray intersects with triangle's plane inside the triangle
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(0.5, 0, -1));
        assertEquals(List.of(new Point(0.5, 0, 0)), triangle.findIntersections(ray),
                "Ray failed to intersect triangle inside the triangle");
        // EP2: Ray intersects with triangle's plane outside the triangle, in front of the triangle's edge
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 1, -1));
        assertNull(triangle.findIntersections(ray),
                "Ray failed to NOT intersect triangle outside the triangle, in front of edge");
        // EP3: Ray intersects with triangle's plane outside the triangle, in front of the triangle's vertex
        ray = new Ray(new Point(0, 0, 1), new Vector(2, -2, -1));
        assertNull(triangle.findIntersections(ray),
                "Ray failed to NOT intersect triangle outside the triangle, in front of vertex");

        // =============== Boundary Values Tests ==================
        // BVA1: Ray intersects with triangle's plane on the triangle's edge
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, -1));
        assertNull(triangle.findIntersections(ray), "Ray failed to NOT intersect triangle, on triangle's edge");
        // BVA2: Ray intersects with triangle's plane on the triangle's vertex
        ray = new Ray(new Point(0, 0, 1), new Vector(-1, 0, -1));
        assertNull(triangle.findIntersections(ray), "Ray failed to NOT intersect triangle, triangle's vertex");
        // BVA3: Ray intersects with triangle's plane outside the triangle, on one of the triangle's edge's vector
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 2, -1));
        assertNull(triangle.findIntersections(ray),
                "Ray failed to NOT intersect triangle, on triangle's edge's vector");
    }
}