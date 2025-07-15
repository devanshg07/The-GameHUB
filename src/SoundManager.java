/*
 * SoundManager.java
 *
 * Manages sound effects and audio playback for the GameHub application.
 */

import javax.sound.sampled.*; // Import sound
import java.io.File; // Import file reading

class SoundManager {

    private Clip backgroundMusicClip;//variable for the wav file

    //method to create a sound similar to a tap
    public void makeNoise() {//start method
        playSound("balloon-bop-medium-228519.wav");//use method with this file
    }//end method

    //method to play a laughing sound
    public void playSuccessSound() {//stat method
        playSound("heheheha.wav");//use method with this file
    }//end method

    //method to play wwomp womp 
    public void playFailureSound() {//start method
        playSound("downer_noise.wav");//use method with that file
    }//end method

    //method to play background music in a loop
    public void playBackgroundMusic(String fileName) {//start method
        //use try catch to handle exceptions
        try {//start try
            //load file
            File file = new File(fileName);//file object
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);//use audiostream
            backgroundMusicClip = AudioSystem.getClip();//get clip from audiosteam
            backgroundMusicClip.open(audioStream);//open the variable
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);//play it conitinously
            backgroundMusicClip.start();//stART IT
        }//end try
         catch (Exception e) {//start catch
            System.out.println( e.getMessage());//show problem
        }//end catcg
    }//end method

    //method to stop the music
    public void stopMusic() {//start method
        //if the msuic isnt nukll, and runnign
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {//start if
            backgroundMusicClip.stop();//stop music
            backgroundMusicClip.close();//close clip
        }//end if
    }//end method

    //play a sound once method
    private void playSound(String fileName) {//start method
        //try catch to handle exception
        try {//start try
            File file = new File(fileName);//use file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);//get audiostream of the file
            Clip clip = AudioSystem.getClip();//convert to a clip
            clip.open(audioStream);//open sound
            clip.start();//start sound
            clip.addLineListener(event -> {
                //if music is done close it
                if (event.getType() == LineEvent.Type.STOP) {//start if
                    clip.close();//close
                }//end if
            });
        }//end try
        catch (Exception e) {//start catch
            System.out.println(e.getMessage());//show error
        }//end catch
    }//end method
}//end class
