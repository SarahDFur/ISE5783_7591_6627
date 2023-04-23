package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Camera {
    private Point CenterPoint;
    //all normalized
    private Vector Vto; //x
    private Vector Vup; //y
    private Vector Vright; //z


    //------View Plane-------
    //size
    private double Width;
    private double Height;
    private double Distance; // distance between camera and shape (focus)

    /**
     * Camera constructor
     * @param centerPoint center point
     * @param vto x axis
     * @param vup y axis
     */
    public Camera(Point centerPoint, Vector vto, Vector vup) {
        CenterPoint = centerPoint;
        Vto = vto.normalize();
        Vup = vup.normalize();
        if(vto.dotProduct(vup) != 90) {
            throw new IllegalArgumentException("Vto & Vup aren't orthogonal");
        }
        Vright = Vto.crossProduct(Vup).normalize(); //vright = vto.cross(vup) - (vup.cross(vto) = back)

    }

    /**
     * Sets width and height for View Plane (size)
     * @param w width
     * @param h height
     * @return camera (this)
     */
    public Camera setViewPlaneSize(double w, double h){
        Width = w;
        Height = h;
        return this;
    }

    /**
     * Sets distance of camera (from View Plane)
     * @param distance distance of camera from View Plane (VP)
     * @return camera (this)
     */
    public Camera setViewPlaneDistance(double distance){
        Distance = distance;
        return this;
    }

    /**
     *
     * @param nX columns (pixels)
     * @param nY rows (pixels)
     * @param j rows
     * @param i columns
     * @return ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        //nx - columns, ny - rows (pixels)
        return null;
    }
//region getters

    public Point getCenterPoint() {
        return CenterPoint;
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
        return Width;
    }

    public double getHeight() {
        return Height;
    }

    public double getDistance() {
        return Distance;
    }

    //endregion
}
