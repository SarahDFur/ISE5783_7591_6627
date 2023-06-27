package renderer;

import geometries.Geometries;
import geometries.Intersectable;
import geometries.Polygon;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * (Extends) class for ray tracing - calculates color of pixels
 * use of Voxels
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class RayTracerRegular extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INIT_CALC_COLOR_K = Double3.ONE;

    private final double EPSILON = 0.001;
    /**
     * Constructor
     *
     * @param scene given scene
     */
    public RayTracerRegular(Scene scene) {
        super(scene);
        scene.calcVoxels();
    }

    //region traceRay - overrides
    @Override
    public Color traceRay(Ray ray) {
        Intersectable.GeoPoint closestIntersection = traversalAlgorithm(ray);
        return closestIntersection == null ? scene.background : calcColor(closestIntersection, ray);
    }

    @Override
    public Color traceRays(List<Ray> rays) {
        Color currentPixelColor = scene.background;
        for(Ray ray :rays)
            currentPixelColor = currentPixelColor.add(traceRay(ray));
        return  currentPixelColor.reduce(rays.size());

//        GeoPoint closestGeoPoint = findClosestIntersection(ray);
//        if (closestGeoPoint == null)
//            return scene.background;
//        Color currentPixelColor = calcColor(closestGeoPoint, ray);
//        return currentPixelColor;
    }
    //endregion

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
    private Color calcColor(Intersectable.GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray, k);
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
    private Color calcColor(Intersectable.GeoPoint geoPoint, Ray ray) {
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
     * @param k   k value
     * @return calculated color of point on Geometry in GeoPoint gp
     */
    private Color calcLocalEffects(Intersectable.GeoPoint gp, Ray ray, Double3 k) {
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
//                if (unshaded(gp, lightSource, lightVector, normal))
                Double3 ktr = transparency(gp, lightSource, lightVector, normal);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(
                            lightIntensity.scale(calcDiffusive(mat, nl)),
                            lightIntensity.scale(calcSpecular(mat, normal, lightVector, nl, vector))
                    );
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

//    /**
//     * check whether a point is unshaded
//     *
//     * @param gp          GeoPint to check
//     * @param lightSource light source
//     * @param l           vector light
//     * @param n           vector normal
//     * @return true if point gp is unshaded
//     */
//    private boolean unshaded(Intersectable.GeoPoint gp, LightSource lightSource, Vector l, Vector n) {
//        Ray lightRay = new Ray(gp.point, l.scale(-1), n);
//        List<Intersectable.GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
//
//        if (intersections == null) return true;
//
//        double distance = alignZero(lightSource.getDistance(gp.point));
//        for (Intersectable.GeoPoint intersection : intersections) {
//
//            if (intersection.geometry.getMaterial().kT != Double3.ZERO)
//                return true;
//
//            if (alignZero(intersection.point.distance(gp.point)) < distance)
//                return false;
//        }
//
//        return true;
//    }

    /**
     * return the transparency factor
     *
     * @param gp          GeoPint to check
     * @param lightSource light source
     * @param l           vector light
     * @param n           vector normal
     * @return Double3 that is the transparency factor
     */
    private Double3 transparency(Intersectable.GeoPoint gp, LightSource lightSource, Vector l, Vector n) {
        Ray lightRay = new Ray(gp.point, l.scale(-1), n);
        Double3 ktr = Double3.ONE;
        Geometries geometries = voxelsPathGeometries(lightRay);
        if (geometries == null) return ktr;
        List<Intersectable.GeoPoint> intersections = geometries.findGeoIntersections(lightRay);
        if (intersections == null) return ktr;

        double distance = alignZero(lightSource.getDistance(gp.point));
        for (Intersectable.GeoPoint intersection : intersections) {
            if (alignZero(intersection.point.distance(gp.point)) < distance)
                ktr = ktr.product(intersection.geometry.getMaterial().kT);
        }
        return ktr;
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
    private Color calcGlobalEffects(Intersectable.GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDir();
        Vector normal = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        Ray reflectedRay = constructReflectionRay(gp.point, normal, v);
        Ray refractedRay = constructRefractionRay(gp.point, normal, v);

        Color diffSamplingSum = Color.BLACK;
        Color glossSamplingSum = Color.BLACK;

        //If diffusive glass
        if (material.kDg != 0) {
            //super sample the refracted ray
            LinkedList<Ray> diffusedSampling = Sampling.superSample(refractedRay, material.kDg, normal);
            //for each sampling ray calculate the global effect
            for (var secondaryRay : diffusedSampling) {
                diffSamplingSum = diffSamplingSum.add(calcGlobalEffects(secondaryRay, level, k, material.kT));
            }
            //take the average of the calculation for all sample rays
            diffSamplingSum = diffSamplingSum.reduce(diffusedSampling.size());
        }
        //If glossy surface
        if (material.kSg != 0) {
            //super sample the reflected ray
            LinkedList<Ray> glossySampling = Sampling.superSample(reflectedRay, material.kSg, normal);
            //for each sampling ray calculate the global effect
            for (var secondaryRay : glossySampling) {
                glossSamplingSum = glossSamplingSum.add(calcGlobalEffects(secondaryRay, level, k, material.kR));
            }
            //take the average of the calculation for all sample rays
            glossSamplingSum = glossSamplingSum.reduce(glossySampling.size());
        }

        //If diffusive and glossy return both of the results above
        if (material.kDg != 0 && material.kSg != 0) {
            return glossSamplingSum
                    .add(diffSamplingSum);
        }
        //else return the matching result
        else if (material.kDg + material.kSg > 0) {
            return material.kDg != 0 ? calcGlobalEffects(reflectedRay, level, k, material.kR).add(diffSamplingSum) :
                    calcGlobalEffects(refractedRay, level, k, material.kT).add(glossSamplingSum);
        }

        return calcGlobalEffects(reflectedRay, level, k, material.kR)
                .add(calcGlobalEffects(refractedRay, level, k, material.kT));
    }

    /**
     * calculate the global effects of specific level on color if there are more intersections to check
     *
     * @param ray   the is used to intersect the geometries
     * @param level the current level
     * @param k    a color factor to reduce the color (according to the current level of recursion)
     * @param kx   the color factor for the next level of recursion
     * @return the new calculated color
     */
    private Color calcGlobalEffects(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        Intersectable.GeoPoint gp = traversalAlgorithm(ray);
        if (gp == null)
            return scene.background.scale(kx);

        return isZero(gp.geometry.getNormal(gp.point).dotProduct(ray.getDir())) ?
                Color.BLACK : calcColor(gp, ray, level - 1, kkx).scale(kx);
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
     * finds the closest intersection GeoPoint to the base of the ray
     *
     * @param ray the ray that we find intersection from
     * @return the closest intersection GeoPoint
     */
    private Intersectable.GeoPoint findClosestIntersection(Ray ray, Geometries geometries) {
        return ray.findClosestGeoPoint(geometries.findGeoIntersections(ray));
    }

    //region voxel algorithms
    /**
     * Implements the 3dda algorithm. It determines through which voxels the ray goes.
     *
     * @param ray the ray through the scene voxels grid
     * @return the first intersection GeoPoint
     */
    private Intersectable.GeoPoint traversalAlgorithm(Ray ray) {
        //first sub algo
        //finds the first intersection with the grid
        Point firstIntersection = firstIntersection(ray);
        if (firstIntersection == null) return null;
        Vector dir = ray.getDir();
        int[][] boundary = scene.geometries.boundary;
        //move the point a little, so it would be inside the grid
        Point fixedFirstIntersection = fixPoint(firstIntersection, boundary);
        //arrays for calculations
        int[] indexes = VoxelByPoint(fixedFirstIntersection, boundary);
        double[] directions = new double[]{dir.getX(), dir.getY(), dir.getZ()};
        int[] steps = new int[3];
        double[] voxelEdges = new double[]{scene.getXEdgeVoxel(), scene.getYEdgeVoxel(), scene.getZEdgeVoxel()};
        double[] tMax = new double[3];
        double[] firstIntersectionCoordinates = new double[]{firstIntersection.getX(), firstIntersection.getY(), firstIntersection.getZ()};
        double[] tDelta = new double[3];

        for (int i = 0; i <= 2; i++) {
            steps[i] = determineDirection(directions[i]);
        }

        for (int i = 0; i <= 2; i++) {
            tMax[i] = determineTmax(boundary[i][0], steps[i], indexes[i], voxelEdges[i], directions[i], firstIntersectionCoordinates[i]);
        }

        for (int i = 0; i <= 2; i++) {
            tDelta[i] = Math.abs(voxelEdges[i] / directions[i]);
        }

        //second sub algo
        //move over all the geometries of the first voxel and find the closest intersection (if there is any)
        Intersectable.GeoPoint farIntersection = null;
        Geometries list = scene.voxels.get(new Double3(indexes[0], indexes[1], indexes[2]));
        Intersectable.GeoPoint closestIntersection;
        if (list != null) {
            closestIntersection = findClosestIntersection(ray, list);
            //check if the intersection point exists, and it's inside the voxel
            if (closestIntersection != null)
                if (!isInsideVoxel(indexes, closestIntersection.point, boundary))
                    farIntersection = closestIntersection;
                else return closestIntersection;
        }

        do {
            if (!nextVoxel(tMax, indexes, tDelta, steps)) {
                return null;
            }
            //move the point a voxel
            //find the intersection inside teh current voxel
            list = scene.voxels.get(new Double3(indexes[0], indexes[1], indexes[2]));
            if (list == null) {
                closestIntersection = null;
            } else {
                if (farIntersection != null && isInsideVoxel(indexes, farIntersection.point, boundary)) {//if it's the voxel with the saved point
                    list = list.remove(farIntersection.geometry);
                    closestIntersection = findClosestIntersection(ray, list);
                    if (closestIntersection != null) {
                        if (isInsideVoxel(indexes, closestIntersection.point, boundary)) {
                            //checks the closest of both
                            if (closestIntersection.point.distanceSquared(fixedFirstIntersection) <= farIntersection.point.distanceSquared(fixedFirstIntersection)) {
                                //even though we reached the voxel that contains the precalculated point, we found closer one
                                return closestIntersection;
                            } else {
                                //the saved point is still the closest
                                return farIntersection;
                            }
                        } else {//the closest intersection is outside the voxel meaning the saved one is closer
                            return farIntersection;
                        }
                    } else {//no other intersection in this voxel, therefore the saved intersection is the closest
                        return farIntersection;
                    }
                } else {//if it's not the voxel with the saved point
                    closestIntersection = findClosestIntersection(ray, list);
                    if (closestIntersection != null) {
                        if (!isInsideVoxel(indexes, closestIntersection.point, boundary)) {
                            //if not in the voxel
                            if (farIntersection == null || closestIntersection.point.distanceSquared(fixedFirstIntersection) <= farIntersection.point.distanceSquared(fixedFirstIntersection)) {
                                //if it's closer than the already saved farIntersection, save this, since tha saved one would not intersect before this
                                farIntersection = closestIntersection;
                                closestIntersection = null;
                            }
                        } else {//if it's in the voxel and closest
                            return closestIntersection;
                        }
                    }
                }
            }

        } while (closestIntersection == null || !isInsideVoxel(indexes, closestIntersection.point, boundary));

        return closestIntersection;
    }

    /**
     * function that finds the geometric objects in all the voxels the ray travels through
     *
     * @param ray the ray through the scene voxels grid
     * @return geometries in all the voxels the ray travels through
     */
    private Geometries voxelsPathGeometries(Ray ray) {
        //finds the first intersection with the grid
        Point firstIntersection = firstIntersection(ray);
        if (firstIntersection == null) return null;
        Vector dir = ray.getDir();
        int[][] boundary = scene.geometries.boundary;
        //move the point a little, so it would be inside the grid
        Point fixedFirstIntersection = fixPoint(firstIntersection, boundary);

        //arrays for calculations
        int[] indexes = VoxelByPoint(fixedFirstIntersection, boundary);
        double[] directions = new double[]{dir.getX(), dir.getY(), dir.getZ()};
        int[] steps = new int[3];
        double[] voxelEdges = new double[]{scene.getXEdgeVoxel(), scene.getYEdgeVoxel(), scene.getZEdgeVoxel()};
        double[] tMax = new double[3];
        double[] firstIntersectionCoordinates = new double[]{firstIntersection.getX(), firstIntersection.getY(), firstIntersection.getZ()};
        double[] tDelta = new double[3];

        for (int i = 0; i <= 2; i++) {
            steps[i] = determineDirection(directions[i]);
        }

        for (int i = 0; i <= 2; i++) {
            tMax[i] = determineTmax(boundary[i][0], steps[i], indexes[i], voxelEdges[i], directions[i], firstIntersectionCoordinates[i]);
        }

        for (int i = 0; i <= 2; i++) {
            tDelta[i] = Math.abs(voxelEdges[i] / directions[i]);
        }

        //find the geometries in the first voxel
        Geometries list = new Geometries();
        Geometries temp = scene.voxels.get(new Double3(indexes[0], indexes[1], indexes[2]));


        if (temp != null) {
            list.add(temp);
        }
        //travel through all the voxels in a loop and their geometries
        while (nextVoxel(tMax, indexes, tDelta, steps)) {
            temp = scene.voxels.get(new Double3(indexes[0], indexes[1], indexes[2]));
            if (temp != null) {
                list.add(temp);
            }
        }
        return list;
    }


    /**
     * moves to the next voxel
     *
     * @param tMax    maximum in units of t to get to the next voxel
     * @param indexes index of the current voxel
     * @param tDelta  width, height and depth of voxel in units of t
     * @param steps   the direction of the steps
     * @return if moved successfully to the next voxel or got out of the grid
     */
    //if there is no intersection points in the first voxel search in the rest of the ray's way
    //since the ray starts in the middle of a voxel (since we moved it on the intersection with the  scene CBR,
    //or the head is already inside the scene SCR), we had to calculate the remaining distance to the fist voxel's edge.
    //But from now on, we can use the constant voxel size, since it would always intersect with the edge of the voxel.
    //now would do the same calculation on the rest of the ray's way in the voxels grid.
    private boolean nextVoxel(double[] tMax, int[] indexes, double[] tDelta, int[] steps) {
        if (tMax[0] < tMax[1]) {
            if (tMax[0] < tMax[2]) {
                indexes[0] = indexes[0] + steps[0];
                if ((indexes[0] > 0 && indexes[0] == scene.resolution + 1) || (indexes[0] < 0))
                    return false; //the ray leaves the scene's CBR with no intersection
                tMax[0] = tMax[0] + tDelta[0];
            }
            else {
                indexes[2] = indexes[2] + steps[2];
                if ((indexes[2] > 0 && indexes[2] == scene.resolution + 1) || (indexes[2] < 0))
                    return false;
                tMax[2] = tMax[2] + tDelta[2];
            }
        }
        else {
            if (tMax[1] < tMax[2]) {
                indexes[1] = indexes[1] + steps[1];
                if ((indexes[1] > 0 && indexes[1] == scene.resolution + 1) || (indexes[1] < 0))
                    return false;
                tMax[1] = tMax[1] + tDelta[1];
            }
            else {
                indexes[2] = indexes[2] + steps[2];
                if ((indexes[2] > 0 && indexes[2] == scene.resolution + 1) || (indexes[2] < 0))
                    return false;
                tMax[2] = tMax[2] + tDelta[2];
            }
        }
        return true;
    }


    /**
     * the intersection of the ray with CBR of the scene
     *
     * @param ray the entering ray
     * @return the intersection point
     */
    private Point firstIntersection(Ray ray) {
        double x = ray.getP0().getX();
        double y = ray.getP0().getY();
        double z = ray.getP0().getZ();
        Point head = ray.getP0();

        int[][] boundary = scene.geometries.boundary;

        //if the head of the ray is inside the regular grid return the head of the ray
        if (x >= boundary[0][0] && x <= boundary[0][1] &&
                y >= boundary[1][0] && y <= boundary[1][1] &&
                z >= boundary[2][0] && z <= boundary[2][1]) {
            return head;
        }

        List<Point> intersections;
        Point closest = null;

        //find the closest intersection
        double distance = Double.POSITIVE_INFINITY;
        for (Polygon p : scene.faces) {
            intersections = p.findIntersections(ray);
            if (intersections != null) {
                if (intersections.get(0).distance(head) < distance) {
                    distance = intersections.get(0).distance(head);
                    closest = intersections.get(0);
                }
            }
        }

        return closest;

    }

    /**
     * this function matches a voxel to a point.
     *
     * @param p        the point in the voxel
     * @param boundary the scene CBR
     * @return the voxel of the specified point
     */
    private int[] VoxelByPoint(Point p, int[][] boundary) {

        int xCoordinate = (int) ((p.getX() - boundary[0][0]) / scene.getXEdgeVoxel());
        int yCoordinate = (int) ((p.getY() - boundary[1][0]) / scene.getYEdgeVoxel());
        int zCoordinate = (int) ((p.getZ() - boundary[2][0]) / scene.getZEdgeVoxel());

        return new int[]{xCoordinate, yCoordinate, zCoordinate};
    }

    /**
     * decides sign of a number
     *
     * @param component direction of the ray inside teh voxel
     * @return positivity or negativity of the direction
     */
    private int determineDirection(double component) {
        if (component > 0)
            return 1;
        else if (component < 0)
            return -1;
        else
            return 0;
    }

    /**
     * Determines the maximum step (units) of t to the next voxel
     *
     * @param minBoundary            minimum boundary coordinate
     * @param step                   the direction of the next voxel
     * @param index                  index of the current voxel
     * @param voxelEdge              length of voxel edge
     * @param direction              the direction of the vector of the ray
     * @param intersectionCoordinate the first intersection of the ray with the regular grid
     * @return the maximum step in units of t to the next voxel
     */
    private double determineTmax(double minBoundary, double step, int index, double voxelEdge, double direction, double intersectionCoordinate) {
        if (step == 1) {
            return Math.abs((minBoundary + (index + 1) * voxelEdge - intersectionCoordinate) / direction);
        } else if (step == -1) {
            return Math.abs((intersectionCoordinate - (minBoundary + index * voxelEdge)) / direction);
        }
        return 0;
    }

    /**
     * checks if the intersection point ith the geometry is inside the voxel
     *
     * @param index        voxel index
     * @param intersection intersection point of the ray with the geometry
     * @param boundary     boundary of the scene
     * @return if the intersection point is inside the voxel
     */
    private boolean isInsideVoxel(int[] index, Point intersection, int[][] boundary) {
        //minimum coordinates
        double xMax = boundary[0][0] + (index[0] + 1) * scene.getXEdgeVoxel();
        double yMax = boundary[1][0] + (index[1] + 1) * scene.getYEdgeVoxel();
        double zMax = boundary[2][0] + (index[2] + 1) * scene.getZEdgeVoxel();

        //maximum coordinates
        double xMin = boundary[0][0] + (index[0]) * scene.getXEdgeVoxel();
        double yMin = boundary[1][0] + (index[1]) * scene.getYEdgeVoxel();
        double zMin = boundary[2][0] + (index[2]) * scene.getZEdgeVoxel();

        return intersection.getX() >= xMin && intersection.getX() <= xMax
                && intersection.getY() >= yMin && intersection.getY() <= yMax
                && intersection.getZ() >= zMin && intersection.getZ() <= zMax;
    }

    /**
     * fixes the intersection of the ray with the scene CBR on exact intersection with voxel edges
     *
     * @param p        intersection point with the scene CBR
     * @param boundary the CBR
     * @return the fixed point
     */
    private Point fixPoint(Point p, int[][] boundary) {
        if (isZero((p.getX() - boundary[0][0]))) {
            p = p.add(new Vector(1, 0, 0).scale(EPSILON));
        }
        if (isZero((p.getX() - boundary[0][1]))) {
            p = p.add(new Vector(1, 0, 0).scale(-EPSILON));
        }
        if (isZero((p.getY() - boundary[1][0]))) {
            p = p.add(new Vector(0, 1, 0).scale(EPSILON));
        }
        if (isZero((p.getY() - boundary[1][1]))) {
            p = p.add(new Vector(0, 1, 0).scale(-EPSILON));
        }
        if (isZero((p.getZ() - boundary[2][0]))) {
            p = p.add(new Vector(0, 0, 1).scale(EPSILON));
        }
        if (isZero((p.getZ() - boundary[2][1]))) {
            p = p.add(new Vector(0, 0, 1).scale(-EPSILON));
        }
        return p;
    }
    //endregion
}
