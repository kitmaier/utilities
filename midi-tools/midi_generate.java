/**
* http://www.automatic-pilot.com/midifile.html
*/

// TODO: generate pitches relative to a tonic instead of absolutely
// TODO: count measures automatically instead of specifying in the file
// TODO: generate chords, chord voicings, and chord progressions automaticall
// TODO: specify sanity/quality checks for generated music (eg pitch within bounds, no dissonant intervals)
// TODO: figure out how to assign different instruments to different tracks
// TODO: try directly generating sounds with midi synthesizer like https://stackoverflow.com/questions/44216711/playing-multiple-instruments-on-a-soft-synth-simultaneoulsy-in-java

import java.io.*;
import java.util.*;
import javax.sound.midi.*;
public class midi_generate
{
	public static void main(String argv[]) throws Exception {
		String[] fileList = {
			"composition_20210519_3"
		};
		for(String fileName : fileList) {
			System.out.println(fileName);
			translateFile(fileName);
		}
	}
	public static void translateFile(String fileName) throws Exception {
		System.out.println("midifile begin ");
		//****  Create a new MIDI sequence with 24 ticks per beat  ****
		Sequence s = new Sequence(Sequence.PPQ,24);

		//****  Obtain a MIDI track from the sequence  ****
		Track L = s.createTrack();
		Track R = s.createTrack();
		Track V = s.createTrack();
		Track P = s.createTrack();

		setupTrack(L);
		setupTrack(R);
		setupTrack(V);
		maxEnd = 0;

		//setInstrument(L,0,PIANO__ACOUSTIC_GRAND_PIANO);
		setInstrument(L,0,STRINGS__ORCHESTRAL_HARP);
		//setInstrument(L,0,GUITAR__ACOUSTIC_GUITAR_NYLON);
		//setInstrument(L,0,REED__ENGLISH_HORN);
		//setInstrument(L,0,STRINGS__CELLO);
		//setInstrument(L,0,REED__SOPRANO_SAX);
		//setInstrument(R,1,PIANO__ACOUSTIC_GRAND_PIANO);
		setInstrument(R,1,STRINGS__ORCHESTRAL_HARP);
		//setInstrument(R,1,GUITAR__ACOUSTIC_GUITAR_NYLON);
		//setInstrument(R,1,REED__ENGLISH_HORN);
		//setInstrument(R,1,REED__SOPRANO_SAX);
		//setInstrument(R,1,STRINGS__VIOLA);
		setInstrument(V,2,REED__ENGLISH_HORN);
		//setInstrument(V,2,STRINGS__VIOLA);
		setInstrument(P,9,00); // percussion

		Scanner scan = new Scanner(new File(fileName+".txt"));
		while(scan.hasNext()) {
			String line = scan.nextLine().trim();
			if(line.isEmpty()) continue;
			if(line.charAt(0)=='#') {
				if(line.length()==1||line.charAt(1)!='!') continue;
				if(line.contains("time_signature:")&&line.contains("6/8")) {
					timeSignature = 6;
				}
				if(line.contains("tempo:")&&line.contains("fast")) {
					ticksPerBeat = 8;
				}
				continue;
			}
			String[] fields = line.split("\t");
			Track track = null;
			int channel = 0;
			byte volume = (byte)127;
			switch(fields[0].charAt(0)) {
				case 'L': 
					track = L;
					channel = 0;
					//volume = 80;
					break;
				case 'R':
					track = R;
					channel = 1;
					//volume = 60;
					break;
				case 'V':
					track = V;
					channel = 2;
					break;
				case 'P':
					track = P;
					channel = 9;
			}
			int measure = Integer.parseInt(fields[1]);
			int beat = Integer.parseInt(fields[2]);
			int duration = Integer.parseInt(fields[3]);
			String note = fields[4];
			makeNote(track, channel, measure, beat, duration, note, volume);
		}

		setTrackEnd(L);
		setTrackEnd(R);
		maxEnd = 0;

		//****  write the MIDI sequence to a MIDI file  ****
		File f = new File(fileName+".mid");
		MidiSystem.write(s,1,f);
		System.out.println("midifile end ");
	} //main

