package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;

public class Sampling {
    /**
     * resolution of the target area
     */
    private static int TARGET_AREA_RESOLUTION = 1;//by default do not super sample
    /**
     * distance of the target area from the main ray head
     */
    private static double TARGET_AREA_DISTANCE = 10;
    /**
     * length of the target area edge
     */
    private static double TARGET_AREA_EDGE = 1;

    /**
     * TARGET_AREA_RESOLUTION setter
     * @param targetAreaResolution the target area resolution (how many rays on each column and row)
     */
    public static void setTargetAreaResolution(int targetAreaResolution) {
        TARGET_AREA_RESOLUTION = targetAreaResolution;
    }

    /**
     * TARGET_AREA_DISTANCE setter
     * @param targetAreaDistance the target area distance from the beginning of the ray
     */
    public static void setTargetAreaDistance(double targetAreaDistance) {
        TARGET_AREA_DISTANCE = targetAreaDistance;
    }

    /**
     * TARGET_AREA_EDGE setter
     * @param targetAreaEdge the size of the edge of the target area
     */
    public static void setTargetAreaEdge(double targetAreaEdge) {
        TARGET_AREA_EDGE = targetAreaEdge;
    }

    /**
     * Creates sample ray for each square in target area
     *
     * @param ray main ray
     * @param k   for glossy/diffusive
     * @param n   normal to head of main ray
     * @return List of sample rays
     */
    public static LinkedList<Ray> superSample(Ray ray, double k, Vector n) {
        LinkedList<Ray> sampling = new LinkedList<>();
        Vector vUp;
        Vector vRight;
        Vector vTo = ray.getDir();
        vUp = Vector.createOrthogonal(vTo);
        vRight = vTo.crossProduct(vUp).normalize();
        Point p0 = ray.getP0();
        for (int i = 0; i < TARGET_AREA_RESOLUTION; i++) {
            for (int j = 0; j < TARGET_AREA_RESOLUTION; j++) {
                Vector sampleDir = createVectorBeam(i, j, ray, vTo, vUp, vRight, k, n);
                if (sampleDir != null) {
                    sampling.add(new Ray(p0, sampleDir, n));
                }
            }
        }
        return sampling;
    }


    /**
     * Generates a ray, starting at the point and going through specific square in grid.
     *
     * @param j      The horizontal index of the square
     * @param i      The vertical index of the square
     * @param ray    The main ray which we built the grid around
     * @param vTo    The direction of the main ray
     * @param vUp    Orthogonal to vTo
     * @param vRight Orthogonal to vTo
     * @param k      for glossy/diffusive
     * @param n      normal to head of main ray
     * @return Vector - goes through requested square in grid
     */
    private static Vector createVectorBeam(int i, int j, Ray ray, Vector vTo, Vector vUp, Vector vRight, double k, Vector n) {
        Point p0 = ray.getP0();
        Point pIj = p0.add(vTo.scale(TARGET_AREA_DISTANCE)); //Center of the grid
        double rC = k * TARGET_AREA_EDGE / TARGET_AREA_RESOLUTION; //height and width of each square
        double yI = -(i - ((double) (TARGET_AREA_RESOLUTION - 1)) / 2) * rC; //vertical distance of the required square from the center of the grid
        double xJ = -(j - ((double) (TARGET_AREA_RESOLUTION - 1)) / 2) * rC; // horizontal distance of the required square from the center of the grid
        //changing the position of the center point so that the ray will intersect the view plane in the right place
        if (xJ != 0) pIj = pIj.add(vRight.scale(xJ));
        if (yI != 0) pIj = pIj.add(vUp.scale(yI));
        //return the ray
        double sign = pIj.subtract(ray.getP0()).dotProduct(n);
        if (vTo.dotProduct(n) * sign < 0) return null; //Checking that the secondary ray doesn't go the other side of the normal plane
        return pIj.subtract(p0).normalize();
    }
}
