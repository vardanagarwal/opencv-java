package va;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class cardetectionrun {
	public void detectAndDisplay(Mat src, CascadeClassifier carCascade) throws IOException {
		// long start = System.currentTimeMillis();
		Mat frameGray = new Mat();
		Imgproc.cvtColor(src, frameGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frameGray, frameGray);

		MatOfRect cars = new MatOfRect();
		carCascade.detectMultiScale(frameGray, cars);
		List<Rect> listOfCars = cars.toList();
		for (Rect car : listOfCars) {
			Imgproc.rectangle(src, new Point(car.x, car.y), new Point(car.x + car.width, car.y + car.height),
					new Scalar(0, 0, 255));
			System.out.println(car.x + "," + car.y + "," + car.width);
		}

		HighGui.imshow("car", src);
		HighGui.waitKey(0);
		// System.exit(0);
	}

	public void run() throws IOException {
		Mat src = new Mat();
		String cardetecter = "/home/vardan/Desktop/carlbpnewdata/cascade.xml";
		CascadeClassifier carCascade = new CascadeClassifier();
		if (!carCascade.load(cardetecter)) {
			System.err.println("--(!)Error loading car cascade: " + cardetecter);
			System.exit(0);
		}
		for (int i = 99; i < 100; i++) {
			String s = "/home/vardan/Pictures/test4.png";
			src = Imgcodecs.imread(s);
			if (src.empty() == true) {
				System.out.println("error loading image");
				return;
			}
			detectAndDisplay(src, carCascade);
		}
	}
}

public class cardetection {
	public static void main(String args[]) throws IOException {
		// Load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new cardetectionrun().run();
	}
}