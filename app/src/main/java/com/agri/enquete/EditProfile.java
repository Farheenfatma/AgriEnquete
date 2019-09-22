package com.agri.enquete;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.google.firebase.database.DatabaseReference;
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

public class EditProfile extends AppCompatActivity implements LocationListener {

    DatabaseReference firebaseDatabase;
    EditText name,phone,email,dob,loc,local,State;;
    String fmobile,fname,femail,fdob,location,currentloc,locality,state,loca,stat;;
    int mYear, mMonth, mDay;
    Spinner locationspin;
    LinearLayout locationlay;
    String visible="";
    TextView curenloc;
    Button farmerdobbtn,updatebtn;
    TextInputLayout namehint,phonehint,emailhint,dobhint,locationhint,localitytag,statetag;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    LocationManager locationManager;
    ProgressBar progressBar;
    String[] option;

    DatePickerDialog datePickerDialog;
    String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";


    DateFormat df;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.Email);
        dob=findViewById(R.id.farmerdob);
        farmerdobbtn=findViewById(R.id.farmerdobbtn);

        loc=findViewById(R.id.location);
        updatebtn=findViewById(R.id.updateprofile);
        curenloc=findViewById(R.id.curloc);
        locationlay=findViewById(R.id.mannuallay);
        locationspin=findViewById(R.id.locationspin);
        progressBar=findViewById(R.id.progress);
        String  t=LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(EditProfile.this, t);
        final Resources resources = context.getResources();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.editprofname));
        option=resources.getStringArray(R.array.locationspin);
        namehint=findViewById(R.id.namehint);
        phonehint=findViewById(R.id.phonehint);
        emailhint=findViewById(R.id.emailhint);
        dobhint=findViewById(R.id.dobhint);
        local=findViewById(R.id.locality);
        State=findViewById(R.id.State);
        locationhint=findViewById(R.id.namloctag);
        localitytag=findViewById(R.id.areatag);
        statetag=findViewById(R.id.statetag);
        Intent intent=getIntent();
        fname=intent.getStringExtra("name");
        fmobile=intent.getStringExtra("mobile");
        femail=intent.getStringExtra("email");
        fdob= intent.getStringExtra("dob");
        location=intent.getStringExtra("location");

        name.setText(fname);
        phone.setText(fmobile);
        email.setText(femail);
        dob.setText(fdob);
        curenloc.setText(location);
        curenloc.setVisibility(View.VISIBLE);
        namehint.setHint(resources.getString(R.string.profilename));
        phonehint.setHint(resources.getString(R.string.profilephone));
        emailhint.setHint(resources.getString(R.string.profileemail));
        dobhint.setHint(resources.getString(R.string.profiledob));
        locationhint.setHint(resources.getString(R.string.profileLocation));
        localitytag.setHint(resources.getString(R.string.profilearea));
        statetag.setHint(resources.getString(R.string.profilestate));
        sp= getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor=sp.edit();
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, ""+resources.getString(R.string.editphone), Toast.LENGTH_SHORT).show();
            }
        });

        getLocation();
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,option);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locationspin.setAdapter(aa);
        locationspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(item.equals(option[0])){
                    Toast.makeText(EditProfile.this, "sgdjfsaud", Toast.LENGTH_SHORT).show();
                }
                if(item.equals(option[1])){
                    locationlay.setVisibility(View.VISIBLE);
                    visible="ed";
                    curenloc.setVisibility(View.GONE);

                }
                if(item.equals(option[2])){
                    //get current location
                    visible="cur";
                    locationlay.setVisibility(View.GONE);
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfile.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
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
        df = new SimpleDateFormat("dd-MM-yyyy");
        dob.setKeyListener(null);
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

        farmerdobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                // Get Current Date
                getDOB();
            }
        });
        firebaseDatabase= FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDOB() {

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {finish();}
        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {
        fname = name.getText().toString();
        femail = email.getText().toString();
        fdob = dob.getText().toString();
               if(fname.equals("")){
            name.setError("Enter Name");
        }
        if(femail.equals("")){
            email.setError("Enter Email");
        }
        if(fdob.equals("")){
            dob.setError("Enter Date of Birth");
        }

        if(visible.equals("ed")){
            location=loc.getText().toString();
            loca=local.getText().toString();
            stat=State.getText().toString();
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

            Matcher matcher = Pattern.compile(validemail).matcher(femail);
            if (matcher.matches()) {
                updatebtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
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
                            Toast.makeText(EditProfile.this, "Profile is Updated", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Please try again later", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });

            }
            else {
                email.setError("Enter Valid Email id");
            }
        }
    }

    private void saveval() {
        Matcher matcher = Pattern.compile(validemail).matcher(femail);
        if (matcher.matches()) {
            progressBar.setVisibility(View.VISIBLE);
            updatebtn.setVisibility(View.GONE);

            editor.putString("fname", fname);
            editor.putString("Login","done");
            editor.putString("Location",location);
            editor.putString("locality",loca);
            editor.putString("state",stat);
            editor.commit();
            farmerdetails user = new farmerdetails(fname, fmobile,femail,fdob,location,loca,stat);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProfile.this, "Profile is Updated", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfile.this, "Please try again later", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });

        }
        else {
            email.setError("Enter Valid Email id");
        }

    }

    public  void compareDatesByDateMethods(DateFormat df, Date current, Date selected) {
        //how to check if two dates are equals in java
        if (current.equals(selected)) {
            date="not";
            Toast.makeText(EditProfile.this, "Please Check Date of Birth", Toast.LENGTH_SHORT).show();
            getDOB();
        }

        //checking if date1 comes before date2
        if (current.before(selected)) {
            date="not";
            Toast.makeText(EditProfile.this, "Please Check Date of Birth", Toast.LENGTH_SHORT).show();
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
            StringBuilder sb = new StringBuilder();
            sb.append(address.getFeatureName()).append(", ");
            sb.append(address.getSubLocality()).append(", ");
            sb.append(address.getSubAdminArea()).append(", ");
            sb.append(address.getLocality()).append(", ");
            sb.append(address.getPostalCode()).append(", ");
            sb.append(address.getCountryName());
            locality=address.getSubLocality();
            state=address.getLocality();
            if(locality==""){
                locality=address.getSubAdminArea();
            }
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
        Toast.makeText(EditProfile.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

}
