package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;
/**
 * Camera class that will create a camera in our scene,
 * and capture our images
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Camera {
    //Non are final - we will want to move the camera
    private Point centerPoint;

    //all normalized
    private Vector Vto; //x
    private Vector Vup; //y
    private Vector Vright; //z


    //------View Plane-------
    //size
    private double width;
    private double height;
    private double distance; // distance between camera and shape (focus)

    /**
     * Camera constructor
     * @param centerPoint center point
     * @param vto x axis
     * @param vup y axis
     */
    public Camera(Point centerPoint, Vector vto, Vector vup) {
        this.centerPoint = centerPoint;
        Vto = vto.normalize();
        Vup = vup.normalize();
        if(vto.dotProduct(vup) != 0) {
            throw new IllegalArgumentException("Vto & Vup aren't orthogonal");
        }
        Vright = Vto.crossProduct(Vup).normalize(); //vright = vto.cross(vup)
                                                    //-vright = back = vup.cross(vto)
    }

    /**
     * Sets width and height for View Plane (size)
     * @param w width (units)
     * @param h height (units)
     * @return camera (this)
     */
    public Camera setViewPlaneSize(double w, double h){
        width = w;
        height = h;
        return this;
    }

    /**
     * Sets distance of camera (from View Plane)
     * @param distance distance of camera from View Plane (VP)
     * @return camera (this)
     */
    public Camera setViewPlaneDistance(double distance){
        this.distance = distance;
        return this;
    }

    /**
     * Calculates ray that crosses the center of a pixel
     * @param nX columns (pixels)
     * @param nY rows (pixels)
     * @param j row in view plane
     * @param i column in view plane
     * @return ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point Pij = findMiddleOfPixel(nX, nY, j, i);
        //pc != p0 => subtract() will not return vector 0
            //pc = centerPoint.add(Vto.scale(distance));
        Vector Vij = Pij.subtract(centerPoint);
        //vi,j = pi,j − p0
        return new Ray(centerPoint, Vij);
    }

    /**
     * Calculates the middle of a pixel (parameters are given to the method)
     * @param nX width (in pixels)
     * @param nY height (in pixels)
     * @param j row in view plane (current)
     * @param i column in view plane (current)
     * @return Point at the middle of a pixel
     */
    private Point findMiddleOfPixel(int nX, int nY, int j, int i) {
        //Pc = centerPoint + d*v
        //calculating middle of view plane
        Point Pc = centerPoint.add(Vto.scale(distance));
        //calculating size of each pixel
        double rY = height / nY;
        double rX = width / nX;
        //calculating how much we need to move to find the wanted pixel
        double xj = (j - ((nX - 1) / 2.0)) * rX;
        double yi = -(i - ((nY - 1) / 2.0)) * rY;
        //Pi,j = pc + (xj*Vright + yi*Vup) <=> calculation based on formula (need to prevent addition with 0)
        Point Pij = Pc;
        if (!isZero(xj)) { Pij = Pij.add(Vright.scale(xj)); }
        if (!isZero(yi)) { Pij = Pij.add(Vup.scale(yi)); }
        return Pij;
    }
//region getters

    public Point getCenterPoint() {
        return centerPoint;
    }

    public Vector getVto() {
        return Vto;
    }

    public Vector getVup() {
        return Vup;
    }

    public Vector getVright() {
        return Vright;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    //endregion
}
