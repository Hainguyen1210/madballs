package madballs.multiplayer;

import madballs.scenes.SceneManager;

/**
 * Created by caval on 03/09/2016.
 */
public class KillData extends Data {
    private Integer sourceID, targetID;
    private String weaponImageName;

    public KillData(Integer sourceID, Integer targetID, String weaponImageName) {
        super("kill");
        this.sourceID = sourceID;
        this.targetID = targetID;
        this.weaponImageName = weaponImageName;
    }

    public void displayKill(){
        SceneManager.getInstance().displayKill(sourceID, targetID, weaponImageName);
    }
}
