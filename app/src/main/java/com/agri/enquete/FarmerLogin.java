package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FarmerLogin extends AppCompatActivity {

    SharedPreferences sp;
    EditText mobile;
    String mobileno;
    Button permissionbtn;
    TextView permissiontext;
    LinearLayout loginlayout;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String mobilecheck="[0-9]{10}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_login);
        permissionbtn=findViewById(R.id.permissionbtn);
        permissiontext=findViewById(R.id.permissiontext);
        loginlayout=findViewById(R.id.loginlayout);
        sp= getSharedPreferences("pref", Context.MODE_PRIVATE);
        mobile=findViewById(R.id.mobile);

        if (checkPermission()) {
            loginlayout.setVisibility(View.VISIBLE);
            permissiontext.setVisibility(View.GONE);
            permissionbtn.setVisibility(View.GONE);

        }
        else{
            requestPermission();
        }

    }

    public void open(View view) {
        mobileno = mobile.getText().toString().trim();

        if(mobileno.isEmpty() || mobileno.length() < 10 || !Pattern.matches(mobilecheck,mobileno)){
            mobile.setError("Enter a valid mobile");
            mobile.requestFocus();
            return;
        }
        else{
            Intent intent=new Intent(this,VerifyOtp.class);
            intent.putExtra("mobile",mobileno);
            startActivity(intent);

        }

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),INTERNET );
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int result5= ContextCompat.checkSelfPermission(getApplicationContext(),ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                &&result2==PackageManager
                .PERMISSION_GRANTED &&result3==PackageManager.PERMISSION_GRANTED
                &&result4==PackageManager.PERMISSION_GRANTED &&result5==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{INTERNET,READ_SMS,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,CALL_PHONE,ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean internetAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readsmsAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readexternelAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeexternalAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean callAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean locationaccepted=grantResults[5]==PackageManager.PERMISSION_GRANTED;



                    if (internetAccepted && readsmsAccepted && readexternelAccepted && writeexternalAccepted && callAccepted && locationaccepted){

                        loginlayout.setVisibility(View.VISIBLE);
                        permissiontext.setVisibility(View.GONE);
                        permissionbtn.setVisibility(View.GONE);

                    }

                    else {
                        loginlayout.setVisibility(View.GONE);
                        permissiontext.setVisibility(View.VISIBLE);
                        permissionbtn.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(INTERNET)) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{INTERNET,READ_SMS,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,CALL_PHONE,ACCESS_FINE_LOCATION},
                                            PERMISSION_REQUEST_CODE);
                                }
                            }

                            return;
                        }
                    }

                }
                break;
        }
    }

    public void accept(View view) {
        requestPermission();
    }


}
