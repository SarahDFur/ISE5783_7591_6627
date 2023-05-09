package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Cylinder class extending class Tube
 * represents a cylinder in 3D Cartesian coordinate system
 * @author Sarah Daatyah Furmanski and Efrat Kartman
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
        Point centerBase2 = axisRay.getPoint(height); //axisRay.getP0().add(axisRay.getDir().scale(height));
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
    }

    public double getHeight() {
        return height;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        //@TODO: Cylinder - findIntersections()
//        List<Point> intersections = super.findIntersections(ray);
//        if(intersections == null){
//            if(ray.getDir().equals(this.axisRay.getDir()) || ray.getDir().equals(this.axisRay.getDir().scale(-1))){
//                //ray is parallel to cylinder's axis
//                if(){ //ray's head is inside tube
//                    // find two "t", return if positives
//                }
//            }
//            return null;
//        }
//        if(intersections.size()==1){
//
//        }
//        if (intersections.size()==2){
//
//        }
        Vector va = axisRay.getDir();
        Point p1 = axisRay.getP0();
        Point p2 = axisRay.getPoint(height);

        List<GeoPoint> interactions = null;
        List<GeoPoint> tubeIntersections = super.findGeoIntersectionsHelper(ray);
        if(tubeIntersections != null){
            for (GeoPoint q : tubeIntersections){
                Vector qMinusP = q.point.subtract(p1);
                double vaDotQMinusP1 = alignZero(va.dotProduct(qMinusP));
                qMinusP = q.point.subtract(p2);
                double vaDotQMinusP2 = alignZero(va.dotProduct(qMinusP));
                if (vaDotQMinusP1 >0 && vaDotQMinusP2 <0){
                    if(interactions == null)
                        interactions = new LinkedList<>();
                    interactions.add(q);
                }
            }
            if (interactions != null && interactions.size() == 2)
                return interactions;
        }

        Plane base = new Plane(axisRay.getP0(), getAxisRay().getDir());
        List<GeoPoint> baseInteraction = base.findGeoIntersectionsHelper(ray);
        if (baseInteraction != null){
            GeoPoint q = baseInteraction.get(0);
            if(q.point.equals(p1)){
                if (interactions == null)
                    interactions = new LinkedList<>();
                interactions.add(q);
            }
            else {
                Vector qMinusP = q.point.subtract(p1);
                if (qMinusP.lengthSquared() < radius * radius) {
                    if (interactions == null)
                        interactions = new LinkedList<>();
                    interactions.add(q);
                }
            }
            if (interactions != null && interactions.size() == 2)
                return interactions;

        }

        base = new Plane(axisRay.getPoint(height)/*=p2*/, getAxisRay().getDir());
        baseInteraction = base.findGeoIntersectionsHelper(ray);
        if (baseInteraction != null){
            GeoPoint q = baseInteraction.get(0);
            if(q.point.equals(p2)){
                if (interactions == null)
                    interactions = new LinkedList<>();
                interactions.add(q);
            }
            else {
                Vector qMinusP = q.point.subtract(p2);
                if (qMinusP.lengthSquared() < radius * radius) {
                    if (interactions == null)
                        interactions = new LinkedList<>();
                    interactions.add(q);
                }
            }
            if (interactions != null && interactions.size() == 2)
                return interactions;

        }

        return interactions;
    }
}
