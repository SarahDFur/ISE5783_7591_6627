package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.LinkedList;

/**
 * (Extends) class for ray tracing
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class RayTracerBasic extends RayTracerBase {

    /**
     * Constructor (sends to super)
     * @param scene given scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        //@TODO: traceRay - change it for stage 6
        LinkedList<Point> intersections = (LinkedList<Point>) scene.geometries.findIntersections(ray);
        if(intersections ==null || intersections.isEmpty()){
            return scene.background;
        }
        Point closestPoint = ray.findClosestPoint(intersections);
        Color currentPixelColor = calcColor(closestPoint);
        return currentPixelColor;
    }

    private Color calcColor(Point closestPoint) {
        //@TODO: calcColor - change it for stage 6
        Color pointColor = new Color (java.awt.Color.MAGENTA);
        return pointColor;
    }
}
