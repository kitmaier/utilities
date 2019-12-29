import java.io.*;
import javax.sound.sampled.*;

/*
	TODO: there are some variations in how the data in a WAV can be stored 
		(little/bigendian, signed/unsigned, different block/word lengths, single/multichannel, int/float)
		it would be good to be able to handle them all consistently
*/
public class ConvertWAVtoTXT {
	public static void main(String[] args) throws Exception {
		if(args.length>0) {
			convertWAVtoTXT(args[0]);
		} else {
			String[] fileList = {"aa","ah","aw","dj","ee","eh","eu","ff","hh","ih","ll","mm","nn","oh","oo","rr","sh","ss","uh","vv","whistle","ww","zz"};
			for(int k=0; k<fileList.length; k++) {
				convertWAVtoTXT(fileList[k]);
			}
		}
	}
	public static void convertWAVtoTXT(String filename) throws Exception {
		String inFilename = "audio/"+filename+".wav";
		String outFilename = "data/"+filename+".txt";
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File(inFilename));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read;
		byte[] buff = new byte[1024];
		while ((read = ais.read(buff)) > 0) {
			out.write(buff, 0, read);
		}
		out.flush();
		byte[] audioBytes = out.toByteArray();
		StringBuilder sb = new StringBuilder();
		for(int k=1; k<audioBytes.length; k+=2) { 
			byte bb = audioBytes[k];
			bb = reverseEndian(bb);
			sb.append(k+"\t"+audioBytes[k]+"\t"+bb+"\n");
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFilename)));
		writer.write(sb.toString());
		writer.close();
	}
	public static byte reverseEndian(byte bb) {
		byte b2 = 0;
		for(int k=0; k<8; k++) {
			b2 = (byte)(b2<<1);
			b2 += bb%2;
			bb = (byte)(bb>>1);
		}
		return b2;
	}
}