package com.agri.enquete.Livemandi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.agri.enquete.LocaleHelper;
import com.agri.enquete.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class YourpostMandi extends Fragment {

    DatabaseReference firebaseDatabase;
    View v;
    ListView showYoupost;
    List<MandiCrops> mandiCrops;
    Button post;
    ArrayList<String> postkey;
    LinearLayout progressBar;
    Handler mHandler;
    Activity curActivity;
    TextView data,refreshtext,addposttext;
    FloatingActionButton fab,refresh,add;
    Animation fab_open,fab_close,rotate_forward,rotate_backward,textopen;

    boolean isFABOpen=false;

    public YourpostMandi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_yourpost_mandi, container, false);
        mHandler = new Handler();
        fab = v.findViewById(R.id.option);
        data=v.findViewById(R.id.data);
        refresh=v.findViewById(R.id.reload);
        add=v.findViewById(R.id.addpost);
        postkey=new ArrayList<String>();
        String  t= LocaleHelper.getLanguage(getActivity());
        Context context = LocaleHelper.setLocale(getActivity(), t);
        Resources resources= context.getResources();

        refreshtext=v.findViewById(R.id.refreshtext);
        addposttext=v.findViewById(R.id.addposttext);
        progressBar=v.findViewById(R.id.progresslay);
        showYoupost=v.findViewById(R.id.yourPostlist);
        mandiCrops = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        curActivity=getActivity();
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        textopen=AnimationUtils.loadAnimation(getActivity(),R.anim.txtopen);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backward);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnew();

            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshlist();

            }
        });

        showYoupost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openpost(position);
            }
        });

        refreshtext.setText(resources.getString(R.string.refresh));
        addposttext.setText(resources.getString(R.string.addnewpost));
        data.setText(resources.getString(R.string.nodatafound));

        firebaseDatabase= FirebaseDatabase.getInstance().getReference("LiveMandi")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                // Click action
                openfab();

//                Intent intent = new Intent(getActivity(), addyourPost.class);
//                startActivity(intent);

            }
        });
        return v;
    }

    private void openpost(int position) {
        String key=postkey.get(position);
//        Toast.makeText(getActivity(), ""+key, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(),ViewPost.class);
        intent.putExtra("keyval",key);
        startActivity(intent);
    }

    private void addnew() {
        isFABOpen=false;
        fab.startAnimation(rotate_backward);
        add.startAnimation(fab_close);
        refresh.startAnimation(fab_close);
        add.setClickable(false);
        refresh.setClickable(false);
        refreshtext.startAnimation(fab_close);
        addposttext.startAnimation(fab_close);
        Intent intent = new Intent(getActivity(), addyourPost.class);
        startActivity(intent);
    }
    private void refreshlist() {
        isFABOpen=false;
        progressBar.setVisibility(View.VISIBLE);
        showYoupost.setVisibility(View.GONE);
        fab.startAnimation(rotate_backward);
        add.startAnimation(fab_close);
        refresh.startAnimation(fab_close);
        add.setClickable(false);
        refresh.setClickable(false);
        refreshtext.startAnimation(fab_close);
        addposttext.startAnimation(fab_close);
        getval();
        Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();
    }
    private void openfab() {
        if(!isFABOpen){
            isFABOpen=true;
            fab.startAnimation(rotate_forward);
            add.startAnimation(fab_open);
            refresh.startAnimation(fab_open);
            refresh.setClickable(true);
            add.setClickable(true);

            refreshtext.startAnimation(textopen);
            addposttext.startAnimation(textopen);

        }else{
            isFABOpen=false;
            fab.startAnimation(rotate_backward);
            add.startAnimation(fab_close);
            refresh.startAnimation(fab_close);
            add.setClickable(false);
            refresh.setClickable(false);
            refreshtext.startAnimation(fab_close);
            addposttext.startAnimation(fab_close);


        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getval();

    }

    private void getval() {
        firebaseDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mandiCrops.clear();
                postkey.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //getting artist
                    postkey.add(postSnapshot.getKey());

                    MandiCrops artist = postSnapshot.getValue(MandiCrops.class);
                    //adding artist to the list
                    mandiCrops.add(artist);
//                            Toast.makeText(getContext(), "" + artist, Toast.LENGTH_SHORT).show();


                }

                setList(mandiCrops);

//                MandiList mandiAdapter = new MandiList(getActivity(), mandiCrops);
//                //attaching adapter to the listview
//                showYoupost.setAdapter(mandiAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setList(List<MandiCrops> mandiCrops) {

        if(mandiCrops.size()<=0){
            progressBar.setVisibility(View.GONE);
            data.setVisibility(View.VISIBLE);
            showYoupost.setVisibility(View.GONE);

        }
        else {
            final List newList = new ArrayList(new LinkedHashSet(mandiCrops));
            Set<MandiCrops> s = new LinkedHashSet<MandiCrops>(mandiCrops);
            MandiList mandiAdapter = new MandiList(curActivity, mandiCrops);
            //attaching adapter to the listview
            showYoupost.setAdapter(mandiAdapter);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    data.setVisibility(View.GONE);
                    showYoupost.setVisibility(View.VISIBLE);

                }

            }, 5000);
        }

    }
}

