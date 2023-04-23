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
    private static final String INCORRECT_INTERSECTIONS = "Incorrect intersections";
    private static final String MUST_HAVE_1_INTERSECTION = "Must have 1 intersection";
    private static final String MUST_HAVE_2_INTERSECTIONS = "Must have 2 intersections";
    private static final String MUST_NOT_HAVE_INTERSECTIONS = "Must not have intersections";
    private static final String MUST_HAVE_INTERSECTIONS = "Must have intersections";
    
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
        // EP1: Ray's line is outside the tube (0 points)
        ray = new Ray(new Point(1, 1, 2), new Vector(1, 1, 0));
        assertNull(tube1.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // EP2: Ray's crosses the tube (2 points)
        ray = new Ray(new Point(0, 0, 0), new Vector(2, 1, 1));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 0.2), new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // EP3: Ray's starts within tube and crosses the tube (1 point)
        ray = new Ray(new Point(1, 0.5, 0.5), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line is parallel to the axis (0 points) *****************
        // BVA1: Ray is inside the tube (0 points)
        ray = new Ray(new Point(0.5, 0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA2: Ray is outside the tube
        ray = new Ray(new Point(0.5, -0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA3: Ray is at the tube surface
        ray = new Ray(new Point(2, 1, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA4: Ray is inside the tube and starts against axis head
        ray = new Ray(new Point(0.5, 0.5, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA5: Ray is outside the tube and starts against axis head
        ray = new Ray(new Point(0.5, -0.5, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA6: Ray is at the tube surface and starts against axis head
        ray = new Ray(new Point(2, 1, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA7: Ray is inside the tube and starts at axis head
        ray = new Ray(new Point(1, 1, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        
        // **** Group: Ray is orthogonal but does not begin against the axis head *****************
        // BVA8: Ray starts outside and the line is outside (0 points)
        ray = new Ray(new Point(0, 2, 2), new Vector(1, 1, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA9: The line is tangent and the ray starts before the tube (0 points)
        ray = new Ray(new Point(0, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA10: The line is tangent and the ray starts at the tube (0 points)
        ray = new Ray(new Point(1, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA11: The line is tangent and the ray starts after the tube (0 points)
        ray = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA12: Ray starts before (2 points)
        ray = new Ray(new Point(0, 0, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 2), new Point(2, 1, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA13: Ray starts at the surface and goes inside (1 point)
        ray = new Ray(new Point(0.4, 0.2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA14: Ray starts inside (1 point)
        ray = new Ray(new Point(1, 0.5, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA15: Ray starts at the surface and goes outside (0 points)
        ray = new Ray(new Point(2, 1, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);
        // BVA16: Ray starts after
        ray = new Ray(new Point(4, 2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA17: Ray starts before and crosses the axis (2 points)
        ray = new Ray(new Point(1, -1, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 2), new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA18: Ray starts at the surface and goes inside and crosses the axis
        ray = new Ray(new Point(1, 0, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA19: Ray starts inside and the line crosses the axis (1 point)
        ray = new Ray(new Point(1, 0.5, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA20: Ray starts at the surface and goes outside and the line crosses the axis (0 points)
        ray = new Ray(new Point(1, 2, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA21: Ray starts after and crosses the axis (0 points)
        ray = new Ray(new Point(1, 3, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA22: Ray start at the axis
        ray = new Ray(new Point(1, 1, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);


        // **** Group: Ray is orthogonal to axis and begins against the axis head *****************
        // BVA23: Ray starts outside and the line is outside (
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 1, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA24: The line is tangent and the ray starts before the tube
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA25: The line is tangent and the ray starts at the tube
        ray = new Ray(new Point(1, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA26: The line is tangent and the ray starts after the tube
        ray = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA27: Ray starts before
        ray = new Ray(new Point(0, 0, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 1), new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // BVA28: Ray starts at the surface and goes inside
        ray = new Ray(new Point(0.4, 0.2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // BVA29: Ray starts inside
        ray = new Ray(new Point(1, 0.5, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, INCORRECT_INTERSECTIONS);

        // BVA30: Ray starts at the surface and goes outside
        ray = new Ray(new Point(2, 1, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA31: Ray starts after
        ray = new Ray(new Point(4, 2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA32: Ray starts before and goes through the axis head
        ray = new Ray(new Point(1, -1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 1), new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // BVA33: Ray starts at the surface and goes inside and goes through the axis head
        ray = new Ray(new Point(1, 0, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // BVA34: Ray starts inside and the line goes through the axis head
        ray = new Ray(new Point(1, 0.5, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);

        // BVA35: Ray starts at the surface and the line goes outside and goes through the axis head
        ray = new Ray(new Point(1, 2, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA36: Ray starts after and the line goes through the axis head
        ray = new Ray(new Point(1, 3, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA37: Ray start at the axis head
        ray = new Ray(new Point(1, 1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, INCORRECT_INTERSECTIONS);


        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and *****************
        // begins against axis head
        Point p0 = new Point(0, 2, 1);
        // BVA38: Ray's line is outside the tube
        ray = new Ray(p0, new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA39: Ray's line crosses the tube and begins before
        ray = new Ray(p0, new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(2, 1, 2), new Point(0.4, 1.8, 1.2)), result, INCORRECT_INTERSECTIONS);

        // BVA40: Ray's line crosses the tube and begins at surface and goes inside
        ray = new Ray(new Point(0.4, 1.8, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1.8)), result, INCORRECT_INTERSECTIONS);

        // BVA41: Ray's line crosses the tube and begins inside
        ray = new Ray(new Point(1, 1.5, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1.5)), result, INCORRECT_INTERSECTIONS);

        // BVA42: Ray's line crosses the tube and begins at the axis head
        ray = new Ray(new Point(1, 1, 1), new Vector(0, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, INCORRECT_INTERSECTIONS);

        // BVA43: Ray's line crosses the tube and begins at surface and goes outside
        ray = new Ray(new Point(2, 1, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA44: Ray's line is tangent and begins before
        ray = new Ray(p0, new Vector(0, 2, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA45: Ray's line is tangent and begins at the tube surface
        ray = new Ray(new Point(1, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA46: Ray's line is tangent and begins after
        ray = new Ray(new Point(2, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and *****************
        // does not begin against axis head
        double sqrt2 = Math.sqrt(2);
        double denominator_Sqrt2 = 1 / sqrt2;
        double value1 = 1 - denominator_Sqrt2;
        double value2 = 1 + denominator_Sqrt2;

        // BVA47: Ray's crosses the tube and the axis
        ray = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, 2 + value1), new Point(value2, value2, 2 + value2)), result,
                INCORRECT_INTERSECTIONS);

        // BVA48: Ray's crosses the tube and the axis head
        ray = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, value1), new Point(value2, value2, value2)), result,
                INCORRECT_INTERSECTIONS);

        // BVA49: Ray's begins at the surface and goes inside
        ray = new Ray(new Point(value1, value1, 2 + value1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value1, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // BVA50: Ray's begins at the surface and goes inside crossing the axis
        ray = new Ray(new Point(value1, value1, 2 + value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // BVA51: Ray's begins at the surface and goes inside crossing the axis head
        ray = new Ray(new Point(value1, value1, value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, value2)), result, INCORRECT_INTERSECTIONS);

        // BVA52: Ray's begins inside and the line crosses the axis
        ray = new Ray(new Point(0.5, 0.5, 2.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // BVA53: Ray's begins inside and the line crosses the axis head
        ray = new Ray(new Point(0.5, 0.5, 0.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, value2)), result, INCORRECT_INTERSECTIONS);

        // BVA54: Ray's begins at the axis
        ray = new Ray(new Point(1, 1, 3), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, INCORRECT_INTERSECTIONS);

        // BVA55: Ray's begins at the surface and goes outside
        ray = new Ray(new Point(2, 1, 2), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA56: Ray's begins at the surface and goes outside and the line crosses the axis
        ray = new Ray(new Point(value2, value2, 2 + value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);

        // BVA57: Ray's begins at the surface and goes outside and the line crosses the axis head
        ray = new Ray(new Point(value2, value2, value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, INCORRECT_INTERSECTIONS);
    }
}