package LLD.DesignPattern.structuralPattern.adapter.thirdResource.mediaplayerapp;

public class Solution {
   public static void main(String[] args) {
        // Existing system works with MP3 interface
        Mp3Player mp3Player = new BasicMp3Player();
        playMedia(mp3Player, "song.mp3");
        
        // New MP4 player needs to be adapted
        AdvancedMp4Player mp4Player = new AdvancedMp4Player();
        Mp3Player adaptedPlayer = new Mp4ToMp3Adapter(mp4Player);
        playMedia(adaptedPlayer, "video.mp3"); // Note: adapter handles conversion
    }
    
    public static void playMedia(Mp3Player player, String filename) {
        System.out.println("\n--- Playing media ---");
        player.playMp3(filename);
        System.out.println("Current time: " + player.getCurrentTime() + "s");
        player.stop();
    }
}
