package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Intersectable interface for calculating intersections
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public interface Intersectable {
    /**
     * Method for finding intersections between a Ray and geometrical bodies
     * @param ray ray that may or may not have intersecting points with a geometrical body
     * @return list of points where the ray meets the geometrical bodies
     */
    public List<Point> findIntersections(Ray ray);
}
