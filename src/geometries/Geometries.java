package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private List<Intersectable> geometriesList;

    /**
     * default constructor
     */
    public Geometries() {
        this.geometriesList = new LinkedList<>();
    }

    /**
     * constructor
     *
     * @param geometries to init in list
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * add param geometries to list
     *
     * @param geometries to add to list
     */
    public void add(Intersectable... geometries) {
        if (geometries.length > 0)
            this.geometriesList.addAll(List.of(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geo : geometriesList) {
            List<Point> temp = geo.findIntersections(ray);
            if (temp != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(temp);
                else
                    intersections.addAll(temp);
            }
        }
        return intersections;
    }
}
