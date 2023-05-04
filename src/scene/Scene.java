package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

import java.awt.*;

public class Scene {

    public  String name;

    public Geometries geometries = new Geometries();
    //@TODO: Is this how the ambient light is supposed to be proclaimed ??
    public AmbientLight ambientLight = new AmbientLight(); // will be defaulted to black
    public Color background = Color.BLACK;

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
}
