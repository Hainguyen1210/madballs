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
            "background1", "background0",
            "obstacle10", "obstacle11",
            "obstacle00", "obstacle01",
            "map0", "map1"
            };

    public static ImageGenerator getInstance(){
        return imageGenerator;
    }

    private ImageGenerator(){
        for( String imageName: imageNames){
            Image gunImage = new Image(new File("assets/img/"+ imageName + ".png").toURI().toString());
            images.put(imageName, gunImage);
        }
    }

    public Image getImage(String imageName){
        if (imageName == null){
            return null;
        }
        return images.get(imageName);

    }
}
