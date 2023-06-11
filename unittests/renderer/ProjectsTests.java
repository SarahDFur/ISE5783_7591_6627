package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * testing our picture for stage7, and improvements of stage8
 *
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class ProjectsTests {

    //region stage7 - our pictures

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
                ,new Cylinder(60d,new Ray(new Point(0,-80,-40),new Vector(0,1,0)),90d)
                     //   .setEmission(new Color(20,20,190))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)
                                .setKr(0.4).setKt(0.99))
                ,new Cylinder(55d,new Ray(new Point(0,-78,-40),new Vector(0,1,0)),60d)
                        .setEmission(new Color(orange))
                        .setMaterial(new Material()
                                .setKr(0.3).setKt(0.99))
                ,new Sphere(40d, new Point(0,-40,-40))
                        .setEmission(new Color(20, 0, 80))
                        .setMaterial(
                                new Material().setKd(0.25).setKs(0.25).setShininess(61)
                                        .setKt(0.99).setKr(0.4)
                        )
//                ,new Sphere(10d,new Point(20,20,-20))
//                        .setEmission(new Color(GREEN))
//                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setKr(0.2).setKt(0.999))

//                ,new Polygon(new Point(-150, -150, -135), new Point(150, -150, -135),
//                        new Point(75, 75, -115), new Point(-75, 75, -115))
//                        .setEmission(new Color(RED))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60))
//               ,new Sphere(40, new Point(30,30,-50))
//                        .setEmission(new Color(120, 20, 200))
//                        .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4)).setKt(0.62)) //reflective
//                ,new Sphere(30,new Point(90,-50,-80))
//                        .setEmission(new Color(RED))
//                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
//                                .setKt(new Double3(0.5, 0, 0))) //transparent

                ,new Tube(10,new Ray(new Point(-80,-80,-80),new Vector(0,1,0)))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKr(0.0002).setKt(0.45))
        );

        scene.lights.add(
                new PointLight(new Color(ORANGE), new Point(30, 70, -100))
                        .setKl(0.001).setKq(0.0000002)
        );
//        scene.lights.add(new SpotLight(new Color(ORANGE), new Point(30, 70, -100), new Vector(0,1,0))
//                .setKl(0.001).setKq(0.0000002));
//        scene.lights.add(new SpotLight(new Color(700, 400, 400),
//                new Point(30, 50, 0), new Vector(0, 0, -1)).setKl(4E-5).setKq(2E-7));
//        scene.lights.add(new SpotLight(new Color(orange),
//                new Point(100, -10, 115), new Vector(-1, -1, -4)).setNarrowBeam(10).setKl(4E-4).setKq(2E-5));
        scene.lights.add(new SpotLight(new Color(orange),
                new Point(0,-78,-40),new Vector(0,1,0)).setNarrowBeam(10).setKl(4E-4).setKq(2E-5));

//        Camera camera = new Camera(new Point(0, 0, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
//                .setRayTracer(new RayTracerBasic(scene))
//                .setImageWriter(new ImageWriter("myPicture-7.3", 700, 700))
//                .renderImage();
//        camera.writeToImage();

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
                .setRayTracer(new RayTracerBasic(scene))
                .setImageWriter(new ImageWriter("myPicture-7.3", 700, 700))
                .renderImage();
//        Camera camera = new Camera(new Point(0, 100, 1000), new Vector(0, -1, 0), new Vector(1, 0, -1))
//                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
//                .setRayTracer(new RayTracerBasic(scene))
//                .setImageWriter(new ImageWriter("myPicture-7.3", 700, 700))
//                .renderImage();
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
        scene.lights.add(new PointLight(new Color(ORANGE), new Point(30, 70, -100))
                .setKl(0.001).setKq(0.0000002));
        Camera camera = new Camera(new Point(-3.6, -14.39,0), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
                .setRayTracer(new RayTracerBasic(scene))
                .setImageWriter(new ImageWriter("MiniBar", 700, 700))
                .renderImage();
        camera.writeToImage();
    }
    //                //region sides:
