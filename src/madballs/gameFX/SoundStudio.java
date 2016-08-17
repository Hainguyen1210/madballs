package madballs.gameFX;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

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
        for (String name : new String[]{"footstep2", "nutfall", "pistol", "awp", "penetrate", "die1", "speedUp", "plasma"}){
            AudioClip audioClip = new AudioClip(new File("assets/sound/" + name +".mp3").toURI().toString());
            audioClipMap.put(name, audioClip);
//            MediaHandler mediaHandler = new MediaHandler(new Media(new File("assets/sound/" + name +".mp3").toURI().toString()));
//            mediaHandlers.put(name, mediaHandler);
        }
    }

    public void playAudio(String audioName, double interval, String requester, long now){
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
            playAudio(audioName);
        }
    }

    public void playAudio(String audioName){
        audioClipMap.get(audioName).play();
    }

//    public MediaHandler getMediaHandler(String name){
//        return mediaHandlers.get(name);
//    }
}


