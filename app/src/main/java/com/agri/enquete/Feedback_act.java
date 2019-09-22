package com.agri.enquete;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Feedback_act extends AppCompatActivity {

    Resources resources;
    String emailtxt,nametxt,message,fmobile;
    TextInputLayout nametag,emailtag,msgtag;
    EditText name,email,msg;
    TextView feedname;
    DatabaseReference firebaseDatabase;
    Button submitbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_act);
        String  t=LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(Feedback_act.this, t);
        resources= context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.feedname));
        feedname=findViewById(R.id.feedname);
        firebaseDatabase= FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    fmobile = dataSnapshot.child("mobile").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        name=findViewById(R.id.name);
        msg=findViewById(R.id.msg);
        submitbtn=findViewById(R.id.submitFeedback);
        nametag=findViewById(R.id.nametag);
        msgtag=findViewById(R.id.msgtag);

        nametag.setHint(resources.getString(R.string.profilename));
        msgtag.setHint(resources.getString(R.string.message));
        submitbtn.setText(resources.getString(R.string.submitbtn));
        feedname.setText(resources.getString(R.string.feeadbackname));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {finish();}
        return super.onOptionsItemSelected(item);
    }

    public void submit(View view) {
        nametxt=name.getText().toString();
        message=msg.getText().toString();
        String txt="Name: "+nametxt+" \nMobile Number: "+fmobile+" \nMessage is:  "+message;
        Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"projectproject199@gmail.com"));

        intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback from Agri Enquete");
        intent.putExtra(Intent.EXTRA_TEXT,txt);

        try{
            startActivity(Intent.createChooser(intent,"Send Feedback...."));

        }
        catch(ActivityNotFoundException ex){

        }


    }
}
