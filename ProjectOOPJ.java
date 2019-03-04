package va;

import java.io.File;
import java.io.FileWriter;
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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

class VideoDetection {

	public void detectAndDisplay(Mat frame, CascadeClassifier carCascade, FileWriter fw) throws IOException {
		// 0 for light off, 1 for light on

		String l1 = "0", l2 = "0", l3 = "0", l4 = "0";
		Mat frameGray = new Mat();
		Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
		// System.out.println(frame.size());
		Imgproc.equalizeHist(frameGray, frameGray);
		Imgproc.line(frame, new Point(400, 0), new Point(400, 1000), new Scalar(0, 0, 255));
		Imgproc.line(frame, new Point(800, 0), new Point(800, 1000), new Scalar(0, 0, 255));
		Imgproc.line(frame, new Point(1200, 0), new Point(1200, 1000), new Scalar(0, 0, 255));
		MatOfRect cars = new MatOfRect();
		carCascade.detectMultiScale(frameGray, cars);
		List<Rect> listOfCars = cars.toList();
		for (Rect car : listOfCars) {

			Point center = new Point(car.x + car.width / 2, car.y + car.height / 2);
			Imgproc.circle(frame, center, 3, new Scalar(255, 0, 0));
			// System.out.println(car.x+","+car.height+","+car.width);
			Imgproc.ellipse(frame, center, new Size(car.width / 2, car.height / 2), 0, 0, 360, new Scalar(255, 0, 255));
			if (center.x <= 400)
				l1 = "1";
			if (center.x > 400 && center.x <= 800)
				l2 = "1";
			if (center.x > 800 && center.x <= 1200)
				l3 = "1";
			if (center.x > 1200)
				l4 = "1";
		}
		fw.write(l1);
		fw.write(l2);
		fw.write(l3);
		fw.write(l4);
		fw.close();
		System.out.println(l1 + l2 + l3 + l4 + "\n");
		l1 = "0";
		l2 = "0";
		l3 = "0";
		l4 = "0";
		System.out.println();
		// -- Show what you got
		HighGui.imshow("Capture - car detection", frame);
	}

	public void run(String[] args) throws IOException {
		String filenameCarCascade = args.length > 2 ? args[0] : "/home/vardan/Desktop/carlbpnewdata/cascade.xml";
		// int cameraDevice = args.length > 2 ? Integer.parseInt(args[2]) : 0;
		CascadeClassifier CarCascade = new CascadeClassifier();
		if (!CarCascade.load(filenameCarCascade)) {
			System.err.println("--(!)Error loading car cascade: " + filenameCarCascade);
			System.exit(0);
		}

		VideoCapture capture = new VideoCapture("/home/vardan/Desktop/testvideo.mp4");
		if (!capture.isOpened()) {
			System.err.println("--(!)Error opening video capture");
			System.exit(0);
		}
		Mat frame = new Mat();
		while (capture.read(frame)) {
			if (frame.empty()) {
				System.err.println("--(!) No captured frame -- Break!");
				break;
			}
			File file = new File("/home/vardan/Documents/filehandi.txt");
			FileWriter fw = new FileWriter(file);
			// -- 3. Apply the classifier to the frame
			detectAndDisplay(frame, CarCascade, fw);
			if (HighGui.waitKey(10) == 27) {
				break;// escape
			}
		}
		System.exit(0);
	}
}

public class ProjectOOPJ {
	public static void main(String[] args) throws IOException {
		// Load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new VideoDetection().run(args);
	}
}