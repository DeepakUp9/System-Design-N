package LLD.DesignPattern.structuralPattern.adapter.thirdResource.mediaplayerapp;

public class BasicMp3Player implements Mp3Player {
    @Override
    public void playMp3(String filename) {
        System.out.println("Playing MP3 file: " + filename);
    }

    @Override
    public void stop() {
        System.out.println("Stopping MP3 playback");
    }

    @Override
    public int getCurrentTime() {
        return (int)(Math.random() * 100); // Simulate current playback time
    }
}
