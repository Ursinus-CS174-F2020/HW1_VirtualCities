/**
 * This code creates JSON scene files for the ggslac library
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
                                            double rx, double ry, double rz, String shape) {
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
        json += "\"shapes\":[\n";
        json += shape;
        json += "]\n";
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
     * @param length Length of box
     * @param width Width of box
     * @param height Height of box
     * @param r Red component in [0, 1]
     * @param g Green component in [0, 1]
     * @param b Blue component in [0, 1]
     * @param rx Rotation about x-axis
     * @param ry Rotation about y-axis
     * @param rz Rotation about z-axis
     */
    public void addBox(double cx, double cy, double cz, double length, 
                       double width, double height, 
                       double r, double g, double b,
                       double rx, double ry, double rz) {
        String boxJSON = "{\"type\":\"box\",\n";
        boxJSON += "\"length\":" + length + ",\n";
        boxJSON += "\"width\":" + width + ",\n";
        boxJSON += "\"height\":" + height + ",\n";
        boxJSON += "\"material\":\"" + getColorString(r, g, b) + "\"}";
        shapesJSON.add(getTransformationHierarchy(cx, cy, cz, rx, ry, rz, boxJSON));
    }
    
    public void addBox(double cx, double cy, double cz, double length,
                        double width, double height,
                        double r, double g, double b) {
        addBox(cx, cy, cz, length, width, height, r, g, b, 0, 0, 0);
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
