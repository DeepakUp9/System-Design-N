package LLD.DesignPattern.structuralPattern.adapter.secondResource.basicImplementation.solution;

import java.util.InputMismatchException;


// Target Interface
interface MediaPlayer {
    void play(String type, String file);
}

// Adaptee Interface
interface AdvancedMediaPlayer {
    void playMP4(String file);
    void playVLC(String file);
}

// Concrete Adaptee for MP4
class MP4PlayerImpl implements AdvancedMediaPlayer {

    @Override
    public void playMP4(String file) {
        System.out.println("Playing MP4 file: " + file);
    }

    @Override
    public void playVLC(String file) {
        // Not supported
    }
}

// Concrete Adaptee for VLC
class VLCPlayerImpl implements AdvancedMediaPlayer {

    @Override
    public void playMP4(String file) {
        // Not supported
    }

    @Override
    public void playVLC(String file) {
        System.out.println("Playing VLC file: " + file);
    }
}

// Adapter Class
class MediaPlayerAdapter implements MediaPlayer {

    private AdvancedMediaPlayer advancedMediaPlayer;

    public MediaPlayerAdapter(AdvancedMediaPlayer advancedMediaPlayer) {
        this.advancedMediaPlayer = advancedMediaPlayer;
    }

    @Override
    public void play(String type, String file) {
        if (type.equalsIgnoreCase("MP4")) {
            advancedMediaPlayer.playMP4(file);
        } else if (type.equalsIgnoreCase("VLC")) {
            advancedMediaPlayer.playVLC(file);
        } else {
            throw new IllegalArgumentException("Unsupported format: " + type);
        }
    }
}

// Client Class
class AudioPlayer implements MediaPlayer {

    @Override
    public void play(String type, String file) {
        if (type.equalsIgnoreCase("MP3")) {
            System.out.println("Playing MP3 file: " + file);
        } else if (type.equalsIgnoreCase("MP4")) {
            MediaPlayer adapter = new MediaPlayerAdapter(new MP4PlayerImpl());
            adapter.play(type, file);
        } else if (type.equalsIgnoreCase("VLC")) {
            MediaPlayer adapter = new MediaPlayerAdapter(new VLCPlayerImpl());
            adapter.play(type, file);
        } else {
            throw new IllegalArgumentException("Unsupported format: " + type);
        }
    }
}

// Main Class
public class Solution {
    public static void main(String[] args) {
        AudioPlayer player = new AudioPlayer();

        player.play("MP3", "song1.mp3");
        player.play("MP4", "song2.mp4");
        player.play("VLC", "song3.vlc");
    }
}