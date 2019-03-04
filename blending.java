package va;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

class blendingrun {
	public void run() throws IOException {
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		double alpha = 0.5, beta, input;
		System.out.println("Enter value between 0 and 1");
		Mat src1, src2, dst = new Mat();
		input = Double.parseDouble(buf.readLine());
		if (input >= 0.0 && input <= 1.0)
			alpha = input;
		beta = 1 - alpha;
		src1 = Imgcodecs.imread("/home/vardan/Pictures/test2.png");
		src2 = Imgcodecs.imread("/home/vardan/Pictures/test3.png");
		if (src1.empty() == true) {
			System.out.println("error loading image");
			return;
		}
		if (src2.empty() == true) {
			System.out.println("error loading image");
			return;
		}
		Core.addWeighted(src1, alpha, src2, beta, 0.0, dst);
		HighGui.imshow("Linear blend", dst);
		HighGui.waitKey(0);
		System.exit(0);
	}
}

public class blending {
	public static void main(String args[]) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new blendingrun().run();
	}
}
