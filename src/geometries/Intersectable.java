package geometries;

import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.LinkedList;
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
     * boundary of the entity represented by the array [x[min,max],y[min,max],z[min,max]]
     */
    //@TODO: allocate appropriate memory
    public int[][] boundary = new int[][] {{0,0},{0,0},{0,0}};

    /**
     * finds the boundary values of the geometric entity or a group of geometric entities
     *
     * @return the geometry boundary
     */
    protected abstract int[][] calcBoundary();

    /**
     * Method for finding intersections between a Ray and geometrical bodies
     * @param ray ray that may or may not have intersecting points with a geometrical body
     * @return list of points where the ray meets the geometrical bodies
     */
    public List<Point> findIntersections(Ray ray){
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Finds all intersection GeoPoints of a ray and a geometric entity.
     *
     * @param ray the ray that intersect with the geometric entity.
     * @return list of intersection Geopoints.
     */
    public List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersectionsHelper(ray);
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    /**
     * boundary getter
     *
     * @return the boundary
     */
    public int[][] getBoundary() {
        return boundary;
    }


    /**
     * return the indexes of all voxels that the geometric entity intersects with
     * @param scene the scene that we would use its voxels
     * @return the indexes of the voxels intersected with this
     */
    protected List<Double3> findVoxels(Scene scene) {
        List<Double3> indexes = new LinkedList<>();//since we won't remove any voxel but only add we will use linked list
        double xEdgeVoxel=scene.getXEdgeVoxel();
        double yEdgeVoxel=scene.getYEdgeVoxel();
        double zEdgeVoxel=scene.getZEdgeVoxel();
        int xMinIndex = (int) ((this.boundary[0][0] - scene.geometries.boundary[0][0]) / xEdgeVoxel - 0.01);
        int xMaxIndex = (int) ((this.boundary[0][1] - scene.geometries.boundary[0][0]) / xEdgeVoxel - 0.01);
        int yMinIndex = (int) ((this.boundary[1][0] - scene.geometries.boundary[1][0]) / yEdgeVoxel - 0.01);
        int yMaxIndex = (int) ((this.boundary[1][1] - scene.geometries.boundary[1][0]) / yEdgeVoxel - 0.01);
        int zMinIndex = (int) ((this.boundary[2][0] - scene.geometries.boundary[2][0]) / zEdgeVoxel - 0.01);
        int zMaxIndex = (int) ((this.boundary[2][1] - scene.geometries.boundary[2][0]) / zEdgeVoxel - 0.01);
        //move over all the voxels in the range of indexes
        for (int i = xMinIndex; i <= xMaxIndex; i++) {
            for (int j = yMinIndex; j <= yMaxIndex; j++) {
                for (int k = zMinIndex; k <= zMaxIndex; k++) {
                    indexes.add(new Double3(i, j, k));
                }
            }
        }
        return indexes;
    }
}
