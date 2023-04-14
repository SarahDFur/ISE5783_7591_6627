package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Cylinder class extending class Tube
 * represents a cylinder in 3D Cartesian coordinate system
 */
public class Cylinder extends Tube {
    private double height;

    /**
     * constructor
     *
     * @param radius radius of geometry
     * @param ray    ray of tube
     * @param height height of cylinder
     */
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        //check if point in center of one of the bases
        Point centerBase1 = axisRay.getP0();
        Point centerBase2 = axisRay.getP0().add(axisRay.getDir().scale(height));
        if (point.equals(centerBase1)) {
            return axisRay.getDir().scale(-1);
        }
        if (point.equals(centerBase2)) {
            return axisRay.getDir();
        }
        //check if point on one of the bases,
        Vector vecAxis = axisRay.getDir();
        Vector vecToBase1 = point.subtract(centerBase1);
        Vector vecToBase2 = point.subtract(centerBase2);
        if (isZero(vecAxis.dotProduct(vecToBase1))) {
            return vecAxis.scale(-1);
        }
        if (isZero(vecAxis.dotProduct(vecToBase2))) {
            return vecAxis;
        }
        //point is on the round surface
        return super.getNormal(point);

        //@TODO: ask Yair - is it better to have more readable code, or to avoid creating "extra" objects? in relation to this code
    }

    public double getHeight() {
        return height;
    }
}
