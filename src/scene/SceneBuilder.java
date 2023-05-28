package scene;

import geometries.*;
import lighting.AmbientLight;
import lighting.LightSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import primitives.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

    private static List<LightSource> getLights(Element root) {
        var geometriesList = root.getChildNodes().item(4).getChildNodes(); // scene - geometries in scene

    }

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
        return new Material()
                .setKd(parseDouble3(elem.getAttribute("kd3")))
                .setKs(parseDouble3(elem.getAttribute("ks3")))
                .setShininess((int)Double.parseDouble(elem.getAttribute("shininess")));
    }

    private static Triangle parseTriangle(Element elem) {
        Triangle triangle = new Triangle(
                parsePoint(elem.getAttribute("p0")),
                parsePoint(elem.getAttribute("p1")),
                parsePoint(elem.getAttribute("p2"))
        );
        triangle.setMaterial(parseMaterial(elem));
        return triangle;
    }

    private static Sphere parseSphere(Element elem) {
        return new Sphere(
                Double.parseDouble(elem.getAttribute("radius")),
                parsePoint(elem.getAttribute("center"))
                );
    }
    private static Plane parsePlane(Element elem) {
        return new Plane(
                parsePoint(elem.getAttribute("point")),
                parseVector(elem.getAttribute("vector"))
        );
    }

    private static Tube parseTube(Element elem) {
        return new Tube(
                Double.parseDouble(elem.getAttribute("radius")),
                new Ray(parsePoint(elem.getAttribute("point")),
                        parseVector(elem.getAttribute("vector")))
                );
    }

    private static Cylinder parseCylinder(Element elem) {
        return new Cylinder(
                Double.parseDouble(elem.getAttribute("radius")),
                new Ray(parsePoint(elem.getAttribute("point")),
                        parseVector(elem.getAttribute("vector"))),
                Double.parseDouble(elem.getAttribute("height"))
        );
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
        return new Double3(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
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
