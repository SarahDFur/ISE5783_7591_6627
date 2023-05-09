package geometries;

/**
 * RadialGeometry abstract class implementing Geometry interface,
 * served all geometric classes that use a radius
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public abstract class RadialGeometry extends Geometry{
    protected final double radius;

    /**
     * constructor
     * @param radius radius of geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
