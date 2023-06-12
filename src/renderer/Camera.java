package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
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

    //-----Improvements-----
    //Anti-Aliasing
    private static int ANTI_ALIASING_FACTOR = 1;

    //Focus
//    private boolean depthOfFieldFlag = false;
//    private double focalDistance = 2;
//    private double apertureSize = 0.01;
//    private static int NUM_OF_APERTURE_POINTS = 2;
//ON/OFF button default is off
    private boolean depthButton = false;
    //focal length
    private double focalLength = 2;
    private double apertureSize = 0.01;
    private static final int NUMBER_OF_APERTURE_POINTS = 10;

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

    //region construct ray calculations

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
        Point Pij = getPixelCenter(nX, nY, j, i);
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
    private Point getPixelCenter(int nX, int nY, int j, int i) {
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

    public List<Ray> constructRays(int nX, int nY, int j, int i) {
        List<Ray> rays = new LinkedList<>();
        Point pixelCenter = getPixelCenter(nX, nY, j, i);
        double rY = (height / nY) / ANTI_ALIASING_FACTOR;
        double rX = (width / nX) / ANTI_ALIASING_FACTOR;
        double x, y;

        for (int rowNumber = 0; rowNumber < ANTI_ALIASING_FACTOR; rowNumber++) {
            for (int colNumber = 0; colNumber < ANTI_ALIASING_FACTOR; colNumber++) {
                y = -(rowNumber - (ANTI_ALIASING_FACTOR - 1d) / 2) * rY;
                x = (colNumber - (ANTI_ALIASING_FACTOR - 1d) / 2) * rX;
                Point pIJ = pixelCenter;
                if (y != 0) pIJ = pIJ.add(vUp.scale(y));
                if (x != 0) pIJ = pIJ.add(vRight.scale(x));
                rays.add(new Ray(centerPoint, pIJ.subtract(centerPoint)));
            }
        }
        return rays;
    }

    //endregion

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

    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    public Camera setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }

    /**
     * set anti-aliasing factor.
     * final number of rays will be (factor * factor)
     *
     * @param antiAliasingFactor num of rays for anti aliasing
     * @return this Camera
     */
    public Camera setAntiAliasingFactor(int antiAliasingFactor) {
        if (antiAliasingFactor <= 0)
            throw new IllegalArgumentException("anti aliasing factor must be positive integer");
        ANTI_ALIASING_FACTOR = antiAliasingFactor;
        return this;
    }

    //    public Camera setDepthOfField(boolean flag) {
//        depthOfFieldFlag = flag;
//        return this;
//    }
//
//    public Camera setDepthOfField(boolean flag, double focalDistance, double apertureSize) {
//        this.depthOfFieldFlag = flag;
//        this.focalDistance = focalDistance;
//        this.apertureSize = apertureSize;
//        return this;
//    }
    public Camera setFocalLength(double focalLength) {
        this.focalLength = focalLength;
        return this;
    }

    public Camera setApertureSize(double apertureSize) {
        this.apertureSize = apertureSize;
        return this;
    }
    public void setDepthButton(boolean button, double apertureSize, double focalLength) {
        this.depthButton = button;
        this.apertureSize = apertureSize;
        this.focalLength = focalLength;
    }
    /***
     * on/off button for depth of field
     * @param depthButton on/off
     */
    public void setDepthButton(boolean depthButton) {
        this.depthButton = depthButton;
    }

    //endregion

    //region stage 4 bonus - Camera transformation

    /**
     * Rotate the vUp and vRight vectors by deg degrees
     *
     * @param deg Angle of rotation in degrees clockwise
     * @return Returns rotated camera.
     */
    public Camera rotateCamera(double deg) {
        if (deg == 0) return this; //there is nothing to turn
        this.vUp = this.vUp.rotateVector(this.vTo, deg);
        this.vRight = this.vRight.rotateVector(this.vTo, deg);
        return this;
    }

    /**
     * Moves camera to certain location and points to a single point
     *
     * @param location the camera's new location
     * @param to       the point the camera points to
     * @return Returns moved camera
     */
    public Camera moveCamera(Point location, Point to) {
        Vector vec;
        try {
            vec = to.subtract(location);
        } catch (IllegalArgumentException ignore) {
            throw new IllegalArgumentException("The camera cannot point at its starting location");
        }
        this.centerPoint = location;
        this.vTo = vec.normalize();
        //in order to determine Vup, we will find the intersection vector of two planes, the plane that Vto is represented
        //as its normal, and the plane that includes the Y axis and the Vto vector (as demanded in the instructions).

        //if the Vto is already on the Y axis, we will use the Z axis instead
        if (this.vTo.equals(Vector.Y) || this.vTo.equals(Vector.Y.scale(-1))) {
            this.vUp = (vTo.crossProduct(Vector.Z)).crossProduct(vTo).normalize();
        } else {
            this.vUp = (vTo.crossProduct(Vector.Y)).crossProduct(vTo).normalize();
        }
        this.vRight = vTo.crossProduct(vUp).normalize();
        return this;
    }

    /**
     * Flips the picture to left-right axis
     *
     * @return Returns object (self)
     */
    public Camera flipCamera() {
        this.vRight = this.vRight.scale(-1);
        return this;
    }
    //endregion

    //region rendering & writing images

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
                Color pixelColor = castRay(nX, nY, i, j);
                imageWriter.writePixel(j, i, pixelColor);
            }
        }
        return this;
    }

    private Color castRay(int nX, int nY, int i, int j) {
        if (ANTI_ALIASING_FACTOR == 1)
            return rayTracer.traceRay(constructRay(nX, nY, j, i));
        return rayTracer.traceRays(constructRays(nX, nY, j, i));
    }

    /**
     * Prints a grid to the image
     *
     * @param interval size of squares in the grid
     * @param color    color for the grid
     */
    public Camera printGrid(int interval, Color color) {
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
        return this;
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

    public Camera setMultithreading(int i) {
        return this;
    }

    public Camera setDebugPrint(double v) {
        return this;
    }
    //endregion
}
