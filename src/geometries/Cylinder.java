package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 *Cylinder class extending class Tube,
 * represents a cylinder in 3D Cartesian coordinate system
 */
public class Cylinder extends Tube{
    private double height;

    /**
     * constructor
     * @param radius radius of geometry
     * @param ray    ray of tube
     * @param height height of cylinder
     */
    public Cylinder(double radius, Ray ray , double height) {
        super(radius, ray);
        this.height=height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    public double getHeight() {
        return height;
    }
}
