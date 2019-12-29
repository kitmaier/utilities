import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

// https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
// https://stackoverflow.com/questions/7782721/java-raw-audio-output/7782749#7782749

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class PlayClip extends JFrame {
	public static void main(String[] args) throws Exception {
		new PlayClip(args[0]);
	}
	// Constructor
	public PlayClip(String filename) throws Exception {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Test Sound Clip");
		this.setSize(300, 200);
		this.setVisible(true);
		// Open an audio input stream.
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("audio/"+filename+".wav"));
		// Get a sound clip resource.
		Clip clip = AudioSystem.getClip();
		// Open audio clip and load samples from the audio input stream.
		clip.open(audioIn);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}