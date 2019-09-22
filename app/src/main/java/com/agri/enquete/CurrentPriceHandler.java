package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentPriceHandler extends AppCompatActivity {

    DatabaseReference demoRef;
    String urlget,version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_price_handler);
        String  t=LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(this, t);
        Resources resources = context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.pricetitlename));
        demoRef= FirebaseDatabase.getInstance().getReference("cropPrice");

        final Intent intent=new Intent(CurrentPriceHandler.this, CurrentPrice.class);
        demoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    urlget = String.valueOf(dataSnapshot.child("price").getValue());
                    version= (String) dataSnapshot.child("version").getValue();
                    Log.d("kjhjg", "Value is: " + urlget);
//                        Toast.makeText(HomePage.this, "url is " + urlget, Toast.LENGTH_SHORT).show();
                    intent.putExtra("url",urlget);
                    intent.putExtra("version",version);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("jmhgfdghj", "Failed to read value.", error.toException());
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //start your activity here
                startActivity(intent);
                finish();
            }

        }, 5000);

    }
}
