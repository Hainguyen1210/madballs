package madballs.multiplayer;

import madballs.scenes.SceneManager;

/**
 * the data of the information to be announced in the top right corner board
 * Created by caval on 03/09/2016.
 */
public class AnnouncementData extends Data {
    private Integer sourceID, targetID;
    private String action;
    private String announcementType;

    public AnnouncementData(Integer sourceID, Integer targetID, String action, String announcementType) {
        super("kill");
        this.sourceID = sourceID;
        this.targetID = targetID;
        this.action = action;
        this.announcementType = announcementType;
    }

    public void displayAnnouncement(){
        if (announcementType.equals("kill")){
            SceneManager.getInstance().announceKill(sourceID, targetID, action);
        }
        else if (announcementType.equals("flag")){
            System.out.println("flag " + targetID);
            SceneManager.getInstance().announceFlag(sourceID, targetID, action);
        }
    }
}
