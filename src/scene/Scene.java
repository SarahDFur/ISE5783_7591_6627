package scene;

import geometries.Geometries;
import lighting.*;
import primitives.Color;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for creating a scene
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Scene {

    public  String name;
    public Geometries geometries = new Geometries();
    public AmbientLight ambientLight = new AmbientLight(); // will be defaulted to black
    public Color background = Color.BLACK;
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructor
     * @param name Name of the scene
     */
    public Scene(String name){
        this.name = name;
        this.geometries = new Geometries();
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
