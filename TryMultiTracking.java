package va;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.tracking.MultiTracker;
import org.opencv.tracking.TrackerKCF;
import org.opencv.videoio.VideoCapture;

class DTrytracking {
	public void run(String args[]) {
		String filenameFaceCascade = args.length > 2 ? args[0] : "/home/vardan/Desktop/carlbpnewdata/cascade.xml";// "/home/vardan/Documents/opencv-3.4.1/data/haarcascades/haarcascade_frontalface_alt.xml";
		CascadeClassifier faceCascade = new CascadeClassifier();
		if (!faceCascade.load(filenameFaceCascade)) {
			System.err.println("--(!)Error loading face cascade: " + filenameFaceCascade);
			System.exit(0);
		}
		trackit(faceCascade);
	}

	public void trackit(CascadeClassifier faceCascade) {
		Rect2d roi = new Rect2d(0, 0, 0, 0);
		boolean flag = false;
		MatOfRect2d rect2d = new MatOfRect2d();
		int l = 0, i = 0;
		Rect2d a[] = null;
		VideoCapture capture = new VideoCapture("/home/vardan/Pictures/video1.mkv");
		VideoCapture capture2 = new VideoCapture("/home/vardan/Pictures/video1.mkv");
		if (!capture.isOpened()) {
			System.err.println("--(!)Error opening video capture");
			System.exit(0);
		}
		Mat frame = new Mat();
		MultiTracker track = MultiTracker.create();
		ArrayList<MultiTracker> track2 = new ArrayList<MultiTracker>();
		while (capture.read(frame)) {
			if (frame.empty()) {
				System.err.println("--(!) No captured frame -- Break!");
				break;
			}
			MatOfRect faces = new MatOfRect();
			faceCascade.detectMultiScale(frame, faces);
			List<Rect> listOfFaces = faces.toList();
			l = faces.toArray().length;
			a = new Rect2d[l];
			for (Rect face : listOfFaces) {
				roi = new Rect2d(face.x, face.y, face.width, face.height);
				flag = true;
				a[i] = roi;
				i++;
				if (roi.width == 0 || roi.height == 0) {
					System.out.println("roi empty");
					System.exit(0);
				}
				track.add(TrackerKCF.create(), frame, roi);
			}
			if (flag)
				break;
		}
		Mat frame2 = new Mat();
		int l2, i2 = 0, c = 0, nof = 0, z = 0;
		while (capture2.read(frame2))
			nof++;
		nof = nof / 100;
		flag = false;
		Rect2d a2[][] = new Rect2d[nof][];
		for (int k = 1; k > 0; k++) {
			if (k % 100 == 0) {
				while (capture.read(frame)) {
					MatOfRect faces2 = new MatOfRect();
					faceCascade.detectMultiScale(frame, faces2);
					List<Rect> listOfFaces2 = faces2.toList();
					for (Rect face : listOfFaces2)
						if (face.y > 550)
							z++;
					a2[c] = new Rect2d[z];
					z = 0;
					i2 = 0;
					// l2=faces2.toArray().length;
					for (Rect face : listOfFaces2) {
						if (face.y > 550) {
							flag = true;
							roi = new Rect2d(face.x, face.y, face.width, face.height);
							a2[c][i2] = roi;
							i2++;
							// System.out.println(i2 + "," + z + "," + c);
							if (roi.width == 0 || roi.height == 0) {
								System.out.println("roi empty");
								System.exit(0);
							}
							track2.add(MultiTracker.create());
							track2.get(c).add(TrackerKCF.create(), frame, roi);

						}
					}
					c++;
					break;
				}
			}
			for (;;) {

				while (capture.read(frame)) {
					if (frame.empty()) {
						System.err.println("--(!) No captured frame -- Break!");
						System.exit(0);
					}
					rect2d.fromArray(a);
					track.update(frame, rect2d);
					a = rect2d.toArray();
					l = a.length;
					for (i = 0; i < l; i++) {

						if (a[i].y + a[i].height / 2 > 140)
							Imgproc.rectangle(frame, new Point(a[i].x, a[i].y),
									new Point(a[i].x + a[i].width, a[i].y + a[i].height), new Scalar(0, 0, 255));

					}
					if (flag) {

						for (int cIndex = 0; cIndex < c; cIndex++) {
							rect2d.fromArray(a2[cIndex]);
							MultiTracker t2 = track2.get(cIndex);
							if (t2 != null)
								t2.update(frame, rect2d);

							a2[cIndex] = rect2d.toArray();
							l2 = a2[cIndex].length;
							for (i2 = 0; i2 < l2; i2++) {

								if (a2[cIndex][i2].y + a2[cIndex][i2].height / 2 > 140)
									Imgproc.rectangle(frame, new Point(a2[cIndex][i2].x, a2[cIndex][i2].y),
											new Point(a2[cIndex][i2].x + a2[cIndex][i2].width,
													a2[cIndex][i2].y + a2[cIndex][i2].height),
											new Scalar(0, 0, 0));

							}
						}
					}
					HighGui.imshow("tracker", frame);
					if (HighGui.waitKey(1) == 27)
						System.exit(0);
					break;
				}
				break;
			}
		}
	}

}

public class TryMultiTracking {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new DTrytracking().run(args);
	}
}
