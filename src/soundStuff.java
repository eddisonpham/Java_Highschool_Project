import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Sound Player For Programs
public class soundStuff {
	//this is called with the name of the .wav sound as the parameter
	public void playSound(String soundLocation) {
		try {
			File soundPath = new File(soundLocation);//sound file path
			if (soundPath.exists()) {//if it exists, play it
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);//open the audio input
				clip.setMicrosecondPosition(0);
				clip.start();//play it
				clip.loop(0);//loop 0 so that it plays once
			}else {
				System.out.println("sound doesn't exist");
			}
		}catch(Exception ex) {//raise error
			ex.printStackTrace();
		}
	}
}
