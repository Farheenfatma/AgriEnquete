package com.agri.enquete;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FarmerHome extends AppCompatActivity implements LocationListener {

    String fmobile, fname,femail,fdob,location,currentloc,locality,state,loca,stat;
   EditText nameed, mobileed,email,dob,loc,local,State;
    int mYear, mMonth, mDay;
    Spinner locationspin;
    LinearLayout locationlay;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String visible="";
    Button farmerdobbtn,savebtn;
    DateFormat df;
    String date,uid;
    TextView curenloc;
    String cityname,areaname;
    LocationManager locationManager;
    ProgressBar progressBar;
    String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";
    DatePickerDialog datePickerDialog;
    String[] option={"Choose Location type","Mannul Location", "Current Location"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_home);
        nameed = findViewById(R.id.farmername);
        mobileed = findViewById(R.id.farmerphone);
        email=findViewById(R.id.farmeremail);
        dob=findViewById(R.id.farmerdob);
        progressBar=findViewById(R.id.progress1st);
        loc=findViewById(R.id.location);
        curenloc=findViewById(R.id.curloc);
        farmerdobbtn=findViewById(R.id.farmerdobbtn);
        locationlay=findViewById(R.id.mannuallay);
        locationspin=findViewById(R.id.locationspin);
        local=findViewById(R.id.locality);
        State=findViewById(R.id.State);
        mobileed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FarmerHome.this, "You can't change Phone Number", Toast.LENGTH_SHORT).show();
            }
        });        progressBar.setVisibility(View.GONE);
        savebtn=findViewById(R.id.save);
        getLocation();
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,option);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locationspin.setAdapter(aa);
        locationspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(item.equals("Mannul Location")){
                    locationlay.setVisibility(View.VISIBLE);
                    visible="ed";
                    curenloc.setVisibility(View.GONE);

                }
                if(item.equals("Current Location")){
                    //get current location

                    visible="cur";
                    locationlay.setVisibility(View.GONE);
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(FarmerHome.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    }
                    getLocation();
                     curenloc.setText(currentloc);
                    curenloc.setVisibility(View.VISIBLE);

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final String cureent=mDay+"-"+mMonth+"-"+mYear;
        mYear=mYear-8;


         datePickerDialog= new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String selected=dayOfMonth + "-" + monthOfYear + "-" + year;
                        try {
                            compareDatesByDateMethods(df, df.parse(cureent), df.parse(selected));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date=="done"){
                            dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        if(date=="not"){


                        }
//                        dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);

        dob.setKeyListener(null);
        df = new SimpleDateFormat("dd-MM-yyyy");
        farmerdobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FarmerHome.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                // Get Current Date
                getDOB();
            }
        });


        sp= getSharedPreferences("pref", Context.MODE_PRIVATE);
        fmobile=sp.getString("mobile",null);
        mobileed.setText(fmobile);
        editor=sp.edit();
    }


    public void save(View view) {
//        progressBar.setVisibility(View.VISIBLE);
        fname = nameed.getText().toString();
        femail=email.getText().toString();
        fdob=dob.getText().toString();


        if(fname.equals("")){
            nameed.setError("Enter Name");
        }
        if(femail.equals("")){
            email.setError("Enter Email");
        }
        if(fdob.equals("")){
            dob.setError("Enter Date of Birth");
        }
        if(visible.equals("ed")){
            if(location.equals("")){
                loc.setError("Enter Your Location");
            }
            if(loca.equals("")){
                local.setError("Enter Your Area");
            }
            if(stat.equals("")){
                State.setError("Enter Your State");
            }
            else{
                saveval();

            }

        }


        else{
            location=currentloc;
            loca=locality;
            stat=state;
            Matcher matcher= Pattern.compile(validemail).matcher(femail);
            if (matcher.matches()){
                progressBar.setVisibility(View.VISIBLE);
                savebtn.setVisibility(View.GONE);
                editor.putString("fname", fname);
                editor.putString("Login","done");
                editor.putString("Location",location);
                editor.putString("locality",loca);
                editor.putString("state",stat);
                editor.putString("currentuid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.commit();

                farmerdetails user = new farmerdetails(fname, fmobile,femail,fdob,location,loca,stat);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(FarmerHome.this,HomePage.class));
                            finish();
                            Toast.makeText(FarmerHome.this, "Welcome", Toast.LENGTH_LONG).show();

                        } else {
                            //display a failure message
                        }
                    }
                });
            }
            else{
                email.setError("Enter Valid email id");
            }
        }



    }
    private void saveval() {
        Matcher matcher = Pattern.compile(validemail).matcher(femail);
        if (matcher.matches()) {
            progressBar.setVisibility(View.VISIBLE);
            savebtn.setVisibility(View.GONE);
            editor.putString("fname", fname);
            editor.putString("Login","done");
            editor.putString("Location",location);
            editor.putString("locality",loca);
            editor.putString("state",stat);
            editor.putString("email",femail);
            editor.commit();
            farmerdetails user = new farmerdetails(fname, fmobile,femail,fdob,location,loca,stat);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FarmerHome.this, "Welcome", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(FarmerHome.this, "Please try again later", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });

        }
        else {
            email.setError("Enter Valid Email id");
        }

    }
    private void getDOB() {

        datePickerDialog.show();

    }

    public  void compareDatesByDateMethods(DateFormat df, Date current, Date selected) {
        //how to check if two dates are equals in java
        if (current.equals(selected)) {
            date="not";
            Toast.makeText(FarmerHome.this, "Please Check Date of Birth", Toast.LENGTH_SHORT).show();
            getDOB();
        }

        //checking if date1 comes before date2
        if (current.before(selected)) {
            date="not";
            Toast.makeText(FarmerHome.this, "Please Check Date of Birth", Toast.LENGTH_SHORT).show();
            getDOB();
        }

        //checking if date1 comes after date2
        if (current.after(selected)) {
            date="done";
//            dob.setText(cDay + "-" + (cMonth + 1) + "-" + cYear);
        }
    }
    public void getLocation() {

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
            getLocation();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addresses.get(0);

//            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                sb.append(address.getAddressLine(i)).append("\n");
//            }
            StringBuilder sb = new StringBuilder();
            sb.append(address.getFeatureName()).append(", ");
            sb.append(address.getSubLocality()).append(", ");
            sb.append(address.getSubAdminArea()).append(", ");
            sb.append(address.getLocality()).append(", ");
            sb.append(address.getPostalCode()).append(", ");
            sb.append(address.getCountryName());
            locality=address.getSubLocality();
            if(locality==""){
                locality=address.getSubAdminArea();
            }
            state=address.getLocality();


            currentloc = sb.toString();



//            taskLoadUp(cityname);
//            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2);

        }catch(Exception e)
        {
            getLocation();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(FarmerHome.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }


}

