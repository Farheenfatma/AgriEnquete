package com.agri.enquete.Livemandi;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.agri.enquete.LocaleHelper;
import com.agri.enquete.R;

import java.util.ArrayList;
import java.util.List;

public class LiveMandi extends AppCompatActivity {
    ViewPager viewPage;
    Resources resources;
    android.support.v7.widget.Toolbar toolbar1;
    TabLayout tabLayout1;

    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_mandi);
        toolbar1=findViewById(R.id.toolbar);
        String  t= LocaleHelper.getLanguage(this);
        mHandler = new Handler();
        Context context = LocaleHelper.setLocale(LiveMandi.this, t);
        resources= context.getResources();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(resources.getString(R.string.livetitlename));


        viewPage=findViewById(R.id.viewpager);
        setupViewPager(viewPage);

        tabLayout1=findViewById(R.id.tabLayout);
        tabLayout1.setupWithViewPager(viewPage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        {

                    finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new YourpostMandi(),resources.getString(R.string.liveyour));

        adapter.addFragment(new AllMandiPost(),resources.getString(R.string.liveall));

        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {

        private  final List<Fragment> mFragmentList=new ArrayList<>();
        private  final List<String> mFragmentTitleList=new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(android.support.v4.app.Fragment fragment,String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}
