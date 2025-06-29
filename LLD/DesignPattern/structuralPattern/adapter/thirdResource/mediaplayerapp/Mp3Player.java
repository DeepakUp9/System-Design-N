package LLD.DesignPattern.structuralPattern.adapter.thirdResource.mediaplayerapp;

public interface Mp3Player {
    void playMp3(String filename);
    void stop();
    int getCurrentTime();
}