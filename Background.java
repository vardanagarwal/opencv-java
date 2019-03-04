package va;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

class DoBackground {

	public void TryBackground() throws InterruptedException {
		VideoCapture capture = new VideoCapture("/home/vardan/Desktop/VID_20180914_183107.mp4");
		Mat frame = new Mat(1500, 1000, CvType.CV_8UC4);
		BackgroundSubtractorMOG2 bg = Video.createBackgroundSubtractorMOG2(500, 16, true);
		Mat fgmask = new Mat(1500, 1000, CvType.CV_8UC1);
		if (!capture.isOpened()) {
			System.err.println("video capture not opened");
			System.exit(0);
		}
		while (capture.read(frame)) {
			if (frame.empty()) {
				System.err.println("frame is empty");
				System.exit(0);
			}
			// Imgproc.GaussianBlur(frame, frame, new Size(7,7), 0,0);
			bg.apply(frame, fgmask);
//			final ArrayList<MatOfPoint> points = new ArrayList<MatOfPoint>();
//			final Mat hierarchy = new Mat();
//			Imgproc.findContours(fgmask, points, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//			Imgproc.drawContours(fgmask, points, 3, new Scalar(255, 255, 255));

			HighGui.imshow("try Background subtraction", fgmask);
			HighGui.imshow("normal", frame);
			if (HighGui.waitKey(30) == 27)
				break;

		}
		System.exit(0);
	}
}

public class Background {
	public static void main(String args[]) throws InterruptedException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new DoBackground().TryBackground();
	}
}