	public static long maxEnd = 0;
	public static int[] noteVals = null;
	public static void prepareNoteVals() {
		if(noteVals!=null) return;
		noteVals = new int[127];
		noteVals['C'] = 60;
		noteVals['D'] = 62;
		noteVals['E'] = 64;
		noteVals['F'] = 65;
		noteVals['G'] = 67;
		noteVals['A'] = 69;
		noteVals['B'] = 71;
	}
	public static int timeSignature = 8;
	public static int ticksPerBeat = 12;
	public static void makeNote(Track t, int channel, int measure, int beat, int duration, String note, byte volume) throws Exception {
		// 8 beats per measure
		// 2 beats per second
		int start = (measure*timeSignature+beat)*ticksPerBeat;
		int end = start+duration*ticksPerBeat;
		maxEnd = Math.max((long)end,maxEnd);

		// octave number rolls over from B to C
		// middle C == 60 == "4C"
		int noteVal = 0;
		if(channel==9) {
			noteVal = Integer.parseInt(note);
		} else {
			prepareNoteVals();
			int octave = Integer.parseInt(note.substring(0,1));
			noteVal = (octave-4)*12+noteVals[note.charAt(1)];
			if(note.length()==3) {
				if(note.charAt(2)=='#') {
					noteVal++;
				} else {
					noteVal--;
				}
			}
		}

		MidiEvent me;
		ShortMessage mm;
		mm = new ShortMessage();
		mm.setMessage(ShortMessage.NOTE_ON,channel,noteVal,volume); // volume at 75% of maximum (0x60)
		me = new MidiEvent(mm,(long)start);
		t.add(me);

		mm = new ShortMessage();
		mm.setMessage(ShortMessage.NOTE_OFF,channel,noteVal,0x00);
		me = new MidiEvent(mm,(long)end);
		t.add(me);
	}

	public static void setupTrack(Track t) throws Exception {

		//****  General MIDI sysex -- turn on General MIDI sound set  ****
		byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
		SysexMessage sm = new SysexMessage();
		sm.setMessage(b, 6);
		MidiEvent me = new MidiEvent(sm,(long)0);
		t.add(me);

		//****  set tempo (meta event)  ****
		MetaMessage mt = new MetaMessage();
		// 0x07 0xA1 0x20 == 120 bpm
		// https://www.recordingblogs.com/wiki/midi-set-tempo-meta-message
		byte[] bt = {0x07, (byte)0xA1, 0x20};
		mt.setMessage(0x51 ,bt, 3);
		me = new MidiEvent(mt,(long)0);
		t.add(me);

		//****  set track name (meta event)  ****
		mt = new MetaMessage();
		String TrackName = new String("midifile track");
		mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
		me = new MidiEvent(mt,(long)0);
		t.add(me);

		//****  set omni on  ****
		ShortMessage mm = new ShortMessage();
		mm.setMessage(ShortMessage.CONTROL_CHANGE, 0x7D,0x00);
		me = new MidiEvent(mm,(long)0);
		t.add(me);

		//****  set poly on  ****
		mm = new ShortMessage();
		mm.setMessage(ShortMessage.CONTROL_CHANGE, 0x7F,0x00);
		me = new MidiEvent(mm,(long)0);
		t.add(me);
	}
	public static void setInstrument(Track t, int channel, int instrument) throws Exception {
		ShortMessage mm = new ShortMessage();
		//mm.setMessage(ShortMessage.PROGRAM_CHANGE, 0x00, 0x00);
		//mm.setMessage(ShortMessage.PROGRAM_CHANGE, 0x14, 0x00);
		//mm.setMessage(ShortMessage.PROGRAM_CHANGE, 0x41, 0x00);
		//mm.setMessage(ShortMessage.PROGRAM_CHANGE, 0x45, 0x00);
		mm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0x00);
		MidiEvent me = new MidiEvent(mm,(long)0);
		t.add(me);
	}
	public static void setTrackEnd(Track t) throws Exception {

		long end = maxEnd+20L;
		MetaMessage mt = new MetaMessage();
		byte[] bet = {}; // empty array
		mt.setMessage(0x2F,bet,0);
		MidiEvent me = new MidiEvent(mt, end);
		t.add(me);

	}
	
