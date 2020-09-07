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
        scene.addLight(0, 100, -20, 1, 1, 1);
        
        // Add a large gray box for the ground
        scene.addBox(0, 0, 0, 1000, 1000, 0.4, 0.5, 0.5, 0.5);
        
        for (int x = 0; x < 1000; x += 100) {
            for (int z = 0; z < 1000; z += 100) {
                scene.addBox(x, 25, -z, 25, 25, 200, 1, 0, 0, 0, 0, 30);
            }
        }
        
        scene.saveScene("myscene.json", "Chris's Scene");
    }
}
