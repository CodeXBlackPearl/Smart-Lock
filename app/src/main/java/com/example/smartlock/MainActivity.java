package com.example.smartlock;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.example.smartlock.databinding.ActivityMain2Binding;

import java.util.concurrent.Executor;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class MainActivity extends AppCompatActivity {

    private ActivityMain2Binding binding;
    private Executor executor;
    private Executor executor2;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt biometricPrompt2;
    private BiometricPrompt.PromptInfo promptInfo;
    ActivityResultLauncher<Intent> fingerPrintLauncher;
    ActivityResultLauncher<Intent> fingerPrintLauncher2;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);

        String[] arr = {"Fingerprint", "Passcode"};

        binding.btnLock.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setItems(arr, (dialog, which) -> {
                        if (which == 0) {
                            fingerPrint();
                        } else if(which == 1){
                           Intent passcodeIntent = new Intent(MainActivity.this, PasscodeActivity.class);
                           startActivityForResult(passcodeIntent,1);
                        }
                    })
                    .show();
        });

        binding.btnUnLock.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setItems(arr, (dialog, which) -> {
                        if (which == 0) {
                            fingerPrint2();
                        } else if(which == 1) {
                            Intent passcodeIntent2 = new Intent(MainActivity.this, PasscodeActivity.class);
                            startActivityForResult(passcodeIntent2,2);
                        }
                    })
                    .show();
        });

        executor = ContextCompat.getMainExecutor(this);
        executor2 = ContextCompat.getMainExecutor(this);

        fingerPrintLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            biometricPrompt.authenticate(promptInfo);
                        }
                    }
                });

        fingerPrintLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result2) {
                        if (result2.getResultCode() == Activity.RESULT_OK) {
                            biometricPrompt2.authenticate(promptInfo);
                        }
                    }
                });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric enable")
                .setSubtitle("Enable touch id by using your biometric credential")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(true) //for face id
                .setAllowedAuthenticators(BIOMETRIC_STRONG | BIOMETRIC_WEAK)
                .build();

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void fingerPrint() {
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        int code = biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK);

        if (code == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
            enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_WEAK);
            fingerPrintLauncher.launch(enrollIntent);

        } else if (code == BiometricManager.BIOMETRIC_SUCCESS) {

            biometricPrompt = new BiometricPrompt(this,
                    executor, new BiometricPrompt.AuthenticationCallback() {

                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
//                   Toast.makeText(MainActivity.this, "Door locked successfully !", Toast.LENGTH_SHORT).show();]
                    MotionToast.Companion.createColorToast(MainActivity.this,
                            "Success",
                            "Door locked successfully !",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(MainActivity.this, R.font.mark_pro_medium));

                    binding.lockUnlockStatus.setText("Locked");
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
//                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();

                }
            });
            biometricPrompt.authenticate(promptInfo);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void fingerPrint2() {
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        int code = biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK);

        if (code == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
            enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_WEAK);
            fingerPrintLauncher2.launch(enrollIntent);

        } else if (code == BiometricManager.BIOMETRIC_SUCCESS) {

            biometricPrompt2 = new BiometricPrompt(this,
                    executor2, new BiometricPrompt.AuthenticationCallback() {

                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
//                    Toast.makeText(MainActivity.this, "Door unlocked successfully !", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createColorToast(MainActivity.this,
                            "Success",
                            "Door unlocked successfully !",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(MainActivity.this, R.font.mark_pro_medium));

                    binding.lockUnlockStatus.setText("Unlocked");
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
//                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
            biometricPrompt2.authenticate(promptInfo);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String lockStatus = data.getStringExtra("LockStatus");
                binding.lockUnlockStatus.setText(lockStatus);

                MotionToast.Companion.createColorToast(MainActivity.this,
                        "Success",
                        "Door locked successfully !",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(MainActivity.this, R.font.mark_pro_medium));
            }
        }

        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String unlockStatus = data.getStringExtra("UnlockStatus");
                binding.lockUnlockStatus.setText(unlockStatus);

                MotionToast.Companion.createColorToast(MainActivity.this,
                        "Success",
                        "Door unlocked successfully !",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(MainActivity.this, R.font.mark_pro_medium));
            }
        }
    }
}