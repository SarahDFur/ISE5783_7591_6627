package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Triangle class extending Polygon class,
 * represents two-dimensional triangle in 3D Cartesian coordinate system
 */
public class Triangle extends Polygon{
    /**
     * constructor that received 3 points
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Triangle(Point p1, Point p2, Point p3){
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return super.findIntersections(ray);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                ", plane=" + plane +
                '}';
    }
}
