package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.PixelManager.Pixel;

import java.util.*;

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
    private int antiAliasingFactor = 1;
    private int maxAdaptiveLevel = 3;
    private boolean useAdaptive = false;

    //@TODO delete
//    //depth of field
//    private boolean depthOfField = false;
//    private double focalDistance;
//    private double apertureSize;
//    private int aperturePointNum;
//    private Point[] aperturePoints;


    //multi-threading
    /**
     * Pixel manager for supporting:
     * multi-threading
     * debug print of progress percentage in Console window/tab
     */
    private PixelManager pixelManager;
    private double printInterval;
    private double threadsCount = 0;
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
     * Calculate a beam of rays
     * @param centerRay         the central ray to build beam around
     * @param vup               vector for target area plane
     * @param vright            vector for target area plane
     * @param targetAreaDis     distance of target area from centerRay's origin
     * @param targetAreaSide    length of target area side. target area full size = side*side
     * @param targetAreaRes     resolution of target area, number of rays on one side. total num of rays = res*res
     * @return list of rays
     */
    public List<Ray> constructRaysBeam(Ray centerRay, Vector vup, Vector vright, double targetAreaDis, double targetAreaSide, int targetAreaRes){

        //List<Point> points = Sampling.constructTargetAreaGrid(centerRay,vup,vright, targetAreaDis, targetAreaSide, targetAreaRes, k)


        List<Ray> rays = new ArrayList<>(targetAreaRes*targetAreaRes);
        Point origin = centerRay.getP0();
        Point centerTarget = centerRay.getPoint(targetAreaDis);
        double spacing = targetAreaSide / (targetAreaRes - 1);

        double scaleUp, scaleRight;
        Point destinationPoint;
        for (int i = 0; i < targetAreaRes; i++) {
            for (int j = 0; j < targetAreaRes; j++) {
                scaleUp = (-i + (targetAreaRes - 1d) / 2) * spacing;
                scaleRight = (j - (targetAreaRes - 1d) / 2) * spacing;
                destinationPoint = centerTarget;
                if (scaleUp != 0)
                    destinationPoint = destinationPoint.add(vup.scale(scaleUp));
                if (scaleRight != 0)
                    destinationPoint = destinationPoint.add(vright.scale(scaleRight));
                rays.add(new Ray(origin, destinationPoint.subtract(origin)));
            }
        }

        return rays;
    }

//@TODO: delete
//
//    /**
//     * Calculate a CONVERGENT beam of rays for DOF
//     *
//     * @param centerRay the central ray to build beam around
//     * @return list of rays
//     */
//    public List<Ray> constructDOFBeam(Ray centerRay) {
//        List<Ray> rays = new ArrayList<>(aperturePointNum * aperturePointNum);
//        Point focalPoint = centerRay.getPoint(focalDistance);
//        for (Point p : aperturePoints)
//            rays.add(new Ray(p, focalPoint.subtract(p)));
//        return rays;
//    }


    /**
     * Calculate color of pixel, using adaptive antialiasing
     *
     * @param centerRay      the central ray to build beam around
     * @param targetAreaSide length of target area side. target area full size = side*side
     * @param level          level of recursion
     * @return color of pixel
     */
    private Color adaptiveHelper(Ray centerRay, Vector vup, Vector vright, double targetAreaDis, double targetAreaSide, int level) {
        // list of colors of four corners
        List<Ray> rays = new ArrayList<>(
                constructRaysBeam(centerRay, vup, vright, targetAreaDis, targetAreaSide / 2, 2));
        List<Color> colors = rays.stream().map(ray -> rayTracer.traceRay(ray)).toList();

        //if finished recursion, return average of colors
        if (level == 1)
            return colors.get(0).add(colors.get(1), colors.get(2), colors.get(3))
                    .reduce(4);

        //If the four colors are similar to each other, return one
        if (colors.get(0).similar(colors.get(1))
                && colors.get(0).similar(colors.get(2))
                && colors.get(0).similar(colors.get(3)))
            return colors.get(0);

        //else, call recursion
        Point origin = centerRay.getP0();
        Point p = centerRay.getPoint(distance);
        Point[] points = {
                p.add(vup.scale(0.25 * targetAreaSide)).add(vright.scale(-0.25 * targetAreaSide)),
                p.add(vup.scale(0.25 * targetAreaSide)).add(vright.scale(0.25 * targetAreaSide)),
                p.add(vup.scale(-0.25 * targetAreaSide)).add(vright.scale(-0.25 * targetAreaSide)),
                p.add(vup.scale(-0.25 * targetAreaSide)).add(vright.scale(0.25 * targetAreaSide))};

        return adaptiveHelper(new Ray(origin, points[0].subtract(origin)), vup, vright, targetAreaDis, targetAreaSide / 2, level - 1).add(
                        adaptiveHelper(new Ray(origin, points[1].subtract(origin)), vup, vright, targetAreaDis, targetAreaSide / 2, level - 1),
                        adaptiveHelper(new Ray(origin, points[2].subtract(origin)), vup, vright, targetAreaDis, targetAreaSide / 2, level - 1),
                        adaptiveHelper(new Ray(origin, points[3].subtract(origin)), vup, vright, targetAreaDis, targetAreaSide / 2, level - 1))
                .reduce(4);
    }

