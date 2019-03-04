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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

class videoDetection {
	int counter = 0;

	public void detectAndDisplay(Mat frame, CascadeClassifier faceCascade, CascadeClassifier eyesCascade) {

		Mat frameGray = new Mat();
		Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frameGray, frameGray);

		MatOfRect faces = new MatOfRect();
		faceCascade.detectMultiScale(frameGray, faces);
		List<Rect> listOfFaces = faces.toList();
		// System.out.println(faces.toArray().length);
		for (Rect face : listOfFaces) {
			Imgproc.line(frame, new Point(10, 154), new Point(1300, 154), new Scalar(0, 0, 0), 4);
			Point center = new Point(face.x + face.width / 2, face.y + face.height / 2);
			// System.out.println(face.x+","+face.height+","+face.width);
			Imgproc.circle(frame, center, 3, new Scalar(255, 0, 0), 3);
			Imgproc.ellipse(frame, center, new Size(face.width / 2, face.height / 2), 0, 0, 360,
					new Scalar(255, 0, 255));
			if (face.y + face.height / 2 >= 152 && face.y + face.height / 2 <= 156) {
				++counter;
				Imgproc.circle(frame, new Point(face.x + face.width / 2, face.y + face.height / 2), 5,
						new Scalar(0, 255, 0), 5);
			}
		}
		// -- Show what you got
		HighGui.imshow("Capture - Face detection", frame);

	}

	public void run(String[] args) {
		String filenameFaceCascade = args.length > 2 ? args[0] : "/home/vardan/Desktop/carlbpnewdata/cascade.xml";
		// int cameraDevice = args.length > 2 ? Integer.parseInt(args[2]) : 0;
		CascadeClassifier faceCascade = new CascadeClassifier();
		CascadeClassifier eyesCascade = new CascadeClassifier();
		if (!faceCascade.load(filenameFaceCascade)) {
			System.err.println("--(!)Error loading face cascade: " + filenameFaceCascade);
			System.exit(0);
		}

		VideoCapture capture = new VideoCapture("/home/vardan/Pictures/video1.mkv");
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
			// -- 3. Apply the classifier to the frame
			detectAndDisplay(frame, faceCascade, eyesCascade);
			if (HighGui.waitKey(10) == 27) {
				break;// escape
			}
		}
		System.out.println(counter);
		System.exit(0);
	}
}

public class Recordedvideo {
	public static void main(String[] args) {
		// Load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new videoDetection().run(args);
	}
}