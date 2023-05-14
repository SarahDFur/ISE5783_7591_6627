package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight{
    private Vector direction;

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction;
    }
    @Override
    public Color getIntensity(Point p) {
        Vector l = getL(p); //vector from light to point p
        double max = Math.max(0, direction.normalize().dotProduct(l));
        return super.getIntensity(p).scale(max); //uses parent function to implement DRY
    }

    public PointLight setNarrowBeam(int i) {
        return this;
    }
}
