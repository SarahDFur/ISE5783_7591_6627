package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Class for ambient lighting of a scene
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class AmbientLight {
    private final Color intensity;
    //@TODO: check the NONE - how is it supposed to be created ??
    public static final AmbientLight NONE = new AmbientLight();

    /***
     * Constructor
     * @param Ia basic light intensity
     * @param Ka factor of intensity
     */
    public AmbientLight(Color Ia, Double3 Ka){
        intensity = Ia.scale(Ka);
    } //Ip = Ia*Ka

    /***
     * Constructor
     */
    public AmbientLight(){
        intensity = Color.BLACK;
    }

    /***
     * getter for ambient light intensity
     * @return intensity of ambient light
     */
    public Color getIntensity() {
        return intensity;
    }
}
