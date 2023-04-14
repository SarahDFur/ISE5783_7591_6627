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
    final Double3 xyz;

    /**
     * constructor which receives 3 doubles
     * @param d1 first coordinate
     * @param d2 second coordinate
     * @param d3 third coordinate
     */
    public Point(double d1, double d2, double d3) {
        xyz = new Double3(d1, d2, d3);
    }

    /**
     * constructor which receives Double3
     * @param xyz the Double3 for creating the Point
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return xyz.equals(point.xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

    /**
     * calculate caller point plus param vector
     * @param vector to add to caller point
     * @return a point result of vector addition
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * calculate caller point minus param point
     * @param other is other point
     * @return a vector starting at caller and ending at other
     */
    public Vector subtract(Point other) {
        if (other.xyz.equals(xyz)) {
            throw new IllegalArgumentException("Cannot create Vector (0,0,0)");
        }
        return new Vector(xyz.subtract(other.xyz));
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
     * finds the squared distance between two points
     * @param p one of the points for comparison
     * @return returns the distance between two points
     */
    public double distanceSquared(Point p) {
        double a = xyz.d1 - p.xyz.d1;
        double b = xyz.d2 - p.xyz.d2;
        double c = xyz.d3 - p.xyz.d3;

        return a * a + b * b + c * c;
    }
}
