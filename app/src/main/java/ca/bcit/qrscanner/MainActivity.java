package ca.bcit.qrscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnScanQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews(){
        btnScanQR = findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        startActivity(new Intent(MainActivity.this, ScannedQRActivity.class));
    }
}
