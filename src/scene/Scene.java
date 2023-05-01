package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;
import primitives.Point;

import java.awt.*;

public class Scene {

    public Geometries geometries;
    private AmbientLight ambientLight;
    private Color background;

    public Scene(String sceneName) {
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }
}
