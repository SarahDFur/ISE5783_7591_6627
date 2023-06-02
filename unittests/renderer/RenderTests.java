package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class RenderTests {

    /**
     * Produce a scene with basic 3D model and render it into a png image with a
     * grid
     */
    @Test
    public void basicRenderTwoColorTest() {
        Scene scene = new Scene("Test scene")//
                .setAmbientLight(new AmbientLight(new Color(255, 191, 191), //
                        new Double3(1, 1, 1))) //
                .setBackground(new Color(75, 127, 90));

        scene.geometries.add(new Sphere(50d, new Point(0, 0, -100)),
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
                // left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100),
                        new Point(-100, -100, -100)), // down
                // left
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down
        // right
        Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneDistance(100) //
                .setViewPlaneSize(500, 500) //
                .setImageWriter(new ImageWriter("base render test", 1000, 1000))
                .setRayTracer(new RayTracerBasic(scene));

        camera.renderImage();
        camera.printGrid(100, new Color(YELLOW));
        camera.writeToImage();
    }

    // For stage 6 - please disregard in stage 5

    /**
     * Produce a scene with basic 3D model - including individual lights of the
     * bodies and render it into a png image with a grid
     */
    @Test
    public void basicRenderMultiColorTest() {
        Scene scene = new Scene("Test scene")//
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.2))); //

        scene.geometries.add( //
                new Sphere(50, new Point(0, 0, -100)),
                // up left
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                        .setEmission(new Color(GREEN)),
                // down left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                        .setEmission(new Color(RED)),
                // down right
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                        .setEmission(new Color(BLUE)));

        Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneDistance(100) //
                .setViewPlaneSize(500, 500) //
                .setImageWriter(new ImageWriter("color render test", 1000, 1000))
                .setRayTracer(new RayTracerBasic(scene));

        camera.renderImage();
        camera.printGrid(100, new Color(WHITE));
        camera.writeToImage();
    }

//    /** Test for XML based scene - for bonus */
//    @Test
//    public void basicRenderXml() throws ParserConfigurationException, IOException, SAXException {
//        Scene scene  = new Scene("XML Test scene");
//        // enter XML file name and parse from XML file into scene object
//        // using the code you added in appropriate packages
//        // ...
//        // NB: unit tests is not the correct place to put XML parsing code
//        SceneBuilder.sceneParser(scene, "basicRenderTestTwoColors.xml");
//
//        Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0))     //
//                .setViewPlaneDistance(100)                                                                //
//                .setViewPlaneSize(500, 500).setImageWriter(new ImageWriter("xml render test", 1000, 1000))
//                .setRayTracer(new RayTracerBasic(scene));
//        camera.renderImage();
//        camera.printGrid(100, new Color(BLACK));
//        camera.writeToImage();
//    }


    //region points
    Point A = new Point(70, -70, -150);
    Point B = new Point(-70, -70, -150);
    Point C = new Point(-70, 90, -150);
    Point D = new Point(70, 90, -150);
    Point E = new Point(100, -100, 0);
    Point F = new Point(-100, -100, 0);
    Point G = new Point(-100, 120, 0);
    Point H = new Point(100, 120, 0);
    //endregion

    /**
     * test for creating a scene from scratch,
     * at least 3-4 object, with transparency, shadows and reflections
     */
    @Test
    public void myPicture() {
        Scene scene = new Scene("Test scene")
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

        //@TODO: i have no idea what is going on
        // sometimes thing works as they should but usually not,
        // i dont
        scene.geometries.add(
                new Polygon(A, B, F, E)
                        //.setEmission(new Color(YELLOW))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20))//floor
                , new Polygon(A, B, C, D)
                        //.setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20))//back
                , new Polygon(C, D, H, G)
                        //.setEmission(new Color(RED))
                        .setMaterial(new Material()
                                .setKd(0.5).setKs(0.5).setShininess(20))
                ,new Cylinder(60d,new Ray(new Point(0,-85,-40),new Vector(0,1,0)),90d)
                        .setEmission(new Color(blue))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)
                                .setKr(0.000001).setKt(0.4))
                ,new Sphere(40d, new Point(0,-50,-40))
                        .setEmission(new Color(0, 50, 100))
                        .setMaterial(
                                new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                .setKt(0.00000025).setKr(0.0001)
                        )
