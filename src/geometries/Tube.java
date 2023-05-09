package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Tube class extending abstract class RadialGeometry,
 * represents a tube in 3D Cartesian coordinate system
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
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
        if (!isZero(t)) {
            o = o.add(v.scale(t));
        }

        Vector N = point.subtract(o);
        return N.normalize();
    }

    public Ray getAxisRay() {
        return axisRay;
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // solve for t : At^2 + Bt + C = 0
        // A = (vr - (vr,va)va)^2
        // B = 2(vr-(vr,va)va , deltaP-(deltaP,va)va)
        // C = (deltaP - (deltaP,va)va)^2 - r^2
        // where: deltaP: pr-pa , (x,y): dot product
        //          axisRay params: pa,va, ray params: pr,vr

        Vector vr = ray.getDir();
        Vector va = axisRay.getDir();
        if (vr.equals(va) || vr.equals(va.scale(-1))) { //ray parallel to axis
            return null;
        }
        double vDotVa = vr.dotProduct(va);

        if (ray.getP0().equals(axisRay.getP0())) { //ray start on axis's head point
            if (isZero(vDotVa)) //ray also orthogonal to axis
                return List.of(new GeoPoint(this, ray.getPoint(radius)));
            double t = alignZero(radius / (vr.subtract(va.scale(vDotVa)).length()));
            return List.of(new GeoPoint(this, ray.getPoint(t)));
        }

        Vector vecDeltaP = ray.getP0().subtract(axisRay.getP0());
        double deltaPDotVa = vecDeltaP.dotProduct(va);
        if (va.equals(vecDeltaP.normalize()) || va.equals(vecDeltaP.normalize().scale(-1))) { //ray start along axis
            if (isZero(vDotVa)) //ray also orthogonal to axis
                return List.of(new GeoPoint(this, ray.getPoint(radius)));
            double t = alignZero(radius / (vr.subtract(va.scale(vDotVa)).length()));
            return List.of(new GeoPoint(this, ray.getPoint(t)));
        }

        // is either of the vectors, vr or deltaP, orthogonal to the vector va?
        Vector v1 = isZero(vDotVa) ? vr : vr.subtract(va.scale(vDotVa));
        Vector v2 = isZero(deltaPDotVa) ? vecDeltaP : vecDeltaP.subtract(va.scale(deltaPDotVa));

        double a = v1.lengthSquared();
        double b = v1.dotProduct(v2) * 2;
        double c = v2.lengthSquared() - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant <= 0)
            return null; // ray doesn't intersect at all OR is tangent to tube

        double t1 = alignZero((-b - Math.sqrt(discriminant)) / (2 * a));
        double t2 = alignZero((-b + Math.sqrt(discriminant)) / (2 * a));
        if (t1 > 0 && t2 > 0)
            return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
        if (t1 > 0)
            return List.of(new GeoPoint(this, ray.getPoint(t1)));
        if (t2 > 0)
            return List.of(new GeoPoint(this, ray.getPoint(t2)));

        return null;
    }
}
