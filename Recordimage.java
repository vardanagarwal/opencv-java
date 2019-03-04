package va;

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

class videoimageDetection {
	int counter = 0;

	public void detectAndDisplay(Mat frame, CascadeClassifier faceCascade, CascadeClassifier eyesCascade) {
		Mat frameGray = new Mat();

		Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frameGray, frameGray);
		// -- Detect faces

		MatOfRect faces = new MatOfRect();
		faceCascade.detectMultiScale(frameGray, faces);
		List<Rect> listOfFaces = faces.toList();

		for (Rect face : listOfFaces) {
			Imgproc.line(frame, new Point(10, 152), new Point(1300, 152), new Scalar(0, 0, 0), 2);
			Point center = new Point(face.x + face.width / 2, face.y + face.height / 2);
			System.out.println(face.x + "," + face.height + "," + face.width);
			Imgproc.circle(frame, center, 3, new Scalar(255, 0, 0), 3);
			Imgproc.ellipse(frame, center, new Size(face.width / 2, face.height / 2), 0, 0, 360,
					new Scalar(255, 0, 255));
			if (face.y + face.height / 2 >= 152 && face.y + face.height / 2 <= 156) {
				++counter;
				System.out.println(counter);
				Imgproc.circle(frame, new Point(face.x, face.y + face.height / 2), 5, new Scalar(0, 255, 0), 5);
			}
		}
		// -- Show what you got
		HighGui.imshow("Capture - Face detection", frame);
		HighGui.waitKey(0);
	}

	public void run(String[] args) {
		String filenameFaceCascade = args.length > 2 ? args[0] : "/home/vardan/Desktop/carlbpnewdata/cascade.xml";
		CascadeClassifier faceCascade = new CascadeClassifier();
		CascadeClassifier eyesCascade = new CascadeClassifier();
		if (!faceCascade.load(filenameFaceCascade)) {
			System.err.println("--(!)Error loading face cascade: " + filenameFaceCascade);
			System.exit(0);
		}
		Mat frame = new Mat();
		// for(int i=1;i<=761;i++)
		// {

		String s = "/home/vardan/Pictures/test4.png";
		frame = Imgcodecs.imread(s);
		if (frame.empty() == true) {
			System.out.println("error loading image");
			return;
		}
		// -- 3. Apply the classifier to the frame
		detectAndDisplay(frame, faceCascade, eyesCascade);
		if (HighGui.waitKey(10) == 27) {
			// break;// escape
		}
		// }
		System.out.println("final" + counter);
		System.exit(0);
	}
}

public class Recordimage {
	public static void main(String[] args) {
		// Load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new videoimageDetection().run(args);
	}
}