package com.store.aurora;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeReadActivity extends AppCompatActivity {


    // Varibale Declaration

    SurfaceView bar_cameraPreview;
    TextView bar_txtResult;
    TextView bar_getcode;
    BarcodeDetector bar_itembarcodeDetector;
    CameraSource bar_cameraSource;
    DatabaseHelper find_itemdb;

    Boolean sate=true;
    final int RequestCameraPermissionID = 1001;
    //============================================================================================================

    public Boolean getSate() {
        return sate;
    }

    public void setSate(Boolean sate) {
        this.sate = sate;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        bar_cameraSource.start(bar_cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_read);


        // Variable declare

        bar_cameraPreview = (SurfaceView) findViewById(R.id.barCodeFindPreview);
        bar_txtResult = (TextView) findViewById(R.id.txtBacode1);
        bar_getcode=(TextView)findViewById(R.id.txtBarCode2);
        find_itemdb=new DatabaseHelper(this);


        //=============================================================================================

        scanBarCode();

        //

        //==========================================================================================


        Button backtolist = (Button) findViewById(R.id.backtolist);

        backtolist.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        Toast.makeText(getApplicationContext(), "Scan Barcode ....", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(BarCodeReadActivity.this,ItemListViewActivity.class);
                        startActivity(intent);

                    }
                }
        );





        //

    }


    //===========================cAMARA Funtions ====================================================

    public void scanBarCode()
    {


        // Function
        bar_itembarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        if(!bar_itembarcodeDetector.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        bar_cameraSource = new CameraSource.Builder(this, bar_itembarcodeDetector)
                .setRequestedPreviewSize(300, 100)
                .setRequestedPreviewSize(width, height)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();


        bar_cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    // Request permission
                    ActivityCompat.requestPermissions(BarCodeReadActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    bar_cameraSource.start(bar_cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                bar_cameraSource.stop();
            }
        });

        bar_itembarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcodes =  detections.getDetectedItems();

                if(qrcodes.size() != 0){

                    bar_txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            // create vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator != null) {
                                vibrator.vibrate(1000);

                            }


                            String inputval=qrcodes.valueAt(0).displayValue;
                            bar_getcode.setText(inputval);
                            String result=find_itemdb.findTheItem(inputval);
                            bar_txtResult.setText(result);

                            if(result!="not found" && getSate()){
                                find_itemdb.pickAction(find_itemdb.findTheItem(inputval));

                                setSate(false);
                                Intent intent = new Intent(BarCodeReadActivity.this,ItemListViewActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Barcode Found and Item Picked ", Toast.LENGTH_LONG).show();
                                System.out.println("Item packed ...");
                                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                                finishActivity(0);
                                finish();
                            }
                            else{

                                Toast.makeText(getApplicationContext(), "Try again Barcode Not Found ", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });

    }

    //==========================================================================================
}
