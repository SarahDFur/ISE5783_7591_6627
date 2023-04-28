package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class CylinderTests {
    private static final String MUST_HAVE_1_INTERSECTION = "Must have 1 intersection";
    private static final String MUST_HAVE_2_INTERSECTIONS = "Must have 2 intersections";
    private static final String MUST_NOT_HAVE_INTERSECTIONS = "Must not have intersections";
    private static final String MUST_HAVE_INTERSECTIONS = "Must have intersections";

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}
     */
    @Test
    public void testGetNormal() {
        Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);
        // ============ Equivalence Partitions Tests ==============
        // EP1: test normal vector using point on top base of cylinder
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(0.5, 0.5, 1)),
                "Bad normal to Cylinder - top base");
        // EP2: test normal vector using point on bottom base of cylinder
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0.5, 0.5, 0)),
                "Bad normal to Cylinder - bottom base");
        // EP3: test normal vector using point on side of cylinder
        assertEquals(new Vector(0, 1, 0), cylinder.getNormal(new Point(0, 1, 0.5)),
                "Bad normal to Cylinder - side");

        // =============== Boundary Values Tests ==================
        // BVA1: test normal vector using point at center of top base of cylinder
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(0, 0, 1)),
                "Bad normal to Cylinder - center of top base");
        // BVA2: test normal vector using point at center of bottom base of cylinder
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0, 0, 0)),
                "Bad normal to Cylinder - center of bottom base");
    }

    @Test
    public void testFindIntersections() {
        Vector vAxis = new Vector(0,0,1);
        Vector vAxisMinus = new Vector(0,0,-1);
        Vector vOrthogonal = new Vector(1,0,0);
        Cylinder cylinder = new Cylinder(1,new Ray(new Point(1,1,1), vAxis),5);
        Ray ray;
        // NOTE: "First base" refers to base around axis's head,
        //       "second base" refers to other base, at height distance from first

        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================

        // **** Group: 0 points of interaction with Tube *****************

        // BVA1: Ray is not parallel to axis (0 p)
        ray = new Ray(new Point(2,2,0), new Vector(1,1,1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA2: Ray is parallel to axis, but outside of tube (0 p)
        ray = new Ray(new Point(2,2,0), vAxis);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA3: Ray is parallel to axis, but at tube's surface (0 p)
        ray = new Ray(new Point(1,2,0), vAxis);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA4: Ray is parallel to axis and inside tube, starts "below" first base and go toward cylinder (2 p)
        ray = new Ray(new Point(1,1.5,0), vAxis);
        List<Point> result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA5: Ray is parallel to axis and inside tube, starts "below" first base and go away from cylinder (0 p)
        ray = new Ray(new Point(1,1.5,0), vAxisMinus);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA6: Ray is parallel to axis and inside tube, on axis, starts "below" first base and go toward cylinder (2 p)
        ray = new Ray(new Point(1,1,0), vAxis);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA7: Ray is parallel to axis and inside tube, on axis, starts "below" first base and go away from cylinder (0 p)
        ray = new Ray(new Point(1,1,0), vAxisMinus);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA8: Ray is parallel to axis and inside tube, starts at first base and go inside cylinder (1 p)
        ray = new Ray(new Point(1,1.5,1), vAxis);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA9: Ray is parallel to axis and inside tube, starts at first base and go outside cylinder (0 p)
        ray = new Ray(new Point(1,1.5,1), vAxisMinus);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA10: Ray is parallel to axis and inside tube, starts at first base, on axis's head, and go inside cylinder (1 p)
        ray = new Ray(new Point(1,1,1), vAxis);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA11: Ray is parallel to axis and inside tube, starts at first base, on axis's head, and go outside cylinder (0 p)
        ray = new Ray(new Point(1,1,1), vAxisMinus);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA12: Ray is parallel to axis and inside tube, starts between bases (1 p)
        ray = new Ray(new Point(1,1.5,2), vAxis);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA13: Ray is parallel to axis and inside tube, starts between bases, on axis (1 p)
        ray = new Ray(new Point(1,1,2), vAxis);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA14: Ray is parallel to axis and inside tube, starts at second base and go inside cylinder (1 p)
        ray = new Ray(new Point(1,1.5,6), vAxisMinus);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA15: Ray is parallel to axis and inside tube, starts at second base and go outside cylinder (0 p)
        ray = new Ray(new Point(1,1.5,6), vAxis);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA16: Ray is parallel to axis and inside tube, starts at second base, on axis, and go inside cylinder (1 p)
        ray = new Ray(new Point(1,1,6), vAxisMinus);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA17: Ray is parallel to axis and inside tube, starts at second base, on axis, and go outside cylinder (0 p)
        ray = new Ray(new Point(1,1,6), vAxis);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA18: Ray is parallel to axis and inside tube, starts "above" second base and go toward cylinder (2 p)
        ray = new Ray(new Point(1,1.5,7), vAxisMinus);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA19: Ray is parallel to axis and inside tube, starts "above" second base and go away from cylinder (0 p)
        ray = new Ray(new Point(1,1.5,7), vAxis);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA20: Ray is parallel to axis and inside tube, on axis, starts "above" second base and go toward cylinder (2 p)
        ray = new Ray(new Point(1,1,7), vAxisMinus);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA21: Ray is parallel to axis and inside tube, on axis, starts "above" second base and go away from cylinder (0 p)
        ray = new Ray(new Point(1,1,7), vAxis);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);


        // **** Group: 1 point of interaction with Tube *****************

        // BVA22: Ray is orthogonal, and "below" first base (0 p)
        ray = new Ray(new Point(0.5,0.5,0), vOrthogonal);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA23: Ray is orthogonal, and at first base (0 p)
        ray = new Ray(new Point(0.5,0.5,1), vOrthogonal);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA24: Ray is orthogonal, and between bases (1 p)
        ray = new Ray(new Point(0.5,0.5,2), vOrthogonal);
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA25: Ray is orthogonal, and at second base (0 p)
        ray = new Ray(new Point(0.5,0.5,6), vOrthogonal);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA26: Ray is orthogonal, and "above" second base (0 p)
        ray = new Ray(new Point(0.5,0.5,7), vOrthogonal);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA27: Ray's head "below" first base, interaction point "below" first base (0 p)
        ray = new Ray(new Point(0.5,0.5,0), new Vector(0.5,-0.5,-1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA28: Ray's head "below" first base, interaction point at first base (0 p)
        ray = new Ray(new Point(0.5,0.5,0), new Vector(0.5,-0.5,1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA29: Ray's head "below" first base, interaction point between bases (2 p)
        ray = new Ray(new Point(0.5,0.5,0), new Vector(0.5,-0.5,2));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA30: Ray's head "below" first base, interaction point at second base (1 p)
        ray = new Ray(new Point(0.5,0.5,0), new Vector(0.5,-0.5,6));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA31: Ray's head "below" first base, interaction point "above" second base (2 p)
        ray = new Ray(new Point(0.5,0.5,0), new Vector(0.5,-0.5,7));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA32: Ray's head at first base, interaction point "below" first base (0 p)
        ray = new Ray(new Point(0.5,0.5,1), new Vector(0.5,-0.5,-2));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA33: Ray's head at first base, interaction point at first base (orthogonal) (0 p)
        ray = new Ray(new Point(0.5,0.5,1), new Vector(0.5,-0.5,0));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA34: Ray's head at first base, interaction point between bases (1 p)
        ray = new Ray(new Point(0.5,0.5,1), new Vector(0.5,-0.5,1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA35: Ray's head at first base, interaction point at second base (0 p)
        ray = new Ray(new Point(0.5,0.5,1), new Vector(0.5,-0.5,5));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA36: Ray's head at first base, interaction point "above" second base (1 p)
        ray = new Ray(new Point(0.5,0.5,1), new Vector(0.5,-0.5,6));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);

        // BVA37: Ray's head between bases, interaction point "below" first base (1 p)
        ray = new Ray(new Point(0.5,0.5,2), new Vector(0.5,-0.5,-3));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA38: Ray's head between bases, interaction point at first base (0 p)
        ray = new Ray(new Point(0.5,0.5,2), new Vector(0.5,-0.5,-1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA39: Ray's head between bases, interaction point between bases (1 p)
        ray = new Ray(new Point(0.5,0.5,2), new Vector(0.5,-0.5,1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA40: Ray's head between bases, interaction point at second base (0 p)
        ray = new Ray(new Point(0.5,0.5,2), new Vector(0.5,-0.5,4));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA41: Ray's head between bases, interaction point "above" second base (1 p)
        ray = new Ray(new Point(0.5,0.5,2), new Vector(0.5,-0.5,6));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);

        // BVA42: Ray's head at second base, interaction point "below" first base (1 p)
        ray = new Ray(new Point(0.5,0.5,6), new Vector(0.5,-0.5,-7));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA43: Ray's head at second base, interaction point at first base (0 p)
        ray = new Ray(new Point(0.5,0.5,6), new Vector(0.5,-0.5,-5));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA44: Ray's head at second base, interaction point between bases (1 p)
        ray = new Ray(new Point(0.5,0.5,6), new Vector(0.5,-0.5,-4));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA45: Ray's head at second base, interaction point at second base (orthogonal) (0 p)
        ray = new Ray(new Point(0.5,0.5,6), new Vector(0.5,-0.5,0));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA46: Ray's head at second base, interaction point "above" second base (0 p)
        ray = new Ray(new Point(0.5,0.5,6), new Vector(0.5,-0.5,1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA47: Ray's head "above" second base, interaction point "below" first base (2 p)
        ray = new Ray(new Point(0.5,0.5,7), new Vector(0.5,-0.5,-8));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA48: Ray's head "above" second base, interaction point at first base (1 p)
        ray = new Ray(new Point(0.5,0.5,7), new Vector(0.5,-0.5,-6));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA49: Ray's head "above" second base, interaction point between bases (2 p)
        ray = new Ray(new Point(0.5,0.5,7), new Vector(0.5,-0.5,-5));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA50: Ray's head "above" second base, interaction point at second base (0 p)
        ray = new Ray(new Point(0.5,0.5,7), new Vector(0.5,-0.5,-1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA51: Ray's head "above" second base, interaction point "above" second base (0 p)
        ray = new Ray(new Point(0.5,0.5,7), new Vector(0.5,-0.5,1));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);


        // **** Group: 2 points of interaction with Tube *****************

        // BVA52: Both points are at first base (orthogonal) (0 p)
        ray = new Ray(new Point(0.5,0.5,1), vOrthogonal);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA53: Both points are at second base (orthogonal (0 p)
        ray = new Ray(new Point(0.5,0.5,6), vOrthogonal);
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA54: Both points are "below" first base (0 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,-2));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA55: Both points are between bases (2 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,2));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA56: Both points are "above" second base (0 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,7));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

        // BVA57: one point "below" first base, the other at first base (0 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,0.5));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA58: one point "below" first base, the other between bases (2 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,0.75));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);
        // BVA59: one point "below" first base, the other at second base (1 p)
        ray = new Ray(new Point(-1,0,-6), new Vector(1,1,6));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA60: one point "below" first base, the other "above" second base (2 p)
        ray = new Ray(new Point(-1,0,-6), new Vector(1,1,6.5));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);

        // BVA61: one point at first base, the other between bases (1 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA62: one point at first base, the other at second base (0 p)
        ray = new Ray(new Point(-1,0,-4), new Vector(1,1,5));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);
        // BVA63: one point at first base, the other "above" second base (1 p)
        ray = new Ray(new Point(-2,1,-6), new Vector(2,0,7));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);

        // BVA64: one point between bases, the other at second base (1 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,3));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_HAVE_1_INTERSECTION);
        // BVA65: one point between bases, the other "above" second base (2 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,4));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, MUST_HAVE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_HAVE_2_INTERSECTIONS);

        // BVA66: one point at second base, the other "above" second base (0 p)
        ray = new Ray(new Point(-1,0,0), new Vector(1,1,6));
        assertNull(cylinder.findIntersections(ray), MUST_NOT_HAVE_INTERSECTIONS);

    }
}