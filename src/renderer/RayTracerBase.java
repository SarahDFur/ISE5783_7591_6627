package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Class for ray tracing
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public abstract class RayTracerBase {
    protected Scene scene;

    /**
     * Constructor
     * @param scene given scene
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Tracing a Ray method
     * @param ray given Ray
     * @return Color that we see from the rays' intersection with a geometrie
     */
    public abstract Color traceRay(Ray ray);

}
