package com.agri.enquete;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CurrentPrice extends AppCompatActivity {
    private Button btnRequest, btnshow;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    ListView list;
    ArrayList<String> arraylist1;
    MyAdapter helper,db;
    int vr;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView mintxt,maxtext,avgtext,setpri;
    String complete;
    Handler mHandler;

    String urlget, version,ur,ver;
    String state, district, market, commodity, arrival_date, min_price, max_price, modal_price;
    String comodity,markt;
    DatabaseReference myRef, demoRef;
    Spinner spin1, spin2, spin3, spin4;
    LinearLayout layview,progress,textlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_price);
        arraylist1 = new ArrayList<String>();
        String  t=LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(this, t);
        Resources resources = context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.pricetitlename));

//        list = findViewById(R.id.lis);
        ur="null";
        version="null";
        complete="NO";
        mHandler = new Handler();
        mintxt=findViewById(R.id.minprice);
        maxtext=findViewById(R.id.maxprice);
        avgtext=findViewById(R.id.avgprice);
        textlay=findViewById(R.id.textlay);
        setpri=findViewById(R.id.setpri);
        spin1 = findViewById(R.id.spinner);
        spin2 = findViewById(R.id.spinner2);
        spin3 = findViewById(R.id.spinner3);
        spin4 = findViewById(R.id.spinner4);
        sp = getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sp.edit();
        urlget=sp.getString("url",null);
        ver=sp.getString("ver",null);

        progress=findViewById(R.id.progress);
        layview=findViewById(R.id.layoutview);



        Intent intent=getIntent();
        ur=intent.getStringExtra("url");
        version=intent.getStringExtra("version");



        helper = new MyAdapter(this);
//        urlget= sp.getString("url",null);
        demoRef= FirebaseDatabase.getInstance().getReference("cropPrice");
        btnRequest = findViewById(R.id.buttonreq);
        btnshow= findViewById(R.id.buttonshow);

//        Toast.makeText(this, version+" "+ur, Toast.LENGTH_SHORT).show();
        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                show();
                getprice();
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Read from the database
                progress.setVisibility(View.VISIBLE);

                layview.setVisibility(View.GONE);
               checkversion();