//                //right side
//                , new Polygon(new Point(120,-80,10),new Point(120,60,10), new Point(120,60,-90), new Point(120,-80,-90))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //left side
//                , new Polygon(new Point(80,-80,10),new Point(80,60,10), new Point(80,60,-90), new Point(80,-80,-90))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //front
//                , new Polygon(new Point(120,-80,10),new Point(120,60,10), new Point(80,60,-90), new Point(80,-80,-90))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //front
//                , new Polygon(new Point(120,-80,10), new Point(80,-80,10),new Point(80,60,10),new Point(120,60,10))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //bottom
//                , new Polygon(new Point(120,-80,10), new Point(120,-80,-90),new Point(80,-80,-90),new Point(80,-80,10))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //endregion
//                //region top:
//                //right triangle
//                , new Polygon(new Point(120,60,10), new Point(120,60,-90), new Point(100,100,-50))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //left triangle
//                , new Polygon(new Point(80,60,10), new Point(80,60,-90), new Point(100,100,-50))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //front triangle
//                , new Polygon(new Point(120,60,10), new Point(80,60,-90), new Point(100,100,-50))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //back triangle
//                , new Polygon(new Point(100,100,-50),new Point(80,60,10),new Point(120,60,10))
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.3).setKr(0.4))
//                //bottle nose
//                ,new Cylinder(5d,new Ray(new Point(100,90,-45),new Vector(0,1,0)),30d)
//                        .setEmission(new Color(0, 106, 78))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5)
//                                .setKr(0.4).setKt(0.3))
//
//                //endregion
    @Test
    public void wisCup()
    {
        Scene scene = new Scene("whiskey cup");
        scene.geometries.add(
//region geometries
        //region surfaces
                new Sphere(500, new Point(100,100,0))
                        //.setEmission(new Color(20, 0, 80))
                        .setMaterial(
                                new Material().setKd(0.25).setKs(0.25).setShininess(61)
                                        .setKr(0.5).setKt(0.9)
                        )
                ,new Polygon(A, B, F, E)
                        //.setEmission(new Color(YELLOW))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20))//floor
//                , new Polygon(A, B, C, D)
//                       // .setEmission(new Color(BLUE))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20))//back
        //endregion
        //region cup
                ,new Cylinder(60d,new Ray(new Point(0,-80,-40),new Vector(0,1,0)),90d)
                        .setEmission(new Color(50,10,15))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)
                                .setKr(0.4).setKt(0.99))
                ,new Cylinder(55d,new Ray(new Point(0,-78,-40),new Vector(0,1,0)),60d)
                        .setEmission(new Color(169,64,7))
                        .setMaterial(new Material()
                                .setKr(0.9).setKt(0.3))
                ,new Sphere(40d, new Point(0,-40,-40))
                        //.setEmission(new Color(20, 0, 80))
                        .setMaterial(
                                new Material().setKd(0.25).setKs(0.25).setShininess(61)
                                        .setKt(0.99).setKr(0.4)
                        )
        //endregion

        //region bottle
                //inside bottle:
                ,new Cylinder(25d,new Ray(new Point(100,-75,-40),new Vector(0,1,0)),100d)
                        .setEmission(new Color(169,64,7))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5)
                                .setKr(0.2).setKt(0.5))
                //bottle
                ,new Cylinder(30d,new Ray(new Point(100,-80,-40),new Vector(0,1,0)),170d)
                        .setEmission(new Color(0, 106, 78))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5)
                                .setKr(0.25).setKt(0.8))
                //bottleneck
                ,new Cylinder(10d,new Ray(new Point(100,90,-40),new Vector(0,1,0)),27d)
                        .setEmission(new Color(0, 106, 78))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5)
                                .setKr(0.25).setKt(0.8))
                //cap
                ,new Cylinder(13d,new Ray(new Point(100,110,-40),new Vector(0,1,0)),8d)
                        .setEmission(new Color(110, 106, 78))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5)
                                .setKr(0.009).setKt(0.095))
        //endregion

        //region clouds
                //region bottom left:
                ,new Sphere(40, new Point(-130,-70,-90))
                        .setEmission(new Color(208,222,236))
                        .setMaterial(new Material().setKd(0.99).setKs(0.79)
                                .setKr(0.04).setKt(0.00001))
                ,new Sphere(30, new Point(-130,-70,-50))
                        .setEmission(new Color(208,222,236))
                        .setMaterial(new Material().setKd(0.99).setKs(0.79)
                                .setKr(0.04).setKt(0.00001))
                ,new Sphere(25, new Point(-100,-70,-90))
                        .setEmission(new Color(208,222,236))
                        .setMaterial(new Material().setKd(0.99).setKs(0.79)
                                .setKr(0.04).setKt(0.00001))
                //endregion
