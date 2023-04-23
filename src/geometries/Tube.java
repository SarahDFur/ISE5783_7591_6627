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
    public List<Point> findIntersections(Ray ray) {
        //@TODO: Tube - findIntersections()
        // solve for t : At^2 + Bt + C = 0
        // A = (v - (v,va)va)^2
        // B = 2(v-(v,va)va , deltaP-(deltaP,va)va)
        // C = (deltaP - (deltaP,va)va)^2 - r^2
        // where: deltaP: p-pa , (x,y): dot product , axisRay: pa,va, ray: p,v

        Vector v = ray.getDir();
        Vector va = axisRay.getDir();
        if(v.equals(va) || v.equals(va.scale(-1))){ //ray parallel to axis
            return null;
        }
        double vDotVa = v.dotProduct(va);

        if(ray.getP0().equals(axisRay.getP0())){ //ray start on axis's head point
            if(isZero(vDotVa)) //ray orthogonal to axis
                return List.of(ray.getPoint(radius));
            //double t = Math.sqrt( radius * radius / (v.subtract(va.scale(vDotVa)).lengthSquared()));
            double t = radius / (v.subtract(va.scale(vDotVa)).length());
            return List.of(ray.getPoint(t));
        }
        Vector vecDeltaP = ray.getP0().subtract(axisRay.getP0());
        double deltaPqDotVa = vecDeltaP.dotProduct(va);
        if(va.equals(vecDeltaP.normalize()) || va.equals(vecDeltaP.normalize())){ //ray start along axis
            if(isZero(vDotVa)) //ray orthogonal to axis
                return List.of(ray.getPoint(radius));
            double t = radius / (v.subtract(va.scale(vDotVa)).length());
            return List.of(ray.getPoint(t));
        }

        Vector v1 = isZero(vDotVa)? v : v.subtract(va.scale(vDotVa));
        Vector v2 = isZero(deltaPqDotVa) ? vecDeltaP : vecDeltaP.subtract(va.scale(deltaPqDotVa));

//        if(isZero(vDotVa)){ // ray orthogonal to axis
//
//            boolean vDotVaIsZero = true;
//        }
//        if(isZero(deltaPqDotVa)){ //ray's head in front of axis's head
//            boolean deltaPqDotVaIsZero = true;
//        }

        double a = v1.lengthSquared();
        double b = v1.dotProduct(v2) * 2;
        double c = v2.lengthSquared() - radius * radius;

        double discriminant = b * b - 4 * a * c;

        if (discriminant <= 0)
            return null; // ray not intersect at all or just touching

//        if (discriminant == 0) {
//            double t1 = alignZero(-b / (2 * a));
//            if (t1 > 0)
//                return List.of(ray.getPoint(t1));
//        }

        double t1 = alignZero((-b - Math.sqrt(discriminant)) / (2 * a));
        double t2 = alignZero((-b + Math.sqrt(discriminant)) / (2 * a));
        if (t1 > 0 && t2 > 0)
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        if (t1 > 0)
            return List.of(ray.getPoint(t1));
        if (t2 > 0)
            return List.of(ray.getPoint(t2));

        return null;


//        Vector v = ray.getDir();
//        Vector va = axisRay.getDir();
//        if (v.equals(va) || v.equals(va.scale(-1))) { // ray is parallel to tube's axis
//            return null;
//        }
//        double vDotVa = v.dotProduct(va);
//        Vector v1 = isZero(vDotVa) ? v : v.subtract(va.scale(vDotVa));
//        double a = v1.lengthSquared();
//
////        if (isZero(vDotVa)) { //ray orthogonal to tube's axis
////            //@TODO
////        }
//
//        if (ray.getP0().equals(axisRay.getP0())) { // ray start at axisRay's head point
//            double t = Math.sqrt(radius*radius/a);
//            if (t > 0)
//                return List.of(ray.getPoint(t));
//        }
//
//        Vector vecDeltaP = ray.getP0().subtract(axisRay.getP0());
//        if (va.equals(vecDeltaP.normalize()) || va.equals(vecDeltaP.normalize().scale(-1)))//deltaP and va same direction - subtract will throw - ray start along tube's axis
//        {
//            double t = Math.sqrt(radius*radius/a);
//            if (t > 0)
//                return List.of(ray.getPoint(t));
//        }
//        double deltaPqDotVa = vecDeltaP.dotProduct(va);
//        Vector v2 = isZero(deltaPqDotVa) ? vecDeltaP : vecDeltaP.subtract(va.scale(deltaPqDotVa));
//        double c = v2.lengthSquared() - radius*radius;
//        double b = v2.dotProduct(v1) * 2;
//
////        if () { // vector (p-p0) is orthogonal to vector v, ray start in front of axis's head point
////            //@TODO
////        }
//
////        if (true)//v and va same direction - subtract will throw - parallel
////        {//@TODO
////
////        }
//
//        double a = v.subtract(va.scale(vDotVa)).lengthSquared();
//        double b = vecDeltaP.subtract(va.scale(deltaPqDotVa)).dotProduct(v.subtract(va.scale(vDotVa))) * 2;
//        double c = vecDeltaP.subtract(va.scale(deltaPqDotVa)).lengthSquared() - radius * radius;
//
//        double discriminant = b * b - 4 * a * c;
//
//        if (discriminant <= 0)
//            return null; // ray not intersect at all or just touching
//
////        if (discriminant == 0) {
////            double t1 = alignZero(-b / (2 * a));
////            if (t1 > 0)
////                return List.of(ray.getPoint(t1));
////        }
//
//        double t1 = alignZero((-b - Math.sqrt(discriminant)) / (2 * a));
//        double t2 = alignZero((-b + Math.sqrt(discriminant)) / (2 * a));
//        if (t1 > 0 && t2 > 0)
//            return List.of(ray.getPoint(t1), ray.getPoint(t2));
//        if (t1 > 0)
//            return List.of(ray.getPoint(t1));
//        if (t2 > 0)
//            return List.of(ray.getPoint(t2));
//
//        return null;
    }
}
