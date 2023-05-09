package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Geometry interface for geometric objects
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * getter for normal vector to the Geometry
     * @param point
     * @return normal vector at received point
     */
    public abstract Vector getNormal(Point point);
}
