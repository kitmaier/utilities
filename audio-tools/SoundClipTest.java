import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.*;

// https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
// https://stackoverflow.com/questions/7782721/java-raw-audio-output/7782749#7782749

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class SoundClipTest extends JFrame {
	static Random rand = new Random();
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
	public static AudioInputStream createAudioInputStream() throws Exception {
		float sampleRate = 8000;
		int totalFrames = 10*(int)sampleRate;
		// TODO: oddly, the sound does not loop well for less than around 5 or so, wavelengths
		// TODO: should end on a whole-number multiple of the wavelength to allow looping
		byte[] buf = new byte[2*totalFrames];
		AudioFormat af = new AudioFormat(
			sampleRate,
			16,  // sample size in bits
			1,  // channels
			true,  // signed
			true  // bigendian
		);
		BufferedWriter out = new BufferedWriter(new FileWriter(new File("sound_clip_test_out.txt")));
		for(int i=0; i<2*totalFrames; i+=2){
			double time = i/(2.0*sampleRate);
			double[] envelope = {0,0.9,0.9,0};
			double[] envelopeTimes = {0,0.1,9.9,10};
			double envelopeMultiplier = 0;
			for(int k=0; k<envelope.length-1; k++) {
				if(time>=envelopeTimes[k]&&time<=envelopeTimes[k+1]) {
					double a = envelope[k];
					double b = envelope[k+1];
					double c = (time-envelopeTimes[k])/(envelopeTimes[k+1]-envelopeTimes[k]);
					envelopeMultiplier = b*c+a*(1-c);
				}
			}
			int x = (int)(envelopeMultiplier*getValue(time));
			out.write(i+"\t"+time+"\t"+envelopeMultiplier+"\t"+x+"\n");
			buf[i] = (byte)(x/256);
			buf[i+1] = (byte)(x%256);
		}
		out.close();
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
	private static double getValue(double time) {
		int maxVol = 127*256;
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
		//double fundamental = 170;//183;
		//double[] fseries = {558, 97, 419, 629, 432, 360, 526, 225, 213, 120, 68, 68, 64, 27, 78, 56, 112, 65, 26, 21}; // ah
		//double fundamental = 170;//170;
		//double[] fseries = {500, 1389, 42, 91, 101, 224, 9, 26, 27, 18, 30, 19, 20, 16, 15, 8, 54, 19, 15}; // oo
		//double fundamental = 178; //178;
		//double[] fseries = {813, 2042, 617, 30, 12, 0, 162, 18, 6, 2, 2, 1, 5, 11, 0, 0, 0, 3}; // oo (audacity)
		//double fundamental = 170;//159; // 190
		//double[] fseries = {260, 197, 1311, 74, 221, 247, 32, 10, 7, 8, 45, 33, 99, 8, 6, 21, 26, 69, 214, 21}; // oh
		//double fundamental = 146;
		//double[] fseries = {145, 100, 194, 262, 640, 515, 279, 198, 239, 311, 514, 1116, 1539, 575, 474, 309, 207, 182, 55}; // aa
		//double fundamental = 73;
		//double[] fseries = {145, 145, 100, 100, 194, 194, 262, 262, 640, 640, 515, 515, 279, 279, 198, 198, 239, 239, 311, 311, 514, 514, 1116, 1116, 1539, 1539, 575, 575, 474, 474, 309, 309, 207, 207, 182, 182, 55, 55}; // aa
		//double fundamental = 292;
		//double[] fseries = {145, 194, 640, 279, 239, 514, 1539, 474, 207, 55}; // aa
		//double fundamental = 170;//150;
		//double[] fseries = {3, 4, 6, 38, 7, 3, 0, 2, 2, 3, 3, 9, 13, 5, 3, 6, 4, 4, 0, 1, 0, 0}; // eh (audacity)
		//double fundamental = 170;//184;
		//double[] fseries = {15, 151, 6, 2, 0, 0, 1, 1, 1, 1, 6, 5, 2, 1, 4, 6, 1, 2, 5}; // ee (audacity)
		//double fundamental = 152;
		//double[] fseries = {2089, 1047, 89, 1, 4, 1, 2, 1, 6, 5, 18, 9, 4, 7, 3, 4, 25}; // nn (audacity)
		//double fundamental = 146;
		//double[] fseries = {78, 355, 2344, 89, 21, 525, 1000, 32, 5}; // rr (audacity)
		//double fundamental = 132;
		//double[] fseries = {24, 19, 12, 5, 1, 5, 4, 13, 5, 1, 1, 1, 1, 1, 1, 2, 4, 1, 2, 2, 3, 3, 6, 5, 2, 1}; // vv (audacity)
		//double fundamental = 135;
		//double[] fseries = {2, 9, 18, 56, 6, 2, 1, 2, 17, 3, 2, 1, 1, 1, 1, 1, 3, 0, 0, 0, 0, 1, 2, 5, 1}; // uh (audacity)
		//double fundamental = 159;
		//double[] fseries = {9, 59, 858, 12, 1, 0, 1, 3, 5, 5, 50, 14, 9, 7, 22, 14, 2, 1, 1, 4, 1}; // ih (audacity)
		//double fundamental = 98;
		//double[] fseries = {5541, 8343, 4425, 2205, 992, 351, 84, 112, 127, 145, 192, 315, 360, 281, 856, 252, 426, 388, 119, 76, 111, 79, 185, 442, 286, 120, 232, 226, 573, 236, 323, 153, 116, 30, 13, 73, 126, 486, 455, 293, 319, 271, 934, 1072, 1221, 351, 1138, 897, 325, 1238, 1966, 2157, 1087, 1648, 712, 1808, 4196, 1797, 2935, 820, 2446, 1770, 1431, 572, 1394, 1125, 900, 112, 760, 912, 725, 525, 233, 66, 24, 1, 24, 19, 21}; // zz
		//double dvalue = generateFromFourierSeries(time,fundamental,fseries);
		double dvalue1 = generateFromFormantList(time,260,"ah");
		double dvalue2 = generateFromFormantList(time,260,"ah");
		double dvalue = 0.5*(dvalue1+dvalue2);
		//double gauss = 0;//Math.max(-1,Math.min(1,rand.nextGaussian()/3));
		//dvalue = dvalue*0.9+gauss*0.1;
		return dvalue*maxVol;
	}
	public static double generateFromFormantList(double time, double fundamental, String vowelName) {
		double referenceFundamental = 150;
		double[] formantSeries = {20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
		if(vowelName=="aa") {
			referenceFundamental = 146;
			double[] tmp = {145, 100, 194, 262, 640, 515, 279, 198, 239, 311, 514, 1116, 1539, 575, 474, 309, 207, 182, 55};
			formantSeries = tmp;
		} else if(vowelName=="oh") {
			referenceFundamental = 159;
			double[] tmp = {260, 197, 1311, 74, 221, 247, 32, 10, 7, 8, 45, 33, 99, 8, 6, 21, 26, 69, 214, 21};
			formantSeries = tmp;
		} else if(vowelName=="uh") {
			referenceFundamental = 135;
			double[] tmp = {2, 9, 18, 56, 6, 2, 1, 2, 17, 3, 2, 1, 1, 1, 1, 1, 3, 0, 0, 0, 0, 1, 2, 5, 1};
			formantSeries = tmp;
		} else if(vowelName=="ih") {
			referenceFundamental = 159;
			double[] tmp = {9, 59, 858, 12, 1, 0, 1, 3, 5, 5, 50, 14, 9, 7, 22, 14, 2, 1, 1, 4, 1};
			formantSeries = tmp;
		} else if(vowelName=="ee") {
			referenceFundamental = 184;
			double[] tmp = {15, 151, 6, 2, 0, 0, 1, 1, 1, 1, 6, 5, 2, 1, 4, 6, 1, 2, 5};
			formantSeries = tmp;
		} else if(vowelName=="ah") {
			referenceFundamental = fundamental;
			//double[] tmp = {2000,4000,400,400,100,400,300,10,100,5,2,5,2}; // piano c4 from sample
			double[] tmp = {4000,4000,400,400,40,40,40};
			formantSeries = tmp;
		}
		//double[] fourierSeries = new double[(int)(formantSeries.length*referenceFundamental/fundamental)];
		//for(int k=0; k<fourierSeries.length; k++) {
		//	// TODO: this should not really be zero-indexed
		//	fourierSeries[k] = formantSeries[(int)(k*fundamental/referenceFundamental)];
		//	//System.out.println(k+","+fourierSeries[k]+","+fundamental*(k+1));
		//}
		
		fundamental = 260; // C4
		formantSeries = new double[]{4000,4000,400,400,40,40,40};
		//fundamental = 260; // C4
		//formantSeries = new double[]{6, 121, 4, 2, 4, 2, 1};
		//fundamental = 130; // C3
		//formantSeries = new double[]{10,10,10,10,10,10,10,0,1,1,1,1,1,1};
		//fundamental = 524; // C5
		//formantSeries = new double[]{1497,977,771,63,48,14,10,2};
		//fundamental = 250;
		//formantSeries = new double[]{1};
		double dvalue = 0;
		double norm = 0;
		for(int k=0; k<formantSeries.length; k++) {
			dvalue += formantSeries[k]*Math.cos(fundamental*(k+1)*time*2*Math.PI);
			norm += formantSeries[k];
		}
		dvalue = dvalue/norm;
		return dvalue;
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