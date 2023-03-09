package geometries;
import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {
    private Point q0;
    Vector normal;

    public Plane(Point point1, Point point2, Point point3) {

    }

    public Vector getNormal() {
        return normal;
    }
    @Override
    public Vector getNormal(Point p) {
        return getNormal();
    }
}
