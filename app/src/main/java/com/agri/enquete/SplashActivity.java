package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    String PREF_NAME = "pref";
    String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences pref;
    String get,un;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        get=pref.getString(IS_FIRST_TIME_LAUNCH,null);
        un= pref.getString("Login",null);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(get==null){
//                    startActivity(new Intent(SplashActivity.this, HomePage.class));

                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }
                else{

                    if(un==null){
                        startActivity(new Intent(SplashActivity.this,FarmerLogin.class));
                        finish();
                    }
                    else{
                        if(un.equals("verify")){
                            startActivity(new Intent(SplashActivity.this,FarmerHome.class));
                            finish();
                        }
                        else if(un.equals("done")){
                            startActivity(new Intent(SplashActivity.this,HomePage.class));
                            finish();
                        }
                    }
                }
            }
        }, 3000);
    }
}