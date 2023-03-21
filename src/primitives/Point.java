package primitives;

import java.util.Objects;

import static java.lang.Math.max;

/**
 * Point class represents a point in 3D Cartesian coordinate system
 */
public class Point {
    /**
     * point zero is frequently used
     */
    public static final Point ZERO = new Point(0, 0, 0);
    /**
     * coordinates of the point
     */
    final Double3 _xyz;

    /**
     * constructor which receives 3 doubles
     * @param d1 first coordinate
     * @param d2 second coordinate
     * @param d3 third coordinate
     */
    public Point(double d1, double d2, double d3) {
        _xyz = new Double3(d1, d2, d3);
    }

    /**
     * constructor which receives Double3
     * @param xyz the Double3 for creating the Point
     */
    public Point(Double3 xyz) {
        _xyz = xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return _xyz.equals(point._xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "_xyz=" + _xyz +
                '}';
    }

    /**
     *
     * @param vector to add to vector
     * @return point result of vector addition
     */
    public Point add(Vector vector) {
        return new Point(_xyz.add(vector._xyz));
    }

    /**
     * vector is in the direction from the other to me
     * @param other is other vector
     * @return my vector minus other vector
     */
    public Vector subtract(Point other) {
        if (other._xyz.equals(_xyz)) {
            throw new IllegalArgumentException("Cannot create Vector (0,0,0)");
        }
        return new Vector(_xyz.subtract(other._xyz));
    }

    /**
     * finds distance between the current point and a different point
     * @param p the point to find the distance from
     * @return the distance
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    /**
     *
     * @param p one of the points for comparison
     * @return returns the distance between two points
     */
    public double distanceSquared(Point p) {
        double a = _xyz.d1 - p._xyz.d1;
        double b = _xyz.d2 - p._xyz.d2;
        double c = _xyz.d3 - p._xyz.d3;

        return a * a + b * b + c * c;
    }
}
