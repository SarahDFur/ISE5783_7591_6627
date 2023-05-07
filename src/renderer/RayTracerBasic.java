package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

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
        LinkedList<Point> listPointsIntersections = (LinkedList<Point>) scene.geometries.findIntersections(ray);
        if(listPointsIntersections ==null || listPointsIntersections.isEmpty()){
            return scene.background;
        }
        Point closestPoint = ray.findClosestPoint(listPointsIntersections);
        Color currentPixelColor = calcColor(closestPoint);
        return currentPixelColor;
    }

    private Color calcColor(Point closestPoint) {
        Color pointColor = new Color (java.awt.Color.MAGENTA);
        return pointColor;
    }
}
