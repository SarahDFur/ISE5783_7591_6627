package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 *Tube class extending abstract class RadialGeometry,
 * represents a tube in 3D Cartesian coordinate system
 */
public class Tube extends RadialGeometry{
    protected Ray axisRay;

    /**
     * constructor
     * @param radius radius of geometry
     * @param ray ray of tube
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        this.axisRay=ray;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    public Ray getAxisRay() {
        return axisRay;
    }
}
