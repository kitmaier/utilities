import java.io.*;
import java.util.*;

/* 
 * Discrete Fourier transform (Java)
 * by Project Nayuki, 2017. Public domain.
 * https://www.nayuki.io/page/how-to-implement-the-discrete-fourier-transform
 */


public final class DiscreteFourierTransform {
	
	/* 
	 * Computes the discrete Fourier transform (DFT) of the given complex vector.
	 * All the array arguments must be non-null and have the same length.
	 */
	public static void main(String args[]) throws Exception {
		Scanner dft_in = new Scanner(new File("dft_in.txt"));
		String[] data_split = dft_in.next().split(",");
		double[] data = new double[data_split.length];
		int len = data_split.length;
		for(int k=0; k<len; k++) {
			double a = (len-1)*1.0/k;
			double hann = 0.5*(1.0-Math.cos(2*Math.PI/a));
			double b = 2*a-1;
			double gauss = Math.exp(-6*b*b);
			data[k] = Integer.parseInt(data_split[k])*hann;
		}
		double[] zeros = new double[data.length];
		double[] outreal = new double[data.length];
		double[] outimag = new double[data.length];
		computeDft(data,zeros,outreal,outimag);
		BufferedWriter out = new BufferedWriter(new FileWriter(new File("dft.txt")));
		for(int k=0; k<data.length; k++) {
			out.write(k+"\t"+outreal[k]+"\t"+outimag[k]+"\t"+Math.sqrt(outreal[k]*outreal[k]+outimag[k]*outimag[k])+"\n");
		}
		out.close();
	}
	public static void computeDft(double[] inreal, double[] inimag, double[] outreal, double[] outimag) {
		int n = inreal.length;
		for (int k = 0; k < n; k++) {  // For each output element
			//if(k%100==0) System.out.println((new Date())+": "+k);
			double sumreal = 0;
			double sumimag = 0;
			for (int t = 0; t < n; t++) {  // For each input element
				double angle = 2 * Math.PI * t * k / n;
				sumreal +=  inreal[t] * Math.cos(angle) + inimag[t] * Math.sin(angle);
				sumimag += -inreal[t] * Math.sin(angle) + inimag[t] * Math.cos(angle);
			}
			outreal[k] = sumreal;
			outimag[k] = sumimag;
		}
	}
	
}