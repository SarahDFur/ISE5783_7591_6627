package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    private Point position;
    double kC = 1;
    double kL = 0;
    double kQ = 0;

    /**
     * Constructor for Point Light
     * @param intensity color of light
     * @param position position of light-source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        double dim = kC + kL * p.distance(position) + kQ * p.distanceSquared(position);
        return intensity.scale(1/dim);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }
}
