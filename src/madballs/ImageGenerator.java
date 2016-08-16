package madballs;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by hainguyen on 8/16/16.
 */
public class ImageGenerator {
    private static ImageGenerator imageGenerator = new ImageGenerator();
    private Image ak47, awp, bazooka, flameThrower, m4a1, minigun, pistol, shotgun, uzi;

    public static ImageGenerator getInstance(){
        return imageGenerator;
    }

    private ImageGenerator(){
        ak47 = new Image(new File("assets/img/ak47.png").toURI().toString());
    }

    public Image getImage(String imageName){
        if (imageName == null){
            return null;
        }
        if (imageName.equalsIgnoreCase("ak47")){
            System.out.println("get ak47");
            return ak47;
        }
        return null;
    }
}