public static final int PIANO__ACOUSTIC_GRAND_PIANO = 0;
public static final int PIANO__BRIGHT_ACOUSTIC_PIANO = 1;
public static final int PIANO__ELECTRIC_GRAND_PIANO = 2;
public static final int PIANO__HONKY_TONK_PIANO = 3;
public static final int PIANO__ELECTRIC_PIANO_1 = 4;
public static final int PIANO__ELECTRIC_PIANO_2 = 5;
public static final int PIANO__HARPSICHORD = 6;
public static final int PIANO__CLAVI = 7;
public static final int CHROMATIC_PERCUSSION__CELESTA = 8;
public static final int CHROMATIC_PERCUSSION__GLOCKENSPIEL = 9;
public static final int CHROMATIC_PERCUSSION__MUSIC_BOX = 10;
public static final int CHROMATIC_PERCUSSION__VIBRAPHONE = 11;
public static final int CHROMATIC_PERCUSSION__MARIMBA = 12;
public static final int CHROMATIC_PERCUSSION__XYLOPHONE = 13;
public static final int CHROMATIC_PERCUSSION__TUBULAR_BELLS = 14;
public static final int CHROMATIC_PERCUSSION__DULCIMER = 15;
public static final int ORGAN__DRAWBAR_ORGAN = 16;
public static final int ORGAN__PERCUSSIVE_ORGAN = 17;
public static final int ORGAN__ROCK_ORGAN = 18;
public static final int ORGAN__CHURCH_ORGAN = 19;
public static final int ORGAN__REED_ORGAN = 20;
public static final int ORGAN__ACCORDION = 21;
public static final int ORGAN__HARMONICA = 22;
public static final int ORGAN__TANGO_ACCORDION = 23;
public static final int GUITAR__ACOUSTIC_GUITAR_NYLON = 24;
public static final int GUITAR__ACOUSTIC_GUITAR_STEEL = 25;
public static final int GUITAR__ELECTRIC_GUITAR_JAZZ = 26;
public static final int GUITAR__ELECTRIC_GUITAR_CLEAN = 27;
public static final int GUITAR__ELECTRIC_GUITAR_MUTED = 28;
public static final int GUITAR__OVERDRIVEN_GUITAR = 29;
public static final int GUITAR__DISTORTION_GUITAR = 30;
public static final int GUITAR__GUITAR_HARMONICS = 31;
public static final int BASS__ACOUSTIC_BASS = 32;
public static final int BASS__ELECTRIC_BASS_FINGER = 33;
public static final int BASS__ELECTRIC_BASS_PICK = 34;
public static final int BASS__FRETLESS_BASS = 35;
public static final int BASS__SLAP_BASS_1 = 36;
public static final int BASS__SLAP_BASS_2 = 37;
public static final int BASS__SYNTH_BASS_1 = 38;
public static final int BASS__SYNTH_BASS_2 = 39;
public static final int STRINGS__VIOLIN = 40;
public static final int STRINGS__VIOLA = 41;
public static final int STRINGS__CELLO = 42;
public static final int STRINGS__CONTRABASS = 43;
public static final int STRINGS__TREMOLO_STRINGS = 44;
public static final int STRINGS__PIZZICATO_STRINGS = 45;
public static final int STRINGS__ORCHESTRAL_HARP = 46;
public static final int STRINGS__TIMPANI = 47;
public static final int ENSEMBLE__STRING_ENSEMBLE_1 = 48;
public static final int ENSEMBLE__STRING_ENSEMBLE_2 = 49;
public static final int ENSEMBLE__SYNTH_STRINGS_1 = 50;
public static final int ENSEMBLE__SYNTH_STRINGS_2 = 51;
public static final int ENSEMBLE__CHOIR_AAHS = 52;
public static final int ENSEMBLE__VOICE_OOHS = 53;
public static final int ENSEMBLE__SYNTH_VOICE = 54;
public static final int ENSEMBLE__ORCHESTRA_HIT = 55;
public static final int BRASS__TRUMPET = 56;
public static final int BRASS__TROMBONE = 57;
public static final int BRASS__TUBA = 58;
public static final int BRASS__MUTED_TRUMPET = 59;
public static final int BRASS__FRENCH_HORN = 60;
public static final int BRASS__BRASS_SECTION = 61;
public static final int BRASS__SYNTH_BRASS_1 = 62;
public static final int BRASS__SYNTH_BRASS_2 = 63;
public static final int REED__SOPRANO_SAX = 64;
public static final int REED__ALTO_SAX = 65;
public static final int REED__TENOR_SAX = 66;
public static final int REED__BARITONE_SAX = 67;
public static final int REED__OBOE = 68;
public static final int REED__ENGLISH_HORN = 69;
public static final int REED__BASSOON = 70;
public static final int REED__CLARINET = 71;
public static final int PIPE__PICCOLO = 72;
public static final int PIPE__FLUTE = 73;
public static final int PIPE__RECORDER = 74;
public static final int PIPE__PAN_FLUTE = 75;
public static final int PIPE__BLOWN_BOTTLE = 76;
public static final int PIPE__SHAKUHACHI = 77;
public static final int PIPE__WHISTLE = 78;
public static final int PIPE__OCARINA = 79;
public static final int SYNTH_LEAD__LEAD_1_SQUARE = 80;
public static final int SYNTH_LEAD__LEAD_2_SAWTOOTH = 81;
public static final int SYNTH_LEAD__LEAD_3_CALLIOPE = 82;
public static final int SYNTH_LEAD__LEAD_4_CHIFF = 83;
public static final int SYNTH_LEAD__LEAD_5_CHARANG = 84;
public static final int SYNTH_LEAD__LEAD_6_VOICE = 85;
public static final int SYNTH_LEAD__LEAD_7_FIFTHS = 86;
public static final int SYNTH_LEAD__LEAD_8_BASS_WITH_LEAD = 87;
public static final int SYNTH_PAD__PAD_1_NEW_AGE = 88;
public static final int SYNTH_PAD__PAD_2_WARM = 89;
public static final int SYNTH_PAD__PAD_3_POLYSYNTH = 90;
public static final int SYNTH_PAD__PAD_4_CHOIR = 91;
public static final int SYNTH_PAD__PAD_5_BOWED = 92;
public static final int SYNTH_PAD__PAD_6_METALLIC = 93;
public static final int SYNTH_PAD__PAD_7_HALO = 94;
public static final int SYNTH_PAD__PAD_8_SWEEP = 95;
public static final int SYNTH_EFFECTS__FX_1_RAIN = 96;
public static final int SYNTH_EFFECTS__FX_2_SOUNDTRACK = 97;
public static final int SYNTH_EFFECTS__FX_3_CRYSTAL = 98;
public static final int SYNTH_EFFECTS__FX_4_ATMOSPHERE = 99;
public static final int SYNTH_EFFECTS__FX_5_BRIGHTNESS = 100;
public static final int SYNTH_EFFECTS__FX_6_GOBLINS = 101;
public static final int SYNTH_EFFECTS__FX_7_ECHOES = 102;
public static final int SYNTH_EFFECTS__FX_8_SCI_FI = 103;
public static final int ETHNIC__SITAR = 104;
public static final int ETHNIC__BANJO = 105;
public static final int ETHNIC__SHAMISEN = 106;
public static final int ETHNIC__KOTO = 107;
public static final int ETHNIC__KALIMBA = 108;
public static final int ETHNIC__BAG_PIPE = 109;
public static final int ETHNIC__FIDDLE = 110;
public static final int ETHNIC__SHANAI = 111;
public static final int PERCUSSIVE__TINKLE_BELL = 112;
public static final int PERCUSSIVE__AGOGO = 113;
public static final int PERCUSSIVE__STEEL_DRUMS = 114;
public static final int PERCUSSIVE__WOODBLOCK = 115;
public static final int PERCUSSIVE__TAIKO_DRUM = 116;
public static final int PERCUSSIVE__MELODIC_TOM = 117;
public static final int PERCUSSIVE__SYNTH_DRUM = 118;
public static final int PERCUSSIVE__REVERSE_CYMBAL = 119;
public static final int SOUND_EFFECTS__GUITAR_FRET_NOISE = 120;
public static final int SOUND_EFFECTS__BREATH_NOISE = 121;
public static final int SOUND_EFFECTS__SEASHORE = 122;
public static final int SOUND_EFFECTS__BIRD_TWEET = 123;
public static final int SOUND_EFFECTS__TELEPHONE_RING = 124;
public static final int SOUND_EFFECTS__HELICOPTER = 125;
public static final int SOUND_EFFECTS__APPLAUSE = 126;
public static final int SOUND_EFFECTS__GUNSHOT = 127;


