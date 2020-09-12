/**
 * A class that holds methods to draw different 3D objects and piece
 * together scenes
 */
public class MyScene {
    
    /**
     * Draw a city block repeated several times
     */
    public static void drawScene() {
        Scene3D scene = new Scene3D();
        scene.addCamera(0, 2, 0, 0);
        scene.addCamera(0, 2, -4, 180);
        scene.addLight(0, 100, 0, 1, 1, 1);
        scene.addLight(0, -100, 0, 1, 1, 1);
        scene.addLight(-100, 100, 0, 1, 1, 1);
        scene.addLight(100, -100, 0, 1, 1, 1);
        
        // Add a large gray box for the ground
        scene.addBox(0, -25, 0, 1000, 50, 1000, 127, 127, 127);
        
        // TODO: Fill this in
        
        scene.saveScene("myscene.json", "Scene Title");
    }
    
    /**
     * Draw your art contest submission
     */
    public static void drawArtContest() {
        Scene3D scene = new Scene3D();
        scene.addCamera(0, 2, 0, 0);
        // TODO: Fill this in
        
        scene.saveScene("artcontest.json", "Scene Title");
    }
    
    public static void main(String[] args) {
        drawScene();
        drawArtContest();
    }
}
