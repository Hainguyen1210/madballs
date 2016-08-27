package madballs;

import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hainguyen on 8/16/16.
 */
public class ImageGenerator {
    private static ImageGenerator imageGenerator = new ImageGenerator();

    private Map<String, Image> images = new HashMap<>();
    private String[] imageNames = new String[]
            {"ak47", "awp", "bazooka", "flameThrower", "m4a1", "minigun", "pistol", "shotgun", "uzi",
                    "bullet1","bullet2","bullet3","bullet4",
                    "background1", "background0",
                    "obstacle10", "obstacle11",
                    "obstacle00", "obstacle01",
                    "ball1", "ball2", "ball3","ball4","ball5","ball6",
                    "map_desert", "map_warehouse"
            };

    public static ImageGenerator getInstance(){
        return imageGenerator;
    }

    private ImageGenerator(){
        for( String imageName: imageNames){
            Image image = new Image(new File("assets/img/"+ imageName + ".png").toURI().toString());
            images.put(imageName, image);
        }
    }

    public Image getImage(String imageName){
        if (imageName == null){
            return null;
        }
        return images.get(imageName);

    }
}
