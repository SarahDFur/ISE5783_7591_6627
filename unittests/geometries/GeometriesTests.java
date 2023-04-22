package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Geometries class
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class GeometriesTests {

    @Test
    public void testFindIntersections() {
        Geometries collection = new Geometries();
        collection.add(new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 2)));
        collection.add(new Sphere(1, new Point(1, 1, 1)));
        collection.add(new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)));

        // ============ Equivalence Partitions Tests ==============
        // EP1: Ray intersects with some Geometries, but not all
        Ray ray = new Ray(new Point(2, 2, 2.5), new Vector(-1, -1, -2));
        assertEquals(3, collection.findIntersections(ray).size(),
                // intersect plane and sphere, but not triangle
                "Wrong number of intersections points - some geometries intersected");

        // =============== Boundary Values Tests ==================
        // BVA1: Ray doesn't intersect with any of Geometries
        ray = new Ray(new Point(2, 2, 2.5), new Vector(0, 0, 1));
        assertNull(collection.findIntersections(ray),
                "Wrong number of intersections points - no geometries intersected");
        // BVA2: Ray intersects with only one of Geometries
        ray = new Ray(new Point(2, 2, 2.5), new Vector(-1, 0, 0));
        assertEquals(1, collection.findIntersections(ray).size(),
                // intersect only plane, and not triangle and sphere
                "Wrong number of intersections points - only one geometries intersected");
        // BVA3: Ray intersects with all the Geometries
        ray = new Ray(new Point(2, 2, 2.5), new Vector(-3, -3, -2.5));
        assertEquals(4, collection.findIntersections(ray).size(),
                "Wrong number of intersections points - all geometries intersected");
        // BVA4: Empty Geometries collection
        ray = new Ray(new Point(2, 2, 2.5), new Vector(1, 1, 1));
        assertNull(new Geometries().findIntersections(ray),
                "Wrong number of intersections points - empty collection");
    }

}
