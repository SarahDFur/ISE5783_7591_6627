package lighting;

import primitives.Color;

/**
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
abstract class Light {
    protected Color intensity;

    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * returns the intensity of the light calculated based on a given point
     * @return intensity of light - I0
     */
    public Color getIntensity() {
        return intensity;
    }
}
