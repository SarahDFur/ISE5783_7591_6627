package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class TubeTests {

    private static final String MUST_HAVE_1_INTERSECTION = "must be 1 intersection";
    private static final String INCORRECT_INTERSECTIONS = "Bad intersections";
    private static final String MUST_HAVE_2_INTERSECTIONS = "must be 2 intersections";
    private static final String MUST_NOT_HAVE_INTERSECTIONS = "Must not be intersections";
    private static final String MUST_BE_INTERSECTIONS = "must be intersections";
    
    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}
     */
    @Test
    public void testGetNormal() {
        Tube tube = new Tube(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        // ============ Equivalence Partitions Tests ==============
        // EP1: test normal vector
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(0, 1, 1)),
                "Bad normal to tube");

        // =============== Boundary Values Tests ==================
        // BVA1: point forms a right angle with ray's head
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(0, 1, 0)),
                "Bad normal to tube - point forms a right angle with ray's head");
    }

    /**
     * Test method for {@link geometries.Tube#findIntersections(Ray)}
     */
    @Test
    public void testFindIntersections() {
        //@TODO: Tube - test findIntersections()
        Tube tube1 = new Tube(1d, new Ray(new Point(1, 0, 0), new Vector(0, 1, 0)));
        Vector vAxis = new Vector(0, 0, 1);
        Tube tube2 = new Tube(1d, new Ray(new Point(1, 1, 1), vAxis));
        Ray ray;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the tube (0 points)
        ray = new Ray(new Point(1, 1, 2), new Vector(1, 1, 0));
        assertNull(tube1.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC02: Ray's crosses the tube (2 points)
        ray = new Ray(new Point(0, 0, 0), new Vector(2, 1, 1));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 0.2), new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // TC03: Ray's starts within tube and crosses the tube (1 point)
        ray = new Ray(new Point(1, 0.5, 0.5), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line is parallel to the axis (0 points) *****************
        // TC11: Ray is inside the tube (0 points)
        ray = new Ray(new Point(0.5, 0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC12: Ray is outside the tube
        ray = new Ray(new Point(0.5, -0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC13: Ray is at the tube surface
        ray = new Ray(new Point(2, 1, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC14: Ray is inside the tube and starts against axis head
        ray = new Ray(new Point(0.5, 0.5, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC15: Ray is outside the tube and starts against axis head
        ray = new Ray(new Point(0.5, -0.5, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC16: Ray is at the tube surface and starts against axis head
        ray = new Ray(new Point(2, 1, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC17: Ray is inside the tube and starts at axis head
        ray = new Ray(new Point(1, 1, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // **** Group: Ray is orthogonal but does not begin against the axis head *****************
        // TC21: Ray starts outside and the line is outside (0 points)
        ray = new Ray(new Point(0, 2, 2), new Vector(1, 1, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC22: The line is tangent and the ray starts before the tube (0 points)
        ray = new Ray(new Point(0, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC23: The line is tangent and the ray starts at the tube (0 points)
        ray = new Ray(new Point(1, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC24: The line is tangent and the ray starts after the tube (0 points)
        ray = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC25: Ray starts before (2 points)
        ray = new Ray(new Point(0, 0, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 2), new Point(2, 1, 2)), result, INCORRECT_INTERSECTIONS);

        // TC26: Ray starts at the surface and goes inside (1 point)
        ray = new Ray(new Point(0.4, 0.2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 2)), result, INCORRECT_INTERSECTIONS);

        // TC27: Ray starts inside (1 point)
        ray = new Ray(new Point(1, 0.5, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 2)), result, INCORRECT_INTERSECTIONS);

        // TC28: Ray starts at the surface and goes outside (0 points)
        ray = new Ray(new Point(2, 1, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);
        // TC29: Ray starts after
        ray = new Ray(new Point(4, 2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC30: Ray starts before and crosses the axis (2 points)
        ray = new Ray(new Point(1, -1, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 2), new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // TC31: Ray starts at the surface and goes inside and crosses the axis
        ray = new Ray(new Point(1, 0, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // TC32: Ray starts inside and the line crosses the axis (1 point)
        ray = new Ray(new Point(1, 0.5, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // TC33: Ray starts at the surface and goes outside and the line crosses the
        // axis (0 points)
        ray = new Ray(new Point(1, 2, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC34: Ray starts after and crosses the axis (0 points)
        ray = new Ray(new Point(1, 3, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC35: Ray start at the axis
        ray = new Ray(new Point(1, 1, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // **** Group: Ray is orthogonal to axis and begins against the axis head *****************
        // TC41: Ray starts outside and the line is outside (
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 1, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC42: The line is tangent and the ray starts before the tube
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC43: The line is tangent and the ray starts at the tube
        ray = new Ray(new Point(1, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC44: The line is tangent and the ray starts after the tube
        ray = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // TC45: Ray starts before
        ray = new Ray(new Point(0, 0, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 1), new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // TC46: Ray starts at the surface and goes inside
        ray = new Ray(new Point(0.4, 0.2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // TC47: Ray starts inside
        ray = new Ray(new Point(1, 0.5, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // TC48: Ray starts at the surface and goes outside
        ray = new Ray(new Point(2, 1, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC49: Ray starts after
        ray = new Ray(new Point(4, 2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC50: Ray starts before and goes through the axis head
        ray = new Ray(new Point(1, -1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 1), new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // TC51: Ray starts at the surface and goes inside and goes through the axis
        // head
        ray = new Ray(new Point(1, 0, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // TC52: Ray starts inside and the line goes through the axis head
        ray = new Ray(new Point(1, 0.5, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // TC53: Ray starts at the surface and the line goes outside and goes through
        // the axis head
        ray = new Ray(new Point(1, 2, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC54: Ray starts after and the line goes through the axis head
        ray = new Ray(new Point(1, 3, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC55: Ray start at the axis head
        ray = new Ray(new Point(1, 1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and *****************
        // begins against axis head
        Point p0 = new Point(0, 2, 1);
        // TC61: Ray's line is outside the tube
        ray = new Ray(p0, new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC62: Ray's line crosses the tube and begins before
        ray = new Ray(p0, new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(2, 1, 2), new Point(0.4, 1.8, 1.2)), result, INCORRECT_INTERSECTIONS);

        // TC63: Ray's line crosses the tube and begins at surface and goes inside
        ray = new Ray(new Point(0.4, 1.8, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1.8)), result, INCORRECT_INTERSECTIONS);

        // TC64: Ray's line crosses the tube and begins inside
        ray = new Ray(new Point(1, 1.5, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1.5)), result, INCORRECT_INTERSECTIONS);

        // TC65: Ray's line crosses the tube and begins at the axis head
        ray = new Ray(new Point(1, 1, 1), new Vector(0, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // TC66: Ray's line crosses the tube and begins at surface and goes outside
        ray = new Ray(new Point(2, 1, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC67: Ray's line is tangent and begins before
        ray = new Ray(p0, new Vector(0, 2, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC68: Ray's line is tangent and begins at the tube surface
        ray = new Ray(new Point(1, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC69: Ray's line is tangent and begins after
        ray = new Ray(new Point(2, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and *****************
        // does not begin against axis head
        double sqrt2 = Math.sqrt(2);
        double denomSqrt2 = 1 / sqrt2;
        double value1 = 1 - denomSqrt2;
        double value2 = 1 + denomSqrt2;

        // TC71: Ray's crosses the tube and the axis
        ray = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, 2 + value1), new Point(value2, value2, 2 + value2)), result,
                INCORRECT_INTERSECTIONS);

        // TC72: Ray's crosses the tube and the axis head
        ray = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, value1), new Point(value2, value2, value2)), result,
                INCORRECT_INTERSECTIONS);

        // TC73: Ray's begins at the surface and goes inside
        ray = new Ray(new Point(value1, value1, 2 + value1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value1, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // TC74: Ray's begins at the surface and goes inside crossing the axis
        ray = new Ray(new Point(value1, value1, 2 + value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // TC75: Ray's begins at the surface and goes inside crossing the axis head
        ray = new Ray(new Point(value1, value1, value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, value2)), result, INCORRECT_INTERSECTIONS);

        // TC76: Ray's begins inside and the line crosses the axis
        ray = new Ray(new Point(0.5, 0.5, 2.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // TC77: Ray's begins inside and the line crosses the axis head
        ray = new Ray(new Point(0.5, 0.5, 0.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, value2)), result, INCORRECT_INTERSECTIONS);

        // TC78: Ray's begins at the axis
        ray = new Ray(new Point(1, 1, 3), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // TC79: Ray's begins at the surface and goes outside
        ray = new Ray(new Point(2, 1, 2), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC80: Ray's begins at the surface and goes outside and the line crosses the
        // axis
        ray = new Ray(new Point(value2, value2, 2 + value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // TC81: Ray's begins at the surface and goes outside and the line crosses the
        // axis head
        ray = new Ray(new Point(value2, value2, value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);
    }
}