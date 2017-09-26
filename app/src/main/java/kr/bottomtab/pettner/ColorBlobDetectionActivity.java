package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColorBlobDetectionActivity extends AppCompatActivity implements OnTouchListener, CvCameraViewListener2 {
    private static final String  TAG              = "OCVSample::Activity";

    private boolean              mIsColorSelected = false;
    private Mat mRgba;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;
    private ColorBlobDetector    mDetector;
    private Mat mSpectrum;
    private Size SPECTRUM_SIZE;
    private Scalar CONTOUR_COLOR;
    int r1,g1,b1,r2,g2,b2,r3,g3,b3,value1,value2,value3;
    private CameraBridgeViewBase mOpenCvCameraView;
    Button select;
    double r;
    double g;
    double b;
    int i=0;
    ImageView img1,img2,img3;
    Button finishBtn;
    ArrayList<Integer> num=new ArrayList<>();
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ColorBlobDetectionActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_color_blob_detection);

        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        finishBtn=(Button) findViewById(R.id.finishBtn);
        select=(Button)findViewById(R.id.take_picture);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    img1.setBackgroundColor(Color.rgb((int)r,(int)g,(int)b));
                    r1=(int)r;
                    g1=(int)g;
                    b1=(int)b;
                }else if(i==1){
                    img2.setBackgroundColor(Color.rgb((int)r,(int)g,(int)b));
                    r2=(int)r;
                    g2=(int)g;
                    b2=(int)b;
                }else if(i==2){
                    img3.setBackgroundColor(Color.rgb((int)r,(int)g,(int)b));
                    r3=(int)r;
                    g3=(int)g;
                    b3=(int)b;
                    finishBtn.setVisibility(View.VISIBLE);
                }
                i++;

            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ColorBlobDetectionActivity.this,KitSaveActivity.class);

                int data1=Math.abs(r1-254)+Math.abs(g1-202)+Math.abs(b1-152);
                int data2=Math.abs(r1-250)+Math.abs(g1-168)+Math.abs(b1-146);
                int data3=Math.abs(r1-235)+Math.abs(g1-146)+Math.abs(b1-142);
                int data4=Math.abs(r1-240)+Math.abs(g1-138)+Math.abs(b1-134);
                int data5=Math.abs(r1-232)+Math.abs(g1-107)+Math.abs(b1-137);
                num.add(data1);
                num.add(data2);
                num.add(data3);
                num.add(data4);
                num.add(data5);
                value1= Collections.min(num);
                if(value1==data1){
                    value1=-1;
                }else if(value1==data2){
                    value1=1;
                }else if(value1==data3){
                    value1=2;
                }else if(value1==data4){
                    value1=4;
                }else if(value1==data5){
                    value1=8;
                }
                num.clear();
                data1=Math.abs(r2-144)+Math.abs(g2-208)+Math.abs(b2-182);
                data2=Math.abs(r2-140)+Math.abs(g2-196)+Math.abs(b2-135);
                data3=Math.abs(r2-123)+Math.abs(g2-176)+Math.abs(b2-86);
                data4=Math.abs(r2-140)+Math.abs(g2-144)+Math.abs(b2-67);
                data5=Math.abs(r2-129)+Math.abs(g2-118)+Math.abs(b2-54);
                int data6=Math.abs(r2-119)+Math.abs(g2-85)+Math.abs(b2-58);
                num.add(data1);
                num.add(data2);
                num.add(data3);
                num.add(data4);
                num.add(data5);
                num.add(data6);
                value2= Collections.min(num);
                if(value2==data1){
                    value2=0;
                }else if(value2==data2){
                    value2=100;
                }else if(value2==data3){
                    value2=250;
                }else if(value2==data4){
                    value2=500;
                }else if(value2==data5){
                    value2=1000;
                }else if(value2==data6){
                    value2=2000;
                }
                num.clear();
                data1=Math.abs(r3-220)+Math.abs(g3-233)+Math.abs(b3-119);
                data2=Math.abs(r3-186)+Math.abs(g3-211)+Math.abs(b3-109);
                data3=Math.abs(r3-165)+Math.abs(g3-193)+Math.abs(b3-119);
                data4=Math.abs(r3-144)+Math.abs(g3-185)+Math.abs(b3-17);
                data5=Math.abs(r3-112)+Math.abs(g3-175)+Math.abs(b3-154);
                data6=Math.abs(r3-89)+Math.abs(g3-156)+Math.abs(b3-138);
                num.add(data1);
                num.add(data2);
                num.add(data3);
                num.add(data4);
                num.add(data5);
                num.add(data6);
                value3= Collections.min(num);
                if(value3==data1){
                    value3=0;
                }else if(value3==data2){
                    value3=1;
                }else if(value3==data3){
                    value3=30;
                }else if(value3==data4){
                    value3=100;
                }else if(value3==data5){
                    value3=300;
                }else if(value3==data6){
                    value3=2000;
                }

                i.putExtra("value1",value1);
                i.putExtra("value2",value2);
                i.putExtra("value3",value3);
                startActivity(i);

            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
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
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255,0,0,255);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int)event.getX() - xOffset;
        int y = (int)event.getY() - yOffset;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x>4) ? x-4 : 0;
        touchedRect.y = (y>4) ? y-4 : 0;

        touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Log.i(TAG,"width: "+touchedRect.width+","+touchedRect.height);

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");
        r=mBlobColorRgba.val[0];
        g=mBlobColorRgba.val[1];
        b=mBlobColorRgba.val[2];
        mDetector.setHsvColor(mBlobColorHsv);
        Log.i(TAG,"mBlobColorHsv: "+mBlobColorHsv);
        /*Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);*/

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        if (mIsColorSelected) {
            mDetector.process(mRgba);
            List<MatOfPoint> contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);

            Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel);
        }

        return mRgba;
    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }
}
