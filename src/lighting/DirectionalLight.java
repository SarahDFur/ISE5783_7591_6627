package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class DirectionalLight extends Light implements LightSource {
    private Vector direction;

    /**
     * Constructor for Directional Light
     * @param intensity color of light
     * @param direction direction of the illumination
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return this.intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
