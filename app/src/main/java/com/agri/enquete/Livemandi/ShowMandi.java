package com.agri.enquete.Livemandi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agri.enquete.LocaleHelper;
import com.agri.enquete.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.agri.enquete.Livemandi.ViewPost.decodeFromFirebaseBase64;

public class ShowMandi extends AppCompatActivity {

    String key,useridval;
    DatabaseReference firebaseDatabase;
    List<MandiCrops> mandiCrops;
    String cname,cqunatity,cprice,cperson,cmobile,cplace,imgurl;
    TextView cropname,quantity,price,personname,mobileno,place;
    TextView cropnametag,quantitytag,pricetag,personnametag,mobilenotag,placetag;

    ImageButton callbtn;
    ImageView cropimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mandi);
        cropname=findViewById(R.id.cropname);
        quantity=findViewById(R.id.Quantity);
        price=findViewById(R.id.price);
        personname=findViewById(R.id.personname);
        mobileno=findViewById(R.id.mobileno);
        place=findViewById(R.id.place);
        callbtn=findViewById(R.id.call);
        cropnametag=findViewById(R.id.croptag);
        quantitytag=findViewById(R.id.quantitytag);
        pricetag=findViewById(R.id.pricetag);
        personnametag=findViewById(R.id.nametag);
        mobilenotag=findViewById(R.id.mobiletag);
        placetag=findViewById(R.id.placetag);
        cropimg=findViewById(R.id.cropimg);
        String  t= LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(ShowMandi.this, t);
        Resources resources= context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.viewposttitle));
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        cropnametag.setText(resources.getString(R.string.cropnametag));
        quantitytag.setText(resources.getString(R.string.quantitytag));
        pricetag.setText(resources.getString(R.string.pricetag));
        personnametag.setText(resources.getString(R.string.nametag));
        mobilenotag.setText(resources.getString(R.string.mobiletag));
        placetag.setText(resources.getString(R.string.placetag));

        useridval=intent.getStringExtra("useridval");
        mandiCrops = new ArrayList<>();
//        Toast.makeText(this, "userid  "+useridval, Toast.LENGTH_SHORT).show();


        firebaseDatabase= FirebaseDatabase.getInstance().getReference("LiveMandi").child(useridval).child(key);
        getvalue();
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void calling() {
        String numbercall="tel:"+cmobile;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(numbercall));
        startActivity(callIntent);
    }


    private void getvalue() {
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mandiCrops.clear();



                    MandiCrops artist=  dataSnapshot.getValue(MandiCrops.class);
                    cperson=artist.getName();
                    cqunatity=artist.getQuantity();
                    cprice=artist.getPrice();
                    imgurl=artist.getImgurl();
                    cmobile=artist.getMobileno();
                    cplace=artist.getPlace();
                    cname=artist.getCropname();



//                    Toast.makeText(ShowMandi.this, "sjgfsadjfgasjdgfjasg    " +us, Toast.LENGTH_SHORT).show();
                 setval();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setval() {

        cropname.setText(cname);
        quantity.setText(cqunatity);
        price.setText(cprice);
        personname.setText(cperson);
        mobileno.setText(cmobile);
        place.setText(cplace);
        Bitmap imageBitmap = null;
        try {
            imageBitmap = decodeFromFirebaseBase64(imgurl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropimg.setImageBitmap(imageBitmap);
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
