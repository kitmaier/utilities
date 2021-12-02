/*
https://stackoverflow.com/questions/3297749/java-reading-manipulating-and-writing-wav-files
*/

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/*
Feature wish list:
	Bending
		An example of a synth bending from one pitch to another
		Control over the speed and path of the bend
		Is there more than one way for the bending to sound?
		The ability to specify bending from one microtonal note to another, or to bend up or down by a given number of cents
		Doppler effect?
*/

public class GenerateWAV {
public static void main(String[] args) throws IOException {
		final double sampleRate = 44100.0;
		final double seconds = 25.0;
		
		float[] buffer = new float[(int)(seconds * sampleRate)];
		
		for (int sample = 0; sample < buffer.length; sample++) {
			double time = sample / sampleRate;
			buffer[sample] = (float)getValue(time,seconds);
		}
		
		final byte[] byteBuffer = new byte[buffer.length * 2];
		
		int bufferIndex = 0;
		for (int i = 0; i < byteBuffer.length; i++) {
			final int x = (int)(buffer[bufferIndex++] * 32767.0);
			
			byteBuffer[i++] = (byte)x;
			byteBuffer[i] = (byte)(x >>> 8);
		}
		
		File out = new File("out10.wav");
		
		final boolean bigEndian = false;
		final boolean signed = true;
		
		final int bits = 16;
		final int channels = 1;
		
		AudioFormat format = new AudioFormat((float)sampleRate, bits, channels, signed, bigEndian);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
		AudioInputStream audioInputStream = new AudioInputStream(bais, format, buffer.length);
		AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
		audioInputStream.close();
	}
	public static int ticksPerSecond = 1;
	public static double getValue(double time, double seconds) {
		double total = 0;
		int tick = (int)(time*ticksPerSecond);
		double timeResidue = time-tick*1.0/ticksPerSecond;
		//total += getFrereJacques(tick, timeResidue);
		//total += getChromaticIntervals(tick, timeResidue);
		//total += getIpanemaChordsStretchNote(tick, timeResidue);
		//total += getIpanemaChordsGridNote(tick, timeResidue);
		//total += getBend(time);
		//total += getJumps(time);
		total += getJumpsAndBends(time);
		return total;
	}
	public static double getBend(double time) {
		// In order to bend from one note to another, we need to keep track of several related variables
		// The real time, the bent time, the pitch, the phase offset, and whether we are before/during/after the bend
		// The actual waveform W(t) = cos(2*pi*Z(t)) where Z(t) is the bent time value times the initial frequency of vibration
		// The perceived pitch, being the rate of oscillation of W(t), is the rate of change of Z(t) transposed from frequency space to pitch space, which is logarithmic
		//   P(t) = ln(Z'(t))
		// Z(t) is a piecewise function, with value F(t) during the bend, G(t) before it, and H(t) after it
		//   Z(t) = {F(t) when t1 <= t <= t2; G(t) when 0 <= t <= t1; H(t) when t2 <= t}
		// This function will need to be continuous and fairly smooth, to avoid discontinuities in W(t) and P(t)
		//   F(t1) = G(t1), F(t2) = H(t2), F'(t1) = G'(t1), F'(t2) = H'(t2)
		// G(t) and H(t) will both be linear functions, because a constant amount of bending amounts to a simple multiplier on the rate of flow of time
		//   G(t) = m*t+n, H(t) = j*t+k
		// Since we may as well begin at the origin, G(0) = 0 implies that n = 0
		// We can select the initial and final pitches of the bend
		//   p1 = P(t1) = ln(Z'(t1)), p2 = P(t2) = ln(Z'(t2))
		// We can solve for the constants in G(t)
		//   G'(t) = m
		//   p1 = P(t1) = ln(G'(t1)) = ln(m)
		//   m = exp(p1)
		// In order for F(t) to represent a linear pitch bend, P(t) must be a linear function, making F'(t) and F(t) exponential
		//   P(t) = ln(F'(t)) = linear
		//   F'(t) = exponential
		//   F(t) = a*exp(b*t)+c
		//   F'(t) = a*b*exp(b*t)
		//   P(t) = ln(a)+ln(b)+b*t
		// Now we can solve for the constants in F(t)
		//   ln(a)+ln(b)+b*t1 = p1
		//   ln(a)+ln(b)+b*t2 = p2
		//   b = (p1-p2)/(t1-t2)
		//   a = exp(p1-ln(b)-b*t1)
		//   F(t1) = a*exp(b*t1)+c = G(t1) = m*t1
		//   c = m*t1-a*exp(b*t1)
		// Now we can solve for the constants in H(t)
		//   H'(t2) = j
		//   p2 = P(t2) = ln(H'(t2)) = ln(j)
		//   j = exp(p2)
		//   F(t2) = a*exp(b*t2)+c = H(t2) = j*t2+k
		//   k = a*exp(b*t2)+c-j*t2
		double p1 = Math.log(200); // pitch representation for 400hz frequency
		double p2 = Math.log(400); // pitch representation for 800hz frequency
		double t1 = 4; // start bend after 4 seconds
		double t2 = 14; // continue bend for 10 seconds
		double m = Math.exp(p1);
		double b = (p1-p2)/(t1-t2);
		double a = Math.exp(p1-Math.log(b)-b*t1);
		double c = m*t1-a*Math.exp(b*t1);
		double j = Math.exp(p2);
		double k = a*Math.exp(b*t2)+c-j*t2;
		double z = m*time; // default value if something goes wrong
		if(time<=t1) {
			z = m*time; // G(t)
		} else if(time<=t2) {
			z = a*Math.exp(b*time)+c; // F(t)
		} else {
			z = j*time+k; // H(t)
		}
		//return getFundamental(1, 1000000, z);
		return getNote(1, 1000000, z);
	}
	public static List<Double> pair(double a, double b) {
		List<Double> newPair = new ArrayList<>();
		newPair.add(a);
		newPair.add(b);
		return newPair;
	}
	public static List<Double> triad(double a, double b, double c) {
		List<Double> newTriad = new ArrayList<>();
		newTriad.add(a);
		newTriad.add(b);
		newTriad.add(c);
		return newTriad;
	}
	public static double getJumps(double time) {
		List<List<Double>> jumps = new ArrayList<>();
		// tonic: 400
		// minor third: 480
		// major third: 500
		// fourth: 533
		// tritone: 560
		// fifth: 600
		jumps.add(pair(0,533));
		jumps.add(pair(1,560));
		jumps.add(pair(2,600));
		jumps.add(pair(3,533));
		jumps.add(pair(4,480));
		jumps.add(pair(5,533));
		jumps.add(pair(6,480));
		jumps.add(pair(7,400));
		List<List<Double>> params = new ArrayList<>();
		double lastA = 0;
		double lastB = 0;
		for(List<Double> jumpPair : jumps) {
			double A = jumpPair.get(1);
			double B = (lastA-A)*jumpPair.get(0)+lastB;
			params.add(pair(A,B));
			lastA = A;
			lastB = B;
		}
		double z = 0;
		for(int k=0; k<jumps.size(); k++) {
			if(time>=jumps.get(k).get(0)) {
				z = params.get(k).get(0)*time+params.get(k).get(1);
			}
		}
		//return getFundamental(1, 1000000, z);
		return getNote(1, 1000000, z);
	}
	public static class JumpsAndBendsHelper {
		double startTime = 0;
		double duration = 0;
		double endTime = 0;
		double startFrequency = 0;
		double endFrequency = 0;
		String rule = "jump";
		List<Double> params = new ArrayList<>();
		public JumpsAndBendsHelper(double duration, double frequency, String rule) {
			this.duration = duration;
			this.startFrequency = frequency;
			this.endFrequency = frequency;
			this.rule = rule;
		}
		public JumpsAndBendsHelper(double duration, double startFrequency, double endFrequency, String rule) {
			this.duration = duration;
			this.startFrequency = startFrequency;
			this.endFrequency = endFrequency;
			this.rule = rule;
		}
	}
	public static List<JumpsAndBendsHelper> jumps = null;
	public static void fillJumpsList_FrereJacques(List<JumpsAndBendsHelper> jumps) {
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,165,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,165,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
	}
	public static void fillJumpsList1(List<JumpsAndBendsHelper> jumps) {
		jumps.add(new JumpsAndBendsHelper(0.25,208,"jump")); // Ab
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(1,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(1,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,208,"jump")); // Ab
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(1,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,208,"jump")); // Ab
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(1,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(1,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,208,"jump")); // Ab
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,208,"jump")); // Ab
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
	}
	public static void fillJumpsList_vibrato(List<JumpsAndBendsHelper> jumps, double cycleTime, double cycles, double bottomFrequency, double topFrequency) {
		for(int k=0; k<cycles; k++) {
			jumps.add(new JumpsAndBendsHelper(cycleTime/2,bottomFrequency,topFrequency,"bend"));
			jumps.add(new JumpsAndBendsHelper(cycleTime/2,topFrequency,bottomFrequency,"bend"));
		}
	}
	public static void fillJumpsList2(List<JumpsAndBendsHelper> jumps) {
		// https://www.guitarlessonworld.com/licks-riffs-tricks/a-minor-blues-lick/
		// straight
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.5,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		// with swing
		jumps.add(new JumpsAndBendsHelper(0.33,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.125,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.125,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.125,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.125,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.33,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.67,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		// with swing and bends
		jumps.add(new JumpsAndBendsHelper(0.33,293,314,"bend")); // D bend
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,293,314,"bend")); // D bend
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,293,314,"bend")); // D bend
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,293,314,"bend")); // D bend
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.125,293,329,"bend")); // D bend
		jumps.add(new JumpsAndBendsHelper(0.125,329,293,"bend")); // E bend
		jumps.add(new JumpsAndBendsHelper(0.125,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.125,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.33,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.67,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
	}
	public static void fillJumpsList3(List<JumpsAndBendsHelper> jumps) {
		// https://www.guitarplayer.com/lessons/12-killer-blues-licks-you-must-know
		// straight
		jumps.add(new JumpsAndBendsHelper(0.25,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.25,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,311,"jump")); // Eb
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.25,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		// with swing
		jumps.add(new JumpsAndBendsHelper(0.33,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.67,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,311,"jump")); // Eb
		jumps.add(new JumpsAndBendsHelper(0.33,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.17,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.33,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.17,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		// with swing and bends
		jumps.add(new JumpsAndBendsHelper(0.33,523,540,"bend")); // C bend
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,523,540,"bend")); // C bend
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,523,540,"bend")); // C bend
		jumps.add(new JumpsAndBendsHelper(0.17,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,523,540,"bend")); // C bend
		jumps.add(new JumpsAndBendsHelper(0.33,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.17,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,370,"bend")); // E bend
		jumps.add(new JumpsAndBendsHelper(0.67,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.17,311,"jump")); // Eb
		jumps.add(new JumpsAndBendsHelper(0.33,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.17,247,261,"bend")); // B bend
		jumps.add(new JumpsAndBendsHelper(0.33,261,247,"bend")); // C bend
		jumps.add(new JumpsAndBendsHelper(0.17,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		
		/*
		jumps.add(new JumpsAndBendsHelper(0.25,196,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.5,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.5,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.25,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.25,311,"jump")); // D#
		jumps.add(new JumpsAndBendsHelper(0.5,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.5,440,"jump")); // A
		*/
		/*
		jumps.add(new JumpsAndBendsHelper(1,196,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,235,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.7,235,247,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.4,247,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,235,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.7,235,247,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.4,247,"jump"));
		jumps.add(new JumpsAndBendsHelper(1,196,"jump"));
		*/
		/*
		fillJumpsList_vibrato(jumps,0.2,5,196,197); // G
		jumps.add(new JumpsAndBendsHelper(0.25,235,"jump")); // Bb+
		jumps.add(new JumpsAndBendsHelper(1,235,245,"bend")); // Bb+ -> Bb++
		jumps.add(new JumpsAndBendsHelper(0.5,245,"jump")); // Bb++
		jumps.add(new JumpsAndBendsHelper(0.25,235,"jump")); // Bb+
		jumps.add(new JumpsAndBendsHelper(1,235,245,"bend")); // Bb+ -> Bb++
		jumps.add(new JumpsAndBendsHelper(0.5,245,"jump")); // Bb++
		jumps.add(new JumpsAndBendsHelper(1,194,198,"bend")); // G
		fillJumpsList_vibrato(jumps,0.2,5,196,197); // G
		*/
		/*
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.5,293,311,"bend")); // D -> D#
		jumps.add(new JumpsAndBendsHelper(0.5,311,"jump")); // D#
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(1,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.5,523,540,"bend")); // C -> C+
		jumps.add(new JumpsAndBendsHelper(0.5,540,"jump")); // C+
		jumps.add(new JumpsAndBendsHelper(1,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		*/
		/*
		jumps.add(new JumpsAndBendsHelper(1,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(1,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(1,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(1,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(1,392,440,"bend")); // G -> A
		jumps.add(new JumpsAndBendsHelper(1,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
		*/
	}
	public static double getJumpsAndBends(double time) {
		if(jumps==null) {
			jumps = new ArrayList<>(); // change start/end time to duration
			//fillJumpsList1(jumps);
			//fillJumpsList2(jumps);
			fillJumpsList3(jumps);
			//fillJumpsList_FrereJacques(jumps);
			double lastEndTime = 0;
			double lastEndValue = 0;
			for(int k=0; k<jumps.size(); k++) {
				JumpsAndBendsHelper jump = jumps.get(k);
				jump.startTime = lastEndTime;
				jump.endTime = jump.startTime+jump.duration;
				double nextEndTime = jump.endTime;
				if(jump.rule.equals("jump")) {
					double A = jump.startFrequency;
					double B = lastEndValue-A*lastEndTime;
					jump.params = pair(A,B);
					lastEndValue = A*nextEndTime+B;
				} else { // bend
					double p1 = Math.log(jump.startFrequency);
					double p2 = Math.log(jump.endFrequency);
					double B = (p2-p1)/(nextEndTime-lastEndTime); // cannot have bend in final position
					double A = Math.exp(p1-B*lastEndTime)/B;
					double C = lastEndValue-A*Math.exp(B*lastEndTime);
					jump.params = triad(A,B,C);
					lastEndValue = A*Math.exp(B*nextEndTime)+C;
				}
				lastEndTime = nextEndTime;
				//System.out.println(jump.startTime+","+jump.duration+","+jump.startFrequency+","+jump.endFrequency+","+jump.rule+","+jump.params);
			}
		}
		double z = 0;
		if(time>jumps.get(jumps.size()-1).endTime) {
			// hold on final value to avoid discontinuity and to avoid going out of range
			JumpsAndBendsHelper jump = jumps.get(jumps.size()-1);
			if(jump.rule.equals("jump")) {
				z = jump.params.get(0)*jump.endTime+jump.params.get(1);
			} else { // bend
				z = jump.params.get(0)*Math.exp(jump.params.get(1)*jump.endTime)+jump.params.get(2);
			}
			return getNote(1, 1000000, z);
		}
		for(int k=jumps.size()-1; k>=0; k--) {
			JumpsAndBendsHelper jump = jumps.get(k);
			if(time>=jump.startTime) {
				if(jump.rule.equals("jump")) {
					z = jump.params.get(0)*time+jump.params.get(1);
				} else { // bend
					z = jump.params.get(0)*Math.exp(jump.params.get(1)*time)+jump.params.get(2);
				}
				break;
			}
		}
		//return getFundamental(1, 1000000, z);
		return getNote(1, 1000000, z);
	}
	public static double getChromaticIntervals(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		int[] noteList = getChromaticIntervals(tick);
		for(int k=0; k<noteList.length; k++) {
			//total += getEvenFundamental(noteList[k],1,timeResidue);
			total += getEvenNote(noteList[k],1,timeResidue);
			//total += getGridNote(noteList[k],1,timeResidue);
		}
		total *= 0.5;
		return total;
	}
	public static int[] getChromaticIntervals(int tick) {
		int[] noteList = {15};
		switch(tick) {
			case 0: noteList = new int[] {15,15}; break;
			case 1: noteList = new int[] {15,16}; break;
			case 2: noteList = new int[] {15,17}; break;
			case 3: noteList = new int[] {15,18}; break;
			case 4: noteList = new int[] {15,19}; break;
			case 5: noteList = new int[] {15,20}; break;
			case 6: noteList = new int[] {15,21}; break;
			case 7: noteList = new int[] {15,22}; break;
			case 8: noteList = new int[] {15,23}; break;
			case 9: noteList = new int[] {15,24}; break;
			case 10: noteList = new int[] {15,25}; break;
			case 11: noteList = new int[] {15,26}; break;
			case 12: noteList = new int[] {15,27}; break;
			case 13: noteList = new int[] {15,28}; break;
			case 14: noteList = new int[] {15,29}; break;
			case 15: noteList = new int[] {15,30}; break;
			case 16: noteList = new int[] {15,31}; break;
			case 17: noteList = new int[] {15,32}; break;
			case 18: noteList = new int[] {15,33}; break;
			case 19: noteList = new int[] {15,34}; break;
			case 20: noteList = new int[] {15,35}; break;
			case 21: noteList = new int[] {15,36}; break;
			case 22: noteList = new int[] {15,37}; break;
			case 23: noteList = new int[] {15,38}; break;
			case 24: noteList = new int[] {15,39}; break;
		}
		return noteList;
	}
	public static double getFrereJacques(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		switch(tick) {
			case 0: note = 15; break;
			case 1: note = 17; break;
			case 2: note = 19; break;
			case 3: note = 15; break;
			case 4: note = 15; break;
			case 5: note = 17; break;
			case 6: note = 19; break;
			case 7: note = 15; break;
			case 8: note = 19; break;
			case 9: note = 20; break;
			case 10: note = 22; break;
			case 11: note = 22; break;
			case 12: note = 19; break;
			case 13: note = 20; break;
			case 14: note = 22; break;
			case 15: note = 22; break;
			case 16: note = 22; break;
			case 17: note = 24; break;
			case 18: note = 22; break;
			case 19: note = 20; break;
			case 20: note = 19; break;
			case 21: note = 15; break;
			case 22: note = 15; break;
			case 23: note = 15; break;
		}
		//total += getEvenFundamental(note,1,timeResidue);
		//total += getEvenNote(note,1,timeResidue);
		//total += getGridNote(note,1,timeResidue);
		//total += getStretchNote(note,1,timeResidue);
		total += getJustNote(note,1,timeResidue);
		//total *= 0.5;
		return total;
	}
	public static double getLocrianFrereJacques(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		switch(tick) {
			case 0: note = 15; break;
			case 1: note = 16; break;
			case 2: note = 18; break;
			case 3: note = 15; break;
			case 4: note = 15; break;
			case 5: note = 16; break;
			case 6: note = 18; break;
			case 7: note = 15; break;
			case 8: note = 18; break;
			case 9: note = 20; break;
			case 10: note = 21; break;
			case 11: note = 21; break;
			case 12: note = 18; break;
			case 13: note = 20; break;
			case 14: note = 21; break;
			case 15: note = 21; break;
			case 16: note = 21; break;
			case 17: note = 23; break;
			case 18: note = 21; break;
			case 19: note = 20; break;
			case 20: note = 18; break;
			case 21: note = 15; break;
			case 22: note = 15; break;
			case 23: note = 15; break;
		}
		//total += getEvenFundamental(note,1,timeResidue);
		total += getEvenNote(note,1,timeResidue);
		//total += getJustNote(note,1,timeResidue);
		//total *= 0.5;
		return total;
	}
	// 15 C
	// 16 C#
	// 17 D
	// 18 D#
	// 19 E
	// 20 F
	// 21 F#
	// 22 G
	// 23 G#
	// 24 A
	// 25 A#
	// 26 B
	// 27 C
	public static double getIpanemaChordsEvenFundamental(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		int[] noteList = getIpanemaChords(tick);
		for(int k=0; k<noteList.length; k++) {
			total += getEvenFundamental(noteList[k],1,timeResidue);
		}
		total *= 0.5;
		return total;
	}
	public static double getIpanemaChordsEvenNote(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		int[] noteList = getIpanemaChords(tick);
		for(int k=0; k<noteList.length; k++) {
			total += getEvenNote(noteList[k],1,timeResidue);
		}
		total *= 0.5;
		return total;
	}
	public static double getIpanemaChordsGridNote(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		int[] noteList = getIpanemaChords(tick);
		for(int k=0; k<noteList.length; k++) {
			total += getGridNote(noteList[k],1,timeResidue);
		}
		total *= 0.5;
		return total;
	}
	public static double getIpanemaChordsStretchNote(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		int[] noteList = getIpanemaChords(tick);
		for(int k=0; k<noteList.length; k++) {
			total += getStretchNote(noteList[k],1,timeResidue);
		}
		total *= 0.5;
		return total;
	}
	public static int[] getIpanemaChords(int tick) {
		int[] noteList = {20,20,20,20};
		int[] Fmaj7 = {20,24,27,31}; // Fmaj7 - F A C E
		int[] G7overF = {20,22,26,29}; // G7/F - F G B D
		int[] Gm7overF = {20,22,25,29}; // G-7/F - F G Bb D
		int[] Gb7overE = {19,21,25,28}; // Gb7/E - E Gb Bb Db
		switch(tick) {
			case 0: noteList = Fmaj7; break;
			case 1: noteList = Fmaj7; break;
			case 2: noteList = Fmaj7; break;
			case 3: noteList = Fmaj7; break;
			case 4: noteList = G7overF; break;
			case 5: noteList = G7overF; break;
			case 6: noteList = Gm7overF; break;
			case 7: noteList = Gm7overF; break;
			case 8: noteList = Gb7overE; break;
			case 9: noteList = Gb7overE; break;
			case 10: noteList = Fmaj7; break;
			case 11: noteList = Fmaj7; break;
			case 12: noteList = Fmaj7; break;
			case 13: noteList = Fmaj7; break;
			case 14: noteList = G7overF; break;
			case 15: noteList = Fmaj7; break;
		}
		return noteList;
	}
	public static double getMinorOdeToJoy(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		switch(tick) {
			case 0: note = 18; break;
			case 1: note = 18; break;
			case 2: note = 20; break;
			case 3: note = 22; break;
			case 4: note = 22; break;
			case 5: note = 20; break;
			case 6: note = 18; break;
			case 7: note = 17; break;
			case 8: note = 15; break;
			case 9: note = 15; break;
			case 10: note = 17; break;
			case 11: note = 18; break;
			case 12: note = 18; break;
			case 13: note = 17; break;
			case 14: note = 17; break;
		}
		total += getEvenFundamental(note,1,timeResidue);
		//total += getEvenNote(note,1,timeResidue);
		//total += getJustNote(note,1,timeResidue);
		//total *= 0.5;
		return total;
	}
	public static double getOdeToJoy(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		switch(tick) {
			case 0: note = 19; break;
			case 1: note = 19; break;
			case 2: note = 20; break;
			case 3: note = 22; break;
			case 4: note = 22; break;
			case 5: note = 20; break;
			case 6: note = 19; break;
			case 7: note = 17; break;
			case 8: note = 15; break;
			case 9: note = 15; break;
			case 10: note = 17; break;
			case 11: note = 19; break;
			case 12: note = 19; break;
			case 13: note = 17; break;
			case 14: note = 17; break;
		}
		total += getEvenFundamental(note,1,timeResidue);
		//total += getEvenNote(note,1,timeResidue);
		//total += getJustNote(note,1,timeResidue);
		//total *= 0.5;
		return total;
	}
	public static double getEvenFundamental(int note, int durationInTicks, double time) {
		double frequency = getEvenFrequency(note);
		return getFundamental(frequency, durationInTicks, time);
	}
	public static double getFundamental(double frequency, int durationInTicks, double time) {
		double durationInTime = durationInTicks*1.0/ticksPerSecond;
		double amplitude = 0.2;
		double cycle = 2*Math.PI*frequency;
		double envelope = 0;
		if(time<0.1) envelope = time*10;
		if(time>=0.1&&time<durationInTime) envelope = 1;
		if(durationInTime-time<0.1) envelope = (durationInTime-time)*10;
		return Math.cos(cycle*time)*envelope*amplitude;
	}
	public static double getJustNote(int note, int durationInTicks, double time) {
		double frequency = getJustFrequency(note);
		return getNote(frequency, durationInTicks, time);
	}
	public static double getJustFrequency(int note) {
		// https://en.wikipedia.org/wiki/Just_intonation
		// C4 = 15
		// TODO: handle cases below C3
		note -= 3;
		if(note<0) note = 0;
		double frequency = 130.81;
		while(note>=12) {
			frequency *= 2;
			note -= 12;
		}
		switch(note) {
			case 0: frequency *= 1/1.0; break; // C
			case 1: frequency *= 16/15.0; break; // Db
			case 2: frequency *= 9/8.0; break; // D
			case 3: frequency *= 6/5.0; break; // Eb
			case 4: frequency *= 5/4.0; break; // E
			case 5: frequency *= 4/3.0; break; // F
			case 6: frequency *= 45/32.0; break; // F#
			case 7: frequency *= 3/2.0; break; // G
			case 8: frequency *= 8/5.0; break; // Ab
			case 9: frequency *= 5/3.0; break; // A
			case 10: frequency *= 9/5.0; break; // Bb
			case 11: frequency *= 15/8.0; break; // B
		}
		return frequency;
	}
	public static double getEvenFrequency(int note) {
		// C4 = 15
		return 110*Math.pow(2,note/12.0);
	}
	public static double getEvenNote(int note, int durationInTicks, double time) {
		double frequency = getEvenFrequency(note);
		return getNote(frequency, durationInTicks, time);
	}
	public static double getGridNote(int note, int durationInTicks, double time) {
		double frequency = getEvenFrequency(note);
		return getGridNote(frequency, durationInTicks, time);
	}
	public static double stretchAmount = 0.94;
	public static double getStretchFrequency(int note) {
		return 110*Math.pow(2*stretchAmount,note/12.0);
	}
	public static double getStretchNote(int note, int durationInTicks, double time) {
		double frequency = getStretchFrequency(note);
		return getStretchNote(frequency, durationInTicks, time);
	}
	public static double getNote(double frequency, int durationInTicks, double time) {
		// TODO: duration not working correctly except for duration==1 case
		// TODO: work out constant-subjective-volume amplitudes across frequency spectrum for pure tones
		double durationInTime = durationInTicks*1.0/ticksPerSecond;
		double amplitude = 0.2;
		//amplitude = amplitude*amplitude*amplitude;
		double cycle = 2*Math.PI*frequency;
		double envelope = 0;
		if(time<0.1) envelope = time*10;
		if(time>=0.1&&time<durationInTime) envelope = 1;
		if(durationInTime-time<0.1) envelope = (durationInTime-time)*10;
		double total = 0;
		total += 100*Math.cos(1*cycle*time);
		total += 100*Math.cos(2*cycle*time);
		total += 10*Math.cos(3*cycle*time);
		total += 10*Math.cos(4*cycle*time);
		total += 1*Math.cos(5*cycle*time);
		total += 1*Math.cos(6*cycle*time);
		total += 1*Math.cos(7*cycle*time);
		total *= 1/223.0;
		return total*envelope*amplitude;
	}
	public static double getGridNote(double frequency, int durationInTicks, double time) {
		// TODO: duration not working correctly except for duration==1 case
		// TODO: work out constant-subjective-volume amplitudes across frequency spectrum for pure tones
		double durationInTime = durationInTicks*1.0/ticksPerSecond;
		double amplitude = 0.2;
		double cycle = 2*Math.PI*frequency;
		double envelope = 0;
		if(time<0.1) envelope = time*10;
		if(time>=0.1&&time<durationInTime) envelope = 1;
		if(durationInTime-time<0.1) envelope = (durationInTime-time)*10;
		double total = 0;
		total += 100*Math.cos(1*cycle*time);
		total += 100*Math.cos(2*cycle*time);
		total += 10*Math.cos(2*Math.pow(2,7/12.0)*cycle*time);
		total += 10*Math.cos(4*cycle*time);
		total += 1*Math.cos(4*Math.pow(2,4/12.0)*cycle*time);
		total += 1*Math.cos(4*Math.pow(2,7/12.0)*cycle*time);
		total += 1*Math.cos(4*Math.pow(2,10/12.0)*cycle*time);
		total *= 1/223.0;
		return total*envelope*amplitude;
	}
	public static double getStretchNote(double frequency, int durationInTicks, double time) {
		// TODO: duration not working correctly except for duration==1 case
		// TODO: work out constant-subjective-volume amplitudes across frequency spectrum for pure tones
		double durationInTime = durationInTicks*1.0/ticksPerSecond;
		double amplitude = 0.2;
		double cycle = 2*Math.PI*frequency;
		double envelope = 0;
		if(time<0.1) envelope = time*10;
		if(time>=0.1&&time<durationInTime) envelope = 1;
		if(durationInTime-time<0.1) envelope = (durationInTime-time)*10;
		double total = 0;
		double stretchOctave = 2*stretchAmount;
		total += 100*Math.cos(1*cycle*time);
		total += 100*Math.cos(stretchOctave*cycle*time);
		total += 10*Math.cos(Math.pow(stretchOctave,1+7/12.0)*cycle*time);
		total += 10*Math.cos(Math.pow(stretchOctave,2)*cycle*time);
		total += 1*Math.cos(Math.pow(stretchOctave,2+4/12.0)*cycle*time);
		total += 1*Math.cos(Math.pow(stretchOctave,2+7/12.0)*cycle*time);
		total += 1*Math.cos(Math.pow(stretchOctave,2+10/12.0)*cycle*time);
		total *= 1/223.0;
		return total*envelope*amplitude;
	}
}