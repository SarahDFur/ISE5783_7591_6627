package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Intersectable interface for calculating intersections
 */
public interface Intersectable {
    public List<Point> findIntersections(Ray ray);
}
