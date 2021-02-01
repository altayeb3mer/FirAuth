package com.altayeb.firauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtp extends AppCompatActivity {

    EditText editText;
    AppCompatButton button;
    //fireAuth
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        mAuth = FirebaseAuth.getInstance();
        init();
    }
    String phone = "";
    LinearLayout progressLay;
    private void init() {
        progressLay = findViewById(R.id.progressLay);
        editText = findViewById(R.id.phone);
        button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               phone  = editText.getText().toString().trim();
                if (!phone.isEmpty()){
                    sendOtp(phone);
                }else {
                    Toast.makeText(SendOtp.this, "ادخل رقم الهاتف", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                Toast.makeText(SendOtp.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(SendOtp.this, "تم الارسال", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),VerifyOtp.class);
                intent.putExtra("phone",phone);
                intent.putExtra("verifyId",verificationId);

                progressLay.setVisibility(View.GONE);
            }
        };





    }

    private void sendOtp(String phone) {
        progressLay.setVisibility(View.VISIBLE);
        try {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phone)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(SendOtp.this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }catch (Exception e){
            e.printStackTrace();
            progressLay.setVisibility(View.GONE);
        }




    }
}