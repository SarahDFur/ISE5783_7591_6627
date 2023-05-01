package renderer;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/***
 * test method for constructRay function
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class IntegrationTests {
    //non-specific function that checks that every call on the same view point for DRY rule
    private void assertCountIntersections(int expected, Camera cam, Intersectable geo) {
        cam.setViewPlaneSize(3,3);
        //distance set to one for ease of calculation
        cam.setViewPlaneDistance(1);
        int countIntersections = 0;
        final int width = 3;//nX
        final int height = 3;//nY
        //check on every row and in each row for every different pixel according to column
        for(int i=0;i< height;i++){
            for(int j=0;j< width;j++){
                //create ray going from center of camera through center of pixel
                Ray camToVPRay=cam.constructRay(width,height,j,i);
                //create list of points of intersections the ray has with all objects in geo
                List<Point>  intersectionsList=geo.findIntersections(camToVPRay);
                if(intersectionsList!=null) {
                    //if the list is not empty add number of points found to sum of points
                    countIntersections += intersectionsList.size();
                }
            }
        }
        //we assume the returned points are the right points, relying on previous checks
        //if sum is not the expected the test failed
        assertEquals(expected, countIntersections, "Wrong number of intersections");
    }

    /**
     * Integration tests of Camera Construct Ray with Plane intersections (with Ray)
     */
    @Test
    //There is a plane
    public void cameraRayPlaneIntegration() {
        //create camera and zero point
        final Point ZERO_POINT = new Point(0, 0, 0);
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1),
                new Vector(0, -1, 0)).setViewPlaneDistance(1);

        //TC00:view plane is parallel to plane right under camera
        Plane plane1 = new Plane(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertCountIntersections(9, camera, plane1);

        //TC01::no intersections
        Plane plane2=new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertCountIntersections(0, camera, plane2);

        //TC02: plane cuts through view plane, there are 6 pixels that see plane
        assertCountIntersections(6, camera, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)));

        //TC03: another one like t2
        assertCountIntersections(3, camera, new Plane(new Point(0, 0.5, -1),new Point(-1,0.5 , -1),new Point(1, 1, -100)));

        //TC04: small angle but all pixels show plane
        Plane plane3 = new Plane(new Point(1, 0, -2), new Point(0, 0, -2), new Point(0, 2, -1));
        assertCountIntersections(9,camera, plane3);
    }
    /**
     * Integration tests of Camera Construct Ray with Triangle intersections (with Ray)
     */
    //There is a triangle
    public void cameraRayTriangleIntegration(){
        //create camera and zero point
        final Point ZERO_POINT = new Point(0, 0, 0);
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1),
                new Vector(0, -1, 0)).setViewPlaneDistance(1);

        //TC00: small triangle
        Triangle triangle1 = new Triangle(new Point(-1, 0, -2),new Point(0, 0.5, -2),new Point(0, -0.5, -2));
        assertCountIntersections(3,camera, triangle1);

        //TC01: large triangle
        Triangle triangle2 = new Triangle(new Point(100, -100, -2),new Point(100, 100, -2),new Point(-100, 0, -2));
        assertCountIntersections(9, camera, triangle2);
    }
    /**
     * Integration tests of Camera Construct Ray with Sphere intersections (with Ray)
     */
    //There is sphere
    public void cameraRaySphereIntegration(){
        final Point ZERO_POINT = new Point(0, 0, 0);
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1),
                new Vector(0, -1, 0)).setViewPlaneDistance(1);

        //TC00: small sphere - goes through one pixel and gives 2 points
        Sphere sphere1 = new Sphere(0.5, new Point(0,0,-2));
        assertCountIntersections(2, camera, sphere1);

        //TC01: medium sphere - goes through four pixels and gives 8 points
        Sphere sphere2 = new Sphere(1, new Point(0.5,-0.5,-2));
        assertCountIntersections(8, camera, sphere2);

        //TC02: big sphere - goes through all nine pixels and gives 18 points
        Sphere sphere3 = new Sphere(5, new Point(0,0,-10));
        assertCountIntersections(18, camera, sphere3);

        //TC03: in sphere - goes through nine pixels and gives 9 points
        Sphere sphere4 = new Sphere(20, new Point(0,0,2));
        assertCountIntersections(9, camera, sphere4);

        //TC04: facing away from sphere - goes through zero pixels
        Sphere sphere5 = new Sphere(1, new Point(0,0,2));
        assertCountIntersections(0, camera, sphere5);

    }
}
