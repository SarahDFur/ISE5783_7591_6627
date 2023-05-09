package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource {
    protected DirectionalLight(Color intensity) {
        super(intensity);
    }

    @Override
    public Color getIntensity(Point p) {
        return null;
    }

    @Override
    public Vector getL(Point p) {
        return null;
    }
}
