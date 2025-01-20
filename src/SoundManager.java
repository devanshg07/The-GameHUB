import javax.sound.sampled.*; // Import sound
import java.io.File; // Import file reading

class SoundManager {

    private Clip backgroundMusicClip; // For background music

    // Play a subtle tap sound
    public void makeNoise() {
        playSound("balloon-bop-medium-228519.wav");
    }

    // Play bounce sound (Placeholder for now)
    public void playBounceSound() {
        System.out.println("Bounce sound played");
    }

    // Play a laughing sound
    public void playSuccessSound() {
        playSound("heheheha.wav");
    }

    // Play a sad sound
    public void playFailureSound() {
        playSound("downer_noise.wav");
    }

    // Play background music in a loop
    public void playBackgroundMusic(String fileName) {
        try {
            // Load the background music file
            File file = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the background music
            backgroundMusicClip.start();
        } catch (Exception e) {
            System.out.println("Error playing background music: " + e.getMessage());
        }
    }

    // Stop the background music
    public void stopMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();  // Stop the music
            backgroundMusicClip.close(); // Close the clip
        }
    }

    public void stopAllSounds() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }
    // Play a one-time sound
    private void playSound(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close(); // Close clip once the sound finishes
                }
            });
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
}