//                //region
//                ,new Sphere(40, new Point(130,190,90))
//                        .setEmission(new Color(208,222,236))
//                        .setMaterial(new Material().setKd(0.9).setKs(0.7)
//                                .setKr(0.9).setKt(0.00001))
//                ,new Sphere(30, new Point(130,150,50))
//                        .setEmission(new Color(208,222,236))
//                        .setMaterial(new Material().setKd(0.9).setKs(0.7)
//                                .setKr(0.9).setKt(0.00001))
//                ,new Sphere(25, new Point(120,70,90))
//                        .setEmission(new Color(208,222,236))
//                        .setMaterial(new Material().setKd(0.9).setKs(0.7)
//                                .setKr(0.9).setKt(0.00001))
        //endregion
        //region rose/flower
//                , new Sphere(50,new Point(-150,-40,-50))
//                        .setEmission(new Color(black))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)
//                                .setKr(0).setKt(1))
//                , new Sphere(5,new Point(-150,-40,-50))
//                        .setEmission(new Color(black))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)
//                                .setKr(0).setKt(1))
//                , new Sphere(20,new Point(-150,-40,-50))
//                        .setEmission(new Color(black))
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)
//                                .setKr(0).setKt(1))
//                , new Triangle(new Point(-150,-50,-20), new Point(-150,-45,-30), new Point(-150,-50,-30))
//                        .setEmission(new Color(yellow))
//                , new Triangle(new Point(-120,-50,-10), new Point(-120,-45,-30), new Point(-120,-50,-30))
//                        .setEmission(new Color(yellow))
//                , new Triangle(new Point(-140,-50,-10), new Point(-140,-40,-30), new Point(-140,-50,-30))
//                        .setEmission(new Color(yellow))

        //endregion
//endregion
        );
    //region lighting
        scene.lights.add(new PointLight(new Color(blue), new Point(30, 70, 0))
                .setKl(0.001).setKq(0.0000002));
        //light inside sphere
//        scene.lights.add(new SpotLight(new Color(ORANGE), new Point(0,-80,-40), new Vector(0,1,0))
//                        .setNarrowBeam(0.0001)
//                .setKl(0.001).setKq(0.0000002));
        scene.lights.add(new SpotLight(new Color(orange), new Point(100,-75,-40), new Vector(0,1,0))
                        .setNarrowBeam(0.00000000000000000000001)
                .setKl(0.001).setKq(0.0000002));
        scene.lights.add(new DirectionalLight(new Color(orange), new Vector(0,1,0)));
    //endregion
//        scene.lights.add(new DirectionalLight(new Color(ORANGE), new Vector(0,-40,-40)));
        Camera camera = new Camera(new Point(-3.6, -14.39,0), new Vector(0, 0, -1), new Vector(0, 1, 0));
//-24.43,3273.3,308.49 - more of a top view
//        camera.moveCamera(new Point(80, 10, 300), new Point(0, -40, -40))
//                .setViewPlaneSize(700, 700).setViewPlaneDistance(700)
//                .setRayTracer(new RayTracerBasic(scene))
//                .setImageWriter(new ImageWriter("whiskeyCup", 1000, 1000))
//                .renderImage();
//        camera.writeToImage();
    //region camera movements
        //move right
        for(int i = 0; i < 5; i=i+1) {
            String j = String.valueOf(i);
            camera.moveCamera(new Point(80+i*80, 10, 300), new Point(0, -40, -40))
                    .setViewPlaneSize(700, 700).setViewPlaneDistance(700)
                    .setRayTracer(new RayTracerBasic(scene))
                    .setImageWriter(new ImageWriter("whiskeyCup"+j, 1000, 1000))
                    .setAntiAliasingFactor(9)
                    .renderImage();
            camera.writeToImage();
        }
