package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * (Extends) class for ray tracing - calculates color of pixels
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class RayTracerBasic extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INIT_CALC_COLOR_K = Double3.ONE;

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
        GeoPoint closestGeoPoint = findClosestIntersection(ray);
        if (closestGeoPoint == null)
            return scene.background;
        Color currentPixelColor = calcColor(closestGeoPoint, ray);
        return currentPixelColor;
    }

    //region CalcColor

    /**
     * calcColor - recursive function
     * calculate local effects (diffuse, specular) and global effects (reflection, refraction)
     *
     * @param geoPoint geometry point to calculate its color
     * @param ray      the ray that intersect geoPoint
     * @param level    depth of recursion for global effects
     * @param k        volume of color
     * @return color of point, including all effects
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray);
        return 1 == level ?
                color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * calcColor - wrapper function
     * calculate the color of point
     *
     * @param geoPoint geometry point to calculate its color
     * @param ray      the ray that intersect geoPoint
     * @return color of point, including all effects
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity()
                .add(calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INIT_CALC_COLOR_K));
    }

    //endregion

    //region Local Effects

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

    //endregion

    //region Global Effects

    /**
     * calculate all global effects on color in point, according to k factor
     *
     * @param gp    calculate the color of this point
     * @param ray   the ray of intersection that 'hit' the point
     * @param level of the recursion
     * @param k     the volume of the color
     * @return the calculated color
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDir();
        Vector normal = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        Ray reflectedRay = constructReflectionRay(gp.point, normal, v);
        Ray refractedRay = constructRefractionRay(gp.point, normal, v);
        return calcGlobalEffects(reflectedRay, level, k, material.kR)
                .add(calcGlobalEffects(refractedRay, level, k, material.kT));
    }

    /**
     * calculate the global effects of specific level on color if there are more intersections to check
     *
     * @param ray   the is used to intersect the geometries
     * @param level the current level
     * @param kx    a color factor to reduce the color (according to the current level of recursion)
     * @param kkx   the color factor for the next level of recursion
     * @return the new calculated color
     */
    private Color calcGlobalEffects(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ?
                scene.background : calcColor(gp, ray, level - 1, kkx)).scale(kx);
    }

    /**
     * construct a refraction ray
     *
     * @param point  point of constructed ray
     * @param normal normal of geometry
     * @param vector vector of original ray to be refracted
     * @return refracted ray
     */
    private Ray constructRefractionRay(Point point, Vector normal, Vector vector) {
        return new Ray(point, vector, normal);
    }

    /**
     * construct a reflection ray
     *
     * @param point  point of constructed ray
     * @param normal normal of geometry
     * @param vector vector of original ray to be reflected
     * @return reflected ray
     */
    private Ray constructReflectionRay(Point point, Vector normal, Vector vector) {
        Vector reflectedVector = vector.subtract(normal.scale(2 * vector.dotProduct(normal)));
        return new Ray(point, reflectedVector, normal);
    }

    //endregion


    /**
     * check whether a point is unshaded
     *
     * @param gp GeoPint to check
     * @param l  vector light
     * @param n  vector normal
     * @return true if point gp is unshaded
     */
    private boolean unshaded(GeoPoint gp, LightSource lightSource, Vector l, Vector n, double nl) {
        Ray lightRay = new Ray(gp.point, l.scale(-1), n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections == null) return true;

        double distance = alignZero(lightSource.getDistance(gp.point));
        for (GeoPoint intersection : intersections) {

            if(intersection.geometry.getMaterial().kT != Double3.ZERO)
                return true;

            if (alignZero(intersection.point.distance(gp.point)) < distance)
                return false;
        }

        return true;
    }

    /**
     * find the closest intersection point with ray
     *
     * @param ray ray to check intersections with
     * @return closest intersection point with ray
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return null;
        return ray.findClosestGeoPoint(intersections);
    }
}
