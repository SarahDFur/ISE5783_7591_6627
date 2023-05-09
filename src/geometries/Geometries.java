package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

/**
 * Class creates a list of geometries (Composite)
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Geometries extends Intersectable {

    private List<Intersectable> geometries;

    /**
     * default constructor
     */
    public Geometries() {
        this.geometries = new LinkedList<>();
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
            this.geometries.addAll(List.of(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geo : geometries) {
            var temp = geo.findIntersections(ray);
            if (temp != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(temp);
                else
                    intersections.addAll(temp);
            }
        }
        return intersections;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersectionsWithAllShapes = null;

        //for each item in the list add the intersections to the list of intersections
        for (var item : geometries) {
            var itemList = item.findGeoIntersectionsHelper(ray);
            //out.print(itemList);
            //out.print("\n");
            //if there are(!) intersections with the specific item
            if (itemList != null) {
                //if the list of intersections with everyone is null, then create a new list
                if (intersectionsWithAllShapes == null) {
                    intersectionsWithAllShapes = new LinkedList<GeoPoint>();
                }
                //(if there are intersections with item) add list of intersections with item to list of all intersections
                intersectionsWithAllShapes.addAll(itemList);
            }
        }

        return intersectionsWithAllShapes;
    }
}
