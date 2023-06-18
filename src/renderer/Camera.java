package renderer;

import geometries.Plane;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
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

    //region fields
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
    //private static int ANTI_ALIASING_FACTOR = 1;
    public enum SUPER_SAMPLING_TYPE {NONE, REGULAR, ADAPTIVE}
    private SUPER_SAMPLING_TYPE superSamplingType = SUPER_SAMPLING_TYPE.ADAPTIVE; // type of the super-sampling (eg. NONE,RANDOM, GRID)
    private int superSamplingGridSize = 9; // grid size for regular super-sampling (e.g. 9 for 9x9 grid)
    private int adaptiveSuperSamplingMaxRecursionDepth = 3; // constant max depth for adaptive super-sampling


    //Focus
//    private boolean depthOfFieldFlag = false;
//    private double focalDistance = 2;
//    private double apertureSize = 0.01;
//    private static int NUM_OF_APERTURE_POINTS = 2;
//ON/OFF button default is off
//    private boolean depthButton = false;
//    //focal length
//    private double focalLength = 2;
//    private double apertureSize = 0.01;
//    private static final int NUMBER_OF_APERTURE_POINTS = 10;
    //Aperture properties
    private int APERTURE_NUMBER_OF_POINTS = 9; //(e.g. 9 for 9x9 grid)
    private double apertureSize = 0;
    private Point[] aperturePoints;
    private double focalDistance = 2;
    private Plane FOCAL_PLANE;


    //multi-threading
    private boolean multiThreading = false;
    private double printInterval;
    private double threadsCount = 3;
    //----------

    //endregion

    //region constructor
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

    //endregion

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
        if (!isZero(xj)) Pij = Pij.add(vRight.scale(xj));
        if (!isZero(yi)) Pij = Pij.add(vUp.scale(yi));
        return Pij;
    }

//    public List<Ray> constructRays(int nX, int nY, int j, int i) {
//        List<Ray> rays = new ArrayList<>(ANTI_ALIASING_FACTOR * ANTI_ALIASING_FACTOR);
//        Point pixelCenter = getPixelCenter(nX, nY, j, i);
//        double rY = (height / nY) / ANTI_ALIASING_FACTOR;
//        double rX = (width / nX) / ANTI_ALIASING_FACTOR;
//        double x, y;
//
//        for (int rowNumber = 0; rowNumber < ANTI_ALIASING_FACTOR; rowNumber++) {
//            for (int colNumber = 0; colNumber < ANTI_ALIASING_FACTOR; colNumber++) {
//                y = -(rowNumber - (ANTI_ALIASING_FACTOR - 1d) / 2) * rY;
//                x = (colNumber - (ANTI_ALIASING_FACTOR - 1d) / 2) * rX;
//                Point pIJ = pixelCenter;
//                if (!isZero(y)) pIJ = pIJ.add(vUp.scale(y));
//                if (!isZero(x)) pIJ = pIJ.add(vRight.scale(x));
//                rays.add(new Ray(centerPoint, pIJ.subtract(centerPoint)));
//            }
//        }
//        return rays;
//    }


