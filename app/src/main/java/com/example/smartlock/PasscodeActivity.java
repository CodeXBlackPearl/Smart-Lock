package com.example.smartlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hanks.passcodeview.PasscodeView;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class PasscodeActivity extends AppCompatActivity {

    PasscodeView passcodeView;
    String passcode;

    String lockStatus = "Locked";
    String unlockStatus = "Unlocked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("PasscodeCollection").document("PasscodeActDoc")
                .get()
                .addOnSuccessListener(documentSnapshot ->
                        passcode = documentSnapshot.getString("Passcode"))
                .addOnFailureListener(e ->
                        Toast.makeText(PasscodeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show()
                );

        passcodeView = findViewById(R.id.passcodeView);

        passcodeView.setPasscodeLength(4)
                .setLocalPasscode("3311")
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        MotionToast.Companion.createColorToast(PasscodeActivity.this,
                                "Fail",
                                "Please enter a valid passcode !",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(PasscodeActivity.this, R.font.mark_pro_medium));
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(String number) {

//                        MotionToast.Companion.createColorToast(PasscodeActivity.this,
//                                "Success",
//                                "Check the status !",
//                                MotionToastStyle.SUCCESS,
//                                MotionToast.GRAVITY_BOTTOM,
//                                MotionToast.SHORT_DURATION,
//                                ResourcesCompat.getFont(PasscodeActivity.this, R.font.mark_pro_medium));

                        Intent intent = new Intent();
                        intent.putExtra("LockStatus", lockStatus);
                        intent.putExtra("UnlockStatus", unlockStatus);
                        setResult(RESULT_OK, intent);
                        finish();

                        onBackPressed();
                    }
                });
    }
}