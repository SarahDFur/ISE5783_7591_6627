package renderer;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class CameraTests {
    static final Point ZERO_POINT = new Point(0, 0, 0);

    /**
     * {@link renderer.Camera#Camera(Point, Vector, Vector)}
     */
    @Test
    public void testConstructor(){

    }

    /**
     * Test method for constructRay()
     * {@link renderer.Camera#constructRay(int, int, int, int)}
     */
    @Test
    public void testConstructRay() {
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0)).setViewPlaneDistance(10);
        String badRay = "Bad ray";

        // ============ Equivalence Partitions Tests ==============
        // EP01: 4X4 Inside (1,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -1, -10)),
                camera.setViewPlaneSize(8, 8).constructRay(4, 4, 1, 1), badRay);

        // =============== Boundary Values Tests ==================
        // BVA1: 3X3 Center (1,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(0, 0, -10)),
                camera.setViewPlaneSize(6, 6).constructRay(3, 3, 1, 1), badRay);

        // BVA2: 3X3 Center of Upper Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(0, -2, -10)),
                camera.setViewPlaneSize(6, 6).constructRay(3, 3, 1, 0), badRay);

        // BVA3: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, 0, -10)),
                camera.setViewPlaneSize(6, 6).constructRay(3, 3, 0, 1), badRay);

        // BVA4: 3X3 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, -2, -10)),
                camera.setViewPlaneSize(6, 6).constructRay(3, 3, 0, 0), badRay);

        // BVA5: 4X4 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(3, -3, -10)),
                camera.setViewPlaneSize(8, 8).constructRay(4, 4, 0, 0), badRay);

        // BVA6: 4X4 Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -3, -10)),
                camera.setViewPlaneSize(8, 8).constructRay(4, 4, 1, 0), badRay);

    }

}
