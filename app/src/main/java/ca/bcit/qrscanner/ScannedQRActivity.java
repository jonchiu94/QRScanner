package ca.bcit.qrscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

public class ScannedQRActivity extends AppCompatActivity{
    SurfaceView surfaceView;
    TextView txtQRValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        initViews();
    }
    private void initViews() {
        txtQRValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);

        btnAction.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (intentData.length() > 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(intentData)));
                }
            }
        });
    }
    private void initialiseDetectorsAndSources(){
        Toast.makeText(getApplicationContext(), "QR Scanner Started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920,1080)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            public void surfaceCreated(SurfaceHolder holder){
                try{
                    if(ActivityCompat.checkSelfPermission(ScannedQRActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(ScannedQRActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            public void surfaceDestroyed(SurfaceHolder holder){
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){
            public void release(){
                Toast.makeText(getApplicationContext(), "Scanner has been stopped",  Toast.LENGTH_SHORT).show();
            }
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtQRValue.post(new Runnable() {
                        public void run() {
                            btnAction.setText("launch URL");
                            intentData = barcodes.valueAt(0).displayValue;
                            txtQRValue.setText(intentData);
                        }
                    });
                }
            }
        });
    }
    protected void onPause(){
        super.onPause();
        cameraSource.release();
    }
    protected void onResume(){
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
