package scene;

import geometries.*;
import lighting.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import primitives.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class responsible for parsing the XML file and creating the scene
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class SceneBuilder {

    /**
     * Method parses XML file
     * @param scene the scene to add the objects to
     * @param fileName file that contains XML
     * @throws ParserConfigurationException exception
     * @throws IOException exception
     * @throws SAXException exception
     */
    public static void sceneParser(Scene scene, String fileName) throws ParserConfigurationException, IOException, SAXException {
        //build the parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File(fileName));
        document.getDocumentElement().normalize();

        //-------------start at root of scene---------------
        var root = document.getDocumentElement();
        //1:
        scene.setBackground(parseColor(root.getAttribute("background-color"))); // scene - background
        //2:
        var ambient = (Element) root.getChildNodes().item(1); // scene - ambient light
        scene.setAmbientLight(new AmbientLight(parseColor(ambient.getAttribute("color")), parseDouble3(ambient.getAttribute("ka"))));
        //3:
        scene.geometries = getGeometries(root);
        scene.lights = getLights(root);
    }

    //region geometry parsing

    /**
     * Gets all geometries from xml file - converter
     * @param root root of file
     * @return returns all geometries in scene
     */
    private static Geometries getGeometries(Element root) {
        var geometriesList = root.getChildNodes().item(3).getChildNodes(); // scene - geometries in scene
        //-------------parse geometries in scene---------------
        Geometries geometries = new Geometries();
        for (int i = 0; i < geometriesList.getLength(); i++) {
            var geometry = geometriesList.item(i);
            if (geometry instanceof Element elem) {
                switch (geometry.getNodeName()) {
                    case "triangle" -> {
                        geometries.add(parseTriangle(elem));
                    }
                    case "sphere" -> {
                        geometries.add(parseSphere(elem));
                    }
                    case "plane" -> {
                        geometries.add(parsePlane(elem));
                    }
                    case "tube" -> {
                        geometries.add(parseTube(elem));
                    }
                    case "cylinder" -> {
                        geometries.add(parseCylinder(elem));
                    }
                }
            }
        }
        return geometries;
    }

    //region parsing geometries

//    private static Polygon parsePolygon(Element elem) {
//        return new Polygon(
//                parsePoint(elem.getAttribute("p0")),
//                parsePoint(elem.getAttribute("p1")),
//                parsePoint(elem.getAttribute("p2"))
//        );
//    }

    private static Material parseMaterial(Element elem) {
        Material material = new Material();
        if(elem.hasAttribute("kd"))
            material.setKr(parseDouble3(elem.getAttribute("kd")));
        if(elem.hasAttribute("ks"))
            material.setKt(parseDouble3(elem.getAttribute("ks")));
        if(elem.hasAttribute("shininess"))
            material.setKt(parseDouble3(elem.getAttribute("shininess")));
        if(elem.hasAttribute("kr"))
            material.setKr(parseDouble3(elem.getAttribute("kr")));
        if(elem.hasAttribute("kt"))
            material.setKt(parseDouble3(elem.getAttribute("kt")));
        return material;
    }

    private static Triangle parseTriangle(Element elem) {
        Triangle triangle = new Triangle(
                parsePoint(elem.getAttribute("p0")),
                parsePoint(elem.getAttribute("p1")),
                parsePoint(elem.getAttribute("p2"))
        );
        triangle.setMaterial(parseMaterial(elem));
        if(elem.hasAttribute("emission"))
            triangle.setEmission(parseColor(elem.getAttribute("emission")));
        return triangle;
    }

    private static Sphere parseSphere(Element elem) {
        Sphere sphere = new Sphere(
                Double.parseDouble(elem.getAttribute("radius")),
                parsePoint(elem.getAttribute("center"))
        );
        sphere.setMaterial(parseMaterial(elem));
        if(elem.hasAttribute("emission"))
            sphere.setEmission(parseColor(elem.getAttribute("emission")));
        return sphere;
    }
    private static Plane parsePlane(Element elem) {
        Plane plane = new Plane(
                parsePoint(elem.getAttribute("point")),
                parseVector(elem.getAttribute("vector"))
        );
        if(elem.hasAttribute("emission"))
            plane.setEmission(parseColor(elem.getAttribute("emission")));
        return plane;
    }

    private static Tube parseTube(Element elem) {
        Tube tube = new Tube(
                Double.parseDouble(elem.getAttribute("radius")),
                new Ray(parsePoint(elem.getAttribute("point")),
                        parseVector(elem.getAttribute("vector")))
        );
        if(elem.hasAttribute("emission"))
            tube.setEmission(parseColor(elem.getAttribute("emission")));
        return tube;
    }

    private static Cylinder parseCylinder(Element elem) {
        Cylinder cylinder = new Cylinder(
                Double.parseDouble(elem.getAttribute("radius")),
                new Ray(parsePoint(elem.getAttribute("point")),
                        parseVector(elem.getAttribute("vector"))),
                Double.parseDouble(elem.getAttribute("height"))
        );
        if(elem.hasAttribute("emission"))
            cylinder.setEmission(parseColor(elem.getAttribute("emission")));
        return cylinder;
    }

    //endregion
    //endregion

    //region light sources parsing
    private static List<LightSource> getLights(Element root) {
        var lightSourceList = root.getChildNodes().item(3).getNextSibling().getNextSibling().getChildNodes();
        List<LightSource> lightSources = new LinkedList<>();
        for (int i = 0; i < lightSourceList.getLength(); i++) {
            var light = lightSourceList.item(i);
            if (light instanceof Element elem) {
                switch (light.getNodeName()) {
                    case "point" -> {
                        lightSources.add(parsePointLight(elem));
                    }
                    case "directional" -> {
                        lightSources.add(parseDirectionalLight(elem));
                    }
                    case "spot" -> {
                        lightSources.add(parseSpotLight(elem));
                    }
                }
            }
        }
        return lightSources;
    }

    private static PointLight parsePointLight(Element elem) {
        PointLight pointLight = new PointLight(
                parseColor(elem.getAttribute("intensity")),
                parsePoint(elem.getAttribute("p0"))
        );
        pointLight.setKl(Double.parseDouble(elem.getAttribute("kl")))
                .setKq(Double.parseDouble(elem.getAttribute("kq")));
        return pointLight;
    }

    private static DirectionalLight parseDirectionalLight(Element elem) {
        return new DirectionalLight(
                parseColor(elem.getAttribute("intensity")),
                parseVector(elem.getAttribute("vector"))
        );
    }

    private static SpotLight parseSpotLight(Element elem) {
        SpotLight spotLight = new SpotLight(
                parseColor(elem.getAttribute("intensity")),
                parsePoint(elem.getAttribute("p0")),
                parseVector(elem.getAttribute("vector"))
        );
        spotLight.setKl(Double.parseDouble(elem.getAttribute("kl")))
                .setKq(Double.parseDouble(elem.getAttribute("kq")));
        return spotLight;
    }

    //endregion

    //region regular parsers
    /**
     * Parses Double3 from a string
     * @param toParse string of 3 numbers to be changed into Double3
     * @return returns a Double3 variable
     */
    private static Double3 parseDouble3(String toParse) {
        var parsed = toParse.split(" ");
        if(parsed.length == 3)
            return new Double3(
                    Double.parseDouble(parsed[0]),
                    Double.parseDouble(parsed[1]),
                    Double.parseDouble(parsed[2])
            );
        else
            return new Double3(Double.parseDouble(parsed[0]));
    }

    /**
     * Parses a vector from a string
     * @param toParse the string to parse
     * @return the vector parsed
     */
    private static Vector parseVector(String toParse) {
        return new Vector(parseDouble3(toParse));
    }

    /**
     * Parses a point from a string
     * @param toParse the string to parse
     * @return the point parsed
     */
    public static Point parsePoint(String toParse) {
        return new Point(parseDouble3(toParse));
    }

    /**
     * Parses a color from a string
     * @param toParse the string to parse
     * @return the color parsed
     */
    public static Color parseColor(String toParse) {
        return new Color(parseDouble3(toParse));
    }
    //endregion
}