//    /**
//     * Constructs a ray through a pixel from the camera
//     *
//     * @param nX The number of pixels in the x direction
//     * @param nY The number of pixels in the y direction
//     * @param j  The pixel's x coordinate
//     * @param i  The pixel's y coordinate
//     *
//     * @return The ray through the pixel
//     */
//    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
//        Point pc = centerPoint.add(vTo.scale(distance)); // center point of the view plane
//        double pixelWidth = width / nX; // width of a pixel
//        double pixelHeight = height / nY; // height of a pixel
//        double pcX = (nX - 1) / 2.0; // center pixel value in x direction
//        double pcY = (nY - 1) / 2.0; // center pixel value in y direction
//        double rightDistance = (j - pcX) * pixelWidth; // x offset of j from the center pixel
//        double upDistance = -1 * (i - pcY) * pixelHeight; // y offset of i from the center pixel
//
//        Point pij = pc; // start at the center of the view plane
//
//        // we need to check if the distance is zero, because we can't scale a vector by
//        // zero
//        if (!isZero(rightDistance)) {
//            // if the distance to move right is not zero, move right
//            pij = pij.add(vRight.scale(rightDistance));
//        }
//
//        if (!isZero(upDistance)) {
//            // if the distance to move up is not zero, move up
//            pij = pij.add(vUp.scale(upDistance));
//        }
//
//        // construct a ray from the camera origin in the direction of the pixel at (j,i)
//        return new Ray(centerPoint, pij.subtract(centerPoint));
//    }
//
//
    /**
     * Construct a grid of rays from a center point, a width and height, and a grid
     * size
     *
     * @param topLeftRayPoint      The top left point of the grid
     * @param raySpacingHorizontal The horizontal spacing between ray points
     * @param raySpacingVertical   The vertical spacing between ray points
     * @param gridSize             the size of the grid
     */
    private List<Ray> constructGridOfRays(Point topLeftRayPoint, double raySpacingHorizontal, double raySpacingVertical,
                                          int gridSize) {
        Ray ray;
        List<Ray> rays = new ArrayList<>(gridSize*gridSize);
        // create the grid of rays
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Point rayPoint = topLeftRayPoint;
                // move the ray down
                if (row > 0) {
                    rayPoint = rayPoint.add(vUp.scale(-row * raySpacingVertical));
                }
                // move the ray right
                if (col > 0) {
                    rayPoint = rayPoint.add(vRight.scale(col * raySpacingHorizontal));
                }
                // create the ray
                ray = new Ray(centerPoint, rayPoint.subtract(centerPoint));
                // add the ray to the list
                rays.add(ray);
            }
        }
        return rays;
    }

    /**
     * Calculates the color of a pixel using super-sampling
     *
     * @param mainRay     the main ray to trace around
     * @param pixelWidth  the width of the pixel
     * @param pixelHeight the height of the pixel
     * @return color of the pixel
     */
    private Color calcSupersamplingColor(Ray mainRay, double pixelWidth, double pixelHeight) {
        // locate the point of the top left ray to start constructing the grid from
        Point centerOfPixel = mainRay.getPoint(distance);
        // amount to move to get from one super-sampling ray location to the next
        double raySpacingVertical = pixelHeight / (superSamplingGridSize + 1);
        double raySpacingHorizontal = pixelWidth / (superSamplingGridSize + 1);
        Point topLeftRayPoint = centerOfPixel //
                .add(vRight.scale(-pixelWidth / 2 - raySpacingHorizontal)) //
                .add(vUp.scale(pixelHeight / 2 - raySpacingVertical));
        List<Ray> rays = constructGridOfRays(topLeftRayPoint, raySpacingHorizontal, raySpacingVertical,
                superSamplingGridSize);
        Color result = Color.BLACK;
        for (Ray ray : rays) {
            result = result.add(rayTracer.traceRay(ray));
        }
        // divide the color by the number of rays to get the average color
        double numRays = (double) superSamplingGridSize * superSamplingGridSize;
        return result.reduce(numRays);
    }

    /**
     * Calculates the color of a pixel using adaptive super-sampling
     *
     * @param centerRay   the ray to trace around
     * @param pixelWidth  the width of the pixel
     * @param pixelHeight the height of the pixel
     * @param level       the level of the adaptive super-sampling (if level is 0,
     *                    return)
     * @return color of the pixel
     */
    private Color calcAdaptiveSupersamplingColor(Ray centerRay, double pixelWidth, double pixelHeight, int level) {
        // spacing between rays vertically
        double halfPixelHeight = pixelHeight / 2;
        // spacing between rays horizontally
        double halfPixelWidth = pixelWidth / 2;
        // move 1/4 left and 1/4 up from the center ray
        Point topLeftRayPoint = centerRay.getPoint(distance) //
                .add(vRight.scale(-(halfPixelWidth / 2))) //
                .add(vUp.scale(halfPixelHeight / 2));
        // gridSize is always 2 since the grid is always a 2x2 grid of rays in
        // adaptive super-sampling
        List<Ray> rays = constructGridOfRays(topLeftRayPoint, halfPixelWidth, halfPixelHeight, 2);

        // get the colors of the rays
        List<Color> colors = rays.stream().map(ray -> rayTracer.traceRay(ray)).toList();

        // if recursion level is 1, return the average color of the rays
        if (level == 1) {
            return colors.get(0).add(colors.get(1), colors.get(2), colors.get(3)).reduce(4);
        }

        // check if the colors are all the similar enough to be considered the same
        if (colors.get(0).similar(colors.get(1)) //
                && colors.get(0).similar(colors.get(2)) //
                && colors.get(0).similar(colors.get(3))) {
            // if they are, return any one of them
            return colors.get(0);
        }

        // otherwise average the colors of the four parts of the pixel
        return calcAdaptiveSupersamplingColor(rays.get(0), halfPixelWidth, halfPixelHeight, level - 1) //
                .add(calcAdaptiveSupersamplingColor(rays.get(1), halfPixelWidth, halfPixelHeight, level - 1), //
                        calcAdaptiveSupersamplingColor(rays.get(2), halfPixelWidth, halfPixelHeight, level - 1), //
                        calcAdaptiveSupersamplingColor(rays.get(3), halfPixelWidth, halfPixelHeight, level - 1)) //
                .reduce(4);
    }

    /**
     * the function that goes through every point in the array and calculate the average color.
     *
     * @param ray the original ray to construct the surrounding beam.
     * @return the average color of the beam.
     */
    private Color averagedBeamColor(Ray ray) {
        // Initializing the averageColor to black and the apertureColor to null.
        Color averageColor = Color.BLACK, apertureColor;
        // The number of points in the aperture.
        int numOfPoints = this.aperturePoints.length;
        // A ray that is used to trace the ray from the aperture point to the focal point.
        Ray apertureRay;
        // Finding the intersection point of the ray and the focal plane.
        Point focalPoint = this.FOCAL_PLANE.findGeoIntersections(ray).get(0).point;

        // A for each loop that goes through every point in the array and calculate the average color.
        for (Point aperturePoint : this.aperturePoints) {
            apertureRay = new Ray(aperturePoint, focalPoint.subtract(aperturePoint));
            apertureColor = rayTracer.traceRay(apertureRay);
            // Adding the color of the ray to the average color.
            averageColor = averageColor.add(apertureColor);
        }

        return averageColor.reduce(numOfPoints);
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
        if (multiThreading){
            Pixel.initialize(nY, nX, printInterval);
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone()) {
                        castRay(nX, nY, pixel.col, pixel.row);
                    }
                }).start();
            }
            Pixel.waitToFinish();
        }
        else {
            Pixel.initialize(nY, nX, printInterval);
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j) {
                    castRay(nX, nY, j, i);
                    Pixel.pixelDone();
                    Pixel.printPixel();
                }
        }
        return this;
    }

