package com.example.nutta.opencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView mImageview;


    Uri Imaguri;

    Bitmap grayBitmap, imageBitmap, BrightnessBitmap;


    SeekBar Mbrightness;
    SeekBar Mcontrast;
    int Brightness;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i("OpenCV", "Success");
        } else {
            Log.i("OpenCV", "Fail");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageview = (ImageView) findViewById(R.id.IV);

        Mbrightness = (SeekBar) findViewById(R.id.seekBar);

        Mcontrast = (SeekBar) findViewById(R.id.contrastBar);

        Bundle bundle = getIntent().getExtras();
        Uri uri = Uri.parse(bundle.getString("selectedImageUri"));

        Imaguri = uri;
        mImageview.setImageURI(uri);

        // mImageview.setImageURI(imageUri);
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mImageview.setImageBitmap(imageBitmap);

        Mbrightness.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Bitmap edited = increaseBrightness(imageBitmap, progress);
                        mImageview.setImageBitmap(edited);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        Mcontrast.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Bitmap edited = increaseContrast(imageBitmap, progress);
                        mImageview.setImageBitmap(edited);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        OpenCVLoader.initDebug();
    }


    private void initViews() {
    }

    /*public void openGallary(View v){
        Intent  myIntent =new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(myIntent,100);




    }*/


    public void convertToGray(View v) {
        Mat Rgba = new Mat();
        Mat grayMat = new Mat();

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        //bitmap to MAT

        Utils.bitmapToMat(imageBitmap, Rgba);
        Imgproc.cvtColor(Rgba, grayMat, Imgproc.COLOR_RGB2GRAY);

        Utils.matToBitmap(grayMat, grayBitmap);

        mImageview.setImageBitmap(grayBitmap);


    }


    private Bitmap increaseBrightness(Bitmap v, int value) {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;


        int width = v.getWidth();
        int height = v.getHeight();

        BrightnessBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);


        Mat BrighMat = new Mat(BrightnessBitmap.getHeight(), BrightnessBitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(v, BrighMat);

        BrighMat.convertTo(BrighMat, -1, 1, value);
        Bitmap result = Bitmap.createBitmap(BrighMat.cols(), BrighMat.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(BrighMat, result);

        return result;


    }

    private Bitmap increaseContrast(Bitmap v, int value) {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;

        int n = -value;


        int width = v.getWidth();
        int height = v.getHeight();

        BrightnessBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);


        Mat BrighMat = new Mat(BrightnessBitmap.getHeight(), BrightnessBitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(v, BrighMat);

        BrighMat.convertTo(BrighMat, -1, 2, n);
        Bitmap result = Bitmap.createBitmap(BrighMat.cols(), BrighMat.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(BrighMat, result);

        return result;


    }


    public void Shapen(View v) {
        Mat Rgba = new Mat();
        Mat grayMat = new Mat();

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int KernelSize = 3;
        Mat Kernel = new Mat(KernelSize, KernelSize, CvType.CV_32F) {
            {
                put(0, 0, -1);
                put(0, 1, -1);
                put(0, 2, -1);

                put(1, 0, -1);
                put(1, 1, 9);
                put(1, 2, -1);

                put(2, 0, -1);
                put(2, 1, -1);
                put(2, 2, -1);

            } };

        //bitmap to MAT

        Utils.bitmapToMat(imageBitmap, Rgba);
        // Imgproc.cvtColor(Rgba,grayMat,Imgproc.COLOR_RGB2GRAY);
        Imgproc.filter2D(Rgba, grayMat, -1, Kernel);
        Utils.matToBitmap(grayMat, grayBitmap);

        mImageview.setImageBitmap(grayBitmap);


    }

    public void cropImage(View view) {
        CropImage.activity(Imaguri).start(MainActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageview.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, " " + error, Toast.LENGTH_LONG).show();
            }
        }
    }
}

