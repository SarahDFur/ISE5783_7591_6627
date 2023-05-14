package lighting;

import primitives.*;
/**
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public interface LightSource {
    /**
     * Calculates the intensity of the light - based on given point
     * @param p point to calculate intensity
     * @return returns the intensity of the light - Il
     */
    public Color getIntensity(Point p);

    /**
     * Calculates the vector between the light-source and point on object
     * @param p point on object
     * @return returns vector between light-source and the point
     */
    public Vector getL(Point p);
}
