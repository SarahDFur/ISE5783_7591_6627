package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    protected PointLight(Color intensity) {
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
