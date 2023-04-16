package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Geometry interface for geometric objects
 */
public interface Geometry extends Intersectable {
    /**
     * getter for normal vector to the Geometry
     * @param point
     * @return normal vector at received point
     */
    public Vector getNormal(Point point);
}
