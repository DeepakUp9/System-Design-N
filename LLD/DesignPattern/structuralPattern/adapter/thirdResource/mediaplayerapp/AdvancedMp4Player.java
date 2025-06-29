package LLD.DesignPattern.structuralPattern.adapter.thirdResource.mediaplayerapp;

public class AdvancedMp4Player {
    public void playMp4(String filename) {
        System.out.println("Playing MP4 file: " + filename);
    }
    
    public void halt() {
        System.out.println("MP4 playback halted");
    }
    
    public int getPlaybackPosition() {
        return (int)(Math.random() * 120); // Different method name and range
    }
}