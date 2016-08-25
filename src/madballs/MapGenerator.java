package madballs;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import madballs.map.Map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by hainguyen on 8/24/16.
 */
public class MapGenerator {
    private static MapGenerator mapGenerator = new MapGenerator();
    private Map map;
    public static MapGenerator getInstance(){
        return mapGenerator;
    }
    public MapGenerator(){

    }
    public void generateMapImage(){
        Random random = new Random();
        Map map;
        for (int i=0; i<Map.getMapFiles().size(); i++){
            map = new Map(i);
            Group group = new Group();
            Scene scene = new Scene(group, map.getWidth(), map.getHeight() );
            Stage stage = new Stage();
            stage.setScene(scene);

            String[][] mapArray = map.getMAP_ARRAY();
            System.out.println("scene: " + scene.getHeight() + " " + scene.getWidth());
            //draw map with image

            ImageView background = new ImageView(ImageGenerator.getInstance().getImage("background" + map.getMapNumber()));
            background.setFitWidth(scene.getWidth());background.setFitHeight(scene.getHeight());
            group.getChildren().addAll(background);

            for(int row = 0; row < map.getNumRows();row++){
                for (int col = 0; col < map.getNumColumns(); col++){
                    System.out.print(mapArray[row][col]);
                    if (mapArray[row][col] != null && ("+x".contains(mapArray[row][col]))) {
    //                    new Obstacle(environment, row*map.getColumnWidth(), col*map.getRowHeight(), map.getColumnWidth(), map.getHeight() );
                        ImageView imageView;
                        imageView = new ImageView(ImageGenerator.getInstance().getImage("obstacle" + map.getMapNumber() + random.nextInt(2)));
                        imageView.setFitHeight(map.getRowHeight());imageView.setFitWidth(map.getColumnWidth());
                        imageView.setX(col*map.getColumnWidth());imageView.setY(row*map.getRowHeight());
                        group.getChildren().addAll(imageView);
                    }
                }
                System.out.println();
            }

            // export image
            int height = (int) map.getHeight();
            int width = (int) map.getWidth();
            WritableImage snapShot = new WritableImage(height, width);
            scene.snapshot(snapShot);
            BufferedImage bImage = SwingFXUtils.fromFXImage(snapShot, null);
            File imageFile = new File("assets/img/map" + map.getMapNumber() + ".png");
            try {
                ImageIO.write(bImage, "png", imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        }
    }
}
