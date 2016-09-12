package madballs.gameFX;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import madballs.MadBalls;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by caval on 15/08/2016.
 */
public class SoundStudio {
    private static SoundStudio instance = new SoundStudio();
    ExecutorService soundPool = Executors.newFixedThreadPool(2);
    private Map<String, AudioClip> audioClipMap = new HashMap<>();
    private Map<String, Map<String, Long>> audioTimerMap = new HashMap<>();

    private  SoundStudio(){
        loadSound();
    }

    public static SoundStudio getInstance(){
        return instance;
    }

    /**
     * load the sound files into the audioClipMap
     */
    private void loadSound(){
        for (String name : new String[]{"footstep2", "nutfall", "pistol", "awp", "penetrate", "die1", "speedUp", "plasma", "uzi", "minigun", "shotgun", "explosion", "bazooka", "reload", "m4a1", "ak47"}){
            AudioClip audioClip = new AudioClip(new File("assets/sound/" + name +".mp3").toURI().toString());
            audioClipMap.put(name, audioClip);
        }
    }

    /**
     * play an audio with a certain interval
     * @param audioName name of the audio
     * @param interval interval to play the audio
     * @param requester the object that request to play the audio
     * @param now the timestamp of the request
     * @param x the coordinates of the requester
     * @param y
     * @param varianceX the horizontal distance the audio can be heard
     * @param varianceY the vertical distance the audio can be heard
     */
    public void playAudio(String audioName, double interval, String requester, long now, double x, double y, double varianceX, double varianceY){
        // update the timer of the request to play the audio
        if (!audioTimerMap.containsKey(requester)){
            audioTimerMap.put(requester, new HashMap<>());
        }
        Map<String, Long> timer = audioTimerMap.get(requester);
        if (!timer.containsKey(audioName)){
            timer.put(audioName, now);
        }
        Long lastPlayTime = timer.get(audioName);
        if ((now - lastPlayTime) / 1000000000 > interval || now == lastPlayTime){
            timer.replace(audioName, now);
            // if the amount of time since the last play time satisfies the interval
            playAudio(audioName, x, y, varianceX, varianceY);
        }
    }

    /**
     * play an audio
     * @param audioName name of the audio
     * @param x coordinates of the source of the sound
     * @param y
     * @param varianceX the horizontal distance the audio can be heard
     * @param varianceY the vertical distance the audio can be heard
     */
    public void playAudio(String audioName, double x, double y, double varianceX, double varianceY){
        // check if the local player can hear the sound
        if (!MadBalls.getMultiplayerHandler().getLocalPlayer().getRelevancy(x,y,varianceX,varianceY)) return;
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
                audioClipMap.get(audioName).play();
            }
        };
        soundPool.execute(soundPlay);
    }
}


