package com.agri.enquete;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Contact_us extends AppCompatActivity {

    ImageButton call,mail;
    TextView developerstag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        call=findViewById(R.id.call);
        mail=findViewById(R.id.mail);
        developerstag=findViewById(R.id.developerstag);
        String  t= LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(Contact_us.this, t);
        Resources resources= context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.contname));
        developerstag.setText(resources.getString(R.string.developersnametag));
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makecall();
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"projectproject199@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Contact from Agri Enquete");
                try{
                    startActivity(Intent.createChooser(intent,"Contact by mail...."));

                }
                catch(ActivityNotFoundException ex){

                }

            }
        });


    }

    private void makecall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:9716623577"));
        startActivity(callIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
