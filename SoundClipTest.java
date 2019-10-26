import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

// https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
// https://stackoverflow.com/questions/7782721/java-raw-audio-output/7782749#7782749

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class SoundClipTest extends JFrame {
	public static void main(String[] args) throws Exception {
		new SoundClipTest();
	}
	// Constructor
	public SoundClipTest() throws Exception {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Test Sound Clip");
		this.setSize(300, 200);
		this.setVisible(true);
		// Open an audio input stream.
		//AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("file_example_WAV_1MG.wav"));
		AudioInputStream audioIn = createAudioInputStream();
		//AudioSystem.write(audioIn, AudioFileFormat.Type.WAVE, new File("test_out_2.wav"));
		// Get a sound clip resource.
		Clip clip = AudioSystem.getClip();
		// Open audio clip and load samples from the audio input stream.
		clip.open(audioIn);
		clip.start();
	}
	public static AudioInputStream createAudioInputStream() {
		float sampleRate = 8000;
		int totalFrames = 10*(int)sampleRate;
		// TODO: oddly, the sound does not loop well for less than around 5 or so, wavelengths
		// TODO: should end on a whole-number multiple of the wavelength to allow looping
		byte[] buf = new byte[totalFrames];
		AudioFormat af = new AudioFormat(
			sampleRate,
			8,  // sample size in bits
			1,  // channels
			true,  // signed
			true  // bigendian
		);
		int maxVol = 127;
		for(int i=0; i<totalFrames; i++){
			double time = i/sampleRate;
			buf[i]=getByteValue(time);
		}
		try {
			byte[] b = buf;
			AudioInputStream ais = new AudioInputStream(
				new ByteArrayInputStream(b),
				af,
				buf.length);
			return ais;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/** Provides the byte value for this point in the sinusoidal wave. */
	private static byte getByteValue(double time) {
		int maxVol = 127;
		//double[] harmonics = {1};
		//double[] harmonics = {1,1.2599,1.4983};
		//double[] harmonics = {1,1.02,1.25,1.27,1.5,1.52,1.85,1.87};
		//double[] harmonics = {4,6};
		//double[] harmonics = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		//double[] harmonics = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50};
		//double[] harmonics = {1,2,4,8,16,32};
		//double dvalue = generateHarmonics(angle,harmonics);
		//double dvalue = generatePulseTrain(angle);
		//double[] fseries = {1};
		//double[] fseries = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		//double[] fseries = {1/1.0,1/2.0,1/3.0,1/4.0,1/5.0,1/6.0,1/7.0,1/8.0,1/9.0,1/10.0,1/11.0,1/12.0,1/13.0,1/14.0,1/15.0,1/16.0,1/17.0,1/18.0,1/19.0,1/20.0};
		//double[] fseries = {1,1/1.5,1/1.8,1/2.0,1/2.2,1/2.3,1/2.5,1/2.8,1/3.0,1/3.1,1/3.2,1/3.3,1/3.4,1/3.5,1/3.6,1/3.8,1/4.0};
		//double[] fseries = {1,0.95,0.9,0.85,0.8,0.75,0.7,0.65,0.6,0.55,0.5,0.45,0.4,0.35,0.3,0.25,0.2,0.15,0.1,0.5};
		//double[] fseries = {1,1,1,1,0.9,0.9,0.9,0.9,0.8,0.8,0.8,0.8,0.7,0.7,0.7,0.7,0.6,0.6,0.6,0.6,0.5,0.5,0.5,0.5,0.4,0.4,0.4,0.4,0.3,0.3,0.3,0.3,0.2,0.2,0.2,0.2,0.1,0.1,0.1,0.1};
		//double[] fseries = {1,0.5,0.5,0.5,0.5,1,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.6,0.2,0.2,0.2,0.2,0.2,0.5,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		//double[] fseries = {0.1,0.2,0.3,0.4,0.5,0.6,1,0.5,0.4,0.5,0.7,0.4,0.3,0.2,0.2,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.2,0.3,0.4,0.5,0.4,0.3,0.2,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		//double[] fseries = {0.3,0.6,1,0.6,0.3,0.3,0.3,0.3,0.3,0.2,0.2,0.2,0.1,0.1,0.1,0.0,0.0,0.1,0.1,0.1,0.2,0.3,0.4,0.3,0.2,0.1,0.0,0.1,0.2,0.2,0.3,0.4,0.3,0.2,0.1,0.1,0.1,0.1,0.1,0.1};
		//double[] fseries = {0.3,0.6,2,4,2,0.6,0.3,0.1,0.1,0.1,0.3,0.6,0.9,2,0.9,0.6,0.3,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		double fundamental = 170;//183;
		//double[] fseries = {558, 97, 419, 629, 432, 360, 526, 225, 213, 120, 68, 68, 64, 27, 78, 56, 112, 65, 26, 21}; // ah
		//double fundamental = 170;//170;
		//double[] fseries = {500, 1389, 42, 91, 101, 224, 9, 26, 27, 18, 30, 19, 20, 16, 15, 8, 54, 19, 15}; // oo
		//double fundamental = 178; //178;
		//double[] fseries = {813, 2042, 617, 30, 12, 0, 162, 18, 6, 2, 2, 1, 5, 11, 0, 0, 0, 3}; // oo (audacity)
		//double fundamental = 170;//159; // 190
		//double[] fseries = {260, 197, 1311, 74, 221, 247, 32, 10, 7, 8, 45, 33, 99, 8, 6, 21, 26, 69, 214, 21}; // oh
		//double fundamental = 170;//159;//146;
		double[] fseries = {145, 100, 194, 262, 640, 515, 279, 198, 239, 311, 514, 1116, 1539, 575, 474, 309, 207, 182, 55}; // aa
		//double[] fseries = {258, 141, 211, 442, 420, 1918, 272, 61, 141, 54, 47, 20}; // criag_ah
		double dvalue = generateFromFourierSeries(time,fundamental,fseries);
		byte bvalue = (new Integer((int)Math.round(dvalue*maxVol))).byteValue();
		return bvalue;
	}
	public static double generateHarmonics(double angle, double[] harmonics) {
		double dvalue = 0;
		for(int k=0; k<harmonics.length; k++) {
			dvalue += Math.sin(harmonics[k]*angle);
		}
		dvalue = dvalue/(double)harmonics.length;
		return dvalue;
	}
	public static double generatePulseTrain(double angle) {
		double dvalue = 0;
		if(Math.sin(angle)>0.95) {
			dvalue = 1;
		}
		//dvalue = dvalue*0.5*(Math.sin(2*angle)+Math.sin(3*angle));
		return dvalue;
	}
	public static double generateFromFourierSeries(double time, double fundamental, double[] fseries) {
		double dvalue = 0;
		double norm = 0;
		for(int k=0; k<fseries.length; k++) {
			dvalue += fseries[k]*Math.cos(fundamental*(k+1)*time*2*Math.PI);
			norm += fseries[k];
		}
		dvalue = dvalue/norm;
		//dvalue = dvalue*Math.sin(angle*5);
		//if(angle<10) {
			//System.out.println(angle+","+dvalue);
		//}
		return dvalue;
	}
}