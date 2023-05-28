package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

public class SpotLight extends PointLight {
    private Vector direction;
    private double narrowBeam;

    /**
     * mConstructor for Spot Light
     *
     * @param intensity color of light
     * @param position  position of light-source
     * @param direction direction of the illumination
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
        this.narrowBeam = 1;
    }

    @Override
    public Color getIntensity(Point p) {
        double factor = alignZero(direction.dotProduct(getL(p)));
        if (factor <= 0)
            return Color.BLACK;
        if (narrowBeam != 1)
            factor = Math.pow(factor, narrowBeam);
        return super.getIntensity(p).scale(factor);
    }

    public PointLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
