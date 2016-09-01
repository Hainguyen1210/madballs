/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.InvulnerableBehaviour;
import madballs.collision.PushBackEffect;

/**
 *
 * @author Caval
 */
public class Obstacle extends GameObject{
    public double length;
    public double height;

    public Obstacle(Environment environment, double x, double y, double length, double height, Integer id) {
        super(environment, x, y, false, id);
        setMobile(false);
        this.length = length;
        this.height = height;
        setDisplay(id);
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
//        Rectangle rect = new Rectangle(49, 49, Paint.valueOf("green"));
//        rect.setArcHeight(15);
//        rect.setArcWidth(15);
//        setHitBox(rect);
        setHitBox(new Rectangle(length, height, Paint.valueOf("green")));
        getHitBox().setOpacity(0);

//        double boxSize;if (length<height)boxSize=length;else boxSize=height;
//        Image background = ImageGenerator.getInstance().getImageView("obstacle/stripebox");
//        backgroundPane = new Pane();
//        backgroundPane.setBackground(
//            new Background(
//                new BackgroundImage(
//                    background,
//                    BackgroundRepeat.REPEAT,
//                    BackgroundRepeat.REPEAT,
//                    BackgroundPosition.DEFAULT,
//                    BackgroundSize.DEFAULT
//                )
//            )
//        );
//        backgroundPane.setPrefSize(length, height);
//        getAnimationG().getChildren().addAll(backgroundPane);
//        setImage(background);
//        getImageView().setFitWidth(boxSize);getImageView().setFitHeight(boxSize);
    }

    @Override
    public void updateUnique(long now) {

    }

}
