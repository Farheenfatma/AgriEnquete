package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {

    private String mVerificationId;
    private EditText editTextCode;
    private FirebaseAuth mAuth;
    SharedPreferences sp;
    String mobile;
    Button resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        sp= getSharedPreferences("pref", Context.MODE_PRIVATE);

        resend=findViewById(R.id.resendotp);

        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60*1000, 1000);
        // Set count down timer source activity.
        myCountDownTimer.setSourceActivity(this);
        myCountDownTimer.start();
        // Disable send verify code button.
        resend.setEnabled(false);
        resend.setBackgroundResource(R.drawable.btn_resnd_layout);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);



        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
         mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });



    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
//                VerifyOtp.this,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        Toast.makeText(this, "Code sent, please wait for a while", Toast.LENGTH_SHORT).show();
    }
//9354643851

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(VerifyOtp.this, "verification complete", Toast.LENGTH_SHORT).show();

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
//            Toast.makeText(VerifyOtp.this, "code sent", Toast.LENGTH_SHORT).show();

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOtp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Login", "verify");
                            editor.putString("mobile",mobile);
                            editor.commit();
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(VerifyOtp.this, FarmerHome.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            Toast.makeText(VerifyOtp.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                            editTextCode.setText("Wrong OTP, please Check your message for OTP");

                        }
                    }
                });
    }

    public void resend(View view) {
        sendVerificationCode(mobile);
    }
    /* This method will be invoked when CountDownTimer finish. */
    public void onCountDownTimerFinishEvent()
    {

        this.resend.setEnabled(true);
        this.resend.setBackgroundResource(R.drawable.btn_layout);
    }

    /* This method will be invoked when CountDownTimer tick event happened.*/
    public void onCountDownTimerTickEvent(long millisUntilFinished)
    {
        // Calculate left seconds.
        long leftSeconds = millisUntilFinished / 1000;

        String sendButtonText = "Left " + leftSeconds + " (s)";

        if(leftSeconds==0)
        {
            sendButtonText = "Resend Code";
        }

        // Show left seconds in send button.
        this.resend.setText(sendButtonText);
    }
}

