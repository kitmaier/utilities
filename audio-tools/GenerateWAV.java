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
		total += getChromaticIntervals(tick, timeResidue);
		//total += getIpanemaChordsStretchNote(tick, timeResidue);
		return total;
	}
	public static double getChromaticIntervals(int tick, double timeResidue) {
		int note = 0;
		double total = 0;
		int[] noteList = getChromaticIntervals(tick);
		for(int k=0; k<noteList.length; k++) {
			//total += getEvenFundamental(noteList[k],1,timeResidue);
			//total += getEvenNote(noteList[k],1,timeResidue);
			total += getGridNote(noteList[k],1,timeResidue);
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
		total += getStretchNote(note,1,timeResidue);
		//total += getJustNote(note,1,timeResidue);
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