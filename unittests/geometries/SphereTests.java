package geometries;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}
     * checks that the normal of a sphere is calculated correctly
     */
    @Test
    public void testGetNormal() {
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

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     * Checks that intersections for a sphere are calculated correctly
     */
    @Test
    public void testFindIntersections() {
        //@TODO: BVA: 12

        Sphere sphere = new Sphere(1d, new Point (1, 0, 0));
        Sphere sphere1 = new Sphere(1d, new Point (3, -2, 4));

        // ============ Equivalence Partitions Tests ==============
        // EP1: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),"Ray's line out of sphere");
        // EP2: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");

        // EP3: Ray starts inside the sphere (1 point)
        List<Point> result1 = sphere.findIntersections(new Ray(new Point(0.6, -0.51, 0), new Vector(0.32,1.18,0.74)));
        assertEquals(1, result1.size(), "Wrong number of points - Ray starts inside the sphere (1 point)");
        // EP4: Ray starts after the sphere (0 points)
        List<Point> result2 = sphere.findIntersections(new Ray(new Point(0.6, 1.44, 0.5), new Vector(0.2,-0.63,0.63)));
        assertNull(result2, "Wrong number of points - Ray starts after the sphere (0 points)");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // BVA1: Ray starts at sphere and goes inside (1 point)
        List<Point> result3 = sphere.findIntersections(new Ray(new Point(1.73, 0.37, 0.57), new Vector(-0.3,-0.37,-0.57)));
        assertEquals(1, result3.size(), "Wrong number of points - Ray starts at sphere and goes inside (1 point)");
        // BVA2: Ray starts at sphere and goes outside (0 points)
        List<Point> result4 = sphere.findIntersections(new Ray(new Point(2,0,0), new Vector(0.56,1.21,0)));
        assertNull(result4, "Wrong number of points - Ray starts at sphere and goes outside (0 points)");

        // **** Group: Ray's line goes through the center
        // BVA3: Ray starts before the sphere (2 points)
        List<Point> result5 = sphere.findIntersections(new Ray(new Point(1.52, -0.78, 1), new Vector(-0.52,0.78,-1)));
        assertEquals(2, result5.size(), "Wrong number of points Ray starts before the sphere (2 points)");
        // BVA4: Ray starts at sphere and goes inside (1 point)
        List<Point> result6 = sphere.findIntersections(new Ray(new Point(1.73, 0.37, 0.57), new Vector(-0.73,-0.37,-0.57)));
        assertEquals(1, result6.size(), "Wrong number of points - Ray starts at sphere and goes inside (1 point)");
        // BVA5: Ray starts inside (1 point)
        List<Point> result7 = sphere.findIntersections(new Ray(new Point(1.52, 0.42, 0.16), new Vector(-0.52,-0.42,-0.16)));
        assertEquals(1, result7.size(), "Wrong number of points - Ray starts inside (1 point)");
        // BVA6: Ray starts at the center (1 point)
        List<Point> result8 = sphere.findIntersections(new Ray(new Point(1,0,0), new Vector(-0.05,0.77,0.63)));
        assertEquals(1, result8.size(), "Wrong number of points - Ray starts at the center (1 point)");
        // BVA7: Ray starts at sphere and goes outside (0 points)
        List<Point> result9 = sphere.findIntersections(new Ray(new Point(2,0,0), new Vector(0.56,1.21,0)));
        assertNull(result9, "Wrong number of points - Ray starts at sphere and goes outside (0 points)");
        // BVA8: Ray starts after sphere (0 points)
        List<Point> result10 = sphere.findIntersections(new Ray(new Point(-0.24, 1.19, -0.59), new Vector(-0.78,-2.59,-0.02)));
        assertNull(result10, "Wrong number of points - Ray starts after sphere (0 points)");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // BVA9: Ray starts before the tangent point
        List<Point> result11 = sphere.findIntersections(new Ray(new Point(2.18,-0.67,1), new Vector(-1.18,0.67,0)));
        //intersect point/ tangent point is (1,0,1)
        assertNull(result11, "Wrong number of points - Ray starts before the tangent point");
        // BVA10: Ray starts at the tangent point
        List<Point> result12 = sphere.findIntersections(new Ray(new Point(1,0,1), new Vector(-1.35,0,0)));
        assertNull(result12, "Wrong number of points - Ray starts at the tangent point");
        // BVA11: Ray starts after the tangent point
        List<Point> result13 = sphere.findIntersections(new Ray(new Point(0.45, 0, 1), new Vector(-1.69,0,0)));
        assertNull(result13, "Wrong number of points - Ray starts after the tangent point");

        // **** Group: Special cases
        // BVA12: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        Ray result14 = new Ray(new Point(3,-2,1),new Vector(0,-2,1));
        assertDoesNotThrow(()-> result14.getDir().crossProduct(sphere1.getNormal(sphere1.getCenter())),
                "Ray is orthogonal to the center line of the sphere");

      //  assertNull(sphere1.getNormal(sphere.getCenter()), "Ray is orthogonal to the center line of the sphere");
    }
}