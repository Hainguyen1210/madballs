package madballs.gameFX;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caval on 15/08/2016.
 */
public class SoundStudio {
    private static SoundStudio instance = new SoundStudio();
    private Map<String, MediaHandler> mediaHandlers = new HashMap<>();

    private  SoundStudio(){
        loadSound();
    }

    public static SoundStudio getInstance(){
        return instance;
    }

    private void loadSound(){
        for (String name : new String[]{"footstep2", "nutfall","Ak47","Uzi","M4A1", "pistol", "awp", "penetrate", "die1", "speedUp", "plasma"}){
            MediaHandler mediaHandler = new MediaHandler(new MediaPlayer(new Media(new File("assets/sound/" + name +".mp3").toURI().toString())));
            mediaHandlers.put(name, mediaHandler);
        }
    }

    public void playSound(String name, long now, double interval){
        MediaHandler mediaHandler = mediaHandlers.get(name);
        if (interval < 0){
            if (mediaHandler.getMediaPlayer().getCurrentTime() == Duration.millis(0)) {
                mediaHandler.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);
                mediaHandler.getMediaPlayer().play();
            }
        }
        else if ((now - mediaHandler.getLastPlayTime()) / 1000000000 >= interval){
            mediaHandler.setLastPlayTime(now);
            mediaHandler.getMediaPlayer().seek(Duration.millis(0));
            mediaHandler.getMediaPlayer().play();
        }
    }

    public void endSoundRepeat(String name){
        MediaPlayer mediaPlayer = mediaHandlers.get(name).getMediaPlayer();
        mediaPlayer.setCycleCount(1);
    }

    public MediaHandler getMediaHandler(String name){
        return mediaHandlers.get(name);
    }
}


