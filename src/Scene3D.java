/**
 * This code creates JSON flat hierarchy scene files for the ggslac library
 * https://github.com/ctralie/ggslac
 * @author ctralie
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Scene3D {
    private ArrayList<double[]> cameras; // xyzr
    private ArrayList<double[]> lights; // xyz, rgb
    private HashMap<String, Integer> colors; // Index "r_g_b" -> ID
    private ArrayList<String> shapesJSON; // JSON strings of shapes
    
    public Scene3D() {
        cameras = new ArrayList<>();
        lights = new ArrayList<>();
        colors = new HashMap<>();
        shapesJSON = new ArrayList<>();
    }
    
    /**
     * Return a hierarchy of JSON for doing a translation, then a sequence
     * of 3 rotations
     * @param tx Translation in x
     * @param ty Translation in y
     * @param tz Translation in z
     * @param rx Rotation about x
     * @param ry Rotation about y
     * @param rz Rotation about z
     * @param shape String with JSOn for the shape
     */
    private String getTransformationHierarchy(double tx, double ty, double tz, 
                                            double rx, double ry, double rz, 
                                            double sx, double sy, double sz, String shape) {
        String json = "{\n";
        // Translation
        json += "\"transform\":[1, 0, 0, " + tx + ", 0, 1, 0, " + ty + ", 0, 0, 1, " + tz + ", 0, 0, 0, 1],\n";
        json += "\"children\":[{";
        // Rotation about x
        double c = Math.cos(rx*Math.PI/180);
        double s = Math.sin(rx*Math.PI/180);
        json += "\"transform\":[1, 0, 0, 0, 0, " + c + ", " + -s + ", 0, 0, " + s + ", " + c + ", 0, 0, 0, 0, 1],\n";
        json += "\"children\":[{\n";
        // Rotation about y
        c = Math.cos(ry*Math.PI/180);
        s = Math.sin(ry*Math.PI/180);
        json += "\"transform\":[" + c + ", 0, " + s + ", 0, 0, 1, 0, 0, " + -s + ", 0, " + c + ", 0, 0, 0, 0, 1],\n";
        json += "\"children\":[{\n";
        // Rotation about z
        c = Math.cos(rz*Math.PI/180);
        s = Math.sin(rz*Math.PI/180);
        json += "\"transform\":[" + c + ", " + -s + ", 0, 0, " + s + ", " + c + ", 0, 0, 0, 0, 1, 0, 0, 0, 0, 1],\n";
        json += "\"children\":[{\n";
        json += "\"transform\":[" + sx + ", 0, 0, 0, 0, " + sy + ", 0, 0, 0, 0, " + sz + ", 0, 0, 0, 0, 1],\n";
        json += "\"shapes\":[\n";
        json += shape;
        json += "]\n";
        json += "}]\n"; // End rotation about z
        json += "}]\n"; // End rotation about y
        json += "}]\n"; // End rotation about x
        json += "}]\n"; // End translation
        json += "}";
        return json;
    }
    
    /**
     * Add a box to the scene
     * @param cx X center of box
     * @param cy Y center of box
     * @param cz Z center of box
     * @param xlen Length of box along x-axis
     * @param ylen Length of box along y-axis
     * @param zlen Length of box along z-axis
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     * @param rx Rotation about x-axis
     * @param ry Rotation about y-axis
     * @param rz Rotation about z-axis
     */
    public void addBox(double cx, double cy, double cz, double xlen, 
                       double ylen, double zlen, 
                       double r, double g, double b,
                       double rx, double ry, double rz) {
        String boxJSON = "{\"type\":\"box\",\n";
        boxJSON += "\"material\":\"" + getColorString(r, g, b) + "\"}";
        shapesJSON.add(getTransformationHierarchy(cx, cy, cz, rx, ry, rz, xlen, ylen, zlen, boxJSON));
    }
    
    /**
     * Add an axis-aligned box to the scene
     * @param cx X center of box
     * @param cy Y center of box
     * @param cz Z center of box
     * @param xlen Length of box along x-axis
     * @param ylen Length of box along y-axis
     * @param zlen Length of box along z-axis
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     */
    public void addBox(double cx, double cy, double cz,
                        double xlen, double ylen, double zlen,
                        double r, double g, double b) {
        addBox(cx, cy, cz, xlen, ylen, zlen, r, g, b, 0, 0, 0);
    }
    
     /**
     * Add a cylinder to the scene
     * @param cx X center of cylinder
     * @param cy Y center of cylinder
     * @param cz Z center of cylinder
     * @param radius Radius of the cylinder
     * @param height Height of the cylinder
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     * @param rx Rotation about x-axis
     * @param ry Rotation about y-axis
     * @param rz Rotation about z-axis
     * @param sx Scale about x-axis
     * @param sy Scale about y-axis
     * @param sz Scale about z-axis
     */
    public void addCylinder(double cx, double cy, double cz, double radius, 
                            double height, double r, double g, double b,
                            double rx, double ry, double rz,
                            double sx, double sy, double sz) {
        String cylinderJSON = "{\"type\":\"cylinder\",\n";
        cylinderJSON += "\"radius\":" + radius + ",\n";
        cylinderJSON += "\"height\":" + height + ",\n";
        cylinderJSON += "\"material\":\"" + getColorString(r, g, b) + "\"}";
        shapesJSON.add(getTransformationHierarchy(cx, cy, cz, rx, ry, rz, sx, sy, sz, cylinderJSON));
    }
    
     /**
     * Add an axis-aligned cylinder to the scene
     * @param cx X center of cylinder
     * @param cy Y center of cylinder
     * @param cz Z center of cylinder
     * @param radius Radius of the cylinder
     * @param height Height of the cylinder
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     */
    public void addCylinder(double cx, double cy, double cz, double radius, 
                            double height, double r, double g, double b) {
        addCylinder(cx, cy, cz, radius, height, r, g, b, 0, 0, 0, 1, 1, 1);
    }
    
     /**
     * Add a cone to the scene
     * @param cx X center of cone
     * @param cy Y center of cone
     * @param cz Z center of cone
     * @param radius Radius of the cone
     * @param height Height of the cone
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     * @param rx Rotation about x-axis
     * @param ry Rotation about y-axis
     * @param rz Rotation about z-axis
     * @param sx Scale about x-axis
     * @param sy Scale about y-axis
     * @param sz Scale about z-axis
     */
    public void addCone(double cx, double cy, double cz, double radius, 
                            double height, double r, double g, double b,
                            double rx, double ry, double rz,
                            double sx, double sy, double sz) {
        String coneJSON = "{\"type\":\"cone\",\n";
        coneJSON += "\"radius\":" + radius + ",\n";
        coneJSON += "\"height\":" + height + ",\n";
        coneJSON += "\"material\":\"" + getColorString(r, g, b) + "\"}";
        shapesJSON.add(getTransformationHierarchy(cx, cy, cz, rx, ry, rz, sx, sy, sz, coneJSON));
    }
    
     /**
     * Add an axis-aligned cone to the scene
     * @param cx X center of cone
     * @param cy Y center of cone
     * @param cz Z center of cone
     * @param radius Radius of the cone
     * @param height Height of the cone
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     */
    public void addCone(double cx, double cy, double cz, double radius, 
                            double height, double r, double g, double b) {
        addCone(cx, cy, cz, radius, height, r, g, b, 0, 0, 0, 1, 1, 1);
    }
    
    /**
     * Add an ellipsoid to the scene
     * @param cx X center of ellipsoid
     * @param cy Y center of ellipsoid
     * @param cz Z center of ellipsoid
     * @param radx Semi-axis x radius
     * @param rady Semi-axis y radius
     * @param radz Semi-axis z radius
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     * @param rx Rotation about x-axis
     * @param ry Rotation about y-axis
     * @param rz Rotation about z-axis
     */
    public void addEllipsoid(double cx, double cy, double cz, 
                            double radx, double rady, double radz,
                            double r, double g, double b, 
                            double rx, double ry, double rz) {
        String ellipsoidJSON = "{\"type\":\"sphere\",\n";
        ellipsoidJSON += "\"material\":\"" + getColorString(r, g, b) + "\"}";
        shapesJSON.add(getTransformationHierarchy(cx, cy, cz, rx, ry, rz, radx, rady, radz, ellipsoidJSON));
    }
    
    /**
     * Add an axis-aligned ellipsoid to the scene
     * @param cx X center of ellipsoid
     * @param cy Y center of ellipsoid
     * @param cz Z center of ellipsoid
     * @param radx Semi-axis x radius
     * @param rady Semi-axis y radius
     * @param radz Semi-axis z radius
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     */
    public void addEllipsoid(double cx, double cy, double cz, 
                            double radx, double rady, double radz,
                            double r, double g, double b) {
        addEllipsoid(cx, cy, cz, radx, rady, radz, r, g, b, 0, 0, 0);
    }
    
    /**
     * Add a sphere to the scene
     * @param cx X center of the sphere
     * @param cy Y center of the sphere
     * @param cz Z center of the sphere
     * @param radius Radius of the sphere
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     */
    public void addSphere(double cx, double cy, double cz, double radius,
                         double r, double g, double b) {
        addEllipsoid(cx, cy, cz, radius, radius, radius, r, g, b);
    }
    
    /**
     * Return the material ID of a lambertian material with a particular
     * RGB color sequence
     * @param r Red component (between 0 and 1)
     * @param g Green component (between 0 and 1)
     * @param b Blue component (between 0 and 1)
     * @return A string corresponding to the material
     */
    private String getColorString(double r, double g, double b) {
        String hash = r + "," + g + "," + b;
        if (!colors.containsKey(hash)) {
            colors.put(hash, colors.size());
        }
        return "color" + colors.get(hash);
    }
    
    /**
     * Add a particular camera to the scene
     * @param x X position of camera
     * @param y Y position of camera
     * @param z Z position of camera
     * @param rot Rotation in degrees about y-axis
     */
    public void addCamera(double x, double y, double z, double rot) {
        double[] xyzr = new double[4];
        xyzr[0] = x;
        xyzr[1] = y;
        xyzr[2] = z;
        cameras.add(xyzr);
    }
    
    /**
     * Add a light to the scene at a particular (x, y, z) position
     * and with a particular (r, g, b) color
     * @param x X position of light
     * @param y Y position of light
     * @param z Z position of light
     * @param r Red component of light in [0, 1]
     * @param g Green component of light in [0, 1]
     * @param b Blue component of light in [0, 1]
     */
    public void addLight(double x, double y, double z, double r, double g, double b) {
        double[] light = new double[6];
        light[0] = x;
        light[1] = y;
        light[2] = z;
        light[3] = r;
        light[4] = g;
        light[5] = b;
        lights.add(light);
    }
    
    /**
     * Create a string with the JSON for the materials, based on the
     * colors that were chosen
     * @return String with JSON
     */
    private String getMaterialsJSON() {
        String json = "\"materials\":{\n";
        ArrayList<String> colorList = new ArrayList<>(colors.keySet());
        for (int i = 0; i < colorList.size(); i++) {
            String rgb = colorList.get(i);
            int ID = colors.get(rgb);
            json += "\"color" + ID + "\": {\n";
            json += "\t\"kd\":[" + rgb + "]";
            json += "}";
            if (i < colorList.size() - 1) {
                json += ",";
            }
            json += "\n";
        }
        json += "}";
        return json;
    }
    
    /**
     * Create a string with the JSON for the lights in the scene, based
     * on the lights that the user added
     * @return String with JSON
     */
    private String getLightsJSON() {
        String json = "\"lights\":[\n";
        for (int i = 0; i < lights.size(); i++) {
            double[] L = lights.get(i);
            json += "{\n";
            json += "\t\"pos\":[" + L[0] + ", " + L[1] + ", " + L[2] + "],\n";
            json += "\t\"color\":[" + L[3] + ", " + L[4] + ", " + L[5] + "]\n";
            json += "}";
            if (i < lights.size() - 1) {
                json += ",";
            }
            json += "\n";
        }
        json += "]";
        return json;
    }
    
    /**
     * Create a string with the JSON for the cameras in the scene, based
     * on the cameras that the user added
     * @return String with JSON
     */
    private String getCamerasJSON() {
        String json = "\"cameras\":[\n";
        for (int i = 0; i < cameras.size(); i++) {
            double[] c = cameras.get(i);
            json += "{\n";
            json += "\t\"pos\":[" + c[0] + ", " + c[1] + ", " + c[2] + "],\n";
            // Compute quaternion for y rotation
            double sh = Math.sin(c[3]*Math.PI/360);
            double ch = Math.cos(c[3]*Math.PI/360);
            json += "\t\"rot\": [0, " + sh + ", 0, " + ch + "]\n";
            json += "}";
            if (i < cameras.size() - 1) {
                json += ",";
            }
            json += "\n";
        }
        json += "]";
        return json;
    }
    
    /**
     * Return a JSON string with the flat hierarchy of shapes
     * in the scene
     * @return JSON string
     */
    private String getShapesJSON() {
        String json = "\"children\":[\n";
        for (int i = 0; i < shapesJSON.size(); i++) {
            json += shapesJSON.get(i);
            if (i < shapesJSON.size() - 1) {
                json += ",";
            }
            json += "\n";
        }
        json += "]\n";
        return json;
    }
    
    /**
     * Save this scene to a file
     * @param filename Path to which to save file (should end with .json)
     * @param sceneName Title of the scene to display in the viewer
     */
    public void saveScene(String filename, String sceneName) {
        String json = "{\n\"name\":\"" + sceneName + "\",\n";
        json += getMaterialsJSON() + ",\n";
        json += getLightsJSON() + ",\n";
        json += getCamerasJSON() + ",\n";
        json += getShapesJSON() + "\n}";
        try (PrintWriter out = new PrintWriter(filename)) {
            out.write(json);
        }
        catch(FileNotFoundException e) {
            System.out.println("Could not open " + filename);
        }
    }
}
