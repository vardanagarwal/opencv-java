package va;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.tracking.TrackerKCF;
import org.opencv.videoio.VideoCapture;

class DoTrytracking {
	public void run(String args[]) {
		String filenameFaceCascade = args.length > 2 ? args[0]
				: "/home/vardan/Documents/opencv-3.4.1/data/haarcascades/haarcascade_frontalface_alt.xml";
		CascadeClassifier faceCascade = new CascadeClassifier();
		if (!faceCascade.load(filenameFaceCascade)) {
			System.err.println("--(!)Error loading face cascade: " + filenameFaceCascade);
			System.exit(0);
		}
		trackit(faceCascade, args);
	}

	public void trackit(CascadeClassifier faceCascade, String args[]) {
		boolean flag = false;
		int cameraDevice = args.length > 2 ? Integer.parseInt(args[2]) : 0;
		VideoCapture capture = new VideoCapture(cameraDevice);
		if (!capture.isOpened()) {
			System.err.println("--(!)Error opening video capture");
			System.exit(0);
		}
		Mat frame = new Mat();
		Rect2d roi = new Rect2d(0, 0, 0, 0);

		TrackerKCF track = TrackerKCF.create();
		while (capture.read(frame)) {
			if (frame.empty()) {
				System.err.println("--(!) No captured frame -- Break!");
				break;
			}
			MatOfRect faces = new MatOfRect();
			faceCascade.detectMultiScale(frame, faces);
			List<Rect> listOfFaces = faces.toList();

			for (Rect face : listOfFaces) {
				roi = new Rect2d(face.x, face.y, face.width, face.height);
				// System.out.println(roi.height+roi.width);
				flag = true;
				if (roi.width == 0 || roi.height == 0) {
					System.out.println("roi empty");
					System.exit(0);
				}
			}
			if (flag)
				break;
		}
		track.init(frame, roi);
		for (;;) {

			while (capture.read(frame)) {
				if (frame.empty()) {
					System.err.println("--(!) No captured frame -- Break!");
					break;
				}
				track.update(frame, roi);
				System.out.println(roi.x + "'" + roi.y);
				Imgproc.rectangle(frame, new Point(roi.x, roi.y), new Point(roi.x + roi.width, roi.y + roi.height),
						new Scalar(0, 0, 255));
				HighGui.imshow("tracker", frame);
				if (HighGui.waitKey(100) == 27)
					System.exit(0);
			}
		}
	}

}

/*
 * public void run(String args[]) {
 * 
 * Imgproc.line(frame, new Point(10,154),new Point(1300,154),new Scalar(0,0,0),
 * 4); HighGui.imshow("der", frame); HighGui.waitKey(0); //System.exit(0);
 * break; } while(capture.read(frame)) { HighGui.imshow("der", frame);
 * if(HighGui.waitKey(100)==27) System.exit(0); } }
 */
public class Trytracking {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new DoTrytracking().run(args);
	}
}
