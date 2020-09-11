/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ctralie
 */
public class MyScene {
    
    
    public static void main(String[] args) {
        Scene3D scene = new Scene3D();
        scene.addCamera(0, 2, 0, 0);
        scene.addLight(0, 100, 0, 1, 1, 1);
        scene.addLight(0, -100, 0, 1, 1, 1);
        scene.addLight(-100, 100, 0, 1, 1, 1);
        scene.addLight(100, -100, 0, 1, 1, 1);
        
        // Add a large gray box for the ground
        scene.addBox(0, -25, 0, 1000, 50, 1000, 0.5, 0.5, 0.5);
        // Draw a cyan sphere with radius 1 centered at (-4, 4, 0)
        scene.addSphere(-4, 4, 0, 1, 0, 1, 1);
        scene.saveScene("myscene.json", "Chris's Scene");
    }
}
