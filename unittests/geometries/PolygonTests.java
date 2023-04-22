package geometries;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static primitives.Util.isZero;

import org.junit.jupiter.api.Test;

import geometries.Polygon;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Testing Polygons
 *
 * @author Dan
 */
public class PolygonTests {

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // EP1: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // EP2: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // EP3: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // EP4: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // BVA1: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // BVA2: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // BVA3: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP1: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1]))),
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(Ray)}
     */
    @Test
    public void testFindIntersections() {
        Polygon polygon = new Polygon(new Point(1,1,0), new Point(1,-1,0), new Point(-2,-1,0), new Point(-1,0,0));
        // ============ Equivalence Partitions Tests ==============
        // EP1: Ray intersects with polygon's plane inside the polygon
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(0.5, 0, -1));
        assertEquals(List.of(new Point(0.5, 0, 0)), polygon.findIntersections(ray),
                "Ray failed to intersect polygon inside the polygon");
        // EP2: Ray intersects with polygon's plane outside the polygon, in front of the polygon's edge
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 1, -1));
        assertNull(polygon.findIntersections(ray),
                "Ray failed to NOT intersect polygon outside the polygon, in front of edge");
        // EP3: Ray intersects with polygon's plane outside the polygon, in front of the polygon's vertex
        ray = new Ray(new Point(0, 0, 1), new Vector(2, -2, -1));
        assertNull(polygon.findIntersections(ray),
                "Ray failed to NOT intersect polygon outside the polygon, in front of vertex");

        // =============== Boundary Values Tests ==================
        // BVA1: Ray intersects with polygon's plane on the polygon's edge
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, -1));
        assertNull(polygon.findIntersections(ray), "Ray failed to NOT intersect polygon, on polygon's edge");
        // BVA2: Ray intersects with polygon's plane on the polygon's vertex
        ray = new Ray(new Point(0, 0, 1), new Vector(-1, 0, -1));
        assertNull(polygon.findIntersections(ray), "Ray failed to NOT intersect polygon, on polygon's vertex");
        // BVA3: Ray intersects with polygon's plane outside the polygon, on one of the polygon's edge's vector
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 2, -1));
        assertNull(polygon.findIntersections(ray),
                "Ray failed to NOT intersect polygon, on polygon's edge's vector");
    }
}
