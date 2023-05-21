package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Geometry interface for geometric objects
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;

    private Material material = new Material();

    /**
     * getter for field emission
     * @return emission
     */
    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * getter for normal vector to the Geometry
     * @param point point on geometry to calculate
     * @return normal vector at received point
     */
    public abstract Vector getNormal(Point point);

}
