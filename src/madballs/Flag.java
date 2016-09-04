package madballs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import madballs.collision.*;
import madballs.map.SpawnLocation;
import madballs.moveBehaviour.StraightMove;
import madballs.scenes.SceneManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caval on 03/09/2016.
 */
public class Flag extends GameObject {
    private int teamNum;
    private SpawnLocation spawnLocation;
    private IntegerProperty carrierID = new SimpleIntegerProperty(-1);
    private final double WEIGHT = 2;
    private final double HEIGHT = 30, WIDTH = 2;

    public Integer getCarrierID() {
        return carrierID.get();
    }

    public void setCarrierID(Integer carrierID) {
        this.carrierID.set(carrierID);
    }

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }

    public int getTeamNum() {
        return teamNum;
    }

    public Flag(Environment environment, Integer id, SpawnLocation spawnLocation) {
        super(environment, spawnLocation.getX(), spawnLocation.getY(), true, id);
        this.teamNum = spawnLocation.getTypeNumber();
        this.spawnLocation = spawnLocation;

//        getHitBox().setFill(SceneManager.teamColors[teamNum - 1]);
        getHitBox().setOpacity(1);
        setImage("flag" + teamNum);
        configImageView(-12.5, 0, HEIGHT, 12.5);

        carrierID.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((Integer)newValue >= 0){
                    StraightMove carrierMovement = (StraightMove) getEnvironment().getObject((Integer)newValue).getMoveBehaviour();
                    carrierMovement.setSpeed(carrierMovement.getSpeed() - WEIGHT * 5);
                }
                else {
                    StraightMove carrierMovement = (StraightMove) getEnvironment().getObject((Integer)oldValue).getMoveBehaviour();
                    carrierMovement.setSpeed(carrierMovement.getSpeed() + WEIGHT * 5);

                }
            }
        });

        setCollisionEffect(new NullEffect(null));

        Map<StackedCollisionPassiveBehaviour, Callback> stackedCollisionPassiveBehaviours = new HashMap<>();
        stackedCollisionPassiveBehaviours.put(new BindingBehaviour(-WIDTH/2, -HEIGHT, false, null), new Callback() {
            @Override
            public boolean checkCondition(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
                boolean result = false;
                if (source instanceof Ball) {
                    result = ((Ball)source).getPlayer().getTeamNum() != ((Flag)target).getTeamNum();
                }
                return result;
            }
        });
        stackedCollisionPassiveBehaviours.put(new RecallBehaviour(spawnLocation, null), new Callback() {
            @Override
            public boolean checkCondition(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
                boolean result = false;
                if (source instanceof Ball) {
                    result = ((Ball)source).getPlayer().getTeamNum() == ((Flag)target).getTeamNum();
                }
                return result;
            }
        });
        setCollisionPassiveBehaviour(new ComboCollisionPassiveBehaviour(stackedCollisionPassiveBehaviours, new InvulnerableBehaviour(null)));
    }

    @Override
    public void setDisplayComponents() {
        setHitBox(new Rectangle(WIDTH, HEIGHT));
    }

    @Override
    public void updateUnique(long now) {

    }

    @Override
    public void die(){
        carrierID.set(-1);
        getTranslateXProperty().unbind();
        getTranslateYProperty().unbind();
        getRotate().angleProperty().unbind();
    }
}
