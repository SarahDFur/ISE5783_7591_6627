package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public class Geometries implements Intersectable {

    private List<Intersectable> intersectables;

    public Geometries() {
        this.intersectables = null;
    }

    public Geometries(Intersectable... geometries) {

    }

    public void add(Intersectable... geometries) {

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