//                get();
//                sendAndRequestResponse();

            }

        });

        set();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void set(){
        if(ur=="null")
        {
            get();
        }
        else{

//            final
            checkversion();
        }
    }


    public void setvaluesql()
    {
//        Toast.makeText(this, "run", Toast.LENGTH_SHORT).show();
        long id = helper.insertData(state, district, market, commodity,arrival_date,min_price,max_price, modal_price);
        if (id <= 0) {
//            Toast.makeText(getApplicationContext(), "Insertion Unsuccessful", Toast.LENGTH_LONG).show();

        } else {
//            Toast.makeText(getApplicationContext(), "Insertion  successful", Toast.LENGTH_LONG).show();

        }

    }
    private void sendAndRequestResponse() {
        mRequestQueue = Volley.newRequestQueue(this);
//        Toast.makeText(this, "please wait a while:hjhjhjhj "+ur, Toast.LENGTH_SHORT).show();
        editor.putString("version",version);
        editor.commit();
        mStringRequest = new StringRequest(Request.Method.GET, ur,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(CurrentPrice.this, "waiting", Toast.LENGTH_SHORT).show();
                        //to get particular data from coloumn of server
                        try {
                            JSONObject obj=new JSONObject(response);
//                            JSONArray obj1=obj.getJSONArray("users");
                            JSONArray obj1=obj.getJSONArray("cropPrice");
                            for(int i=0; i<obj1.length();i++){
                                JSONObject jsonObject=obj1.getJSONObject(i);

//                                int id =jsonObject.getInt("id");
                                state =jsonObject.getString("state");
                                district=jsonObject.getString("district");
                                market=jsonObject.getString("market");
                                commodity=jsonObject.getString("commodity");
                                arrival_date=jsonObject.getString("arrival_date");
                                min_price=jsonObject.getString("min_price");
                                max_price=jsonObject.getString("max_price");
                                modal_price =jsonObject.getString("modal_price");


//                                checkversion();
                                setvaluesql();

//                                arraylist1.add(state);

//                                Toast.makeText(MainActivity.this, "dfgwebfv", Toast.LENGTH_SHORT).show();
//
//                                Toast.makeText(CurrentPrice.this, "id is:  "+state, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        show();

                        //to get whole data from server
//                        Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("dd","Error :" + error.toString());
                    }
                });
        mRequestQueue.add(mStringRequest);
        show();
    }
    //
    private void show() {
////

        ArrayList<String> data = helper.getData();

        final List newList = new ArrayList(new LinkedHashSet(data));
        Set<String> s = new LinkedHashSet<String>(data);
        Collections.sort(newList);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,newList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin1.setAdapter(aa);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(CurrentPrice.this, "selected "+item, Toast.LENGTH_SHORT).show();
                getDistirct(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //
    private void getDistirct(String item) {
        ArrayList<String> data = helper.getDistrict(item);

//        Toast.makeText(this, "DATA IS " + data, Toast.LENGTH_SHORT).show();
        final List newList = new ArrayList(new LinkedHashSet(data));
        Set<String> s = new LinkedHashSet<String>(data);
        Collections.sort(newList);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,newList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin2.setAdapter(aa);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(CurrentPrice.this, "selected "+item, Toast.LENGTH_SHORT).show();
                getmarket(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void getmarket(String  item) {
        ArrayList<String> data = helper.getMarket(item);
//        Toast.makeText(this, "DATA IS " + data, Toast.LENGTH_SHORT).show();

        final List newList = new ArrayList(new LinkedHashSet(data));
        Set<String> s = new LinkedHashSet<String>(data);
        Collections.sort(newList);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,newList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin3.setAdapter(aa);
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                markt = parent.getItemAtPosition(position).toString();
//                Toast.makeText(CurrentPrice.this, "selected "+markt, Toast.LENGTH_SHORT).show();
                getCommodity(markt);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCommodity(String item) {
        ArrayList<String> data = helper.getcommodity(item);
//        Toast.makeText(this, "DATA IS " + data, Toast.LENGTH_SHORT).show();
        final List newList = new ArrayList(new LinkedHashSet(data));
        Set<String> s = new LinkedHashSet<String>(data);
        Collections.sort(newList);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,newList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin4.setAdapter(aa);
        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comodity = parent.getItemAtPosition(position).toString();
//                Toast.makeText(CurrentPrice.this, "selected "+comodity, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void get()
    {
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                Toast.makeText(CurrentPrice.this, "value is in working", Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    ur = String.valueOf(dataSnapshot.child("price").getValue());
                    version= (String) dataSnapshot.child("version").getValue();
                    Log.d("kjhjg", "Value is: " + ur);
//                    Toast.makeText(CurrentPrice.this, "bb" + ur, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("jmhgfdghj", "Failed to read value.", error.toException());
            }
        });
        checkversion();
//        editor.putString("version", version);
//        editor.commit();
//        sendAndRequestResponse();
    }
    //
    private void checkversion()
    {
        String value=helper.check();
//        boolean value=true;

        String ver=sp.getString("version",null);
        if(value.equals("true")){
            if(version==null){
                get();
            }
            else {
                if (version.equals(ver)) {
                    show();
//                int spinval=spin1.getDropDownWidth();
                    int spinval = spin1.getAdapter().getCount();
//                    Toast.makeText(this, "f1 " + spinval, Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
//                    checkversion();
                            progress.setVisibility(View.GONE);

                            layview.setVisibility(View.VISIBLE);

                        }

                    }, 5000);
                    complete = "yes";
//                Toast.makeText(this, "data show", Toast.LENGTH_SHORT).show();
                } else {
//                Toast.makeText(this, "data set database", Toast.LENGTH_SHORT).show();
//                helper.deletetable(CurrentPrice.this);
////                //write update code in database
//                db=new MyAdapter(CurrentPrice.this);

                    //need to update the table
                    getjsonval();

//                sendAndRequestResponse();
                    editor.putString("version", version);
                    editor.commit();
//                setvaluesql();
                    show();
//                int spinval=spin1.getDropDownWidth();
                    int spinval = spin1.getAdapter().getCount();
//                    Toast.makeText(this, "f2 " + spinval, Toast.LENGTH_SHORT).show();
                    show();
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
//                    checkversion();

                            progress.setVisibility(View.GONE);

                            layview.setVisibility(View.VISIBLE);


                        }

                    }, 5000);
                    complete = "yes";

                }
            }
        }
        else
        {
//            sendAndRequestResponse();
//            Toast.makeText(this, "data set in prefrence", Toast.LENGTH_SHORT).show();
//            sp.edit().putString("version", version).commit();
            editor.putString("version", version);
            editor.commit();
            sendAndRequestResponse();
//            setvaluesql();

            show();
//            int spinval=spin1.getDropDownWidth();
            int spinval=  spin1.getAdapter().getCount();
//            Toast.makeText(this, "f3 "+spinval, Toast.LENGTH_SHORT).show();
            show();
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
//                    checkversion();
                    progress.setVisibility(View.GONE);

                    layview.setVisibility(View.VISIBLE);

                }

            }, 5000);
            complete="yes";
            show();
        }

    }

    private void getjsonval() {
        mRequestQueue = Volley.newRequestQueue(this);
//        Toast.makeText(this, "please wait a while: "+ur, Toast.LENGTH_SHORT).show();
        editor.putString("version",version);
        editor.commit();
        mStringRequest = new StringRequest(Request.Method.GET, ur,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(CurrentPrice.this, "waiting", Toast.LENGTH_SHORT).show();
                        //to get particular data from coloumn of server
                        try {
                            JSONObject obj=new JSONObject(response);
//                            JSONArray obj1=obj.getJSONArray("users");
                            JSONArray obj1=obj.getJSONArray("cropPrice");
                            for(int i=0; i<obj1.length();i++){
                                JSONObject jsonObject=obj1.getJSONObject(i);

//                                int id =jsonObject.getInt("id");
                                state =jsonObject.getString("state");
                                district=jsonObject.getString("district");
                                market=jsonObject.getString("market");
                                commodity=jsonObject.getString("commodity");
                                arrival_date=jsonObject.getString("arrival_date");
                                min_price=jsonObject.getString("min_price");
                                max_price=jsonObject.getString("max_price");
                                modal_price =jsonObject.getString("modal_price");

                                String done=helper.updateName(state,district,market,commodity,arrival_date,min_price,max_price,modal_price);
//                                Toast.makeText(CurrentPrice.this, ""+done, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(CurrentPrice.this, "wrong ", Toast.LENGTH_SHORT).show();
                        }


                        //to get whole data from server
//                        Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("dd","Error :" + error.toString());
                    }
                });
        mRequestQueue.add(mStringRequest);
        show();
    }

    public void getprice()
    {
        if(markt==null){
            Toast.makeText(this, "Please Select the value", Toast.LENGTH_SHORT).show();
        }
        if(comodity==null){
            Toast.makeText(this, "Please Select the value", Toast.LENGTH_SHORT).show();
        }
        else {
            ArrayList<String> data = helper.getprice(markt, comodity);
            textlay.setVisibility(View.VISIBLE);
            setpri.setText(comodity+" Price in Rs./Quintal ");
            mintxt.setText(data.get(0)+"/- ₹");
            maxtext.setText(data.get(1)+"/- ₹");
            avgtext.setText(data.get(2)+"/- ₹");
        }
//        Toast.makeText(this, "min price is: "+data.get(0), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "max price is: "+data.get(1), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "model price is: "+data.get(2), Toast.LENGTH_SHORT).show();

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