//@TODO: delete
//
//    /**
//     * @param centerRay
//     * @param targetAreaSide
//     * @return
//     */
//    private Color depthOfFieldHelper(Ray centerRay, double targetAreaSide) {
//        Color color = Color.BLACK;
//        List<Ray> rays = constructDOFBeam(centerRay);
//
//        if (useAdaptive) { //DOF + adaptiveAA
//            for (Ray ray : rays)
//                color = color.add(adaptiveHelper(
//                        centerRay, vUp, centerRay.getDir().crossProduct(vUp), focalDistance, targetAreaSide, maxAdaptiveLevel));
//            color.reduce(aperturePointNum * aperturePointNum);
//        }
//
//        else if (antiAliasingFactor == 1) //DOF only
//            color = rayTracer.traceRays(rays);
//
//        else { //DOF + AA
//            Vector v1, v2;
//            for (Ray ray : rays) {
//                v1 = Vector.createOrthogonal(ray.getDir());
//                v2 = v1.crossProduct(ray.getDir());
//                color = color.add(rayTracer.traceRays(
//                        constructRaysBeam(ray, v1, v2, focalDistance, targetAreaSide, antiAliasingFactor)));
//            }
//            color.reduce(aperturePointNum * aperturePointNum);
//        }
//        return color;
//    }


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
    //endregion

    //region rendering & writing images

    /**
     * This function renders image's pixel color map from the scene
     * included in the ray tracer object
     *
     * @return the camera object itself
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
        pixelManager = new PixelManager(nY, nX, printInterval);

        if (threadsCount == 0) // no multi threading
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j)
                    castRay(nX, nY, j, i);
        else {
            var threads = new LinkedList<Thread>(); // list of threads
            while (threadsCount-- > 0) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null)
                        // cast ray through pixel (and color it – inside castRay)
                        castRay(nX, nY, pixel.col(), pixel.row());
                }));
            // start all the threads
            for (var thread : threads) thread.start();
            // wait until all the threads have finished
            try {
                for (var thread : threads) thread.join();
            } catch (InterruptedException ignore) {
            }
        }
        return this;
    }

    /**
     * Cast ray from camera and color a pixel
     *
     * @param nX resolution on X axis (number of pixels in row)
     * @param nY resolution on Y axis (number of pixels in column)
     * @param i  pixel's column number (pixel index in row)
     * @param j  pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int i, int j) {
        Color color;
        Ray ray = constructRay(nX, nY, i, j);
        //@TODO delete
//        if (depthOfField)
//            //depthOfFieldHelper know to combine DOF with other improvements
//            color = depthOfFieldHelper(ray, height / nY);
//        else
        if (useAdaptive)
            color = adaptiveHelper(ray, vUp, vRight, distance, height / nY, maxAdaptiveLevel);
        else if (antiAliasingFactor == 1)
            color = rayTracer.traceRay(ray);
        else
            color = rayTracer.traceRays(
                    constructRaysBeam(ray, vUp, vRight, distance, height / nY, antiAliasingFactor));

        imageWriter.writePixel(i, j, color);
        pixelManager.pixelDone();
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

        for (int i = 0; i < imageWriter.getNx(); i += interval) { // row
            for (int j = 0; j < imageWriter.getNy(); j += interval) { // column
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
     * function that sets the antiAliasingFactor
     *
     * @param antiAliasingFactor value to set
     * @return camera itself
     */
    public Camera setAntiAliasingFactor(int antiAliasingFactor) {
        this.antiAliasingFactor = antiAliasingFactor;
        return this;
    }

    /**
     * setter for UseAdaptive
     *
     * @param useAdaptive- the number of pixels in row/col of every pixel
     * @return camera itself
     */
    public Camera setUseAdaptive(boolean useAdaptive) {
        this.useAdaptive = useAdaptive;
        return this;
    }

    /**
     * setter for maxAdaptiveLevel
     *
     * @param maxAdaptiveLevel- The depth of the recursion
     * @return camera itself
     */
    public Camera setMaxAdaptiveLevel(int maxAdaptiveLevel) {
        this.maxAdaptiveLevel = maxAdaptiveLevel;
        return this;
    }

    //@TODO delete
//    public Camera setDepthOfField(double focalDistance, double apertureSize, int aperturePointNum) {
//        this.depthOfField = true;
//        this.focalDistance = focalDistance;
//        this.apertureSize = apertureSize;
//        this.aperturePointNum = aperturePointNum;
//        initAperturePoints();
//        return this;
//    }
//
//    public void initAperturePoints() {
//        aperturePoints = new Point[aperturePointNum * aperturePointNum];
//        double spacing = apertureSize / (aperturePointNum - 1);
//        int k = 0;
//        double scaleUp, scaleRight;
//        for (int i = 0; i < aperturePointNum; i++) {
//            for (int j = 0; j < aperturePointNum; j++) {
//                scaleUp = (-i + (aperturePointNum - 1d) / 2) * spacing;
//                scaleRight = (j - (aperturePointNum - 1d) / 2) * spacing;
//                Point point = centerPoint;
//                if (scaleUp != 0)
//                    point = point.add(vUp.scale(scaleUp));
//                if (scaleRight != 0)
//                    point = point.add(vRight.scale(scaleRight));
//                aperturePoints[k++] = point;
//            }
//        }
//    }


    /**
     * setter for whether you want to do multi threading
     * on image(x set and not zero) or not(x set to zero)
     *
     * @param threads number of threads
     * @return camera
     */
    public Camera setMultithreading(double threads) {
        if (threads < 0)
            throw new IllegalArgumentException("threads count must be non-negative");
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