//        //move left
//        for(int i = 0; i < 5; i=i+1) {
//            String j = String.valueOf(i+5);
//            camera.moveCamera(new Point(80-i*80, 10, 300), new Point(0, -40, -40))
//                    .setViewPlaneSize(700, 700).setViewPlaneDistance(700)
//                    .setRayTracer(new RayTracerBasic(scene))
//                    .setImageWriter(new ImageWriter("whiskeyCup"+j, 1000, 1000))
//                    .renderImage();
//            camera.writeToImage();
//        }
//        //move up
//        for(int i = 0; i < 5; i=i+1) {
//            String j = String.valueOf(i+10);
//            camera.moveCamera(new Point(80, 10+i*80, 300), new Point(0, -40, -40))
//                    .setViewPlaneSize(700, 700).setViewPlaneDistance(700)
//                    .setRayTracer(new RayTracerBasic(scene))
//                    .setImageWriter(new ImageWriter("whiskeyCup"+j, 1000, 1000))
//                    .renderImage();
//            camera.writeToImage();
//        }
//        //move down
//        for(int i = 0; i < 5; i=i+1) {
//            String j = String.valueOf(i+15);
//            camera.moveCamera(new Point(80, 10-i*80, 300), new Point(0, -40, -40))
//                    .setViewPlaneSize(700, 700).setViewPlaneDistance(700)
//                    .setRayTracer(new RayTracerBasic(scene))
//                    .setImageWriter(new ImageWriter("whiskeyCup"+j, 1000, 1000))
//                    .renderImage();
//            camera.writeToImage();
//        }
//        //rotate right
//        for(int i = 0; i < 5; i=i+1) {
//            String j = String.valueOf(i+20);
//            camera.rotateCamera(9)
//                    .setViewPlaneSize(700, 700).setViewPlaneDistance(700)
//                    .setRayTracer(new RayTracerBasic(scene))
//                    .setImageWriter(new ImageWriter("whiskeyCup" + j, 1000, 1000))
//                    .renderImage();
//            camera.writeToImage();
//        }
    //endregion

    }
//endregion

    //region stage8 - improvements
//    private Intersectable sphere = new Sphere(60d, new Point(0, 0, -200))
//            .setEmission(new Color(BLUE))
//            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
//    private Material trMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(30);

//    private Scene scene = new Scene("Test scene");
//    private Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0))
//            .setViewPlaneSize(200, 200).setViewPlaneDistance(1000)
//            .setRayTracer(new RayTracerBasic(scene));

//    void sphereTriangleHelper(String pictName, Triangle triangle, Point spotLocation) {
//        scene.geometries.add(sphere, triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial));
//        scene.lights.add(
//                new SpotLight(new Color(400, 240, 0), spotLocation, new Vector(1, 1, -3))
//                        .setKl(1E-5).setKq(1.5E-7));
//        camera.setImageWriter(new ImageWriter(pictName, 400, 400))
//                .renderImage()
//                .writeToImage();
//    }

    @Test
    public void sphereTriangleMove2_AntiAliasing() {
        Scene scene = new Scene("Test scene");

        scene.geometries.add(
                new Sphere(60d, new Point(0, 0, -200))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                        new Triangle(new Point(-62, -32, 0), new Point(-32, -62, 0), new Point(-60, -60, -4))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)));

        scene.lights.add(
                new SpotLight(new Color(400, 240, 0), new Point(-100, -100, 200), new Vector(1, 1, -3))
                        .setKl(1E-5).setKq(1.5E-7));

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setViewPlaneSize(200, 200)
                .setViewPlaneDistance(1000)
                .setRayTracer(new RayTracerBasic(scene))
                .setImageWriter(new ImageWriter("antiAliasing_shadowSphereTriangleMove2", 400, 400))
                .setAntiAliasingFactor(3)
                .renderImage();

        camera.writeToImage();
    }

    //endregion

}
