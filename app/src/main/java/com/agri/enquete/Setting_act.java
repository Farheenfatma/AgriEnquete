package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Setting_act extends AppCompatActivity {

Button eng,hin;
TextView lang;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_act);

        eng = findViewById(R.id.eng);
        hin =  findViewById(R.id.hin);
        lang=findViewById(R.id.lang);

        String  t=LocaleHelper.getLanguage(this);
        context = LocaleHelper.setLocale(Setting_act.this, t);
        resources = context.getResources();
        lang.setText(resources.getString(R.string.text_translation));


        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = LocaleHelper.setLocale(Setting_act.this, "en");
                resources = context.getResources();
                lang.setText(resources.getString(R.string.text_translation));
                Intent intent = new Intent(getApplicationContext(), HomePage.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        hin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = LocaleHelper.setLocale(Setting_act.this, "hi");
                resources = context.getResources();
                lang.setText(resources.getString(R.string.text_translation));
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        } );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {finish();}
        return super.onOptionsItemSelected(item);
    }

}
