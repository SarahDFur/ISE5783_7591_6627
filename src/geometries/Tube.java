package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Tube class extending abstract class RadialGeometry,
 * represents a tube in 3D Cartesian coordinate system
 */
public class Tube extends RadialGeometry {
    protected Ray axisRay;

    /**
     * constructor
     *
     * @param radius radius of geometry
     * @param ray    ray of tube
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        this.axisRay = ray;
    }

    @Override
    public Vector getNormal(Point point) {
        // finding normal vector:
        // normal = normalize(p - o)
        // o = p0 + t * v
        // t = v * (p - p0)
        // BVA: if t=0, then vector (p-p0) is orthogonal to vector v, and point o = point p0

        Vector v = axisRay.getDir();
        double t = v.dotProduct(point.subtract(axisRay.getP0()));

        Point o = axisRay.getP0();
        if (!isZero(t)){
            o = o.add(v.scale(t));
        }

        Vector N = point.subtract(o);
        return N.normalize();
    }

    public Ray getAxisRay() {
        return axisRay;
    }
}
