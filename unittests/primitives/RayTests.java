package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for Ray class
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class RayTests {

    /**
     *  Test method for {@link Ray#findClosestPoint(List)}.
     *  Find the closest point
     */
    @Test
    public void findClosestPoint() {
        Ray ray = new Ray(new Point(0,0,1), new Vector(0,0,1));
        // ============ Equivalence Partitions Tests ==============
        // EP1: Closest point is in the middle of the list
        List<Point> pointList = List.of(new Point(2,1,4), new Point(0,1,1), new Point(0,3,4));
        assertEquals(new Point(0,1,1), ray.findClosestPoint(pointList),
                "Closest point to start of ray was not found");
        // =============== Boundary Values Tests ==================
        // BVA1: List is empty
        List<Point> pointList1 = List.of();
        assertNull(ray.findClosestPoint(pointList1),
                "Closest point to start of ray was not found");
        // BVA2: First point in the list is the closest
        List<Point> pointList2 = List.of(new Point(0,1,1), new Point(2,1,4), new Point(0,3,4));
        assertEquals(new Point(0,1,1), ray.findClosestPoint(pointList2),
                "Closest point to start of ray was not found");
        // BVA3: Last point in the list is the closest
         List<Point> pointList3 = List.of(new Point(2,1,4), new Point(0,3,4), new Point(0,1,1));
                assertEquals(new Point(0,1,1), ray.findClosestPoint(pointList3),
                        "Closest point to start of ray was not found");
    }
}