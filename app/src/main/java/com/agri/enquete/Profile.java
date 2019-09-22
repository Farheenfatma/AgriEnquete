package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    DatabaseReference firebaseDatabase;

    String fmobile,fname,femail,fdob,location,locality,state;
    EditText name,phone,email,dob,loc,loca,stat;
    List<farmerdetails> farmers;
    TextInputLayout namehint,phonehint,emailhint,dobhint,locationhint,localityhint,statehint;
    DateFormat df;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.profiletool);
        toolbar.setTitle("My Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        String  t=LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(Profile.this, t);
        Resources resources = context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.myprofname));
        namehint=findViewById(R.id.namehint);
        phonehint=findViewById(R.id.phonehint);
        emailhint=findViewById(R.id.emailhint);
        dobhint=findViewById(R.id.dobhint);
        locationhint=findViewById(R.id.locationhint);
        loca=findViewById(R.id.localityvalue);
        stat=findViewById(R.id.statevalue);
        localityhint=findViewById(R.id.localitynhint);
        statehint=findViewById(R.id.statehint);

        name=findViewById(R.id.namevalue);
        phone=findViewById(R.id.phonevalue);
        email=findViewById(R.id.emailvalue);
        dob=findViewById(R.id.dobvalue);
        loc=findViewById(R.id.locationvalue);

        namehint.setHint(resources.getString(R.string.profilename));
        phonehint.setHint(resources.getString(R.string.profilephone));
        emailhint.setHint(resources.getString(R.string.profileemail));
        dobhint.setHint(resources.getString(R.string.profiledob));
        locationhint.setHint(resources.getString(R.string.profileLocation));
        localityhint.setHint(resources.getString(R.string.profilearea));
        statehint.setHint(resources.getString(R.string.profilestate));

       firebaseDatabase= FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        farmers = new ArrayList<>();
        database();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {finish();}
        return super.onOptionsItemSelected(item);
    }

    public void database() {

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                farmers.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    //getting artist

                    fname = dataSnapshot.child("name").getValue(String.class);
                    fmobile = dataSnapshot.child("mobile").getValue(String.class);
                    femail = dataSnapshot.child("email").getValue(String.class);
                    fdob = dataSnapshot.child("dob").getValue(String.class);
                    location = dataSnapshot.child("location").getValue(String.class);
                    locality=dataSnapshot.child("locality").getValue(String.class);
                    state=dataSnapshot.child("state").getValue(String.class);

                    name.setText(fname);
                    phone.setText(fmobile);
                    email.setText(femail);
                    dob.setText(fdob);
                    loc.setText(location);
                    loca.setText(locality);

                    stat.setText(state);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void open(View view) {
        Intent intent=new Intent(Profile.this,EditProfile.class);
        intent.putExtra("name",fname);
        intent.putExtra("mobile",fmobile);
        intent.putExtra("email",femail);
        intent.putExtra("dob",fdob);
        intent.putExtra("location",location);
        startActivity(intent);
    }
}