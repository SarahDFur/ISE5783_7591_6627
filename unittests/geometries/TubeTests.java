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
        //@TODO: Tube - test findIntersections()
        Tube tube = new Tube(2d, new Ray(new Point(2,0,0), new Vector(0,0,1)));
        // ============ Equivalence Partitions Tests ==============
        // EP1: Ray's line is outside the cylinder (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),"Ray's line out of tube");
        // EP2: Ray starts before and crosses the cylinder (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = tube.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses tube");

        // EP3: Ray starts inside the cylinder (1 point)
        List<Point> result1 = tube.findIntersections(new Ray(new Point(0.6, -0.51, 0), new Vector(0.32,1.18,0.74)));
        assertEquals(1, result1.size(), "Wrong number of points - Ray starts inside the sphere (1 point)");
        // EP4: Ray starts after the cylinder (0 points)
        List<Point> result2 = tube.findIntersections(new Ray(new Point(0.6, 1.44, 0.5), new Vector(0.2,-0.63,0.63)));
        assertNull(result2, "Wrong number of points - Ray starts after the sphere (0 points)");

        // =============== Boundary Values Tests ==================
        //-----not through normal-----
        //0 point intersections
        //BVA1: starts at tube and goes out
        //BVA2: outside of tube - parallel to normal or (look at sphere for ref (when not parallel to normal))
        //BVA3: ray start is orthogonal to normal/ height of tube


        //1 point intersections
        //BVA4: starts at tube and goes in
        //BVA5: starts inside tube

        //2 point intersections
        //BVA6: ray starts outside of tube

        //-----through normal-----

        //0 point intersections
        //BVA7: starts at tube and goes out
        //BVA8: outside of tube - parallel to normal or (look at sphere for ref (when not parallel to normal))
        //BVA9: ray start is orthogonal to normal/ height of tube

        //1 point intersections
        //BVA10: starts at tube and goes in
        //BVA11: starts inside tube

        //2 point intersections
        //BVA12: ray starts outside of tube
    }
}