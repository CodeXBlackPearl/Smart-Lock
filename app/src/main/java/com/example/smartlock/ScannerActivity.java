package com.example.smartlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ScannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    String scannedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        codeScanner();
    }

    public void codeScanner(){

        CodeScannerView scanner_view = findViewById(R.id.scannerView);
        TextView scannedInfo = findViewById(R.id.scannedInfo);

        codeScanner = new CodeScanner(ScannerActivity.this, scanner_view);

        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                        vibrator.vibrate(50);

                        scannedCode = result.getText();

                        scannedInfo.setText(result.getText());

                        scannedInfo.setOnClickListener(v -> {

                            if(!scannedCode.equals("Scanning...")){
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Information", scannedInfo.getText());
                                clipboardManager.setPrimaryClip(clipData);

                                if(clipData != null){
                                    successToast();
                                    onBackPressed();
                                }
                                else{
                                    failureToast();
                                }
                            }
                            else{
                                MotionToast.Companion.createColorToast(ScannerActivity.this,
                                        "Fail",
                                        "Scanning failed !",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(ScannerActivity.this, R.font.mark_pro_regular));
                            }
                        });
                    }
                });
            }
        });

        codeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Throwable thrown) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScannerActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        scanner_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void successToast(){
        MotionToast.Companion.createColorToast(this,
                "Success",
                "Text copied successfully !",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this , R.font.mark_pro_regular));
    }

    private void failureToast(){
        MotionToast.Companion.createColorToast(this,
                "Fail",
                "Text copied failed !",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.mark_pro_regular));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ScannedCode", scannedCode);
        setResult(RESULT_OK, intent);
        finish();
    }
}