//    /**
//     * Constructs a ray through a pixel from the camera and write its color to the
//     * image
//     *
//     * @param nX The number of pixels in the x direction
//     * @param nY The number of pixels in the y direction
//     * @param xIndex The pixel's x coordinate
//     * @param yIndex The pixel's y coordinate
//     */
//    private void castRay(int nX, int nY, int xIndex, int yIndex) {
//        Color color;
//        if (ANTI_ALIASING_FACTOR == 1)
//            color = rayTracer.traceRay(constructRay(nX, nY, xIndex, yIndex));
//        color = rayTracer.traceRays(constructRays(nX, nY, xIndex, yIndex));
//        imageWriter.writePixel(xIndex, yIndex, color);
//    }


    /**
     * Constructs a ray through a pixel from the camera and write its color to the
     * image
     *
     * @param numColumns The number of pixels in the x direction
     * @param numRows    The number of pixels in the y direction
     * @param col        The pixel's x coordinate
     * @param row        The pixel's y coordinate
     */
    public void castRay(int numColumns, int numRows, int col, int row) {
        Color color;
        // height and width of the pixel
        double pixelWidth = width / numColumns;
        double pixelHeight = height / numRows;
        Ray ray = constructRay(numColumns, numRows, col, row);

        if (superSamplingType == SUPER_SAMPLING_TYPE.ADAPTIVE) {
            color = calcAdaptiveSupersamplingColor(ray, pixelWidth, pixelHeight,
                    adaptiveSuperSamplingMaxRecursionDepth);
        } else if (superSamplingType == SUPER_SAMPLING_TYPE.REGULAR) {
            color = calcSupersamplingColor(ray, pixelWidth, pixelHeight);
        } else {
            color = rayTracer.traceRay(ray);
        }

        if (!isZero(this.apertureSize)) {
            color = color.add(averagedBeamColor(ray));
            color = color.reduce(2);
        }

        imageWriter.writePixel(col, row, color);
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

        for (int i = 0; i < imageWriter.getNx(); i+=interval) { // row
            for (int j = 0; j < imageWriter.getNy(); j+=interval) { // column
                //grid: 800/50 = 16, 500/50 = 10
                imageWriter.writePixel(i, j, color);
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

//    /**
//     * set anti-aliasing factor.
//     * final number of rays will be (factor * factor)
//     *
//     * @param antiAliasingFactor num of rays for anti aliasing
//     * @return this Camera
//     */
//    public Camera setAntiAliasingFactor(int antiAliasingFactor) {
//        if (antiAliasingFactor <= 0)
//            throw new IllegalArgumentException("anti aliasing factor must be positive integer");
//        ANTI_ALIASING_FACTOR = antiAliasingFactor;
//        return this;
//    }

    /**
     * Set the super-sampling to NONE, REGULAR or ADAPTIVE
     *
     * @param type SUPER_SAMPLING_TYPE type of the super-sampling (NONE, REGULAR, ADAPTIVE)
     * @return the current camera
     */
    public Camera setSuperSampling(SUPER_SAMPLING_TYPE type) {
        this.superSamplingType = type;
        return this;
    }

    /**
     * Set the grid size of the super-sampling
     *
     * @param gridSize grid size of the super-sampling (e.g. 9 for 9x9 grid)
     * @return the current camera
     */
    public Camera setSuperSamplingGridSize(int gridSize) {
        this.superSamplingGridSize = gridSize;
        return this;
    }

    /**
     * Set the max recursion depth for the adaptive super-sampling
     *
     * @param maxRecursionDepth max recursion depth for the adaptive super-sampling
     * @return the current camera
     */
    public Camera setAdaptiveSuperSamplingMaxRecursionDepth(int maxRecursionDepth) {
        this.adaptiveSuperSamplingMaxRecursionDepth = maxRecursionDepth;
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
//    public Camera setFocalLength(double focalLength) {
//        this.focalLength = focalLength;
//        return this;
//    }
//
//    public Camera setApertureSize(double apertureSize) {
//        this.apertureSize = apertureSize;
//        return this;
//    }
//    public void setDepthButton(boolean button, double apertureSize, double focalLength) {
//        this.depthButton = button;
//        this.apertureSize = apertureSize;
//        this.focalLength = focalLength;
//    }
//    /***
//     * on/off button for depth of field
//     * @param depthButton on/off
//     */
//    public void setDepthButton(boolean depthButton) {
//        this.depthButton = depthButton;
//    }

    public Camera setFocalDistance(double focalDistance) {
        this.focalDistance = focalDistance;
        this.FOCAL_PLANE = new Plane(this.centerPoint.add(this.vTo.scale(this.focalDistance)), this.vTo);
        return this;
    }

    /**
     * setting the aperture size as the given parameter, and initialize the points array.
     *
     * @param size the given parameter.
     * @return the camera itself for farther initialization.
     */
    public Camera setApertureSize(double size) {
        this.apertureSize = size;

        /////initializing the points of the aperture.
        if (size != 0) initializeAperturePoint();

        return this;
    }

    /**
     * the function that initialize the aperture size and the points that it represents.
     */
    private void initializeAperturePoint() {
        //the number of points in a row
        int pointsInRow = (int) Math.sqrt(this.APERTURE_NUMBER_OF_POINTS);

        //the array of point saved as an array
        this.aperturePoints = new Point[pointsInRow * pointsInRow];

        //calculating the initial values.
        double pointsDistance = (this.apertureSize * 2) / pointsInRow;

        //calculate the initial point to be the point with coordinates outside the aperture in the down left point,
        // so we won`t have to deal with illegal vectors.
        Point initialPoint = this.centerPoint
                .add(this.vUp.scale(-this.apertureSize - pointsDistance / 2)
                .add(this.vRight.scale(-this.apertureSize - pointsDistance / 2)));

        //initializing the points array
        for (int i = 1; i <= pointsInRow; i++) {
            for (int j = 1; j <= pointsInRow; j++) {
                this.aperturePoints[(i - 1) + (j - 1) * pointsInRow] = initialPoint
                        .add(this.vUp.scale(i * pointsDistance).add(this.vRight.scale(j * pointsDistance)));
            }
        }
    }


    /**
     * setter for whether you want to do multi threading
     * on image(x set and not zero) or not(x set to zero)
     *
     * @param threads number of threads
     * @return camera
     */
    public Camera setMultithreading(double threads) {
        multiThreading = threads != 0;
        threadsCount = threads;
        return this;
    }

    /**
     * setter for printInterval
     *
     * @param interval The interval between prints in seconds
     * @return camera
     */
    public Camera setDebugPrint(double interval) {
        printInterval = interval;
        return this;
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

}
