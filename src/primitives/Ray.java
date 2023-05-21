package primitives;

import java.util.LinkedList;
import java.util.List;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.isZero;

/**
 * Ray class represents a ray in 3D Cartesian coordinate system,
 * using a point and a vector
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Ray {
    final private Point p0;
    final private Vector dir;

    /**
     * constructor
     * normalize the vector received
     * @param p0 point
     * @param dir direction vector
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    public Point getPoint(double t) {
        return isZero(t) ? p0 : p0.add(dir.scale(t));
    }

    public Point getP0() {
        return p0;
    }

    public Vector getDir() {
        return dir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        if (!p0.equals(ray.p0)) return false;
        return dir.equals(ray.dir);
    }

    @Override
    public int hashCode() {
        int result = p0.hashCode();
        result = 31 * result + dir.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPointList) {
        //if the list is empty return null, there is no closest point
        if (geoPointList == null || geoPointList.isEmpty())
            return null;

        //closest point starts as first point on the list
        GeoPoint closestPoint = geoPointList.get(0);
        //distance initialized to the largest value
        double distance = Double.MAX_VALUE;
        double d;

        //compare the distance of each point, if smaller, then update the closest point and distance
        for (var pt : geoPointList) {
            d = p0.distance(pt.point);
            if (d < distance) {
                distance = d;
                closestPoint = pt;
            }
        }
        return closestPoint;
    }

    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;


//        //if the list is empty return null, there is no closest point
//        if(points == null || points.isEmpty())
//            return null;
//        //initial initialization of variables:
//        Point closestPoint = points.get(0); //closest point starts as first point on the list
//        double distance = Double.MAX_VALUE; //distance initialized to the largest value
//        double tempDist;
//
//        //compare the distance of each point, if smaller, then update the closest point and distance
//        for (Point pt: points) {
//            tempDist = p0.distance(pt);
//            if (tempDist < distance){
//                distance = tempDist;
//                closestPoint = pt;
//            }
//        }
//        return closestPoint;
    }
}
