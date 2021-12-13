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
		TODO: implement non-linear bend (x^1.6)
		TODO: implement percussive effects (tick, drum, cymbal) and guitar pick attack pattern (how???)
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
		//total += getJumpsAndBends(time);
		//total += getDrone(time);
		//for(int k=0; k<24; k++) {
		//	double t = k*1;
		//	total += getBasicGuitarChord("C",t,t+1,time);
		//}
		total += getBell(200,0,1,time);
		total += getBell(400,1,2,time);
		total += getBell(300,2,3,time);
		total += getBell(220,3,4,time);
		total += getBell(233,4,5,time);
		total += getBell(300,5,6,time);
		total += getBell(200,6,10,time);
		total += getBell(400,6,10,time);
		return total;
	}
	public static double getBell(double frequency, double start, double end, double time) {
		// http://acoustics.ae.illinois.edu/pdfs/Vibration%20of%20Plates%20(Leissa,%20NASA%20SP-160).pdf
		if(time<=start || time>=end) return 0;
		//setupStandardNoteFrequencies();
		double durationInTime = end-start;
		double timeInNote = time-start;
		double amplitude = 0.3;
		//double frequency = standardNoteFrequency.get(note);
		//double frequency = 250;
		double cycle = 2*Math.PI*frequency;
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		double decay = Math.pow(0.5,timeInNote);
		double decay2 = decay*decay;
		double decay3 = decay2*decay;
		double total = 0;
		total += 100*Math.sin(1*cycle*timeInNote)*decay;
		total += 100*Math.sin(1.70*cycle*timeInNote)*decay2;
		total += 10*Math.sin(2.29*cycle*timeInNote)*decay2;
		total += 10*Math.sin(3.99*cycle*timeInNote)*decay3;
		total += 1*Math.sin(4.10*cycle*timeInNote)*decay3;
		total += 1*Math.sin(6.19*cycle*timeInNote)*decay3;
		total += 1*Math.sin(6.79*cycle*timeInNote)*decay3;
		total += 1*Math.sin(7.51*cycle*timeInNote)*decay3;
		total += 1*Math.sin(8.80*cycle*timeInNote)*decay3;
		total *= 1/224.0;
		return total*envelope*amplitude;
	}
	public static double getBasicGuitarChord(String name, double start, double end, double time) {
		// TODO: this runs pretty slow
		if(time<start || time>end) return 0;
		double value = 0;
		//double offset = 0.03;
		double offset = 0;
		setupStandardGuitarChords();
		String chord = standardGuitarChord.get(name);
		String[] notes = chord.split(",");
		for(int k=0; k<notes.length; k++) {
			value += getStandardNote(notes[k],start+offset*k,end,time);
			//value += getStandardDecayingNote(notes[k],0.5,start+offset*k,end,time);
			//value += getStandardDecayingNote2(notes[k],start+offset*k,end,time);
		}
		return 0.5*value;
	}
	public static Map<String,String> standardGuitarChord = null;
	public static void setupStandardGuitarChords() {
		// Bass notes:
		// E1 41.20
		// A1 55.00
		// D2 73.42
		// G2 98.00
		// E1,A1,D2,G2
		// Guitar notes:
		// E2 82.41
		// A2 110.00
		// D3 146.83
		// G3 196.00
		// B3 246.94
		// E4 329.63
		// 2E,2A,3D,3G,3B,4E
		if(standardGuitarChord!=null) return;
		standardGuitarChord = new HashMap<>();
		standardGuitarChord.put("C","3C,3E,3G,4C,4E");
		standardGuitarChord.put("Dm","3D,3A,4D,4F");
		standardGuitarChord.put("Em","2E,2B,3E,3G,3B,4E");
		standardGuitarChord.put("F","2F,3C,3F,3A,4C,4F");
		standardGuitarChord.put("G","2G,2B,3D,3G,3B,4G");
		standardGuitarChord.put("Am","2A,3E,3A,4C,4E");
	}
	public static double getStandardNote(String note, double start, double end, double time) {
		if(time<=start || time>=end) return 0;
		setupStandardNoteFrequencies();
		double durationInTime = end-start;
		double timeInNote = time-start;
		double amplitude = 0.2;
		double frequency = standardNoteFrequency.get(note);
		double cycle = 2*Math.PI*frequency;
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		double total = 0;
		total += 100*Math.sin(1*cycle*timeInNote);
		total += 100*Math.sin(2*cycle*timeInNote);
		total += 10*Math.sin(3*cycle*timeInNote);
		total += 10*Math.sin(4*cycle*timeInNote);
		total += 1*Math.sin(5*cycle*timeInNote);
		total += 1*Math.sin(6*cycle*timeInNote);
		total += 1*Math.sin(7*cycle*timeInNote);
		total *= 1/223.0;
		return total*envelope*amplitude;
	}
	public static double getStandardDecayingNote(String note, double rate, double start, double end, double time) {
		if(time<=start || time>=end) return 0;
		setupStandardNoteFrequencies();
		double durationInTime = end-start;
		double timeInNote = time-start;
		double amplitude = 0.2;
		double frequency = standardNoteFrequency.get(note);
		double cycle = 2*Math.PI*frequency;
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		envelope *= Math.pow(rate,timeInNote);
		double total = 0;
		total += 100*Math.sin(1*cycle*timeInNote);
		total += 100*Math.sin(2*cycle*timeInNote);
		total += 10*Math.sin(3*cycle*timeInNote);
		total += 10*Math.sin(4*cycle*timeInNote);
		total += 1*Math.sin(5*cycle*timeInNote);
		total += 1*Math.sin(6*cycle*timeInNote);
		total += 1*Math.sin(7*cycle*timeInNote);
		total *= 1/223.0;
		return total*envelope*amplitude;
	}
	public static double getStandardDecayingNote2(String note, double start, double end, double time) {
		if(time<=start || time>=end) return 0;
		setupStandardNoteFrequencies();
		double durationInTime = end-start;
		double timeInNote = time-start;
		double amplitude = 0.2;
		double frequency = standardNoteFrequency.get(note);
		double cycle = 2*Math.PI*frequency;
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		double decay = Math.pow(0.5,timeInNote);
		double decay2 = decay*decay;
		double decay3 = decay2*decay;
		double total = 0;
		total += 100*Math.sin(1*cycle*timeInNote)*decay;
		total += 100*Math.sin(2*cycle*timeInNote)*decay2;
		total += 10*Math.sin(3*cycle*timeInNote)*decay2;
		total += 10*Math.sin(4*cycle*timeInNote)*decay3;
		total += 1*Math.sin(5*cycle*timeInNote)*decay3;
		total += 1*Math.sin(6*cycle*timeInNote)*decay3;
		total += 1*Math.sin(7*cycle*timeInNote)*decay3;
		total *= 1/223.0;
		return total*envelope*amplitude;
	}
	public static Map<String,Double> standardNoteFrequency = null;
	public static void setupStandardNoteFrequencies() {
		if(standardNoteFrequency!=null) return;
		standardNoteFrequency = new HashMap<>();
		String rawNotesData = "0C,16.35;0C#,17.32;0Db,17.32;0D,18.35;0D#,19.45;0Eb,19.45;0E,20.6;0F,21.83;0F#,23.12;0Gb,23.12;0G,24.5;0Ab,25.96;0G#,25.96;0A,27.5;0A#,29.14;0Bb,29.14;0B,30.87;1C,32.7;1C#,34.65;1Db,34.65;1D,36.71;1D#,38.89;1Eb,38.89;1E,41.2;1F,43.65;1F#,46.25;1Gb,46.25;1G,49;1Ab,51.91;1G#,51.91;1A,55;1A#,58.27;1Bb,58.27;1B,61.74;2C,65.41;2C#,69.3;2Db,69.3;2D,73.42;2D#,77.78;2Eb,77.78;2E,82.41;2F,87.31;2F#,92.5;2Gb,92.5;2G,98;2Ab,103.83;2G#,103.83;2A,110;2A#,116.54;2Bb,116.54;2B,123.47;3C,130.81;3C#,138.59;3Db,138.59;3D,146.83;3D#,155.56;3Eb,155.56;3E,164.81;3F,174.61;3F#,185;3Gb,185;3G,196;3Ab,207.65;3G#,207.65;3A,220;3A#,233.08;3Bb,233.08;3B,246.94;4C,261.63;4C#,277.18;4Db,277.18;4D,293.66;4D#,311.13;4Eb,311.13;4E,329.63;4F,349.23;4F#,369.99;4Gb,369.99;4G,392;4Ab,415.3;4G#,415.3;4A,440;4A#,466.16;4Bb,466.16;4B,493.88;5C,523.25;5C#,554.37;5Db,554.37;5D,587.33;5D#,622.25;5Eb,622.25;5E,659.25;5F,698.46;5F#,739.99;5Gb,739.99;5G,783.99;5Ab,830.61;5G#,830.61;5A,880;5A#,932.33;5Bb,932.33;5B,987.77;6C,1046.5;6C#,1108.73;6Db,1108.73;6D,1174.66;6D#,1244.51;6Eb,1244.51;6E,1318.51;6F,1396.91;6F#,1479.98;6Gb,1479.98;6G,1567.98;6Ab,1661.22;6G#,1661.22;6A,1760;6A#,1864.66;6Bb,1864.66;6B,1975.53;7C,2093;7C#,2217.46;7Db,2217.46;7D,2349.32;7D#,2489.02;7Eb,2489.02;7E,2637.02;7F,2793.83;7F#,2959.96;7Gb,2959.96;7G,3135.96;7Ab,3322.44;7G#,3322.44;7A,3520;7A#,3729.31;7Bb,3729.31;7B,3951.07;8C,4186.01;8C#,4434.92;8Db,4434.92;8D,4698.63;8D#,4978.03;8Eb,4978.03;8E,5274.04;8F,5587.65;8F#,5919.91;8Gb,5919.91;8G,6271.93;8Ab,6644.88;8G#,6644.88;8A,7040;8A#,7458.62;8Bb,7458.62;8B,7902.13";
		String[] items = rawNotesData.split(";");
		for(int k=0; k<items.length; k++) {
			String[] fields = items[k].split(",");
			standardNoteFrequency.put(fields[0],Double.parseDouble(fields[1]));
		}
	}
	public static double getDrone(double time) {
		double value = 0;
		//value += 0.5*getNote(58.27, 1000000, time); // Bb
		//value += 0.5*getNote(73.42, 1000000, time); // D
		//value += 0.5*getNote(82.41, 1000000, time); // E
		value += 0.5*getNote(98, 1000000, time); // G
		//value += 0.5*getNote(110, 1000000, time); // A
		//value += 0.5*getNote(116.54, 1000000, time); // Bb
		value += 0.5*getNote(123, 1000000, time); // B
		value += 0.5*getNote(146.83, 1000000, time); // D
		//value += 0.5*getNote(164.81, 1000000, time); // E
		value += 0.5*getNote(196, 1000000, time); // G
		//value += 0.5*getNote(220, 1000000, time); // A
		value += 0.5*getNote(247, 1000000, time); // B
		//value += 0.5*getNote(329.63, 1000000, time); // E
		return value;
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
		jumps.add(new JumpsAndBendsHelper(0.49,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.01,215,"jump")); // beat
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,246,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.99,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.01,321,"jump")); // beat
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
		jumps.add(new JumpsAndBendsHelper(0.49,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.01,215,"jump")); // beat
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,165,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.99,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.01,215,"jump")); // beat
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,165,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(1,220,"jump")); // A
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
	}
	public static void fillJumpsList4(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/01-srv-p1-flat-5-slide-bl-501
		jumps.add(new JumpsAndBendsHelper(0.3,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.01,300,"jump")); // beat
		jumps.add(new JumpsAndBendsHelper(0.29,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.1,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.2,311,"jump")); // Eb
		jumps.add(new JumpsAndBendsHelper(0.3,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.1,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.2,311,"jump")); // Eb
		jumps.add(new JumpsAndBendsHelper(0.3,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.3,261,293,"bend")); // C -> D
		jumps.add(new JumpsAndBendsHelper(0.3,293,"jump")); // D
		fillJumpsList_vibrato(jumps,0.15,6,293,300); // D
		jumps.add(new JumpsAndBendsHelper(0.3,293,"jump")); // D
	}
	public static void fillJumpsList5(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/22-srv-p2-maj-3-bend-bl-522
		jumps.add(new JumpsAndBendsHelper(0.2,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.1,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.1,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.5,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.5,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,277,294,"bend")); // C# -> D
		jumps.add(new JumpsAndBendsHelper(0.5,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.6,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.01,200,"jump")); // beat
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		fillJumpsList_vibrato(jumps,0.3,4,220,225); // A
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
	}
	public static void fillJumpsList6(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/29-srv-p1-classic-p-bl-529
		jumps.add(new JumpsAndBendsHelper(0.05,146.83,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.05,196,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.05,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.3,523,587,"bend")); // C -> D
		jumps.add(new JumpsAndBendsHelper(0.6,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.3,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.6,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.3,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.3,329,293,"bend")); // E -> D
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		fillJumpsList_vibrato(jumps,0.15,2,261,275); // C
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
	}
	public static void fillJumpsList7(List<JumpsAndBendsHelper> jumps) {
		double duration = 0.03;
		int reps = 20;
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,196,"jump")); // G
			jumps.add(new JumpsAndBendsHelper(duration,261,"jump")); // C
			jumps.add(new JumpsAndBendsHelper(duration,329,"jump")); // E
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,196,"jump")); // G
			jumps.add(new JumpsAndBendsHelper(duration,247,"jump")); // B
			jumps.add(new JumpsAndBendsHelper(duration,293,"jump")); // D
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,220,"jump")); // A
			jumps.add(new JumpsAndBendsHelper(duration,261,"jump")); // C
			jumps.add(new JumpsAndBendsHelper(duration,329,"jump")); // E
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,220,"jump")); // A
			jumps.add(new JumpsAndBendsHelper(duration,261,"jump")); // C
			jumps.add(new JumpsAndBendsHelper(duration,349,"jump")); // F
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,196,"jump")); // G
			jumps.add(new JumpsAndBendsHelper(duration,261,"jump")); // C
			jumps.add(new JumpsAndBendsHelper(duration,329,"jump")); // E
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,220,"jump")); // A
			jumps.add(new JumpsAndBendsHelper(duration,261,"jump")); // C
			jumps.add(new JumpsAndBendsHelper(duration,329,"jump")); // E
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,220,"jump")); // A
			jumps.add(new JumpsAndBendsHelper(duration,293,"jump")); // D
			jumps.add(new JumpsAndBendsHelper(duration,349,"jump")); // F
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,247,"jump")); // B
			jumps.add(new JumpsAndBendsHelper(duration,293,"jump")); // D
			jumps.add(new JumpsAndBendsHelper(duration,392,"jump")); // G
		}
		for(int k=0; k<reps; k++) {
			jumps.add(new JumpsAndBendsHelper(duration,261,"jump")); // C
			jumps.add(new JumpsAndBendsHelper(duration,329,"jump")); // E
			jumps.add(new JumpsAndBendsHelper(duration,392,"jump")); // G
		}
	}
	public static void fillJumpsList8(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/43-srv-p1-b9-hammer-bl-543
		jumps.add(new JumpsAndBendsHelper(0.05,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.35,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.1,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.6,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.6,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.6,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,466,"jump")); // A#
		jumps.add(new JumpsAndBendsHelper(0.6,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.6,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.05,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.35,523,587,"bend")); // C -> D
		jumps.add(new JumpsAndBendsHelper(0.2,587,"jump")); // D
		fillJumpsList_vibrato(jumps,0.15,2,587,575); // D
		jumps.add(new JumpsAndBendsHelper(0.3,587,"jump")); // D
	}
	public static void fillJumpsList9(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/50-srv-p1-9th-hammer-bl-550
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.55,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.55,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.55,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.05,466,"jump")); // Bb
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.35,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.3,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.05,293,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.35,293,311,"bend")); // D -> D#
		jumps.add(new JumpsAndBendsHelper(0.2,311,"jump")); // D#
		jumps.add(new JumpsAndBendsHelper(0.6,329,"jump")); // E
		fillJumpsList_vibrato(jumps,0.15,4,329,320); // E
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
	}
	public static void fillJumpsList10(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/09-a-king-p3-double-flick-bl-509
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.05,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.35,523,587,"bend")); // C -> D
		jumps.add(new JumpsAndBendsHelper(0.2,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.15,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.45,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.15,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.15,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.6,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.25,523,554,"bend")); // C -> C#
		jumps.add(new JumpsAndBendsHelper(0.15,554,587,"bend")); // C# -> D
		jumps.add(new JumpsAndBendsHelper(1,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
	}
	public static void fillJumpsList11(List<JumpsAndBendsHelper> jumps) {
		// https://www.justinguitar.com/guitar-lessons/23-a-king-p2-call-it-bl-523
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.45,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.45,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.15,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.05,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.35,587,659,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.2,659,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.45,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.45,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.45,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.15,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.45,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.05,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.35,587,659,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.2,659,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.35,587,659,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.2,659,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.15,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.1,440,"jump")); // A
		fillJumpsList_vibrato(jumps,0.15,2,440,450); // A
		jumps.add(new JumpsAndBendsHelper(0.05,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		fillJumpsList_vibrato(jumps,0.15,2,440,450); // A
		jumps.add(new JumpsAndBendsHelper(2,10,"jump")); // rest
	}
	public static void fillJumpsList12(List<JumpsAndBendsHelper> jumps) {
		// https://www.guitarlessons.com/guitar-lessons/lead-guitar/4-essential-blues-licks
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.3,659,"jump")); // E
		fillJumpsList_vibrato(jumps,0.15,6,659,674); // E
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,440,466,"bend")); // A -> A#
		jumps.add(new JumpsAndBendsHelper(0.15,466,440,"bend")); // A# -> A
		jumps.add(new JumpsAndBendsHelper(0.2,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.55,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.05,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,440,494,"bend")); // A -> B
		jumps.add(new JumpsAndBendsHelper(0.15,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.2,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,440,466,"bend")); // A -> A#
		jumps.add(new JumpsAndBendsHelper(0.15,466,440,"bend")); // A# -> A
		jumps.add(new JumpsAndBendsHelper(0.2,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.1,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.5,587,659,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.55,659,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,659,630,"bend")); // E -> E-
		jumps.add(new JumpsAndBendsHelper(0.05,630,659,"bend")); // E- -> E
		jumps.add(new JumpsAndBendsHelper(0.5,659,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,440,466,"bend")); // A -> A#
		jumps.add(new JumpsAndBendsHelper(0.15,466,440,"bend")); // A# -> A
		jumps.add(new JumpsAndBendsHelper(0.2,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.4,164.81,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.2,196,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.4,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,233,"jump")); // A#
		jumps.add(new JumpsAndBendsHelper(0.4,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.2,294,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.6,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.1,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,440,494,"bend")); // A -> B
		jumps.add(new JumpsAndBendsHelper(0.2,494,440,"bend")); // B -> A
		jumps.add(new JumpsAndBendsHelper(0.4,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(1,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
	}
	public static void fillJumpsList13(List<JumpsAndBendsHelper> jumps) {
		// https://www.guitarlessons.com/guitar-lessons/lead-guitar/3-classic-bending-licks
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.3,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.05,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.2,392,440,"bend")); // G -> A
		jumps.add(new JumpsAndBendsHelper(0.05,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.05,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,523,587,"bend")); // C -> D
		jumps.add(new JumpsAndBendsHelper(0.1,587,"jump")); // D
		fillJumpsList_vibrato(jumps,0.15,4,587,570); // D
	}
	public static void fillJumpsList14(List<JumpsAndBendsHelper> jumps) {
		// https://www.guitarlessons.com/guitar-lessons/lead-guitar/3-classic-bending-licks
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.1,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.05,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.1,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.05,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.4,440,453,"bend")); // A -> A+
		jumps.add(new JumpsAndBendsHelper(0.15,453,"jump")); // A+
		
		jumps.add(new JumpsAndBendsHelper(0.05,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.4,440,480,"bend")); // A -> A#+
		jumps.add(new JumpsAndBendsHelper(0.15,480,"jump")); // A#+
		
		jumps.add(new JumpsAndBendsHelper(0.05,466,"jump")); // A#
		jumps.add(new JumpsAndBendsHelper(0.4,466,480,"bend")); // A# -> A#+
		jumps.add(new JumpsAndBendsHelper(0.15,480,"jump")); // A#+
		
		jumps.add(new JumpsAndBendsHelper(0.05,466,"jump")); // A#
		jumps.add(new JumpsAndBendsHelper(0.4,466,494,"bend")); // A# -> B
		jumps.add(new JumpsAndBendsHelper(0.15,494,"jump")); // B
		
		jumps.add(new JumpsAndBendsHelper(0.05,466,"jump")); // A#
		jumps.add(new JumpsAndBendsHelper(0.4,466,509,"bend")); // A# -> B+
		jumps.add(new JumpsAndBendsHelper(0.15,509,"jump")); // B+
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.4,494,509,"bend")); // B -> B+
		jumps.add(new JumpsAndBendsHelper(0.15,509,"jump")); // B+
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.4,494,523,"bend")); // B -> C
		jumps.add(new JumpsAndBendsHelper(0.15,523,"jump")); // C
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.4,494,539,"bend")); // B -> C+
		jumps.add(new JumpsAndBendsHelper(0.15,539,"jump")); // C+
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.4,494,554,"bend")); // B -> C#
		jumps.add(new JumpsAndBendsHelper(0.15,554,"jump")); // C#
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.25,494,554,"bend")); // B -> C#
		jumps.add(new JumpsAndBendsHelper(0.15,554,"jump")); // C#
		
		jumps.add(new JumpsAndBendsHelper(0.15,494,554,"bend")); // B -> C#
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.4,494,554,"bend")); // B -> C#
		jumps.add(new JumpsAndBendsHelper(0.15,554,"jump")); // C#
		fillJumpsList_vibrato(jumps,0.15,4,554,539); // C#
		
		jumps.add(new JumpsAndBendsHelper(0.05,494,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.15,494,554,"bend")); // B -> C#
		jumps.add(new JumpsAndBendsHelper(0.1,554,"jump")); // C#
		
		jumps.add(new JumpsAndBendsHelper(0.15,440,"jump")); // A
		
		jumps.add(new JumpsAndBendsHelper(0.45,370,"jump")); // F#
		fillJumpsList_vibrato(jumps,0.15,8,370,381); // F#
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
	}
	public static void fillJumpsList15(List<JumpsAndBendsHelper> jumps) {
		// https://www.guitarlessons.com/guitar-lessons/lead-guitar/3-classic-bending-licks
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.05,294,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.4,294,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.15,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.4,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.4,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,294,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.4,294,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.15,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.4,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.4,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,294,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.4,294,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.15,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.4,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.4,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.2,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.05,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.4,392,440,"bend")); // G -> A
		jumps.add(new JumpsAndBendsHelper(0.15,440,"jump")); // A
		fillJumpsList_vibrato(jumps,0.15,4,440,428); // A
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
	}
	public static void fillJumpsList16(List<JumpsAndBendsHelper> jumps) {
		// https://youtu.be/Kr3quGh7pJA?t=850
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(2,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.67,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,329,277,"bend")); // E -> C#
		jumps.add(new JumpsAndBendsHelper(1,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.67,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,440,370,"bend")); // A -> F#
		jumps.add(new JumpsAndBendsHelper(1,370,"jump")); // F#
		jumps.add(new JumpsAndBendsHelper(0.67,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.33,329,277,"bend")); // E -> C#
		jumps.add(new JumpsAndBendsHelper(1,277,"jump")); // C#
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
	}
	public static void fillJumpsList17(List<JumpsAndBendsHelper> jumps) {
		// https://youtu.be/wSbT0ovpr7Y?t=216
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,277,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.2,277,554,"bend")); // Db -> Db
		jumps.add(new JumpsAndBendsHelper(0.4,554,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.15,554,494,"bend")); // Db -> Cb
		jumps.add(new JumpsAndBendsHelper(0.65,494,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.15,494,466,"bend")); // Cb -> Bb
		fillJumpsList_vibrato(jumps,0.125,4,466,485); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,466,"jump")); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,466,494,"bend")); // Bb -> Cb
		jumps.add(new JumpsAndBendsHelper(0.65,494,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.15,494,277,"bend")); // Cb -> Db
		jumps.add(new JumpsAndBendsHelper(0.2,277,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.2,277,554,"bend")); // Db -> Db
		jumps.add(new JumpsAndBendsHelper(0.4,554,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.15,554,494,"bend")); // Db -> Cb
		jumps.add(new JumpsAndBendsHelper(0.65,494,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.15,494,466,"bend")); // Cb -> Bb
		fillJumpsList_vibrato(jumps,0.125,4,466,485); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,466,"jump")); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,466,494,"bend")); // Bb -> Cb
		jumps.add(new JumpsAndBendsHelper(0.8,494,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,554,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.2,554,1108,"bend")); // Db -> Db
		jumps.add(new JumpsAndBendsHelper(0.4,1108,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.15,1108,988,"bend")); // Db -> Cb
		jumps.add(new JumpsAndBendsHelper(0.65,988,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.15,988,932,"bend")); // Cb -> Bb
		fillJumpsList_vibrato(jumps,0.125,4,932,970); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,932,"jump")); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,932,988,"bend")); // Bb -> Cb
		jumps.add(new JumpsAndBendsHelper(0.65,988,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.15,988,554,"bend")); // Cb -> Db
		jumps.add(new JumpsAndBendsHelper(0.2,554,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.2,554,1108,"bend")); // Db -> Db
		jumps.add(new JumpsAndBendsHelper(0.4,1108,"jump")); // Db
		jumps.add(new JumpsAndBendsHelper(0.15,1108,988,"bend")); // Db -> Cb
		jumps.add(new JumpsAndBendsHelper(0.65,988,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.15,988,932,"bend")); // Cb -> Bb
		fillJumpsList_vibrato(jumps,0.125,4,932,970); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,932,"jump")); // Bb
		jumps.add(new JumpsAndBendsHelper(0.15,932,988,"bend")); // Bb -> Cb
		jumps.add(new JumpsAndBendsHelper(0.8,988,"jump")); // Cb
		jumps.add(new JumpsAndBendsHelper(0.6,10,"jump")); // rest
		
		
		/*
		jumps.add(new JumpsAndBendsHelper(0.05,146.83,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.05,196,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.05,247,"jump")); // B
		jumps.add(new JumpsAndBendsHelper(0.3,523,587,"bend")); // C -> D
		jumps.add(new JumpsAndBendsHelper(0.6,587,"jump")); // D
		jumps.add(new JumpsAndBendsHelper(0.3,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.6,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,392,"jump")); // G
		jumps.add(new JumpsAndBendsHelper(0.3,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.3,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.3,329,293,"bend")); // E -> D
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.3,293,329,"bend")); // D -> E
		jumps.add(new JumpsAndBendsHelper(0.3,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		fillJumpsList_vibrato(jumps,0.15,2,261,275); // C
		jumps.add(new JumpsAndBendsHelper(0.3,261,"jump")); // C
		*/
		/*
		// https://www.guitarplayer.com/lessons/12-killer-blues-licks-you-must-know
		// straight
		jumps.add(new JumpsAndBendsHelper(0.5,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.5,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.5,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.5,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.5,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(1,146.83,"jump")); // D
		// with swing
		jumps.add(new JumpsAndBendsHelper(0.33,523,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.67,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.67,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.33,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(1.67,146.83,"jump")); // D
		// with swing and bends
		jumps.add(new JumpsAndBendsHelper(0.33,523,540,"bend")); // C bend
		jumps.add(new JumpsAndBendsHelper(0.67,440,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.33,329,"jump")); // E
		jumps.add(new JumpsAndBendsHelper(0.67,261,"jump")); // C
		jumps.add(new JumpsAndBendsHelper(0.33,220,"jump")); // A
		jumps.add(new JumpsAndBendsHelper(0.27,146.83,"jump")); // D
		fillJumpsList_vibrato(jumps,0.2,7,146,148); // D
		*/
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
			//fillJumpsList_FrereJacques(jumps);
			//fillJumpsList1(jumps);
			//fillJumpsList2(jumps);
			//fillJumpsList3(jumps);
			//fillJumpsList4(jumps);
			//fillJumpsList5(jumps);
			//fillJumpsList6(jumps);
			//fillJumpsList7(jumps);
			//fillJumpsList8(jumps);
			fillJumpsList9(jumps);
			//fillJumpsList10(jumps);
			//fillJumpsList11(jumps);
			//fillJumpsList12(jumps);
			//fillJumpsList13(jumps);
			//fillJumpsList14(jumps);
			//fillJumpsList15(jumps);
			//fillJumpsList16(jumps);
			//fillJumpsList17(jumps);
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