public static final int PERCUSSION__HIGH_Q = 27;
public static final int PERCUSSION__SLAP = 28;
public static final int PERCUSSION__STRATCH_PUSH = 29;
public static final int PERCUSSION__STRATCH_PULL = 30;
public static final int PERCUSSION__STICKS = 31;
public static final int PERCUSSION__SQUARE_CLICK = 32;
public static final int PERCUSSION__METRONOME_CLICK = 33;
public static final int PERCUSSION__METRONOME_BELL = 34;
public static final int PERCUSSION__ACOUSTIC_BASS_DRUM = 35;
public static final int PERCUSSION__ELECTRIC_BASS_DRUM = 36;
public static final int PERCUSSION__SIDE_STICK = 37;
public static final int PERCUSSION__ACOUSTIC_SNARE = 38;
public static final int PERCUSSION__HAND_CLAP = 39;
public static final int PERCUSSION__ELECTRIC_SNARE = 40;
public static final int PERCUSSION__LOW_FLOOR_TOM = 41;
public static final int PERCUSSION__CLOSED_HI_HAT = 42;
public static final int PERCUSSION__HIGH_FLOOR_TOM = 43;
public static final int PERCUSSION__PEDAL_HI_HAT = 44;
public static final int PERCUSSION__LOW_TOM = 45;
public static final int PERCUSSION__OPEN_HI_HAT = 46;
public static final int PERCUSSION__LOW_MID_TOM = 47;
public static final int PERCUSSION__HI_MID_TOM = 48;
public static final int PERCUSSION__CRASH_CYMBAL_1 = 49;
public static final int PERCUSSION__HIGH_TOM = 50;
public static final int PERCUSSION__RIDE_CYMBAL_1 = 51;
public static final int PERCUSSION__CHINESE_CYMBAL = 52;
public static final int PERCUSSION__RIDE_BELL = 53;
public static final int PERCUSSION__TAMBOURINE = 54;
public static final int PERCUSSION__SPLASH_CYMBAL = 55;
public static final int PERCUSSION__COWBELL = 56;
public static final int PERCUSSION__CRASH_CYMBAL_2 = 57;
public static final int PERCUSSION__VIBRA_SLAP = 58;
public static final int PERCUSSION__RIDE_CYMBAL_2 = 59;
public static final int PERCUSSION__HIGH_BONGO = 60;
public static final int PERCUSSION__LOW_BONGO = 61;
public static final int PERCUSSION__MUTE_HIGH_CONGA = 62;
public static final int PERCUSSION__OPEN_HIGH_CONGA = 63;
public static final int PERCUSSION__LOW_CONGA = 64;
public static final int PERCUSSION__HIGH_TIMBALE = 65;
public static final int PERCUSSION__LOW_TIMBALE = 66;
public static final int PERCUSSION__HIGH_AGOGO = 67;
public static final int PERCUSSION__LOW_AGOGO = 68;
public static final int PERCUSSION__CABASA = 69;
public static final int PERCUSSION__MARACAS = 70;
public static final int PERCUSSION__SHORT_WHISTLE = 71;
public static final int PERCUSSION__LONG_WHISTLE = 72;
public static final int PERCUSSION__SHORT_GUIRO = 73;
public static final int PERCUSSION__LONG_GUIRO = 74;
public static final int PERCUSSION__CLAVES = 75;
public static final int PERCUSSION__HIGH_WOODBLOCK = 76;
public static final int PERCUSSION__LOW_WOODBLOCK = 77;
public static final int PERCUSSION__MUTE_CUICA = 78;
public static final int PERCUSSION__OPEN_CUICA = 79;
public static final int PERCUSSION__MUTE_TRIANGLE = 80;
public static final int PERCUSSION__OPEN_TRIANGLE = 81;
public static final int PERCUSSION__SHAKER = 82;
public static final int PERCUSSION__JINGLE_BELL = 83;
public static final int PERCUSSION__BELLTREE = 84;
public static final int PERCUSSION__CASTANETS = 85;
public static final int PERCUSSION__MUTE_SURDO = 86;
public static final int PERCUSSION__OPEN_SURDO = 87;

} //midifile