//                ,new Sphere(10d,new Point(20,20,-20))
 //                       .setEmission(new Color(GREEN))

//                ,new Polygon(new Point(-150, -150, -135), new Point(150, -150, -135))
//                        new Point(75, 75, -115), new Point(-75, 75, -115))
//                        .setEmission(new Color(RED))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60))
//               ,new Sphere(40, new Point(30,30,-50))
//                        .setEmission(new Color(20, 20, 20))
//                        .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))) //reflective
//                ,new Sphere(50,new Point(70,-70,-70))
//                        .setEmission(new Color(RED))
//                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
//                                .setKt(new Double3(0.5, 0, 0))) //transparent
//                , new Sphere(30d, new Point(30, 50, -50))
//                        .setEmission(new Color(BLUE))
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)) //transparent
//                ,new Sphere(30d, new Point(60, -50, -50))
//                        .setEmission(new Color(BLUE))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))
                ,new Tube(10,new Ray(new Point(-80,-80,-80),new Vector(0,1,0)))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKr(0.0002).setKt(0.45))
        );

        scene.lights.add(new PointLight(new Color(orange), new Point(30, 70, -100))
                .setKl(0.001).setKq(0.0000002));
//        scene.lights.add(new SpotLight(new Color(700, 400, 400),
//                new Point(30, 50, 0), new Vector(0, 0, -1)).setKl(4E-5).setKq(2E-7));
//        scene.lights.add(new SpotLight(new Color(400, 400, 700),
//                new Point(100, -10, 115), new Vector(-1, -1, -4)).setNarrowBeam(10).setKl(4E-4).setKq(2E-5));

//        Camera camera = new Camera(new Point(0, 0, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
//                .setRayTracer(new RayTracerBasic(scene))
//                .setImageWriter(new ImageWriter("myPicture-7.3", 700, 700))
//                .renderImage();
//        camera.writeToImage();

        Camera camera = new Camera(new Point(0, 0, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
                .setRayTracer(new RayTracerBasic(scene))
                .setImageWriter(new ImageWriter("myPicture-7.3", 700, 700))
                .renderImage();
        camera.writeToImage();
    }

//    @Test
//    public void xmlMiniBar() throws ParserConfigurationException, IOException, SAXException {
//        Scene scene = new Scene("mini bar test");
//        SceneBuilder.sceneParser(scene, "xmlMiniBarRoom.xml");
//
//        Camera camera = new Camera(new Point(0, 0, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
//                .setRayTracer(new RayTracerBasic(scene))
//                .setImageWriter(new ImageWriter("xmlMiniBar", 500, 500))
//                .renderImage();
//        camera.writeToImage();
//    }
    @Test
    public void miniBar() {
        Scene scene = new Scene("mini bar test");
        scene.setAmbientLight(new AmbientLight(new Color(255,255,255), new Double3(0.2,0,0.4)));
        scene.geometries.add(

                new Polygon(new Point(80,-80,-150), new Point(-80,-80,-150), new Point(-110,-110,0), new Point(110,-110,0))
                        //.setEmission(new Color(RED))
                        .setMaterial(new Material()
                                .setKd(0.5).setKs(0.5).setShininess(20))
                , new Polygon(A, B, C, D)
                        //.setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20))//back
                , new Sphere(300, new Point(2,2,0))
                    //   .setEmission(new Color(138,0,210))
                     //   .setMaterial(new Material().setKd(0.001).setKs(0.00001).setKr(1))
        );
        scene.lights.add(new PointLight(new Color(orange), new Point(30, 70, -100))
                .setKl(0.001).setKq(0.0000002));
        Camera camera = new Camera(new Point(-3.6, -14.39,0), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
                .setRayTracer(new RayTracerBasic(scene))
                .setImageWriter(new ImageWriter("MiniBar", 700, 700))
                .renderImage();
        camera.writeToImage();
    }
}

