/*
https://stackoverflow.com/questions/3297749/java-reading-manipulating-and-writing-wav-files
*/

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
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
	How does subjective volume scale relative to 1) amplitude (increase height of wave), vs 2) number of frequency components
		Is it the case that a single frequency component at amplitude 1 is the same subjective loudness as 10 frequency components at amplitude 0.1?
		Does how far apart the frequency components are matter? Does phase matter? Is the relationship of loudness to amplitude linear/quadratic/logarithmic/other?
	TODO: random walk of pitch
		like brown noise, but for pitch
		small-scale (in time and pitch) vibrato-like textures
		sounds more natural?
		could easily be implemented using the pitch bending system
	TODO: implement 1/f^2 pure note but with scattered phase offsets, compare subjective sound to smooth parabola wave, what is the impact of phase relationships?
*/

public class GenerateWAV {
	public static void main(String[] args) throws IOException {
		//try {createPinkNoise();} catch(Exception e) {e.printStackTrace();}
		try {loadPinkNoise();} catch(Exception e) {e.printStackTrace();}
		
		final double sampleRate = 44100.0;
		//final double seconds = 300.0;
		//final double seconds = 128.0;
		final double seconds = 120.0;
		//final double seconds = 96.0;
		//final double seconds = 72.0;
		//final double seconds = 60.0;
		//final double seconds = 48.0;
		//final double seconds = 36.0;
		//final double seconds = 32.0;
		//final double seconds = 25.0;
		//final double seconds = 16.0;
		//final double seconds = 10.0;
		
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
		//total += getAxisPattern(time);
		//total += getFakeAndalusianPattern(time);
		//total += getFakeAndalusian2Pattern(time);
		//total += getAndalusianPattern(time);
		//total += getInverseAndalusianPattern(time);
		//total += getCraig1Pattern(time);
		//total += getBellsMusic(time);
		//total += 0.05*getWhiteNoise(1.99,2.1,time);
		//total += 0.05*getWhiteNoise(2,3,time);
		//total += 0.2*getPinkNoise(1.99,2.02,time);
		//total += getBrownNoise(0,24,1234,time);
		//total += 0.5*getWavelet(2,0.01,200,time);
		//total += getSawtoothWavelet(time);
		//total += getPluck("3C",4,5,time);
		//total += getTunedBrownNoise(time);
		//total += getTunedBrownNoise2(time);
		//total += getMultipliedNotes(time);
		//total += getSwayingTimbre2(time);
		//total += getAudacityPluck(0,2,200,1357,time);
		//total += 0.5*getGaussianPulses(200,time);
		//total += 0.03*getMorletPulses(250,30,20,time);
		//total += 0.1*getMorletPulses(250,9,6,time);
		//total += getTriangleWave(200,time);
		//total += getSawtoothWave(200,time);
		//total += getDeltaPulses(200,time);
		//total += getSquareWave(200,time);
		//total += 0.1*getSineWave(1000,time);
		//total += 0.1*getSineWave(750,time);
		//total += getParabolicWave(200,time);
		//total += 0.3*getParabolicSinePulses(150,8,time);
		//total += getVowel(time);
		//total += getCubicWave(200,time);
		//total += getAudacityPinkNoise(time);
		//total += 0.3*getRandomMelodyEm6(time);
		//total += 0.3*getRandomMelodyC67(time);
		//total += getSymmetricExponentialWave(200,time);
		//total += getSymmetricExponentialWave2(200,time);
		//total += getExponentialWave(200,time);
		//total += get12BarBluesPattern(time);
		//total += get12BarBlues7Pattern(time);
		//total += getGuitar145Pattern(time);
		total += getGuitarQuarterTonePattern(time);
		//total += getPluckStarSpangledBanner(time);
		//total += 0.1*getPluckWhatItsLikeIntro(time);
		//total += getAndalusian2Pattern(time);
		//total += getHarmonicSeventhChords(time);
		//total += 2*getAeroplaneChords(time);
		//total += get24TetDominantResolution(time);
		//total += get24TetQuarterToneShuttle(time);
		return total;
	}
	public static double getHarmonicSeventhChords(double time) {
		double total = 0;
		int t=0;
		double frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.7817974,-1,time);
		t++;
		frequency = 200*1.6666666;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.7817974,-1,time);
		t++;
		frequency = 200*1.125;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.7817974,-1,time);
		t++;
		frequency = 200*1.5;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.7817974,-1,time);
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.7817974,-1,time);
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*2.00,-1,time);
		t++;
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time);
		t++;
		frequency = 200*1.6666666;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time);
		t++;
		frequency = 200*1.125;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time);
		t++;
		frequency = 200*1.5;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time);
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time);
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time);
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time);
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time);
		total += getAudacityPluck(t+0.06,t+1,frequency*2.00,-1,time);
		t++;
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 350 - 7/4 - 9.69
		t++;
		frequency = 250;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.75/2,-1,time); // 218.75 - 35/32 - 1.55
		total += getAudacityPluck(t+0.02,t+1,frequency*1.00,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.25,-1,time); // 312.5 - 25/16 - 7.73
		total += getAudacityPluck(t+0.06,t+1,frequency*1.50,-1,time); // 375 - 15/8 - 10.88
		t++;
		frequency = 300;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.50/2,-1,time); // 225 - 9/8 - 2.04
		total += getAudacityPluck(t+0.02,t+1,frequency*1.75/2,-1,time); // 262.5 - 21/16 - 4.71
		total += getAudacityPluck(t+0.04,t+1,frequency*1.00,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.25,-1,time); // 375 - 15/8 - 10.88
		t++;
		frequency = 350;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.25/2,-1,time); // 218.75 - 35/32 - 1.55
		total += getAudacityPluck(t+0.02,t+1,frequency*1.50/2,-1,time); // 262.5 - 21/16 - 4.71
		total += getAudacityPluck(t+0.04,t+1,frequency*1.75/2,-1,time); // 306.25 - 49/32 - 7.38
		total += getAudacityPluck(t+0.06,t+1,frequency*1.00,-1,time); // 350 - 7/4 - 9.69
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 350 - 7/4 - 9.69
		t++;
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 350 - 7/4 - 9.69
		t++;
		frequency = 337.5; // 27/16 = (1/2)(3/2)^3
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25/2,-1,time); // 210.9375 - 135/128 - 0.92
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50/2,-1,time); // 253.125 - 81/64 - 4.08
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75/2,-1,time); // 295.3125 - 189/128 - 6.75
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 337.5 - 27/16 - 9.06
		t++;
		frequency = 225; // 9/8 = (1/2)(3/2)^2
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 225 - 9/8 - 2.04
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 281.25 - 45/32 - 5.90
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 337.5 - 27/16 - 9.06
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 393.75 - 63/32 - 11.73
		t++;
		frequency = 300; // 3/2 = (3/2)^1
		total += getAudacityPluck(t+0.00,t+1,frequency*1.50/2,-1,time); // 225 - 9/8 - 2.04
		total += getAudacityPluck(t+0.02,t+1,frequency*1.75/2,-1,time); // 262.5 - 21/16 - 4.71
		total += getAudacityPluck(t+0.04,t+1,frequency*1.00,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.25,-1,time); // 375 - 15/8 - 10.88
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 350 - 7/4 - 9.69
		t++;
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 350 - 7/4 - 9.69
		t++;
		frequency = 333.3333; // 5/3
		total += getAudacityPluck(t+0.06,t+1,frequency*1.20/2,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.00,t+1,frequency*1.50/2,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.02,t+1,frequency*1.75/2,-1,time); // 291.6667 - 35/24 - 6.53
		total += getAudacityPluck(t+0.04,t+1,frequency*1.00,-1,time); // 333.3333 - 5/3 - 8.84
		t++;
		frequency = 225; // 9/8
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 225 - 9/8 - 2.04
		total += getAudacityPluck(t+0.02,t+1,frequency*1.20,-1,time); // 270 - 27/20 - 5.20
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 337.5 - 27/16 - 9.06
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 393.75 - 63/32 - 11.73
		t++;
		frequency = 300; // 3/2
		total += getAudacityPluck(t+0.00,t+1,frequency*1.50/2,-1,time); // 225 - 9/8 - 2.04
		total += getAudacityPluck(t+0.02,t+1,frequency*1.75/2,-1,time); // 262.5 - 21/16 - 4.71
		total += getAudacityPluck(t+0.04,t+1,frequency*1.00,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.25,-1,time); // 375 - 15/8 - 10.88
		t++;
		frequency = 200;
		total += getAudacityPluck(t+0.00,t+1,frequency*1.00,-1,time); // 200 - 1/1 - 0.00
		total += getAudacityPluck(t+0.02,t+1,frequency*1.25,-1,time); // 250 - 5/4 - 3.86
		total += getAudacityPluck(t+0.04,t+1,frequency*1.50,-1,time); // 300 - 3/2 - 7.02
		total += getAudacityPluck(t+0.06,t+1,frequency*1.75,-1,time); // 350 - 7/4 - 9.69
		t++;
		return 0.2*total;
	}
	public static double get24TetDominantResolution(double time) {
		double total = 0;
		int t=0;
		double tonic1 = 200;
		double tonic3 = tonic1*Math.pow(2,4.0/12);
		double tonic5 = tonic1*Math.pow(2,7.0/12);
		double tonic8 = tonic1*2;
		double tonic10 = tonic3*2;
		//double dominant7 = tonic1*Math.pow(2,1.0/24);
		//double dominant1 = dominant7*Math.pow(2,2.0/12);
		double dominant1 = tonic1*Math.pow(2,5.0/24);
		double dominant3 = dominant1*Math.pow(2,4.0/12);
		double dominant5 = dominant1*Math.pow(2,7.0/12);
		double dominant7 = dominant1*Math.pow(2,10.0/12);
		double subdominant1 = dominant1*Math.pow(2,3.0/24);
		double subdominant3 = subdominant1*Math.pow(2,3.0/12);
		double subdominant5 = subdominant1*Math.pow(2,7.0/12);
		double subdominant7 = subdominant1*Math.pow(2,10.0/12);
		for(int j=0; j<2; j++) {
			for(int k=0; k<8; k++) {
				total += getAudacityPluck(t+0.00,t+1,tonic1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,tonic3,-1,time);
				total += getAudacityPluck(t+0.04,t+1,tonic5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,tonic8,-1,time);
				//total += getAudacityPluck(t+0.08,t+1,tonic10,-1,time);
				t++;
			}
			for(int k=0; k<4; k++) {
				total += getAudacityPluck(t+0.00,t+1,subdominant1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,subdominant3,-1,time);
				total += getAudacityPluck(t+0.04,t+1,subdominant5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,subdominant7,-1,time);
				t++;
			}
			for(int k=0; k<4; k++) {
				total += getAudacityPluck(t+0.00,t+1,dominant1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,dominant3,-1,time);
				total += getAudacityPluck(t+0.04,t+1,dominant5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,dominant7,-1,time);
				t++;
			}
		}
		return 0.2*total;
	}
	public static double get24TetQuarterToneShuttle(double time) {
		double total = 0;
		int t=0;
		double chord1_1 = 200;
		double chord1_3 = chord1_1*Math.pow(2,4.0/12);
		double chord1_5 = chord1_1*Math.pow(2,7.0/12);
		double chord1_8 = chord1_1*2;
		double chord2_1 = chord1_1/Math.pow(2,1.0/24);
		double chord2_4 = chord2_1*Math.pow(2,5.0/12);
		double chord2_5 = chord2_1*Math.pow(2,7.0/12);
		double chord2_8 = chord2_1*2;
		double chord3_1 = chord1_1*Math.pow(2,1.0/24);
		double chord3_3 = chord3_1*Math.pow(2,4.0/12);
		double chord3_5 = chord3_1*Math.pow(2,7.0/12);
		double chord3_8 = chord3_1*2;
		double chord4_1 = chord1_1*Math.pow(2,1.0/24);
		double chord4_3 = chord4_1*Math.pow(2,3.0/12);
		double chord4_5 = chord4_1*Math.pow(2,7.0/12);
		double chord4_8 = chord4_1*2;
		for(int j=0; j<2; j++) {
			for(int k=0; k<4; k++) {
				total += getAudacityPluck(t+0.00,t+1,chord1_1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,chord1_3,-1,time);
				total += getAudacityPluck(t+0.04,t+1,chord1_5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,chord1_8,-1,time);
				t++;
			}
			for(int k=0; k<4; k++) {
				total += getAudacityPluck(t+0.00,t+1,chord4_1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,chord4_3,-1,time);
				total += getAudacityPluck(t+0.04,t+1,chord4_5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,chord4_8,-1,time);
				t++;
			}
			for(int k=0; k<4; k++) {
				total += getAudacityPluck(t+0.00,t+1,chord3_1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,chord3_3,-1,time);
				total += getAudacityPluck(t+0.04,t+1,chord3_5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,chord3_8,-1,time);
				t++;
			}
			for(int k=0; k<4; k++) {
				total += getAudacityPluck(t+0.00,t+1,chord2_1,-1,time);
				total += getAudacityPluck(t+0.02,t+1,chord2_4,-1,time);
				total += getAudacityPluck(t+0.04,t+1,chord2_5,-1,time);
				total += getAudacityPluck(t+0.06,t+1,chord2_8,-1,time);
				t++;
			}
		}
		return 0.2*total;
	}
	public static double getPluckWhatItsLikeIntro(double time) {
		double total = 0;
		double beatDuration = 0.1;
		String data = "3D,5;3A,4;4D,4;4G,8;"
			+ "4F,3;4G,2;4F,2;4D,2;3B,3;"
			+ "3F,3;3A,4;4C,6;4C,3;"
			+ "3C,4;3E,4;3G,6;3G,10;";
		String[] events = data.split(";");
		double startTime = 0;
		for(int k=0; k<events.length; k++) {
			String[] fields = events[k].split(",");
			String note = fields[0];
			double duration = Integer.parseInt(fields[1])*beatDuration;
			double endTime = startTime+duration;
			total += getAudacityPluckStandardNote(startTime,endTime,note,-1,time);
			startTime += duration;
		}
		return total;
	}
	public static double getPluckStarSpangledBanner(double time) {
		// TODO: https://en.wikipedia.org/wiki/The_Star-Spangled_Banner#/media/File:Star_Spangled_Banner_(Carr)_(1814).png
		double start = 0;
		double end = 0;
		//double note = "4C";
		double key = -1;
		double total = 0;
		//total += getAudacityPluckStandardNote(double start, double end, String note, int key, double time);
		
		return total;
	}
	public static String randomMelodyEm6Note = "4A";
	public static double randomMelodyEm6StartTime = 0;
	public static double randomMelodyEm6EndTime = 1;
	public static double getRandomMelodyEm6(double time) {
		if(time>=randomMelodyEm6EndTime) {
			randomMelodyEm6StartTime = time;
			int randNum = rand.nextInt(88);
			int increment = 0;
			if(randNum<36) {
				increment += 1;
			} else if(randNum<54) {
				increment += 2;
			} else if(randNum<66) {
				increment += 3;
			} else if(randNum<75) {
				increment += 4;
			} else if(randNum<81) {
				increment += 6;
			} else if(randNum<85) {
				increment += 8;
			} else if(randNum<88) {
				increment += 12;
			}
			randomMelodyEm6EndTime += increment/2.0;
			//int randNum2 = rand.nextInt(5);
			int randNum2 = rand.nextInt(4);
			if(randNum2==4) {
				randomMelodyEm6Note = "XX";
			} else {
				if(randomMelodyEm6Note.equals("XX")) {
					switch(randNum2) {
						case 0:
							randomMelodyEm6Note = "4E";
							break;
						case 1:
							randomMelodyEm6Note = "4A";
							break;
						case 2:
							randomMelodyEm6Note = "4C";
							break;
						case 3:
							randomMelodyEm6Note = "4E";
					}
				} else if(randomMelodyEm6Note.equals("4E")) {
					switch(randNum2) {
						case 0:
						case 1:
							randomMelodyEm6Note = "4G";
							break;
						case 2:
						case 3:
							randomMelodyEm6Note = "4A";
					}
				} else if(randomMelodyEm6Note.equals("4G")) {
					switch(randNum2) {
						case 0:
							randomMelodyEm6Note = "4E";
							break;
						case 1:
							randomMelodyEm6Note = "4A";
							break;
						case 2:
							randomMelodyEm6Note = "4B";
							break;
						case 3:
							randomMelodyEm6Note = "5C";
					}
				} else if(randomMelodyEm6Note.equals("4A")) {
					switch(randNum2) {
						case 0:
							randomMelodyEm6Note = "4E";
							break;
						case 1:
							randomMelodyEm6Note = "4G";
							break;
						case 2:
							randomMelodyEm6Note = "4B";
							break;
						case 3:
							randomMelodyEm6Note = "5C";
					}
				} else if(randomMelodyEm6Note.equals("4B")) {
					switch(randNum2) {
						case 0:
							randomMelodyEm6Note = "4G";
							break;
						case 1:
							randomMelodyEm6Note = "4A";
							break;
						case 2:
							randomMelodyEm6Note = "5C";
							break;
						case 3:
							randomMelodyEm6Note = "5E";
					}
				} else if(randomMelodyEm6Note.equals("5C")) {
					switch(randNum2) {
						case 0:
							randomMelodyEm6Note = "4G";
							break;
						case 1:
							randomMelodyEm6Note = "4A";
							break;
						case 2:
							randomMelodyEm6Note = "4B";
							break;
						case 3:
							randomMelodyEm6Note = "5E";
					}
				} else if(randomMelodyEm6Note.equals("5E")) {
					switch(randNum2) {
						case 0:
						case 1:
							randomMelodyEm6Note = "4B";
							break;
						case 2:
						case 3:
							randomMelodyEm6Note = "5C";
					}
				}
			}
		}
		if(randomMelodyEm6Note.equals("XX")) return 0;
		//return getStandardNote(randomMelodyEm6Note,-1,1000000,time);
		setupStandardNoteFrequencies();
		double frequency = standardNoteFrequency.get(randomMelodyEm6Note);
		//return 0.03*getSawtoothWave(frequency,time);
		//return 0.3*getParabolicWave(frequency,time);
		double envelope = 1;
		if(time<randomMelodyEm6StartTime+0.01) envelope = 100*(time-randomMelodyEm6StartTime);
		if(time>randomMelodyEm6EndTime-0.01) envelope = 100*(randomMelodyEm6EndTime-time);
		double envelope2 = -Math.cos(time/3)*0.15+0.85;
		return 0.2*getCubicWave(frequency,time)*envelope*envelope2;
	}
	public static String randomMelodyC67Note = "4G";
	public static double randomMelodyC67StartTime = 0;
	public static double randomMelodyC67EndTime = 1;
	public static double getRandomMelodyC67(double time) {
		if(time>=randomMelodyC67EndTime) {
			randomMelodyC67StartTime = time;
			int randNum = rand.nextInt(88);
			int increment = 0;
			if(randNum<36) {
				increment += 1;
			} else if(randNum<54) {
				increment += 2;
			} else if(randNum<66) {
				increment += 3;
			} else if(randNum<75) {
				increment += 4;
			} else if(randNum<81) {
				increment += 6;
			} else if(randNum<85) {
				increment += 8;
			} else if(randNum<88) {
				increment += 12;
			}
			randomMelodyC67EndTime += increment/2.0;
			//int randNum2 = rand.nextInt(5);
			int randNum2 = rand.nextInt(4);
			if(randNum2==4) {
				randomMelodyC67Note = "XX";
			} else {
				if(randomMelodyC67Note.equals("XX")) {
					switch(randNum2) {
						case 0:
							randomMelodyC67Note = "4C";
							break;
						case 1:
							randomMelodyC67Note = "4E";
							break;
						case 2:
							randomMelodyC67Note = "4G";
							break;
						case 3:
							randomMelodyC67Note = "5C";
					}
				} else if(randomMelodyC67Note.equals("4C")) {
					switch(randNum2) {
						case 0:
						case 1:
							randomMelodyC67Note = "4E";
							break;
						case 2:
						case 3:
							randomMelodyC67Note = "4G";
					}
				} else if(randomMelodyC67Note.equals("4E")) {
					switch(randNum2) {
						case 0:
							randomMelodyC67Note = "4C";
							break;
						case 1:
							randomMelodyC67Note = "4G";
							break;
						case 2:
							randomMelodyC67Note = "4A";
							break;
						case 3:
							randomMelodyC67Note = "4B";
					}
				} else if(randomMelodyC67Note.equals("4G")) {
					switch(randNum2) {
						case 0:
							randomMelodyC67Note = "4C";
							break;
						case 1:
							randomMelodyC67Note = "4E";
							break;
						case 2:
							randomMelodyC67Note = "4A";
							break;
						case 3:
							randomMelodyC67Note = "4B";
					}
				} else if(randomMelodyC67Note.equals("4A")) {
					switch(randNum2) {
						case 0:
							randomMelodyC67Note = "4E";
							break;
						case 1:
							randomMelodyC67Note = "4G";
							break;
						case 2:
							randomMelodyC67Note = "4B";
							break;
						case 3:
							randomMelodyC67Note = "5C";
					}
				} else if(randomMelodyC67Note.equals("4B")) {
					switch(randNum2) {
						case 0:
							randomMelodyC67Note = "4E";
							break;
						case 1:
							randomMelodyC67Note = "4G";
							break;
						case 2:
							randomMelodyC67Note = "4A";
							break;
						case 3:
							randomMelodyC67Note = "5C";
					}
				} else if(randomMelodyC67Note.equals("5C")) {
					switch(randNum2) {
						case 0:
						case 1:
							randomMelodyC67Note = "4A";
							break;
						case 2:
						case 3:
							randomMelodyC67Note = "4B";
					}
				}
			}
		}
		if(randomMelodyC67Note.equals("XX")) return 0;
		//return getStandardNote(randomMelodyC67Note,-1,1000000,time);
		setupStandardNoteFrequencies();
		double frequency = standardNoteFrequency.get(randomMelodyC67Note);
		//return 0.03*getSawtoothWave(frequency,time);
		//return 0.3*getParabolicWave(frequency,time);
		double envelope = 1;
		if(time<randomMelodyC67StartTime+0.01) envelope = 100*(time-randomMelodyC67StartTime);
		if(time>randomMelodyC67EndTime-0.01) envelope = 100*(randomMelodyC67EndTime-time);
		double envelope2 = -Math.cos(time/3)*0.15+0.85;
		return 0.2*getCubicWave(frequency,time)*envelope*envelope2;
	}
	public static double getVowel(double time) {
		// https://www.voicescienceworks.org/filtered-listening-and-vocal-regions.html
		// https://www.youtube.com/watch?v=f8gRfFfLDss
		//   Ian Howell - Parsing the spectral envelope: Toward a general theory of tone color
		//   0-523 - b(oo)t
		//   523-587 transition
		//   587-740 t(o)te
		//   740-784 transition
		//   784-1174 h(aw)k
		//   1174-1480 h(o)t
		//   1480-1760 transition
		//   1760-2093 h(a)t
		//   2093-2217 transition
		//   2217-2350 h(a)te
		//   2350-2500 transition
		//   2500-3700 h(ea)t
		//   3700+ bright "ee"
		double total = 0;
		
		// b(ee)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,2,time); // 300
		//total += 0.2*getParabolicSinePulses(150,12,time); // 1800
		//total += 0.1*getParabolicSinePulses(150,18,time); // 2700
		
		// b(e)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,3,time); // 450
		//total += 0.2*getParabolicSinePulses(150,11,time); // 1650
		//total += 0.1*getParabolicSinePulses(150,17,time); // 2550
		
		// b(a)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,4,time); // 600
		//total += 0.2*getParabolicSinePulses(150,11,time); // 1650
		//total += 0.1*getParabolicSinePulses(150,17,time); // 2550
		
		// b(a)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,5,time); // 750
		//total += 0.2*getParabolicSinePulses(150,11,time); // 1650
		//total += 0.1*getParabolicSinePulses(150,17,time); // 2550
		
		// b(a)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,5,time); // 750
		//total += 0.2*getParabolicSinePulses(150,10,time); // 1500
		//total += 0.1*getParabolicSinePulses(150,17,time); // 2550
		
		// t(a)co
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,5,time); // 750
		//total += 0.2*getParabolicSinePulses(150,9,time); // 1350
		//total += 0.1*getParabolicSinePulses(150,17,time); // 2550
		
		// b(o)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,4,time); // 600
		//total += 0.2*getParabolicSinePulses(150,7,time); // 1050
		//total += 0.1*getParabolicSinePulses(150,17,time); // 2550
		
		// b(e)t
		//total += 0.2*getParabolicWave(150,time);
		//total += 0.5*getParabolicSinePulses(150,5,time); // 750
		//total += 0.2*getParabolicSinePulses(150,12,time); // 1800
		//total += 0.1*getParabolicSinePulses(150,20,time); // 3000
		
		// b(i)t
		total += 0.1*getParabolicWave(150,time);
		total += 0.3*getParabolicSinePulses(150,3,time); // 450
		total += 0.2*getParabolicSinePulses(150,13,time); // 1950
		total += 0.05*getParabolicSinePulses(150,17,time); // 2550
		total += 0.2*getParabolicSinePulses(150,24,time); // 3600
		total += 0.03*getParabolicSinePulses(150,37,time); // 5550
		total += 0.02*getParabolicSinePulses(150,51,time); // 7650
		
		return total;
	}
	public static double getDeltaPulses(double fundamental, double time) {
		// equivalent to average of cos(nx) or sin(nx)
		// frequency spectrum flat for all harmonics
		// approximately narrow rectangle wave at sample frequency, falloff around 10khz
		double phase = (fundamental*time)%1.0;
		double sampleRate = 44100;
		double samplesPerCycle = sampleRate/fundamental;
		if(phase*samplesPerCycle<0.99999) return 0.99; // TODO: getting some weird rounding errors or something
		return -1/samplesPerCycle;
	}
	public static double getSawtoothWave(double fundamental, double time) {
		// equivalent to sum of sin(nx)/n
		// frequency spectrum 1/f for all harmonics
		double phase = (fundamental*time)%1.0;
		return (phase-0.5)*2*0.99;
	}
	public static double getSquareWave(double fundamental, double time) {
		// equivalent to sum of sin((2n-1)x)/(2n-1)
		// frequency spectrum 1/f for odd harmonics
		double phase = (fundamental*time)%1.0;
		if(phase<0.5) return -0.99;
		return 0.99;
	}
	public static double getParabolicWave(double fundamental, double time) {
		// equivalent to sum of cos(nx)/n^2
		// frequency spectrum 1/f^2 for all harmonics
		double phase = (fundamental*time)%1.0;
		double parabola = (phase*2-1)*(phase*2-1);
		return (parabola*3-1)*0.4;
	}
	public static double getTriangleWave(double fundamental, double time) {
		// equivalent to cos((2n-1)x)/(2n-1)^2
		// frequency spectrum 1/f^2 for odd harmonics
		double phase = (fundamental*time)%1.0;
		return Math.abs(phase-0.5)*4*0.99-0.99;
	}
	public static double getCubicWave(double fundamental, double time) {
		// equivalent to sum of sin(nx)/n^3
		// frequency spectrum 1/f^3 for all harmonics
		double phase = (fundamental*time)%1.0;
		double offset = phase*2-1;
		double cubic = 2.5*offset*(offset+1)*(offset-1);
		return cubic;
	}
	public static double getSymmetricExponentialWave(double fundamental, double time) {
		// https://mathworld.wolfram.com/FourierTransformExponentialFunction.html
		// equivalent (approximately?) to sum of cos(nx)/(1+n^2)
		// frequency spectrum of around 1/f^2 for all harmonics
		// TODO: try out with different parameters inside the exponential
		// TODO: fix the discontinuity at the beginning
		double phase = ((fundamental*time)%1.0)*2*Math.PI;
		double exp1 = Math.exp(-phase);
		double exp2 = Math.exp(-(2*Math.PI-phase));
		return (exp1+exp2)*1.49-0.5;
	}
	public static double getSymmetricExponentialWave2(double fundamental, double time) {
		// TODO: try out the two sides with different exponents
		double phase = ((fundamental*time)%1.0)*2*Math.PI;
		double parameter1 = 1;//10;
		double parameter2 = 10;//10;
		double exp1 = Math.exp(-parameter1*phase);
		double exp2 = Math.exp(-parameter2*(2*Math.PI-phase));
		return (exp1+exp2)-0.01;
	}
	public static double getExponentialWave(double fundamental, double time) {
		double parameter = 100;//10;//2*Math.PI;
		double phase = ((fundamental*time)%1.0)*parameter;
		phase = parameter-phase;
		return Math.exp(-phase)-0.3;
	}
	public static double getSineWave(double fundamental, double time) {
		double phase = (fundamental*time)%1.0;
		return -Math.cos(phase*2*Math.PI);
	}
	public static double getParabolicSinePulses(double fundamental, int harmonic, double time) {
		double phase = (fundamental*time)%1.0;
		double parabola = (phase*2-1)*(phase*2-1)*0.99;
		double wave = Math.sin(phase*2*Math.PI*harmonic);
		return parabola*wave;
	}
	public static double getMorletPulses(double fundamental, int harmonic, double standardDeviationsPerCycle, double time) {
		double phase = (fundamental*time)%1.0;
		double dev = phase-0.5;
		double sigma = 1/standardDeviationsPerCycle;
		double gaussian = Math.exp(-dev*dev/(2*sigma*sigma));
		double wave = Math.sin(phase*2*Math.PI*harmonic);
		return gaussian*wave;
	}
	public static double getGaussianPulses(double frequency, double time) {
		double phase = (frequency*time)%1.0;
		double standardDeviationsPerCycle = 10; // Don't reduce below about 8
		double dev = phase-0.5;
		double sigma = 1/standardDeviationsPerCycle;
		double gaussian = Math.exp(-dev*dev/(2*sigma*sigma));
		double area = sigma*Math.sqrt(2*Math.PI);
		// TODO: this offset calculation is a bit janky
		return gaussian-area;
	}
	public static double getAeroplaneChords(double time) {
		// chords from In the Aeroplane Over the Sea transposed to C major at ~150 BPM
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*9.6;
			total += getBasicGuitarChord("C",t,t+0.4,time)*1.0; t+=0.4;
			total += getBasicGuitarChord("C",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("C",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("C",t,t+0.4,time)*0.9; t+=0.4;
			total += getBasicGuitarChord("C",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("C",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("Am",t,t+0.4,time)*1.0; t+=0.4;
			total += getBasicGuitarChord("Am",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("Am",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("Am",t,t+0.4,time)*0.9; t+=0.4;
			total += getBasicGuitarChord("Am",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("Am",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("F",t,t+0.4,time)*1.0; t+=0.4;
			total += getBasicGuitarChord("F",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("F",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("F",t,t+0.4,time)*0.9; t+=0.4;
			total += getBasicGuitarChord("F",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("F",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("G",t,t+0.4,time)*1.0; t+=0.4;
			total += getBasicGuitarChord("G",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("G",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("G",t,t+0.4,time)*0.9; t+=0.4;
			total += getBasicGuitarChord("G",t,t+0.4,time)*0.8; t+=0.4;
			total += getBasicGuitarChord("G",t,t+0.4,time)*0.8; t+=0.4;
		}
		return total;
	}
	public static double getFakeAndalusianPattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*8;
			total += getBasicGuitarChord("E7",t,t+1,time);
			total += getBasicGuitarChord("E7",t+1,t+2,time);
			total += getBasicGuitarChord("G",t+2,t+3,time);
			total += getBasicGuitarChord("G",t+3,t+4,time);
			total += getBasicGuitarChord("Am",t+4,t+5,time);
			total += getBasicGuitarChord("Am",t+5,t+6,time);
			total += getBasicGuitarChord("F",t+6,t+7,time);
			total += getBasicGuitarChord("F",t+7,t+8,time);
		}
		total += getBasicGuitarChord("D7",24,25,time);
		return total;
	}
	public static double getFakeAndalusian2Pattern(double time) {
		double total = 0;
		int cycles = 3;
		//int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*8*4;
			for(int j=0; j<8; j++) {
				total += getBasicGuitarChord("E7",t,t+1,time); t++;
			}
			for(int j=0; j<8; j++) {
				total += getBasicGuitarChord("G",t,t+1,time); t++;
			}
			for(int j=0; j<8; j++) {
				total += getBasicGuitarChord("Am",t,t+1,time); t++;
			}
			for(int j=0; j<8; j++) {
				total += getBasicGuitarChord("F",t,t+1,time); t++;
			}
		}
		return total;
	}
	public static double getAndalusianPattern(double time) {
		double total = 0;
		int cycles = 3;
		//int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*8;
			total += getBasicGuitarChord("Am",t,t+1,time);
			total += getBasicGuitarChord("Am",t+1,t+2,time);
			total += getBasicGuitarChord("G",t+2,t+3,time);
			total += getBasicGuitarChord("G",t+3,t+4,time);
			total += getBasicGuitarChord("F",t+4,t+5,time);
			total += getBasicGuitarChord("F",t+5,t+6,time);
			total += getBasicGuitarChord("E7",t+6,t+7,time);
			total += getBasicGuitarChord("E7",t+7,t+8,time);
		}
		total += getBasicGuitarChord("D7",24,25,time);
		return total;
	}
	public static double getAndalusian2Pattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*16;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("Am",t,t+1,time); t++;
			total += getBasicGuitarChord("G",t,t+1,time); t++;
			total += getBasicGuitarChord("F",t,t+1,time); t++;
			total += getBasicGuitarChord("E7",t,t+1,time); t++;
		}
		return total;
	}
	public static double getInverseAndalusianPattern(double time) {
		double total = 0;
		int cycles = 5;
		//int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*8*4;
			for(int j=0; j<8; j++) {
				total += getOpenEmGuitarChord("Am",t,t+1,time); t++;
			}
			for(int j=0; j<8; j++) {
				total += getOpenEmGuitarChord("Bm",t,t+1,time); t++;
			}
			for(int j=0; j<8; j++) {
				total += getOpenEmGuitarChord("C#m",t,t+1,time); t++;
			}
			for(int j=0; j<8; j++) {
				total += getOpenEmGuitarChord("Dm",t,t+1,time); t++;
			}
		}
		return total;
	}
	public static double getCraig1Pattern(double time) {
		double total = 0;
		int cycles = 5;
		//int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*3*4;
			for(int j=0; j<4; j++) {
				//total += getEnumeratedGuitarChord("3F,3A,4C,4E",t,t+1,time); t++;
				total += getEnumeratedGuitarChord("2F,3C,3F,3A,4C,4E",t,t+1,time); t++;
			}
			for(int j=0; j<4; j++) {
				//total += getEnumeratedGuitarChord("3E,3G,3B,4D",t,t+1,time); t++;
				total += getEnumeratedGuitarChord("2E,2B,3E,3G,3B,4D",t,t+1,time); t++;
			}
			for(int j=0; j<4; j++) {
				//total += getEnumeratedGuitarChord("3D,3F,3A,4C",t,t+1,time); t++;
				total += getEnumeratedGuitarChord("2D,2A,3D,3F,3A,4C",t,t+1,time); t++;
			}
		}
		return total;
	}
	public static double get12BarBluesPattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*24;
			if(time<t||time>t+24) continue;
			double step = 1;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
		}
		return total;
	}
	public static double get12BarBlues7Pattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*24;
			if(time<t||time>t+24) continue;
			double step = 1;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F7",t,t+0.3,time);
			total += getBasicGuitarChord("F7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F7",t,t+0.3,time);
			total += getBasicGuitarChord("F7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F7",t,t+0.3,time);
			total += getBasicGuitarChord("F7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F7",t,t+0.3,time);
			total += getBasicGuitarChord("F7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G7",t,t+0.3,time);
			total += getBasicGuitarChord("G7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G7",t,t+0.3,time);
			total += getBasicGuitarChord("G7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F7",t,t+0.3,time);
			total += getBasicGuitarChord("F7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F7",t,t+0.3,time);
			total += getBasicGuitarChord("F7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C7",t,t+0.3,time);
			total += getBasicGuitarChord("C7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G7",t,t+0.3,time);
			total += getBasicGuitarChord("G7",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G7",t,t+0.3,time);
			total += getBasicGuitarChord("G7",t+0.3,t+1,time); t += step;
		}
		return total;
	}
	public static double getGuitar145Pattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*16;
			if(time<t||time>t+16) continue;
			double step = 1;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+0.3,time);
			total += getBasicGuitarChord("C",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+0.3,time);
			total += getBasicGuitarChord("F",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
			total += getBasicGuitarChord("G",t,t+0.3,time);
			total += getBasicGuitarChord("G",t+0.3,t+1,time); t += step;
		}
		return total;
	}
	public static double getGuitarQuarterTonePattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*16;
			if(time<t||time>t+16) continue;
			double step = 1;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getQuarterToneGuitarChord("D",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getQuarterToneGuitarChord("D",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getQuarterToneGuitarChord("D",t,t+1,time); t += step;
			total += getBasicGuitarChord("C",t,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+1,time); t += step;
			total += getBasicGuitarChord("F",t,t+1,time); t += step;
		}
		return total;
	}
	public static double getAxisPattern(double time) {
		double total = 0;
		//int cycles = 3;
		int cycles = 40;
		for(int k=0; k<cycles; k++) {
			double t = k*8;
			total += getBasicGuitarChord("C",t,t+1,time);
			total += getBasicGuitarChord("C",t+1,t+2,time);
			total += getBasicGuitarChord("G",t+2,t+3,time);
			total += getBasicGuitarChord("G",t+3,t+4,time);
			total += getBasicGuitarChord("Am",t+4,t+5,time);
			total += getBasicGuitarChord("Am",t+5,t+6,time);
			total += getBasicGuitarChord("F",t+6,t+7,time);
			total += getBasicGuitarChord("F",t+7,t+8,time);
		}
		total += getBasicGuitarChord("G",24,25,time);
		return total;
	}
	public static double getSwayingTimbre(double time) {
		time += 2000;
		double fundamental = 100.0;
		int step = (int)(time*fundamental);
		double phase = time*fundamental-step;
		double value = 0;
		int harmonics = 8;
		for(int harmonic=1; harmonic<=harmonics; harmonic++) {
			double frequency = fundamental*harmonic;
			double wave = Math.sin(2*Math.PI*phase*harmonic);
			double amplitude = 0.5*(1+Math.cos(2*Math.PI*((harmonics*3+harmonic)*1.0/(4*harmonics))*(step/fundamental)));
			value += (1.0/harmonics)*wave*amplitude;
		}
		return 0.5*value;
	}
	public static double getSwayingTimbre2(double time) {
		//double[] frequencies = {100,125,150,175,200,250,300,350,400,500,600,700,800}; // tuned to harmonic seventh chords
		double[] frequencies = {100,125,150,200,250,300,400,500,600,800};
		double value = 0;
		for(int k=0; k<frequencies.length; k++) {
			double frequency = frequencies[k];
			double wave = Math.sin(2*Math.PI*frequency*time);
			int step = (int)(time*frequency);
			double wobble = (k+2*frequencies.length)/(10.0*frequencies.length);
			double amplitude = 0.5*(1+Math.cos(2*Math.PI*(step/frequency)*wobble));
			value += (1.0/frequencies.length)*wave*amplitude;
		}
		return 0.5*value;
	}
	public static double getMultipliedNotes(double time) {
		double value = 0.3*Math.cos(2*Math.PI*time*300);
		value *= Math.cos(2*Math.PI*time*100);
		value *= Math.cos(2*Math.PI*time*10);
		value *= Math.cos(2*Math.PI*time*30);
		value *= Math.cos(2*Math.PI*time*200);
		return value;
	}
	public static class AudacityPluckData {
		double sr;
		double hz;
		double dur;
		double decayRatio;
		double t;
		double y;
		double lf;
		double tdecay;
		double st;
		double loss;
		double stretch;
		int len;
		double x;
		double cons;
		double x2;
		double x3;
		double[] shiftreg;
		int i1;
		int i2;
		int i3;
		int i4;
		int endptr;
		public AudacityPluckData(double sr, double hz, double dur, double decayRatio) {
			//System.out.println("AAA "+hz+" "+dur);
			this.sr = sr;
			this.hz = hz;
			this.dur = dur;
			this.decayRatio = decayRatio;
			
			for(int k=0; k<1000; k++) {rand.nextGaussian();}
			
			// Pluck parameters
			t = Math.PI*(hz/sr);
			y = Math.abs(Math.cos(t));
			lf = -Math.log(decayRatio);
			tdecay = -lf/(hz*Math.log(y));
			st = hz*dur;
			if(dur<tdecay) {
				loss = Math.exp(-lf/st)/y;
				stretch = 0.5;
			} else {
				loss = 1;
				stretch = 0.5+Math.sqrt(0.25-(1-Math.exp(2*lf*(hz-sr)/(st*sr)))/(2-2*Math.cos(2*t)));
			}
			len = (int)((sr/hz)-stretch-0.001);
			x = (sr/hz)-len-stretch;
			cons = (1-x)/(1+x);
			
			// Setup other vars
			x2 = -cons*(stretch-1);
			x3 = cons*stretch-stretch+1;
			shiftreg = new double[len+4];
			i1 = len+1;
			i2 = len;
			i3 = len-1;
			i4 = len-2;
			endptr = len+2;
			
			// Pluck initialize
			//initializeWhiteNoise();
			//initializeBrownNoise();
			//initializePinkNoise();
			//initializeSawtoothWave();
			//initializeParabolicWave();
			//initializeParabolicWave2();
			initializeParabolicWave3();
		}
		public void initializeSawtoothWave() {
			// should have 1/f frequency spectrum, similar to pink noise
			// power per overtone dropping by 3dB/octave and 10dB/decade
			// deterministic, so will always produce the exact same sound
			// TODO: why does the spectrum in audacity shows 1/f^2 for this? Maybe the value is getting doubled somehow? But it analyzes its own noise correctly. 
			// TODO: may need some adjustment since the smoothing loop does not have exactly the same length as the period of the waveform
			for(int k=0; k<=len; k++) {
				double phase = k*1.0/len;
				shiftreg[k] = 1.8*((phase+0.5)%1.0)-0.9;
			}
		}
		public void initializeParabolicWave() {
			// should have 1/f^2 frequency spectrum, similar to pink noise
			// power per overtone dropping by 6dB/octave and 20dB/decade
			// deterministic, so will always produce the exact same sound
			for(int k=0; k<=len+1; k++) {
				double phase = k*1.0/(len+1);
				double parabola = (phase*2-1)*(phase*2-1);
				shiftreg[k] = (parabola*3-1)*0.49;
			}
		}
		public void initializeParabolicWave2() {
			// adjusted implementation to start and end at 0 amplitude
			for(int k=0; k<=len+1; k++) {
				double phase = k*1.0/(len+1);
				phase = (phase+0.5*(1.0-1.0/Math.sqrt(3)))%1.0;
				double parabola = (phase*2-1)*(phase*2-1);
				shiftreg[k] = (parabola*3-1)*0.49;
			}
		}
		public void initializeParabolicWave3() {
			// incorporate a small amount of sawtooth wave for balance
			for(int k=0; k<=len+1; k++) {
				double phase1 = k*1.0/(len+1);
				double phase2 = (phase1+0.5*(1.0-1.0/Math.sqrt(3)))%1.0;
				double parabola = (phase2*2-1)*(phase2*2-1);
				parabola = (parabola*3-1)*0.49;
				double sawtooth = phase1;
				shiftreg[k] = parabola*0.6+sawtooth*0.4;
			}
		}
		public void initializeWhiteNoise() {
			// should have flat frequency spectrum on average
			// particular frequencies will have random power around a central range
			int sum = 0;
			for(int k=0; k<len; k++) {
				shiftreg[k] = rand.nextInt(2)*1.8-0.9;
				sum += shiftreg[k];
			}
			double average = sum*1.0/len;
			for(int k=0; k<len; k++) {
				shiftreg[k] -= average;
			}
		}
		public void initializePinkNoise() {
			// should have 1/f frequency spectrum
			// power per overtone dropping by 3dB/octave and 10dB/decade
			// https://github.com/audacity/audacity/blob/deaa3f157ee38c2a82f87c39a63cfdd71f0f5c36/src/effects/Noise.cpp
			// https://www.firstpr.com.au/dsp/pink-noise/
			double mAmp = 0.99; // ?????
			double amplitude = mAmp*0.129;
			double buf0 = 0;
			double buf1 = 0;
			double buf2 = 0;
			double buf3 = 0;
			double buf4 = 0;
			double buf5 = 0;
			double buf6 = 0;
			int sum = 0;
			for(int k=0; k<len; k++) {
				double white = rand.nextDouble()*2-1;
				buf0 = 0.99886*buf0+0.0555179*white;
				buf1 = 0.99332*buf1+0.0750759*white;
				buf2 = 0.96900*buf2+0.1538520*white;
				buf3 = 0.86650*buf3+0.3104856*white;
				buf4 = 0.55000*buf4+0.5329522*white;
				buf5 = -0.7616*buf5-0.0168980*white;
				double buffer = amplitude*(buf0+buf1+buf2+buf3+buf4+buf5+buf6+white*0.5362);
				buf6 = white*0.115926;
				shiftreg[k] = buffer;
				sum += shiftreg[k];
			}
			double average = sum*1.0/len;
			for(int k=0; k<len; k++) {
				shiftreg[k] -= average;
			}
		}
		public void initializeBrownNoise() {
			// https://github.com/audacity/audacity/blob/deaa3f157ee38c2a82f87c39a63cfdd71f0f5c36/src/effects/Noise.cpp
			// should have 1/f^2 frequency spectrum
			// power per overtone dropping by 6dB/octave and 20dB/decade
			double mAmp = 0.99; // ?????
			double mSampleRate = 44100;
			double leakage = (mSampleRate-144)/mSampleRate;
			double scaling = 9/Math.sqrt(mSampleRate);
			double y = 0;
			double z = 0;
			int sum = 0;
			for(int k=0; k<len; k++) {
				double white = rand.nextDouble()*2-1;
				z = leakage*y+white*scaling;
				y = Math.abs(z)>1.0 ? leakage*y-white*scaling : z;
				shiftreg[k] = mAmp*y;
				sum += shiftreg[k];
			}
			double average = sum*1.0/len;
			for(int k=0; k<len; k++) {
				shiftreg[k] -= average;
			}
		}
		public double getNext() {
			double sum = x2*shiftreg[i1]+x3*shiftreg[i2]+stretch*shiftreg[i3]-cons*shiftreg[i4];
			i1 = (i1+1)%endptr;
			i2 = (i2+1)%endptr;
			i3 = (i3+1)%endptr;
			i4 = (i4+1)%endptr;
			shiftreg[i4] = sum*loss;
			return sum;
		}
	}
	public static Map<Integer,AudacityPluckData> audacityPluckDataMap = new HashMap<>();
	public static double getAudacityPluck(double start, double end, double frequency, int key, double time) {
		// https://github.com/audacity/audacity/blob/95a7fc6362729594565204b6db927b2e025d20c0/lib-src/libnyquist/nyquist/tran/pluck.c
		// TODO: how are the values calculated for the seed wavelet?
		// TODO: where can I find the original implementation that this code refers to?
		// TODO: implement a gentler/slower onset (ramp up over 0.01 seconds?)
		if(time<=start || time>=end) return 0;
		double sr = 44100;
		double hz = frequency; // 200
		double dur = end-start; // 2
		double decayRatio = 0.4; // 0.001; // TODO: bring this as an optional parameter
		if(key==-1) { // TODO: make this automatic hashing calculation more robust
			key = ((int)frequency)%1000+(((int)(start*10))%10000)*1000;
		}
		AudacityPluckData data;
		if(audacityPluckDataMap.containsKey(key)) {
			data = audacityPluckDataMap.get(key);
		} else {
			data = new AudacityPluckData(sr,hz,dur,decayRatio);
			audacityPluckDataMap.put(key,data);
		}
		double wave = data.getNext();
		// leaving control of the onset of the sound to the AudacityPluckData class for now
		// TODO: experiment with extending the sound slightly past the recommended end time
		double envelope = 1;
		if(end-time<0.001) envelope = (end-time)*1000;
		return wave*envelope;
	}
	public static double getAudacityPluckStandardNote(double start, double end, String note, int key, double time) {
		setupStandardNoteFrequencies();
		double frequency = standardNoteFrequency.get(note);
		return getAudacityPluck(start,end,frequency,key,time);
	}
	public static double getAudacityPluckQuarterToneNote(double start, double end, String note, int key, double time) {
		setupStandardNoteFrequencies();
		// shifting up by one even tempered quarter tone = 2^(1/24)
		double frequency = standardNoteFrequency.get(note)*1.02930223664;
		return getAudacityPluck(start,end,frequency,key,time);
	}
	public static double getPluck(String note, double start, double end, double time) {
		if(time<=start || time>=end) return 0;
		setupStandardNoteFrequencies();
		double durationInTime = end-start;
		double timeInNote = time-start;
		double timeLeftInNote = end-time;
		double frequency = standardNoteFrequency.get(note);
		double cycle = 2*Math.PI*frequency;
		double wave = 0;
		wave += 0.5*Math.cos(cycle*timeInNote);
		wave += 0.25*Math.cos(cycle*timeInNote*2);
		wave += 0.15*Math.cos(cycle*timeInNote*3);
		wave += 0.05*Math.cos(cycle*timeInNote*4);
		wave += 0.01*Math.cos(cycle*timeInNote*5);
		wave += 0.01*Math.cos(cycle*timeInNote*6);
		wave += 0.01*Math.cos(cycle*timeInNote*7);
		wave += 0.01*Math.cos(cycle*timeInNote*8);
		wave += 0.01*Math.cos(cycle*timeInNote*9);
		double envelope = 0;
		if(timeInNote<0.01) envelope = timeInNote*(1.0/0.01);
		else if(timeInNote<0.02) envelope = 1-(timeInNote-0.01)*(0.7/0.01);
		else if(timeInNote<0.5) envelope = 0.3-(timeInNote-0.02)*(0.2/0.48);
		else if(timeInNote<1) envelope = 0.1-(timeInNote-0.5)*(0.1/0.5);
		if(durationInTime<1 && timeLeftInNote<0.01) envelope = Math.min(envelope,timeLeftInNote*100);
		return wave*envelope;
	}
	public static double getSawtoothWavelet(double time) {
		if(time<2) return 0;
		if(time<2.001) return (time-2)*1000;
		if(time<2.004) return 1-(time-2.001)*(2000/3.0);
		if(time<2.005) return -1+(time-2.004)*1000;
		return 0;
	}
	public static double getWavelet(double peakTime, double duration, double frequency, double time) {
		double timeDelta = (time-peakTime)/duration;
		if(timeDelta<=-0.5 || timeDelta >= 0.5) return 0;
		// https://en.wikipedia.org/wiki/Morlet_wavelet
		// https://en.wikipedia.org/wiki/Gaussian_function#:~:text=In%20mathematics%2C%20a%20Gaussian%20function,symmetric%20%22bell%20curve%22%20shape.
		double wave = Math.cos(2*Math.PI*frequency*time);
		double spread = 3.0;
		double gaussian = Math.exp(-0.5*(timeDelta*timeDelta)*(4*spread*spread));
		double envelope = 1;
		if(timeDelta<-0.49) envelope = (timeDelta+0.5)*100;
		if(timeDelta>0.49) envelope = (0.5-timeDelta)*100;
		return wave*gaussian*envelope;
	}
	// TODO: is this random number generator well enough distributed for signal processing applications?
	public static Random rand = new Random(0);
	public static double getWhiteNoise(double start, double end, double time) {
		if(time<=start || time>=end) return 0;
		// Generate "gaussian white noise"
		double amplitude = 0.1;//0.01;
		double value = rand.nextGaussian()*amplitude;
		double durationInTime = end-start;
		double timeInNote = time-start;
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		return Math.min(Math.max(-0.9,value*envelope),0.9);
	}
	// can only support one channel of tuned brown noise at a time
	// 441 samples for 100hz fundamental tone
	public static double[] tunedBrownNoiseData = new double[441];
	public static double getTunedBrownNoise(double time) {
		int sampleRate = 44100;
		int index = (int)(time*sampleRate+0.5);
		int key = index%441;
		int lastKey = (key-1+441)%441;
		double oldValue = tunedBrownNoiseData[key];
		double lastValue = tunedBrownNoiseData[lastKey];
		rand.nextGaussian();
		//double value = oldValue*0.1+lastValue*0.89+rand.nextGaussian()*0.1;
		double value = 0;
		if(time<=1) {
			value = lastValue*0.99+rand.nextGaussian()*0.01;
		} else if(time<5) {
			value = oldValue*0.95+lastValue*0.05;
		} else {
			value = oldValue*0.5+lastValue*0.5;
		}
		value = Math.min(Math.max(value,-0.9),0.9);
		tunedBrownNoiseData[key] = value;
		return value;
	}
	public static double getTunedBrownNoise2(double time) {
		double value = getBrownNoise(0,24,0,time);
		double wave = Math.cos(2*Math.PI*432*time);
		return value*wave;
	}
	public static Map<Integer,Double> brownNoiseData = new HashMap<>();
	public static double getBrownNoise(double start, double end, int key, double time) {
		// https://en.wikipedia.org/wiki/Brownian_noise
		if(time<=start || time>=end) return 0;
		double lastValue = brownNoiseData.getOrDefault(key,0.0);
		double value = lastValue*0.99+rand.nextGaussian()*0.01;
		value = Math.min(Math.max(value,-0.9),0.9);
		brownNoiseData.put(key,value);
		double durationInTime = end-start;
		double timeInNote = time-start;
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		return value*envelope;
	}
	public static class AudacityPinkNoiseData {
		public double amplitude = 0;
		public double buf0 = 0;
		public double buf1 = 0;
		public double buf2 = 0;
		public double buf3 = 0;
		public double buf4 = 0;
		public double buf5 = 0;
		public double buf6 = 0;
	}
	public static AudacityPinkNoiseData audacityPinkNoiseData = null;
	public static double getAudacityPinkNoise(double time) {
		// https://github.com/audacity/audacity/blob/deaa3f157ee38c2a82f87c39a63cfdd71f0f5c36/src/effects/Noise.cpp
		// https://www.firstpr.com.au/dsp/pink-noise/
		if(audacityPinkNoiseData==null) {
			audacityPinkNoiseData = new AudacityPinkNoiseData();
			double mAmp = 0.99; // ?????
			audacityPinkNoiseData.amplitude = mAmp*0.129;
		}
		AudacityPinkNoiseData data = audacityPinkNoiseData;
		double white = rand.nextDouble()*2-1;
		data.buf0 = 0.99886*data.buf0+0.0555179*white;
		data.buf1 = 0.99332*data.buf1+0.0750759*white;
		data.buf2 = 0.96900*data.buf2+0.1538520*white;
		data.buf3 = 0.86650*data.buf3+0.3104856*white;
		data.buf4 = 0.55000*data.buf4+0.5329522*white;
		data.buf5 = -0.7616*data.buf5-0.0168980*white;
		double buffer = data.amplitude*(data.buf0+data.buf1+data.buf2+data.buf3+data.buf4+data.buf5+data.buf6+white*0.5362);
		data.buf6 = white*0.115926;
		return buffer;
	}
	public static double getPinkNoise(double start, double end, double time) {
		if(time<=start || time>=end) return 0;
		// This and the other pinkNoise functions will need to change if the sample rate does
		double sampleRate = 44100;
		double amplitude = 10.0;
		double durationInTime = end-start;
		double timeInNote = time-start;
		// This will go out of bounds if the duration lasts longer than the loaded sample
		int index = (int)(timeInNote*sampleRate+0.5);
		double value = pinkNoiseData[index];
		double envelope = 1;
		if(timeInNote<0.01) envelope = timeInNote*100;
		if(timeInNote>durationInTime-0.01) envelope = (durationInTime-timeInNote)*100;
		return value*envelope*amplitude;
	}
	public static void createPinkNoise() throws Exception {
		// https://stackoverflow.com/questions/616897/how-can-i-make-a-pink-noise-generator
		// had longs for variables samples and i, but caused lossy conversion to int error for array index
		int seconds = 30;
		int quality = 5000;
		double lowestFrequency = 20;
		double highestFrequency = 20000;
		double volumeAdjust = 1.0;
		int sampleRate = 44100;
		int samples = sampleRate * seconds;
		double[] d = new double[samples];
		double[] offsets = new double[samples];
		//double lowestWavelength = highestFrequency / lowestFrequency;
		double initialPitch = Math.log(lowestFrequency);
		double limitPitch = Math.log(highestFrequency);
		for (int j = 0; j < quality; j++) {
			System.out.println(j);
			double pitch = (limitPitch-initialPitch)*(j*1.0/quality)+initialPitch;
			double frequency = Math.exp(pitch);
			//double wavelength = Math.pow(lowestWavelength, (j * 1.0) / quality)  * sampleRate / highestFrequency;
			//double frequency = 1/wavelength;
			// Important offset is needed, as otherwise all the waves will be almost in phase, and this will ruin the effect!
			double offset = rand.nextDouble() * Math.PI*2;
			for (int i = 0; i < samples; i++) {
				d[i] += Math.cos(i * Math.PI * 2 * frequency / sampleRate + offset) / quality * volumeAdjust;
			}
		}
		BufferedWriter out = new BufferedWriter(new FileWriter("./pinknoise_gitignore.txt"));
		out.write(d.length+"\n");
		for(int k=0; k<d.length; k++) {
			out.write(k+","+d[k]+"\n");
		}
		out.close();
	}
	public static double[] pinkNoiseData = null;
	public static void loadPinkNoise() throws Exception {
		if(pinkNoiseData!=null) return;
		Scanner scan = new Scanner(new File("./pinknoise_gitignore.txt"));
		int samples = Integer.parseInt(scan.nextLine());
		pinkNoiseData = new double[samples];
		for(int k=0; k<samples; k++) {
			String line = scan.nextLine();
			String[] fields = line.split(",");
			pinkNoiseData[k] = Double.parseDouble(fields[1]);
		}
	}
	public static double getBellsMusic(double time) {
		double total = 0;
		total += getBell(200,0,1,time); // 1
		total += getBell(400,1,2,time); // 8
		total += getBell(300,2,3,time); // 5
		total += getBell(225,3,4,time); // 2
		total += getBell(233,4,5,time); // 3m
		total += getBell(300,5,6,time); // 5
		total += getBell(200,6,8,time); // 1
		total += getBell(400,6,8,time); // 8
		total += getBell(267,8,9,time); // 4
		total += getBell(250,9,9.5,time); // 3
		total += getBell(267,9.5,10,time); // 4
		total += getBell(225,10,11,time); // 2
		total += getBell(267,11,12,time); // 4
		total += getBell(250,12,12.5,time); // 3
		total += getBell(267,12.5,13,time); // 4
		total += getBell(225,13,14,time); // 2
		total += getBell(267,14,15,time); // 4
		total += getBell(250,15,15.5,time); // 3
		total += getBell(267,15.5,16,time); // 4
		total += getBell(225,16,18,time); // 2
		total += getBell(200,18,22,time); // 1
		total += getBell(300,18,22,time); // 5
		total += getBell(400,18,22,time); // 8
		return total;
	}
	public static double getBell(double frequency, double start, double end, double time) {
		// http://acoustics.ae.illinois.edu/pdfs/Vibration%20of%20Plates%20(Leissa,%20NASA%20SP-160).pdf
		if(time<=start || time>=end) return 0;
		double durationInTime = end-start;
		double timeInNote = time-start;
		double amplitude = 0.3;
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
	public static double getBell2(double frequency, double start, double end, double time) {
		// TODO: create some bell sounds with overtones like below and with various decay patterns
		// https://en.wikipedia.org/wiki/Strike_tone
		return 0;
	}
	public static double getRissetDrum(double time) {
		// TODO: copy out the algorithm from audacity
		// https://github.com/audacity/audacity/blob/95a7fc6362729594565204b6db927b2e025d20c0/plug-ins/rissetdrum.ny
		return 0;
	}
	public static double getEnumeratedGuitarChord(String chord, double start, double end, double time) {
		// TODO: this runs pretty slow
		if(time<start || time>end) return 0;
		double value = 0;
		double offset = 0.02;
		String[] notes = chord.split(",");
		for(int k=0; k<notes.length; k++) {
			value += 0.2*getAudacityPluckStandardNote(start+offset*k,end,notes[k],-1,time);
		}
		return Math.max(Math.min(0.5*value,0.75),-0.75);
	}
	public static double getOpenEmGuitarChord(String name, double start, double end, double time) {
		// TODO: this runs pretty slow
		if(time<start || time>end) return 0;
		double value = 0;
		double offset = 0.02;
		setupOpenEmGuitarChords();
		String chord = openEmGuitarChord.get(name);
		String[] notes = chord.split(",");
		for(int k=0; k<notes.length; k++) {
			value += 0.2*getAudacityPluckStandardNote(start+offset*k,end,notes[k],-1,time);
		}
		return Math.max(Math.min(0.5*value,0.75),-0.75);
	}
	public static Map<String,String> openEmGuitarChord = null;
	public static void setupOpenEmGuitarChords() {
		if(openEmGuitarChord!=null) return;
		openEmGuitarChord = new HashMap<>();
		openEmGuitarChord.put("Em","2E,2B,3E,3G,3B,4E");
		openEmGuitarChord.put("Fm","2F,3C,3F,3Ab,4C,4F");
		openEmGuitarChord.put("Gm","2G,3D,3G,3Bb,4D,4G");
		openEmGuitarChord.put("Am","2A,3E,3A,4C,4E,4A");
		openEmGuitarChord.put("Bm","2B,3F#,3B,4D,4F#,4B");
		openEmGuitarChord.put("Cm","3C,3G,4C,3Eb,4G,5C");
		openEmGuitarChord.put("C#m","3C#,3G#,4C#,3E,4G#,5C#");
		openEmGuitarChord.put("Dm","3D,3A,4D,3F,4A,5D");
		openEmGuitarChord.put("Em2","3E,3B,4E,4G,4B,5E");
	}
	public static double getOpenEGuitarChord(String name, double start, double end, double time) {
		// TODO: this runs pretty slow
		if(time<start || time>end) return 0;
		double value = 0;
		double offset = 0.02;
		setupOpenEGuitarChords();
		String chord = openEGuitarChord.get(name);
		String[] notes = chord.split(",");
		for(int k=0; k<notes.length; k++) {
			value += 0.2*getAudacityPluckStandardNote(start+offset*k,end,notes[k],-1,time);
		}
		return Math.max(Math.min(0.5*value,0.75),-0.75);
	}
	public static Map<String,String> openEGuitarChord = null;
	public static void setupOpenEGuitarChords() {
		if(openEGuitarChord!=null) return;
		openEGuitarChord = new HashMap<>();
		openEGuitarChord.put("E","2E,2B,3E,3G#,3B,4E");
		openEGuitarChord.put("F","2F,3C,3F,3A,4C,4F");
		openEGuitarChord.put("G","2G,3D,3G,3B,4D,4G");
		openEGuitarChord.put("A","2A,3E,3A,4C#,4E,4A");
		openEGuitarChord.put("B","2B,3F#,3B,4D#,4F#,4B");
		openEGuitarChord.put("C","3C,3G,4C,3E,4G,5C");
		openEGuitarChord.put("D","3D,3A,4D,3F#,4A,5D");
		openEGuitarChord.put("E2","3E,3B,4E,4G#,4B,5E");
	}
	public static double getBasicGuitarChord(String name, double start, double end, double time) {
		// TODO: this runs pretty slow
		if(time<start || time>end) return 0;
		double value = 0;
		double offset = 0.02;
		//double offset = 0;
		setupStandardGuitarChords();
		String chord = standardGuitarChord.get(name);
		String[] notes = chord.split(",");
		for(int k=0; k<notes.length; k++) {
			//value += getStandardNote(notes[k],start+offset*k,end,time);
			//value += getStandardDecayingNote(notes[k],0.5,start+offset*k,end,time);
			//value += getStandardDecayingNote2(notes[k],start+offset*k,end,time);
			//value += getPluck(notes[k],start+offset*k,end,time);
			value += 0.2*getAudacityPluckStandardNote(start+offset*k,end,notes[k],-1,time);
			//value += 0.2*getAudacityPluckStandardNote(start+offset*k,end+1,notes[k],-1,time);
		}
		return Math.max(Math.min(0.5*value,0.75),-0.75);
	}
	public static double getQuarterToneGuitarChord(String name, double start, double end, double time) {
		// TODO: this runs pretty slow
		// TODO: come up with a better set of parameters for this function
		if(time<start || time>end) return 0;
		double value = 0;
		double offset = 0.02;
		setupStandardGuitarChords();
		String chord = standardGuitarChord.get(name);
		String[] notes = chord.split(",");
		for(int k=0; k<notes.length; k++) {
			value += 0.2*getAudacityPluckQuarterToneNote(start+offset*k,end,notes[k],-1,time);
		}
		return Math.max(Math.min(0.5*value,0.75),-0.75);
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
		// https://www.amazon.com/PRACTICAL-GUITAR-CHORD-BOARD-CHART/dp/B0785QMRQP
		if(standardGuitarChord!=null) return;
		standardGuitarChord = new HashMap<>();
		standardGuitarChord.put("C","3C,3E,3G,4C,4E");
		//standardGuitarChord.put("C7","3C,3E,3Bb,4C,4E"); // TODO: Shouldn't this have a G?
		standardGuitarChord.put("C7","3C,3E,3G,3Bb,4E");
		standardGuitarChord.put("Dm","3D,3A,4D,4F");
		standardGuitarChord.put("D","3D,3A,4D,4F#");
		standardGuitarChord.put("D7","3D,3A,4C,4F#");
		standardGuitarChord.put("Em","2E,2B,3E,3G,3B,4E");
		standardGuitarChord.put("E7","2E,2B,3E,3G#,4D,4E");
		standardGuitarChord.put("F","2F,3C,3F,3A,4C,4F");
		//standardGuitarChord.put("F7","2F,3C,3E,3A,4E,4F"); // TODO: Is the top E too close to the F?
		standardGuitarChord.put("F7","2F,3C,3F,3A,4C,4E");
		standardGuitarChord.put("G","2G,2B,3D,3G,3B,4G");
		standardGuitarChord.put("G7","2G,2B,3D,3G,3B,4F");
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
		//value += 0.5*getNote(98, 1000000, time); // G
		//value += 0.5*getNote(110, 1000000, time); // A
		//value += 0.5*getNote(116.54, 1000000, time); // Bb
		//value += 0.5*getNote(123, 1000000, time); // B
		//value += 0.5*getNote(146.83, 1000000, time); // D
		//value += 0.5*getNote(164.81, 1000000, time); // E
		//value += 0.5*getNote(196, 1000000, time); // G
		//value += 0.5*getNote(220, 1000000, time); // A
		//value += 0.5*getNote(247, 1000000, time); // B
		//value += 0.5*getNote(329.63, 1000000, time); // E
		
		value += getNote(400, 1000000, time);
		//value += getNote(503.968, 1000000, time);
		value += getNote(495, 1000000, time);
		
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
	}
	public static void fillJumpsList18(List<JumpsAndBendsHelper> jumps) {
		// Amazing Grace
		// https://www.sheetmusicdirect.com/en-US/se/ID_No/436700/Product.aspx?currency=USD&utm_source=google&utm_medium=cpc&adpos=&scid=scplp436700&sc_intid=436700&gclid=Cj0KCQiA_c-OBhDFARIsAIFg3ezsET6TSJYUmxEEZMREQC4WQKS-ki3WlYqdE_R1UyNhig97YVaDlV0aAh1MEALw_wcB
		
		setupStandardNoteFrequencies();
		double G = standardNoteFrequency.get("4G");
		double A = standardNoteFrequency.get("4A");
		double C = standardNoteFrequency.get("5C");
		double D = standardNoteFrequency.get("5D");
		double E = standardNoteFrequency.get("5E");
		double F = standardNoteFrequency.get("5F");
		double G2 = standardNoteFrequency.get("5G");
		double W = 0.00579294107; // 10 cents
		// straight ahead
		jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,D,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,A,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.01,G*0.9,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.166,D,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.166,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.166,F,"jump"));
		jumps.add(new JumpsAndBendsHelper(2.5,G2,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,G2,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.01,G2*0.9,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,G2,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,A,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,A,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.01,G*0.9,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,D,"jump"));
		jumps.add(new JumpsAndBendsHelper(2.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
		// with bends and vibrato
		jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,C,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,E/(1+W),E*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.15,D,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.35,D,C,"bend"));
		fillJumpsList_vibrato(jumps,0.2,5,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.5,A,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,G/(1+W),G*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.01,G*0.5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,C,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,E/(1+W),E*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.15,D,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.35,D,E,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.15,F,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.35,F,G2,"bend"));
		fillJumpsList_vibrato(jumps,0.2,10,G2/(1+W),G2*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.5,E,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,G2/(1+W),G2*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.01,G2*0.5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,G2,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.15,A,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.35,A,G,"bend"));
		fillJumpsList_vibrato(jumps,0.2,5,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.15,A,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.35,A,G,"bend"));
		fillJumpsList_vibrato(jumps,0.2,5,G/(1+W),G*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.01,G*0.5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.25,E,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.25,C,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,E/(1+W),E*(1+W));
		jumps.add(new JumpsAndBendsHelper(0.15,D,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.35,D,C,"bend"));
		fillJumpsList_vibrato(jumps,0.2,10,C/(1+W),C);
		jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
	}
	public static void fillJumpsList19(List<JumpsAndBendsHelper> jumps) {
		// TODO: add chords under this (F C Bb)
		// Go Easy On Me
		// https://www.youtube.com/watch?v=njyCxQxR41M
		
		setupStandardNoteFrequencies();
		double Q = 1.02930223664; // 50 cents
		double F = standardNoteFrequency.get("5F");
		double G = standardNoteFrequency.get("5G");
		double A = standardNoteFrequency.get("5A");
		double Bb = standardNoteFrequency.get("5Bb");
		double Bhf = standardNoteFrequency.get("5Bb")*Q;
		double C = standardNoteFrequency.get("6C");
		
		for(int n=0; n<4; n++) {
			// straight ahead
			jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
			for(int k=0; k<8; k++) {
				jumps.add(new JumpsAndBendsHelper(0.05,F,"jump"));
				jumps.add(new JumpsAndBendsHelper(0.05,A,"jump"));
				jumps.add(new JumpsAndBendsHelper(0.05,C,"jump"));
			}
			jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
			jumps.add(new JumpsAndBendsHelper(1,F,"jump"));
			jumps.add(new JumpsAndBendsHelper(1.5,C,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,Bb,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,C,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,Bb,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.3,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.4,Bb,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,Bb,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.3,G,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.4,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.01,A/2,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.25,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.01,A/2,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.5,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.25,G,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.01,G/2,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.25,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
			// with microtonal half-sharp 4th
			jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
			for(int k=0; k<8; k++) {
				jumps.add(new JumpsAndBendsHelper(0.05,F,"jump"));
				jumps.add(new JumpsAndBendsHelper(0.05,A,"jump"));
				jumps.add(new JumpsAndBendsHelper(0.05,C,"jump"));
			}
			jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
			jumps.add(new JumpsAndBendsHelper(1,F,"jump"));
			jumps.add(new JumpsAndBendsHelper(1.5,C,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,Bhf,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,C,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,Bhf,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.3,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.4,Bhf,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,Bhf,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.1,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.3,G,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.4,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.01,A/2,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.25,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.01,A/2,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.5,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.25,G,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.01,G/2,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.5,G,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.25,A,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
		}
	}
	public static void fillJumpsList20(List<JumpsAndBendsHelper> jumps) {
		// https://www.youtube.com/watch?v=NVWX10c0bNc
		// Key of A microtonal blues scale using as many just intervals as possible
		setupStandardNoteFrequencies();
		double T1 = standardNoteFrequency.get("4A"); // tonic - 440hz
		double T2 = standardNoteFrequency.get("4B"); // 12TET second
		double T3u = standardNoteFrequency.get("5C#"); // 12TET major third
		double T3d = standardNoteFrequency.get("5C"); // 12TET minor third
		double T4 = standardNoteFrequency.get("5D"); // 12TET fourth
		double Ttt = standardNoteFrequency.get("5Eb"); // 12TET tritone
		double T5 = standardNoteFrequency.get("5E"); // 12TET fifth
		double T7d = standardNoteFrequency.get("5G"); // 12TET minor seventh
		double T8 = standardNoteFrequency.get("5A"); // tonic - 880hz
		double M3u = T1*1.25; // 386 cents - just major third (5/4)
		double M3n = T1*1.222; // 347 cents - just neutral third (11/9)
		double M3d = T1*1.2; // 316 cents - just minor third (6/5)
		double M4u = T1*1.4; // 583 cents - just sub-tritone (7/5)
		double M4n = T1*1.375; // 551 cents - just neutral fourth (11/8)
		double M4d = T1*1.3636; // 537 cents - just sub-neutral fourth (15/11)
		double M5n = T1*1.4545; // 649 cents - just neutral fifth (16/11)
		double M7h = T1*1.4286; // 969 cents - just harmonic seventh (7/4)
		double M7d = T1*1.8; // 1018 cents - just minor seventh (9/5)
		
		jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.5,T1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,T2,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,M4n,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,T5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,M4n,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,T2,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,T1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,10,"jump")); // rest
	}
	public static void fillJumpsList21(List<JumpsAndBendsHelper> jumps) {
		// goes with getAeroplaneChords()
		// based on https://www.hooktheory.com/hookpad/view/JkmZlD_jxqn
		
		setupStandardNoteFrequencies();
		double FH = standardNoteFrequency.get("5F");
		double EH = standardNoteFrequency.get("5E");
		double DH = standardNoteFrequency.get("5D");
		double CH = standardNoteFrequency.get("5C");
		double BL = standardNoteFrequency.get("4B");
		double AL = standardNoteFrequency.get("4A");
		double GL = standardNoteFrequency.get("4G");
		double FL = standardNoteFrequency.get("4F");
		double EL = standardNoteFrequency.get("4E");
		
		jumps.add(new JumpsAndBendsHelper(0.4*6*4,10,"jump")); // rest
		//jumps.add(new JumpsAndBendsHelper(1.200,EL,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.200,10,"jump")); // rest
		for(int k=0; k<4; k++) {
			jumps.add(new JumpsAndBendsHelper(0.400,CH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,EH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.266,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,CH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.666,EH,"jump"));
			
			jumps.add(new JumpsAndBendsHelper(0.100,EH,"jump")); // ???
			jumps.add(new JumpsAndBendsHelper(2.300,10,"jump")); // rest
			
			jumps.add(new JumpsAndBendsHelper(0.400,CH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,EH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.266,FH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.800,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.100,DH,"jump")); // ???
			
			jumps.add(new JumpsAndBendsHelper(2.433,10,"jump")); // rest
			jumps.add(new JumpsAndBendsHelper(0.267,CH,"jump"));
			
			jumps.add(new JumpsAndBendsHelper(0.400,CH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,EH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.266,DH,"jump"));
			
			jumps.add(new JumpsAndBendsHelper(0.533,EH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.533,DH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.534,CH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.100,CH,"jump")); // ???
			jumps.add(new JumpsAndBendsHelper(0.700,10,"jump")); // rest
			
			jumps.add(new JumpsAndBendsHelper(0.933,10,"jump")); // rest
			jumps.add(new JumpsAndBendsHelper(0.267,CH,"jump"));
			
			jumps.add(new JumpsAndBendsHelper(0.400,CH,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,BL,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.267,AL,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.266,BL,"jump"));
			
			jumps.add(new JumpsAndBendsHelper(0.800,GL,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.400,GL,"jump")); // ???
			
			jumps.add(new JumpsAndBendsHelper(0.800,FL,"jump"));
			jumps.add(new JumpsAndBendsHelper(0.400,FL,"jump")); // ???
			
			jumps.add(new JumpsAndBendsHelper(1.200,EL,"jump"));
		}
		
	}
	public static void fillJumpsList22(List<JumpsAndBendsHelper> jumps) {
		// original blues riffs hitting many quarter notes
		setupStandardNoteFrequencies();
		double qs = Math.pow(2,1.0/24);
		double n7f = standardNoteFrequency.get("5D");
		double n1 = standardNoteFrequency.get("5E");
		double n0 = standardNoteFrequency.get("4E");
		double n2f = standardNoteFrequency.get("5F");
		double n2 = standardNoteFrequency.get("5F#");
		double n3f = standardNoteFrequency.get("5G");
		double n3 = standardNoteFrequency.get("5G#");
		double n4 = standardNoteFrequency.get("5A");
		
		double n2hf = n2/qs;
		double n2hs = n2*qs;
		double n3hf = n3/qs;
		double n4hs = n4*qs;
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,n1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,n1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,n1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,n3f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,n1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,n3f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,n1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,n2hf,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,n3hf,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,n2hs,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,n1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,n1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,n1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,n1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,n3f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,n1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.1,n0,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,n1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,n4hs,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.3,n4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,n1,"jump"));
		
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
	}
	public static void fillJumpsList23(List<JumpsAndBendsHelper> jumps) {
		// original blues riffs hitting many quarter notes
		setupStandardNoteFrequencies();
		double m1 = standardNoteFrequency.get("3D");
		double m2 = m1*Math.pow(2,4.0/24);
		double m2s = m1*Math.pow(2,5.0/24);
		double m4f = m1*Math.pow(2,9.0/24);
		double m4 = m1*Math.pow(2,10.0/24);
		double m5 = m1*Math.pow(2,14.0/24);
		double m5s = m1*Math.pow(2,15.0/24);
		double m7f = m1*Math.pow(2,19.0/24);
		double m7 = m1*Math.pow(2,20.0/24);
		double h1 = m1*2;
		double h2 = m2*2;
		double h2s = m2s*2;
		double h4f = m4f*2;
		double h4 = m4*2;
		double h5 = m5*2;
		double h5s = m5s*2;
		double h7f = m7f*2;
		double h7 = m7*2;
		double h8 = h1*2;
		double l1 = m1/2;
		double l2 = m2/2;
		double l2s = m2s/2;
		double l4f = m4f/2;
		double l4 = m4/2;
		double l5 = m5/2;
		double l5s = m5s/2;
		double l7f = m7f/2;
		double l7 = m7/2;
		double hh1 = m1*4;
		double hh2 = m2*4;
		double hh2s = m2s*4;
		double hh4f = m4f*4;
		double hh4 = m4*4;
		double hh5 = m5*4;
		double hh5s = m5s*4;
		double hh7f = m7f*4;
		double hh7 = m7*4;
		double hh8 = h8*2;
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.1,l5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,m1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,l5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,m1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,l5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,m1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.1,m5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,m5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.1,m5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,h1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,m5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,m7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,m5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,m7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,m7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,h1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,m7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,m7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h2s,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h2s,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,h1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,h4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,h5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,hh1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,hh2s,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,hh1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,h4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,h5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,hh1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,hh2s,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,hh1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
		jumps.add(new JumpsAndBendsHelper(0.4,hh1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,h7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,h4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,h5,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,h1,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,m7f,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,h1,"jump"));
		
		jumps.add(new JumpsAndBendsHelper(0.4,10,"jump")); // rest
		
	}
	public static void fillJumpsList24(List<JumpsAndBendsHelper> jumps) {
		// funkpunk - Microtonal Blues Scales p.2 ["Majorish 4-7" scale]
		// https://www.youtube.com/watch?v=73ejjQ37WLc
		
		setupStandardNoteFrequencies();
		double ss = Math.pow(2,0.86/12);
		double G = standardNoteFrequency.get("3G");
		double G3 = standardNoteFrequency.get("3Bb");
		double G3ss = G3*ss;
		double Gh = G*2;
		double Gh3 = G3*2;
		double Gh3ss = G3ss*2;
		double fourth = Math.pow(2,5.0/12);
		double C = G*fourth;
		double C3 = G3*fourth;
		double C3ss = G3ss*fourth;
		double C4 = C*fourth;
		double Ch = Gh*fourth;
		double Ch3 = Gh3*fourth;
		double Ch3ss = Gh3ss*fourth;
		double W = 0.00579294107; // 10 cents
		double W2 = 0.00057778951; // 1 cent
		
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		
		// 01:20 - tonic (G)
		jumps.add(new JumpsAndBendsHelper(1.0,G,"jump"));
		fillJumpsList_vibrato(jumps,0.2,10,G/(1+3*W2),G*(1+3*W2));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(1.0,Gh,"jump"));
		fillJumpsList_vibrato(jumps,0.2,10,Gh/(1+3*W2),Gh*(1+3*W2));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		
		// 01:55 - 3rd (G)
		jumps.add(new JumpsAndBendsHelper(0.8,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,G3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,G3,G3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.6,G3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,G3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,G3,G3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.6,G3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.8,G,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,G,G*(1+3*W2));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,G3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,G3,G3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.2,G3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,G,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,Gh3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.4,Gh3,Gh3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.2,Gh3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.8,Gh,"jump"));
		fillJumpsList_vibrato(jumps,0.2,5,Gh,Gh*(1+3*W2));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.8,Gh,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,Gh3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,Gh3,Gh3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.6,Gh3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.2,Gh3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,Gh3,Gh3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.6,Gh3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.8,Gh,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		
		// 02:25 - 3rd examples (C)
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		fillJumpsList_vibrato(jumps,0.2,10,C,C*(1+3*W2));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(1.0,Ch,"jump"));
		fillJumpsList_vibrato(jumps,0.2,10,Ch,Ch*(1+3*W2));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.5,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.5,C4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,C3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,C3,C3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.6,C3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		jumps.add(new JumpsAndBendsHelper(0.5,C4,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.2,C3,"jump"));
		jumps.add(new JumpsAndBendsHelper(0.6,C3,C3ss,"bend"));
		jumps.add(new JumpsAndBendsHelper(0.6,C3ss,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,C,"jump"));
		jumps.add(new JumpsAndBendsHelper(1.0,0.1,"jump")); // rest
		
		
		
		
		
		
		
		// https://www.youtube.com/watch?v=sak5Ii_yP00
		// https://www.youtube.com/watch?v=5GD9eigWD3o
		// https://www.youtube.com/watch?v=TerZezpxMgs
		// https://www.youtube.com/shorts/nH11cXeLlQY
		// https://www.youtube.com/watch?v=HOpF75D7UJA
		// https://www.youtube.com/watch?v=uLcdFO5y9G0
		// https://www.youtube.com/watch?v=Ezy2pqG7fxE
		
		
		
		
		// TODO: process these notes
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
			//fillJumpsList9(jumps);
			//fillJumpsList10(jumps);
			//fillJumpsList11(jumps);
			//fillJumpsList12(jumps);
			//fillJumpsList13(jumps);
			//fillJumpsList14(jumps);
			//fillJumpsList15(jumps);
			//fillJumpsList16(jumps);
			//fillJumpsList17(jumps);
			//fillJumpsList18(jumps);
			//fillJumpsList19(jumps);
			//fillJumpsList20(jumps);
			//fillJumpsList21(jumps);
			//fillJumpsList22(jumps);
			//fillJumpsList23(jumps);
			fillJumpsList24(jumps);
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
			//return getNote(1, 1000000, z);
			return 0.1*getSawtoothWave(1,z);
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
		//return getNote(1, 1000000, z);
		return 0.1*getSawtoothWave(1,z);
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