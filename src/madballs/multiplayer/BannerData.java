package madballs.multiplayer;

import madballs.scenes.SceneManager;

/**
 * the Data of the information to be displayed on the banner of the game Scene
 * Created by caval on 03/09/2016.
 */
public class BannerData extends Data {
    private String bannerText;
    private int duration;

    public BannerData(String bannerText, int duration) {
        super("banner");
        this.bannerText = bannerText;
        this.duration = duration;
    }

    public void displayBanner(){
        if (bannerText.equals("")){
            SceneManager.getInstance().displayCountdown(duration);
        }
        else {
            SceneManager.getInstance().displayBanner(bannerText, duration);
        }
    }
}
