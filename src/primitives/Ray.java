package primitives;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Ray class represents a ray in 3D Cartesian coordinate system,
 * using a point and a vector
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

    public Point findClosestPoint(List<Point> pointList) {
        return null;
    }
}
