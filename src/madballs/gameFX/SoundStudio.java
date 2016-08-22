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
//    private Map<String, Media> medias = new HashMap<>();
//    private Map<String, MediaHandler> mediaHandlers = new HashMap<>();

    private  SoundStudio(){
        loadSound();
    }

    public static SoundStudio getInstance(){
        return instance;
    }

    private void loadSound(){
        for (String name : new String[]{"footstep2", "nutfall", "pistol", "awp", "penetrate", "die1", "speedUp", "plasma", "uzi", "minigun", "shotgun", "explosion", "bazooka", "reload", "m4a1"}){
            AudioClip audioClip = new AudioClip(new File("assets/sound/" + name +".mp3").toURI().toString());
            audioClipMap.put(name, audioClip);
//            MediaHandler mediaHandler = new MediaHandler(new Media(new File("assets/sound/" + name +".mp3").toURI().toString()));
//            mediaHandlers.put(name, mediaHandler);
        }
    }

    public void playAudio(String audioName, double interval, String requester, long now, double x, double y, double varianceX, double varianceY){
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
            playAudio(audioName, x, y, varianceX, varianceY);
        }
    }

    public void playAudio(String audioName, double x, double y, double varianceX, double varianceY){
        if (!MadBalls.getMultiplayerHandler().getLocalPlayer().getRelevancy(x,y,varianceX,varianceY)) return;
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
                audioClipMap.get(audioName).play();
            }
        };
        soundPool.execute(soundPlay);
    }

//    public MediaHandler getMediaHandler(String name){
//        return mediaHandlers.get(name);
//    }
}


