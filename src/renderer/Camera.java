package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Camera {
    private Point P0;
    //all normalized
    private Vector Vto;
    private Vector Vup;
    private Vector Vright;


    //------View Plane-------
    //size
    private double width;
    private double height;
    private double distance; // distance between camera and shape (focus)
    //setViewPlaneSize()
    public Camera(Point p0, Vector vto, Vector vup) {
        P0 = p0;
        Vto = vto;
        Vup = vup;
        Vright = Vto.crossProduct(Vup); //vright = vto.cross(vup) - (vup.cross(vto) = back)
    }

    public Point getP0() { return P0; }

    public void setP0(Point p0) { P0 = p0; }

    public Vector getVright() { return Vright; }

    public void setVright(Vector vright) { Vright = vright; }

    public Vector getVto() { return Vto; }

    public void setVto(Vector vto) { Vto = vto; }

    public Vector getVup() { return Vup; }

    public void setVup(Vector vup) { Vup = vup; }



    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }

}
