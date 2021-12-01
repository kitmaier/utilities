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
		total += getJumps(time);
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
	public static double getJumps(double time) {
		double t1 = 0;
		double t2 = 4;
		double t3 = 8;
		double t4 = 12;
		double a1 = 400;
		double a2 = 500;
		double a3 = 650;
		double a4 = 800;
		double b1 = 0;
		double b2 = a1*t2+b1-a2*t2;
		double b3 = a2*t3+b2-a3*t3;
		double b4 = a3*t4+b3-a4*t4;
		double z = a1*time;
		if(time<=t2) {
			z = a1*time+b1;
		} else if(time<=t3) {
			z = a2*time+b2;
		} else if(time<=t4) {
			z = a3*time+b3;
		} else {
			z = a4*time+b4;
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