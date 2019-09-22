package com.agri.enquete;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.agri.enquete.Livemandi.LiveMandi;
import com.agri.enquete.Weather.WeatherActivity;
import com.agri.enquete.main.MainVedio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , Language_Trans.BottomSheetListener{

    View dialogLayout;
    SmileRating smileRating;
    SharedPreferences sp;
    DatabaseReference demoRef,getval;
    String urlget,version,femail,fname;
    Resources resources;
    TextView nametv,emailtv;

    SharedPreferences.Editor edit;
    TextView firstop,secondndop,thirdop,fourthop,fifthop,sixthop,seventhop;

    CardView cardView;
    private static final float END_SCALE = 0.7f;
    private View contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        firstop=findViewById(R.id.cropinformation);
        secondndop=findViewById(R.id.organicfarming);
        thirdop=findViewById(R.id.agrivideo);
        fourthop=findViewById(R.id.weatherforcast);
        fifthop=findViewById(R.id.currentpric);
//        sixthop=findViewById(R.id.forumm);
        seventhop=findViewById(R.id.livetext);


        String  t=LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(HomePage.this, t);
        resources= context.getResources();

//        demoRef= FirebaseDatabase.getInstance().getReference("cropPrice").child("details");
        demoRef= FirebaseDatabase.getInstance().getReference("cropPrice");
        getval=FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        firstop.setText(resources.getString(R.string.firstop));
        secondndop.setText(resources.getString(R.string.secondndop));
        thirdop.setText(resources.getString(R.string.thirdop));
        fourthop.setText(resources.getString(R.string.fourthop));
        fifthop.setText(resources.getString(R.string.fifthop));
//        sixthop.setText(resources.getString(R.string.sixthop));
        seventhop.setText(resources.getString(R.string.seventhop));

       get();


        sp= getSharedPreferences("pref", Context.MODE_PRIVATE);



        contentView=findViewById(R.id.content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.color.title);
        setSupportActionBar(toolbar);

//
//
     final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

      final  NavigationView navigationView =  findViewById(R.id.nav_view);
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.contact).setTitle(resources.getString(R.string.contname));
            menu.findItem(R.id.rate).setTitle(resources.getString(R.string.ratename));
            menu.findItem(R.id.feedback).setTitle(resources.getString(R.string.feedname));
            menu.findItem(R.id.myprofile).setTitle(resources.getString(R.string.myprofname));
            menu.findItem(R.id.logout).setTitle(resources.getString(R.string.Logoutname));

            navigationView.setNavigationItemSelectedListener(this);
        }

        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawer.isDrawerOpen(navigationView)) {
                                                         drawer.closeDrawer(navigationView);
                                                     }
                                                     else {
                                                         drawer.openDrawer(navigationView);
                                                     }
                                                 }
                                             }
        );

        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawerView, float slideOffset) {
//                                               labelView.setVisibility(slideOffset > 0 ? View.VISIBLE : View.GONE);

                                               // Scale the View based on current slide offset
                                               final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                               final float offsetScale = 1 - diffScaledOffset;
                                               contentView.setScaleX(offsetScale);
                                               contentView.setScaleY(offsetScale);

                                               // Translate the View, accounting for the scaled width
                                               final float xOffset = drawerView.getWidth() * slideOffset;
                                               final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                               final float xTranslation = xOffset - xOffsetDiff;
                                               contentView.setTranslationX(xTranslation);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
                                           }
                                       }
        );
        View header=navigationView.getHeaderView(0);
        nametv=header.findViewById(R.id.navtv);
        emailtv=header.findViewById(R.id.emailtv);


        getval.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    fname = dataSnapshot.child("name").getValue(String.class);
                    femail = dataSnapshot.child("email").getValue(String.class);


                }
                nametv.setText(fname);
                emailtv.setText(femail);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//

    }

    private void get() {
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
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("url",urlget);
                    editor.putString("ver",version);
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("jmhgfdghj", "Failed to read value.", error.toException());
            }
        });
//        Toast.makeText(this, ""+urlget+" "+version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            startActivity(new Intent(this,Contact_us.class));
        } else if (id == R.id.rate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
            LayoutInflater inflater = getLayoutInflater();
            dialogLayout = inflater.inflate(R.layout.activity_rate_act, null);
            TextView ratetext=dialogLayout.findViewById(R.id.lblRateMe);
            ratetext.setText(resources.getString(R.string.ratename));


            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    smileRating = dialogLayout.findViewById(R.id.smile_rating);
                    smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                        @Override
                        public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                            // reselected is false when user selects different smiley that previously selected one
                            // true when the same smiley is selected.
                            // Except if it first time, then the value will be false.
                            switch (smiley) {
                                case SmileRating.BAD:
                                    Toast.makeText(HomePage.this, "Bad", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmileRating.GOOD:
                                    Toast.makeText(HomePage.this, "Good", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmileRating.GREAT:
                                    Toast.makeText(HomePage.this, "Great", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmileRating.OKAY:
                                    Toast.makeText(HomePage.this, "Okay", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmileRating.TERRIBLE:
                                    Toast.makeText(HomePage.this, "Terrible", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });

                }
            });
            builder.setView(dialogLayout);
            builder.show();
        }

        else if(id==R.id.myprofile){
            startActivity(new Intent(HomePage.this,Profile.class));
        }
         else if (id == R.id.feedback) {
            startActivity(new Intent(HomePage.this,Feedback_act.class));

        }
//        else if (id == R.id.setting) {
//            startActivity(new Intent(HomePage.this,Setting_act.class));
//
//        }
        else if (id == R.id.logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomePage.this,FarmerLogin.class));
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("Login");
            editor.remove("currentuid");
            editor.commit();
            finish();

        }
        else if (id == R.id.contact) {
            Intent i =new Intent(this,Contact_us.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void open(View view) {
        if(view.getId()==R.id.cropinfo){
            Intent intent=new Intent(this,CropList.class);
            startActivity(intent);
        }
//        if(view.getId()==R.id.forum){
//            Intent intent=new Intent(this, SignUpActivity.class);
//            startActivity(intent);
//        }
        if(view.getId()==R.id.agrivideos){
            Intent intent=new Intent(this,MainVedio.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.weather){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.organic){
            Intent intent=new Intent(this,Organic_Farming.class);
            startActivity(intent);
        }
//        if(view.getId()==R.id.farmequi){
//            Intent intent=new Intent(this, equip_list.class);
//            startActivity(intent);
//        }
        if(view.getId()==R.id.currentprice){

            Intent intent=new Intent(this,CurrentPriceHandler.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.livemandi)
        {
            Intent intent=new Intent(this,LiveMandi.class);
            startActivity(intent);
        }
//
//        if(view.getId()==R.id.forum){
//            Toast.makeText(this, "Should be implementation", Toast.LENGTH_SHORT).show();
//        }
//        if(view.getId()==R.id.diseases){
//            Intent intent=new Intent(this,diseasesTreatement.class);
//            startActivity(intent);
//        }

    }

    @Override
    public void onButtonClicked(String text) {

//        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
    }

    public void langchange(View view) {
        Language_Trans bottomSheet = new Language_Trans();
            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }
}

