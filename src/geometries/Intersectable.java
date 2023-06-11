package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;
import java.util.Objects;

/**
 * Intersectable interface for calculating intersections
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public abstract class Intersectable {
    //region GeoPoint
    /**
     * Nested class for geo-point - to connect point to Geometry
     */
    public static class GeoPoint {
        /**
         * Geometric entity
         */
        public Geometry geometry;
        /**
         * point on the geometric entity
         */
        public Point point;

        /**
         * constructor
         * @param geo Geometric entity
         * @param p point on the geometric entity
         */
        public GeoPoint(Geometry geo, Point p){
            geometry = geo;
            point = p;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GeoPoint geoPoint = (GeoPoint) o;

            if (!Objects.equals(geometry, geoPoint.geometry)) return false;
            return point.equals(geoPoint.point);
        }

        @Override
        public int hashCode() {
            int result = geometry != null ? geometry.hashCode() : 0;
            result = 31 * result + point.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
    //endregion

    /**
     * Method for finding intersections between a Ray and geometrical bodies
     * @param ray ray that may or may not have intersecting points with a geometrical body
     * @return list of points where the ray meets the geometrical bodies
     */
    public List<Point> findIntersections(Ray ray){
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }
    
    public List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersectionsHelper(ray);
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);
}
