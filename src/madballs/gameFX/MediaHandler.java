package madballs.gameFX;

import javafx.scene.media.MediaPlayer;

/**
 * Created by caval on 16/08/2016.
 */
public class MediaHandler {
    private MediaPlayer mediaPlayer;
    private long lastPlayTime = 0;

    public MediaHandler(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
}
