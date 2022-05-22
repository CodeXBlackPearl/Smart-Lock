package com.example.smartlock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.smartlock.databinding.ActivityMainBinding;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignInActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    String scannedCode;

    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scannedCode = data.getStringExtra("ScannedCode");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnSignIn.setOnClickListener(v -> {

            if (binding.edtEmail.getText().toString().equals("meetdesai3311@gmail.com")
                    && binding.edtPassword.getText().toString().equals("00000007")
                        && binding.edtScannedCode.getText().toString().equals(scannedCode)) {

                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);

            } else {
                failureToast();
            }

        });

        binding.scanIcon.setOnClickListener(v -> {
            Intent scanIntent = new Intent(SignInActivity.this, ScannerActivity.class);
            startActivityForResult(scanIntent, 1);
        });

        binding.tvRegister.setOnClickListener (v -> {
            MotionToast.Companion.createColorToast(this,
                    "Info",
                    "Please register from FDL !",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.mark_pro_regular));
        });
    }

    private void failureToast(){
        MotionToast.Companion.createColorToast(this,
                "Error",
                "Invalid Email/Password/ScannedCode",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.mark_pro_regular));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}