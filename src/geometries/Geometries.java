package geometries;

import primitives.Double3;
import primitives.Ray;
import scene.Scene;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class creates a list of geometries (Composite)
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Geometries extends Intersectable {

    private List<Intersectable> geometries;

    /**
     * default constructor
     */
    public Geometries() {
        this.geometries = new LinkedList<>();
    }

    /**
     * constructor
     *
     * @param geometries to init in list
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * add param geometries to list
     *
     * @param geometries to add to list
     */
    public void add(Intersectable... geometries) {
        if (geometries.length > 0)
            this.geometries.addAll(List.of(geometries));
    }

    public Geometries remove(Geometry givenGeometry) {
        Geometries list = new Geometries();
        for (var geometry : geometries) {
            if (!geometry.equals(givenGeometry))
                list.add(geometry);
        }
        return list;
    }
//    @Override
//    public List<Point> findIntersections(Ray ray) {
//        List<Point> intersections = null;
//        for (Intersectable geo : geometries) {
//            var temp = geo.findIntersections(ray);
//            if (temp != null) {
//                if (intersections == null)
//                    intersections = new LinkedList<>(temp);
//                else
//                    intersections.addAll(temp);
//            }
//        }
//        return intersections;
//    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersectionsWithAllShapes = null;

        //for each item in the list add the intersections to the list of intersections
        for (var item : geometries) {
            var itemList = item.findGeoIntersectionsHelper(ray);
            //out.print(itemList);
            //out.print("\n");
            //if there are(!) intersections with the specific item
            if (itemList != null) {
                //if the list of intersections with everyone is null, then create a new list
                if (intersectionsWithAllShapes == null) {
                    intersectionsWithAllShapes = new LinkedList<GeoPoint>();
                }
                //(if there are intersections with item) add list of intersections with item to list of all intersections
                intersectionsWithAllShapes.addAll(itemList);
            }
        }

        return intersectionsWithAllShapes;
    }
    @Override
    public int[][] calcBoundary() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        for (var geometry : geometries) {
            if (geometry.boundary[0][0] < minX)
                minX = geometry.boundary[0][0];
            if (geometry.boundary[0][1] > maxX)
                maxX = geometry.boundary[0][1];
            if (geometry.boundary[1][0] < minY)
                minY = geometry.boundary[1][0];
            if (geometry.boundary[1][1] > maxY)
                maxY = geometry.boundary[1][1];
            if (geometry.boundary[2][0] < minZ)
                minZ = geometry.boundary[2][0];
            if (geometry.boundary[2][1] > maxZ)
                maxZ = geometry.boundary[2][1];
        }
        return new int[][]{{(int) minX, (int) Math.ceil(maxX)},
                {(int) minY, (int) Math.ceil(maxY)},
                {(int) minZ, (int) Math.ceil(maxZ)}};
    }

    /**
     * move over all geometric entities of a scene and return a hashmap of all the none empty voxels
     *
     * @param scene the scene
     * @return the hash map of voxels
     */
    public HashMap<Double3, Geometries> attachVoxel(Scene scene) {
        HashMap<Double3, Geometries> voxels = new HashMap<>();
        List<Double3> voxelIndexes;
        for (var geometry : geometries) {
            voxelIndexes = geometry.findVoxels(scene);
            for (var index : voxelIndexes) {
                if (!voxels.containsKey(index))//the voxel already exists in the map
                    voxels.put(index, new Geometries(geometry));
                else {
                    voxels.get(index).add(geometry);
                }
            }
        }
        return voxels;
    }

    /**
     * boundary getter
     * @return the matrix of the boundary
     */
    public int[][] getBoundary(){
        return boundary;
    }
}
