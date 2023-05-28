package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
    private Vector direction;
    private int narrowBeam;

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
        Vector l = getL(p); //vector from light to point p
        double max = Math.max(0, direction.dotProduct(l));
        if (narrowBeam != 1)
            return super.getIntensity(p).scale(Math.pow(max, narrowBeam));
        return super.getIntensity(p).scale(max); //uses parent function to implement DRY
    }

    public PointLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
