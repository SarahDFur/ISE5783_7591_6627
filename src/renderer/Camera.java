package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera class that will create a camera in our scene,
 * and capture our images
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Camera {
    //Non are final - we will want to move the camera
    private Point centerPoint;

    //all normalized
    private Vector vTo; //x
    private Vector vUp; //y
    private Vector vRight; //z


    //------View Plane-------
    //size
    private double width;
    private double height;
    private double distance; // distance between camera and shape (focus)

    //----------
    ImageWriter imageWriter;
    RayTracerBase rayTracer;

    /**
     * Camera constructor
     *
     * @param centerPoint center point
     * @param vto         x axis
     * @param vup         y axis
     */
    public Camera(Point centerPoint, Vector vto, Vector vup) {
        this.centerPoint = centerPoint;
        vTo = vto.normalize();
        vUp = vup.normalize();
        if (vto.dotProduct(vup) != 0) {
            throw new IllegalArgumentException("Vto & Vup aren't orthogonal");
        }
        vRight = vTo.crossProduct(vUp).normalize(); //vright = vto.cross(vup)
        //-vright = back = vup.cross(vto)
    }

    /**
     * Calculates ray that crosses the center of a pixel
     *
     * @param nX columns (pixels)
     * @param nY rows (pixels)
     * @param j  row in view plane
     * @param i  column in view plane
     * @return ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point Pij = findPixelCenter(nX, nY, j, i);
        //pc != p0 => subtract() will not return vector 0
        //pc = centerPoint.add(Vto.scale(distance));
        Vector Vij = Pij.subtract(centerPoint);
        //vi,j = pi,j − p0
        return new Ray(centerPoint, Vij);
    }

    /**
     * Calculates the middle of a pixel (parameters are given to the method)
     *
     * @param nX width (in pixels)
     * @param nY height (in pixels)
     * @param j  row in view plane (current)
     * @param i  column in view plane (current)
     * @return Point at the middle of a pixel
     */
    private Point findPixelCenter(int nX, int nY, int j, int i) {
        //Pc = centerPoint + d*v
        //calculating middle of view plane
        Point Pc = centerPoint.add(vTo.scale(distance));
        //calculating size of each pixel
        double rY = height / nY;
        double rX = width / nX;
        //calculating how much we need to move to find the wanted pixel
        double xj = (j - ((nX - 1) / 2.0)) * rX;
        double yi = -(i - ((nY - 1) / 2.0)) * rY;
        //Pi,j = pc + (xj*Vright + yi*Vup) <=> calculation based on formula (need to prevent addition with 0)
        Point Pij = Pc;
        if (!isZero(xj)) {
            Pij = Pij.add(vRight.scale(xj));
        }
        if (!isZero(yi)) {
            Pij = Pij.add(vUp.scale(yi));
        }
        return Pij;
    }

    //region getters
    public Point getCenterPoint() {
        return centerPoint;
    }

    public Vector getVto() {
        return vTo;
    }

    public Vector getVup() {
        return vUp;
    }

    public Vector getVright() {
        return vRight;
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

    public ImageWriter getImageWriter() {
        return imageWriter;
    }

    public RayTracerBase getRayTracer() {
        return rayTracer;
    }
    //endregion

    //region setters
    public Camera setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
        return this;
    }

    public Camera setVto(Vector vto) {
        vTo = vto;
        return this;
    }

    public Camera setVup(Vector vup) {
        vUp = vup;
        return this;
    }

    public Camera setWidth(double width) {
        this.width = width;
        return this;
    }

    public Camera setHeight(double height) {
        this.height = height;
        return this;
    }

    public Camera setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Sets width and height for View Plane (size)
     *
     * @param w width (units)
     * @param h height (units)
     * @return camera (this)
     */
    public Camera setViewPlaneSize(double w, double h) {
        width = w;
        height = h;
        return this;
    }

    /**
     * Sets distance of camera (from View Plane)
     *
     * @param distance distance of camera from View Plane (VP)
     * @return camera (this)
     */
    public Camera setViewPlaneDistance(double distance) {
        this.distance = distance;
        return this;
    }
    //endregion

    /**
     * Renders an image
     */
    public Camera renderImage() {
        //info: coordinates of the camera != null
        if ((vRight == null) || (vUp == null) || (vTo == null) || (centerPoint == null))
            throw new MissingResourceException("Camera coordinates are not initialized", "Camera", "coordinates");

        //info: view plane variables != null (doubles != null)

        //info: final image creation
        if ((imageWriter == null) || (rayTracer == null))
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer info");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nY; i++) { //row
            for (int j = 0; j < nX; j++) { //column
                Ray thisPixelRay = constructRay(nX, nY, j, i);
                Color thisPixelColor = rayTracer.traceRay(thisPixelRay);
                imageWriter.writePixel(j, i, thisPixelColor);
            }
        }
        return this;
    }

    /**
     * Prints a grid to the image
     *
     * @param interval size of squares in the grid
     * @param color    color for the grid
     */
    public void printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer info");

        for (int i = 0; i < imageWriter.getNx(); i++) { // row
            for (int j = 0; j < imageWriter.getNy(); j++) { // column
                //grid: 800/50 = 16, 500/50 = 10
                if ((i % interval == 0) || (j % interval == 0)) {
                    imageWriter.writePixel(i, j, color);
                }
            }
        }
    }

    /**
     * Creates image - sends to method inside ImageWriter class
     */
    public void writeToImage() {
        if (imageWriter == null)
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer info");
        //call image writer
        imageWriter.writeToImage();
    }

    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    public Camera setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }
}
