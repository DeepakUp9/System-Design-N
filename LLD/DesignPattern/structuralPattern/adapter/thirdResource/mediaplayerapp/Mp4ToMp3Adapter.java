package LLD.DesignPattern.structuralPattern.adapter.thirdResource.mediaplayerapp;

public class Mp4ToMp3Adapter implements Mp3Player {
    private AdvancedMp4Player mp4Player;
    
    public Mp4ToMp3Adapter(AdvancedMp4Player mp4Player) {
        this.mp4Player = mp4Player;
    }
    
    @Override
    public void playMp3(String filename) {
        // Convert MP3 filename to MP4 (just for demonstration)
        String mp4Filename = filename.replace(".mp3", ".mp4");
        mp4Player.playMp4(mp4Filename);
    }
    
    @Override
    public void stop() {
        mp4Player.halt(); // Map stop to halt
    }
    
    @Override
    public int getCurrentTime() {
        return mp4Player.getPlaybackPosition(); // Map to different method
    }
}