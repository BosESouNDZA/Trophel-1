package com.example.bhurivatmontri.trophel;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bhurivatmontri.trophel.adapter.GridAdapter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by BOSESOUND on 17/12/2560.
 */

public class Camera extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    private int w, h;
    Mat mRgba,mRgbaF,mRgbaT;
    private CameraBridgeViewBase mOpenCvCameraView;
    TextView tvName;
    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);
    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;
    Mat descriptors2,descriptors1,descriptors3;
    Mat img1,img2;
    MatOfKeyPoint keypoints1,keypoints2,keypoints3;
    Context context;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d("ERROR", "Unable to load OpenCV");
        else
            Log.d("SUCCESS", "OpenCV loaded");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    try {
                        initializeOpenCVDependencies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private void initializeOpenCVDependencies() throws IOException {
        mOpenCvCameraView.enableView();
        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        img1 = new Mat();
        img2 = new Mat();
        AssetManager assetManager = getAssets();
        //InputStream test1 = assetManager.open("zzz.jpeg");
        // InputStream test2 = assetManager.open("ccc.jpeg");
        //InputStream test1 = getResources().get(R.drawable.zzz);
        if(GridAdapter.class.getName() == "")
        {

        }
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.test1);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.test2);
        Utils.bitmapToMat(bitmap1, img1);
        Utils.bitmapToMat(bitmap2, img2);
//        Size sz = new Size(1024,768);
//        Imgproc.resize( img1, img1, sz );
//        Imgproc.resize( img2, img2, sz );
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGB2GRAY);
        img1.convertTo(img1, 0); //converting the image to match with the type of the cameras image
        descriptors1 = new Mat();
        keypoints1 = new MatOfKeyPoint();
        detector.detect(img1, keypoints1);
        descriptor.compute(img1, keypoints1, descriptors1);
        // img2
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGB2GRAY);
        img2.convertTo(img2, 0); //converting the image to match with the type of the cameras image
        descriptors3 = new Mat();
        keypoints3 = new MatOfKeyPoint();
        detector.detect(img2, keypoints3);
        descriptor.compute(img2, keypoints3, descriptors3);
    }

    public Camera() {

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.camera);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        tvName = (TextView) findViewById(R.id.text1);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    public void onCameraViewStopped() {
    }

    public Mat recognize(Mat aInputFrame) {
//         Imgproc.cvtColor(aInputFrame, aInputFrame, Imgproc.COLOR_RGB2GRAY);
//        // -- Step 1: Detect the keypoints using Detector
//        descriptors2 = new Mat();
//        keypoints2 = new MatOfKeyPoint();
//        detector.detect(aInputFrame, keypoints2);
//
//
//        // -- Step 2: Calculate descriptors (feature vectors)
//        descriptor.compute(aInputFrame, keypoints2, descriptors2);


        // -- Step 3: Matching descriptor vectors using matcher
       // MatOfDMatch matches = new MatOfDMatch();

        List<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        matcher.knnMatch(descriptors1, descriptors3, matches, 5);
//        if (img1.type() == img2.type()) {
//            matcher.match(descriptors1, descriptors3 ,matches);
//        } else {
//            return aInputFrame;
//        }
//        List<DMatch> matchesList = matches.toList();
//
//        Double max_dist = 0.0;
//        Double min_dist = 100.0;
//        for (int i = 0; i < matchesList.size(); i++) {
//            Double dist = (double) matchesList.get(i).distance;
//            if (dist < min_dist)
//                min_dist = dist;
//            if (dist > max_dist)
//                max_dist = dist;
//        }
//
//        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
//        for (int i = 0; i < matchesList.size(); i++) {
//            if (matchesList.get(i).distance <= (3 * min_dist))
//                good_matches.addLast(matchesList.get(i));
//        }
//
//        MatOfDMatch goodMatches = new MatOfDMatch();
//        goodMatches.fromList(good_matches);
        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();
//        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
//            return aInputFrame;
//        }
        //--------------------------------------------------
        // ratio test
        LinkedList<DMatch> good_matches = new LinkedList<>();
        for (Iterator<MatOfDMatch> iterator = matches.iterator(); iterator.hasNext();) {
            MatOfDMatch matOfDMatch = (MatOfDMatch) iterator.next();
            if (matOfDMatch.toArray()[0].distance / matOfDMatch.toArray()[1].distance < 0.9) {
                good_matches.add(matOfDMatch.toArray()[0]);
            }
        }

        // get keypoint coordinates of good matches to find homography and remove outliers using ransac
        List<Point> pts1 = new ArrayList<Point>();
        List<Point> pts2 = new ArrayList<Point>();
        for(int i = 0; i<good_matches.size(); i++){
            pts1.add(keypoints1.toList().get(good_matches.get(i).queryIdx).pt);
            pts2.add(keypoints3.toList().get(good_matches.get(i).trainIdx).pt);
        }

        // convertion of data types - there is maybe a more beautiful way
        Mat outputMask = new Mat();
        MatOfPoint2f pts1Mat = new MatOfPoint2f();
        pts1Mat.fromList(pts1);
        MatOfPoint2f pts2Mat = new MatOfPoint2f();
        pts2Mat.fromList(pts2);

        // Find homography - here just used to perform match filtering with RANSAC, but could be used to e.g. stitch images
        // the smaller the allowed reprojection error (here 15), the more matches are filtered
        Mat Homog = Calib3d.findHomography(pts1Mat, pts2Mat, Calib3d.RANSAC, 15, outputMask, 2000, 0.995);

        // outputMask contains zeros and ones indicating which matches are filtered
        LinkedList<DMatch> better_matches = new LinkedList<DMatch>();
        for (int i = 0; i < good_matches.size(); i++) {
            if (outputMask.get(i, 0)[0] != 0.0) {
                better_matches.add(good_matches.get(i));
            }
        }
        MatOfDMatch better_matches_mat = new MatOfDMatch();
        better_matches_mat.fromList(better_matches);
        Features2d.drawMatches(img1, keypoints1, img2, keypoints3, better_matches_mat, outputImg, RED, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
        Imgproc.resize(outputImg, outputImg, aInputFrame.size());

        return outputImg;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return recognize(inputFrame.rgba());
    }
}
