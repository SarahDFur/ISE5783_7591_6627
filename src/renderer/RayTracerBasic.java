package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * (Extends) class for ray tracing - calculates color of pixels
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class RayTracerBasic extends RayTracerBase {

    /**
     * value for moving GoePoint, for shading bug
     */
    private static final double DELTA = 0.1;

    /**
     * Constructor (sends to super)
     *
     * @param scene given scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        GeoPoint closestGeoPoint = ray.findClosestGeoPoint(intersections);
        Color currentPixelColor = calcColor(closestGeoPoint, ray);
        return currentPixelColor;
    }

    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity()
                .add(calcLocalEffects(geoPoint, ray));

    }

    /**
     * calculate all local effects of the light sources on color in point
     *
     * @param gp  GeoPoint of point to calculate effects on
     * @param ray camera's ray that intersect Geometry
     * @return calculated color of point on Geometry in GeoPoint gp
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Color color = gp.geometry.getEmission();
        Vector vector = ray.getDir();
        Vector normal = gp.geometry.getNormal(gp.point);
        double nv = alignZero(normal.dotProduct(vector));
        if (nv == 0)
            return color;
        Material mat = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector lightVector = lightSource.getL(gp.point);
            double nl = alignZero(normal.dotProduct(lightVector));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                if (unshaded(gp, lightSource, lightVector, normal, nl)) {
                    Color lightIntensity = lightSource.getIntensity(gp.point);
                    color = color.add(lightIntensity.scale(calcDiffusive(mat, nl)),
                            lightIntensity.scale(calcSpecular(mat, normal, lightVector, nl, vector)));
                }
            }
        }
        return color;
    }

    /**
     * calculate the specular effect
     *
     * @param mat material of Geometry
     * @param n   normal vector of Geometry
     * @param l   light source vector
     * @param nl  dot product of normal and light vectors
     * @param v   direction of camera's ray
     * @return specular effect on color
     */
    private Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
        Vector reflectedVector = l.subtract(n.scale(2 * nl));
        double max = Math.max(0, alignZero(v.scale(-1).dotProduct(reflectedVector)));
        return mat.kS.scale(Math.pow(max, mat.nShininess));
    }

    /**
     * calculate the diffusive effect
     *
     * @param mat material of Geometry
     * @param nl  dot product of normal and light vectors
     * @return diffusive effect on color
     */
    private Double3 calcDiffusive(Material mat, double nl) {
        return mat.kD.scale(Math.abs(nl));
    }

    /**
     * check whether a point is unshaded
     *
     * @param gp GeoPint to check
     * @param l  vector light
     * @param n  vector normal
     * @return true if point gp is unshaded
     */
    private boolean unshaded(GeoPoint gp, LightSource lightSource, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections == null) return true;

        double distance = alignZero(lightSource.getDistance(gp.point));
        for (GeoPoint intersection : intersections) {
            if (alignZero(intersection.point.distance(gp.point)) < distance)
                return false;
        }


        return true;
    }

}
