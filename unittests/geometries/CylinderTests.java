package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        //@TODO: cylinder - test findIntersections()
        Cylinder cylinder = new Cylinder(2d, new Ray(new Point(2,0,0), new Vector(0,0,1)), 6);
        // ============ Equivalence Partitions Tests ==============
        // EP1: Ray's line is outside the tube (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),"Ray's line out of sphere");
        // EP2: Ray starts before and crosses the tube (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = cylinder.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");

        // EP3: Ray starts inside the tube (1 point)
        List<Point> result1 = cylinder.findIntersections(new Ray(new Point(0.6, -0.51, 0), new Vector(0.32,1.18,0.74)));
        assertEquals(1, result1.size(), "Wrong number of points - Ray starts inside the sphere (1 point)");
        // EP4: Ray starts after the tube (0 points)
        List<Point> result2 = cylinder.findIntersections(new Ray(new Point(0.6, 1.44, 0.5), new Vector(0.2,-0.63,0.63)));
        assertNull(result2, "Wrong number of points - Ray starts after the sphere (0 points)");
        //add EPs for rays on/through bases

        // =============== Boundary Values Tests ==================
        //-----not through normal-----
        //0 point intersections
        //BVA1: starts at tube and goes out
        //BVA2: outside of tube - parallel to normal or (look at sphere for ref (when not parallel to normal))
        //BVA3: ray start is orthogonal to normal/ height of tube
        //BVA4:Cylinder: top base and out
        //BVA5:Cylinder: bottom base and out

        //1 point intersections
        //BVA6: starts at tube and goes in
        //BVA7: starts inside tube
        //BVA8: cylinder: top base and in
        //BVA9: cylinder: bottom base and in

        //2 point intersections
        //BVA10: ray starts outside of tube

        //-----through normal-----

        //0 point intersections
        //BVA11: starts at tube and goes out
        //BVA12: outside of tube - parallel to normal or (look at sphere for ref (when not parallel to normal))
        //BVA13: ray start is orthogonal to normal/ height of tube
        //BVA14: Cylinder: top base and out
        //BVA15: Cylinder: bottom base and out

        //1 point intersections
        //BVA16: starts at tube and goes in
        //BVA17: starts inside tube
        //BVA18: cylinder: top base and in
        //BVA19: cylinder: bottom base and in

        //2 point intersections
        //BVA20: ray starts outside of tube
    }
}