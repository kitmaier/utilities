import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

/** Beeper presents a small, loopable tone that can be heard
by clicking on the Code Key.  It uses a Clip to loop the sound,
as well as for access to the Clip's gain control.
@author Andrew Thompson
@version 2009-12-19
@license LGPL */

// https://stackoverflow.com/questions/7782721/java-raw-audio-output/7782749#7782749

public class Beeper {
	static Clip clip;
	public static void main(String args[]) throws Exception {
		generateTone();
		loopSound(true);
		clip.start();
	}

    public static void stop() {
        loopSound(false);
    }

    /** Loops the current Clip until a commence false is passed. */
    public static void loopSound(boolean commence) {
        if ( commence ) {
            clip.setFramePosition(0);
            clip.loop( Clip.LOOP_CONTINUOUSLY );
        } else {
            clip.stop();
        }
    }

    /** Generates a tone, and assigns it to the Clip. */
    public static void generateTone()
        throws LineUnavailableException {
        if ( clip!=null ) {
            clip.stop();
            clip.close();
        } else {
            clip = AudioSystem.getClip();
        }
        boolean addHarmonic = false;

        int intSR = 8000;
        int intFPW = 8000;

        float sampleRate = (float)intSR;

        // oddly, the sound does not loop well for less than
        // around 5 or so, wavelengths
        int wavelengths = 20;
        byte[] buf = new byte[2*intFPW*wavelengths];
        AudioFormat af = new AudioFormat(
            sampleRate,
            8,  // sample size in bits
            2,  // channels
            true,  // signed
            false  // bigendian
            );

        int maxVol = 127;
        for(int i=0; i<intFPW*wavelengths; i++){
            double angle = ((float)(i*2)/((float)intFPW))*(Math.PI);
            buf[i*2]=getByteValue(angle);
            if(addHarmonic) {
                buf[(i*2)+1]=getByteValue(2*angle);
            } else {
                buf[(i*2)+1] = buf[i*2];
            }
        }

        try {
            byte[] b = buf;
            AudioInputStream ais = new AudioInputStream(
                new ByteArrayInputStream(b),
                af,
                buf.length/2 );

            clip.open( ais );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Provides the byte value for this point in the sinusoidal wave. */
    private static byte getByteValue(double angle) {
        int maxVol = 127;
        return (new Integer(
            (int)Math.round(
            Math.sin(angle)*maxVol))).
            byteValue();
    }
}
