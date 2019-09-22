package com.agri.enquete.Livemandi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agri.enquete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class addyourPost extends AppCompatActivity {
    DatabaseReference firebaseDatabase,firebaseData;
    View v;
    String imageEncoded,pname,location,mob,locality,state;
    EditText cropname,quantity,price,place,mobile,yname,localityval,stateval;
    List<MandiCrops> mandiCrops;
    Button post;
    ProgressBar progressBar;
    SharedPreferences sp;
    private static final int PICK_IMAGE_REQUEST = 1;
    final static int PICK_PDF_CODE = 2342;
    Bitmap imageBitmap;
    private Uri mImageUri;
    ImageView cropImage;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference, databaseReference;
    private Uri filePath;
    String mobilecheck="[0-9]{10}";

    String imgurl;
    private static final int REQUEST_CAPTURE_IMAGE = 100;


    //    private final int PICK_IMAGE_REQUEST = 71;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addyour_post);
        cropname=findViewById(R.id.cropname);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price);
        place=findViewById(R.id.place);
        mobile=findViewById(R.id.mobileno);
        cropImage=findViewById(R.id.cropimg);
        yname=findViewById(R.id.yname);
        localityval=findViewById(R.id.localityvalue);
        stateval=findViewById(R.id.statevalue);
        progressBar=findViewById(R.id.progress);


        sp= getSharedPreferences("pref", Context.MODE_PRIVATE);
        pname=sp.getString("fname",null);
        location=sp.getString("Location",null);
        locality=sp.getString("locality",null);
        state=sp.getString("state",null);

        mob=sp.getString("mobile",null);

        cropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });



        firebaseData= FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getMobile();

        firebaseDatabase= FirebaseDatabase.getInstance().getReference("LiveMandi");
        mandiCrops = new ArrayList<>();
        post=findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdone();
            }
        });
        place.setText(location);
        mobile.setText(mob);
        yname.setText(pname);
        localityval.setText(locality);
        stateval.setText(state);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getMobile() {
        firebaseData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list


                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String fmobile = dataSnapshot.child("mobile").getValue(String.class);
                    String name=dataSnapshot.child("name").getValue(String.class);

                    mobile.setText(fmobile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {finish();}
        return super.onOptionsItemSelected(item);
    }

    private void postdone() {
//        uploadImage(imageEncoded);
postvalues(imageEncoded);
    }
    private void get() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(imageBitmap==null){
            if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }

        }
        else{
            if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 250, 320, true);

                cropImage.setImageBitmap(scaled);
               encodeBitmapAndSaveToFirebase(imageBitmap);
            }
        }



    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

    }

    private void postvalues(String  imgurl) {
        String cropn=cropname.getText().toString();
        String quan=quantity.getText().toString();
        String pri= price.getText().toString();
        String pla=place.getText().toString();
        String mob= mobile.getText().toString();
        String pname=yname.getText().toString();
        String locval=localityval.getText().toString();
        String state=stateval.getText().toString();
        if(cropn.equals("")){
            cropname.setError("Enter Crop name");
        }
        if(quan.equals("")){
            quantity.setError("Enter quantity");
        }
        if(pri.equals("")){
            price.setError("Enter  Price");
        }
        if(pla.equals("")){
            place.setError("Enter place");
        }
        if(pname.equals("")){
            yname.setError("Enter Your name");
        }
        if(locval.equals("")){
            localityval.setError("Enter Locality");
        }
        if(state.equals("")){
            stateval.setError("Enter State");
        }

        if( mob.equals("")||!Pattern.matches(mobilecheck,mob)){
            mobile.setError("Enter a valid Mobile no.");
        }

        else {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (imgurl == null) {
                Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();
            } else {
                post.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                MandiCrops mandiCrops = new MandiCrops(cropn, quan, pri, pla, mob, userid, imgurl, pname,locval,state);
                FirebaseDatabase.getInstance().getReference("LiveMandi")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                        .setValue(mandiCrops).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                    progressBar.setVisibility(View.GONE);
                            Toast.makeText(addyourPost.this, "Data is Updated", Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            Toast.makeText(addyourPost.this, "Some error occured, Please try again later", Toast.LENGTH_LONG).show();

                            //display a failure message
                        }
                    }
                });
            }
        }
    }

    public void getimg(View view) {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(imageBitmap==null){
            if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }

        }
        else{

            if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
        return;
    }
}



