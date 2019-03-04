package va;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class multirun {
	public void detectAndDisplay(Mat src, CascadeClassifier carCascade, CascadeClassifier carCascade2) {
		Mat frameGray = new Mat();
		Imgproc.cvtColor(src, frameGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frameGray, frameGray);
		MatOfRect cars = new MatOfRect();
		carCascade.detectMultiScale(frameGray, cars);
		List<Rect> listOfCars = cars.toList();
		for (Rect car : listOfCars) {
			Point center = new Point(car.x + car.width / 2, car.y + car.height / 2);
			Imgproc.ellipse(src, center, new Size(car.width / 2, car.height / 2), 0, 0, 360, new Scalar(0, 0, 255));
		}
		carCascade2.detectMultiScale(frameGray, cars);
		List<Rect> listOfCars2 = cars.toList();
		for (Rect car : listOfCars2) {
			Point center2 = new Point(car.x + car.width / 2, car.y + car.height / 2);
			Imgproc.ellipse(src, center2, new Size(car.width / 2, car.height / 2), 0, 0, 360, new Scalar(0, 255, 0));
		}
		HighGui.imshow("car", src);
		HighGui.waitKey(0);
		System.exit(0);
	}

	public void run() {
		Mat src = new Mat();
		String cardetecter = "/home/vardan/Desktop/carlbpdata/cascade.xml";
		CascadeClassifier carCascade = new CascadeClassifier();
		if (!carCascade.load(cardetecter)) {
			System.err.println("--(!)Error loading car cascade: " + cardetecter);
			System.exit(0);
		}
		String cardetecter2 = "/home/vardan/Desktop/carhaardata/cascade.xml";
		CascadeClassifier carCascade2 = new CascadeClassifier();
		if (!carCascade2.load(cardetecter2)) {
			System.err.println("--(!)Error loading car cascade: " + cardetecter);
			System.exit(0);
		}
		src = Imgcodecs.imread("/home/vardan/Pictures/Webcam/test4.png");
		if (src.empty() == true) {
			System.out.println("error loading image");
			return;
		}
		detectAndDisplay(src, carCascade, carCascade2);

	}
}

public class Multicardetection {
	public static void main(String args[]) throws IOException {
		// Load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new multirun().run();
	}
}