package scene;

import lighting.*;
import geometries.*;
import primitives.*;

import org.xml.sax.SAXException;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import java.io.*;

/**
 * Class responsible for parsing the XML file and creating the scene
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class XML {

    //@TODO: add material (variable) to all geometry options

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
        scene.setAmbientLight(new AmbientLight(parseColor(ambient.getAttribute("color")), new Double3(1d, 1d, 1d)));
        //3:
        var geometriesList = root.getChildNodes().item(3).getChildNodes(); // scene - geometries in scene

        //-------------parse geometries in scene---------------
        Geometries geometries = new Geometries();
        Point p0;

        for (int i = 0; i < geometriesList.getLength(); i++) {
            var geometry = geometriesList.item(i);
            if (geometry instanceof Element elem) {
                switch (geometry.getNodeName()) {
                    case "triangle" -> {
                        p0 = parsePoint(elem.getAttribute("p0"));
                        Point p1 = parsePoint(elem.getAttribute("p1"));
                        Point p2 = parsePoint(elem.getAttribute("p2"));
                        geometries.add(new Triangle(p0, p1, p2));
                    }
                    case "sphere" -> {
                        Point center = parsePoint(elem.getAttribute("center"));
                        double radius = Double.parseDouble(elem.getAttribute("radius"));
                        geometries.add(new Sphere(radius, center));
                    }
                    case "plane" -> {
                        p0 = parsePoint(elem.getAttribute("point"));
                        Vector v = parseVector(elem.getAttribute("vector"));
                        geometries.add(new Plane(p0, v));
                    }
                    case "tube" -> { //@TODO: add parse for Ray class (XML class) ??
                        p0 = parsePoint(elem.getAttribute("point"));
                        Vector v = parseVector(elem.getAttribute("vector"));
                        double radius = Double.parseDouble(elem.getAttribute("radius"));
                        geometries.add(new Tube(radius, new Ray(p0, v)));
                    }
                    case "cylinder" -> {
                        p0 = parsePoint(elem.getAttribute("point"));
                        Vector v = parseVector(elem.getAttribute("vector"));
                        double radius = Double.parseDouble(elem.getAttribute("radius"));
                        double height = Double.parseDouble(elem.getAttribute("height"));
                        geometries.add(new Cylinder(radius, new Ray(p0, v), height));
                    }
                }
            }
            scene.setGeometries(geometries);
        }
    }

    /**
     * Parses a vector from a string
     * @param toParse the string to parse
     * @return the vector parsed
     */
    private static Vector parseVector(String toParse) {
        var parsed = toParse.split(" ");
        return new Vector(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
    }

    /**
     * Parses a point from a string
     * @param toParse the string to parse
     * @return the point parsed
     */
    public static Point parsePoint(String toParse) {
        var parsed = toParse.split(" ");
        return new Point(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
    }

    /**
     * Parses a color from a string
     * @param toParse the string to parse
     * @return the color parsed
     */
    public static Color parseColor(String toParse) {
        var parsed = toParse.split(" ");
        return new Color(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
    }
}
