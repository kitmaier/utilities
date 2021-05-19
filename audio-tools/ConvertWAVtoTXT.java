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
			String[] fileList = {"aa","ah","aw","dj","ee","eh","eu","ff","hh","ih","ll","mm","nn","oh","oo","rr","sh","ss","uh","vv","whistle","ww","zz","sample123","clean_piano"};
			for(int k=0; k<fileList.length; k++) {
				convertWAVtoTXT(fileList[k]);
			}
		}
	}
	public static void convertWAVtoTXT(String filename) throws Exception {
		String inFilename = "audio/"+filename+".wav";
		String outFilename = "data/"+filename+".txt";
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File(inFilename));
		System.out.println(filename+": "+ais.getFormat());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read;
		byte[] buff = new byte[1024];
		while ((read = ais.read(buff)) > 0) {
			out.write(buff, 0, read);
		}
		out.flush();
		byte[] audioBytes = out.toByteArray();
		StringBuilder sb = new StringBuilder();
		for(int k=0; k<audioBytes.length; k+=2) {
			byte bb = audioBytes[k];
			bb = reverseEndian(bb);
			int total = ((int)audioBytes[k+1])*256+(int)audioBytes[k];
			sb.append(k+"\t"+audioBytes[k]+"\t"+audioBytes[k+1]+"\t"+total+"\n");
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