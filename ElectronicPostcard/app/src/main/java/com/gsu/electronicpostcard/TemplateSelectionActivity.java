package com.gsu.electronicpostcard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gsu.fragments.TemplateSelectionFragment;

public class TemplateSelectionActivity extends AppCompatActivity {


    public class TemplateSelectionViewPagerAdapter extends FragmentPagerAdapter {


        public TemplateSelectionViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return (Fragment)TemplateSelectionFragment.newInstance(position);
        }


        @Override
        public int getCount() {
            return 4;
        }


    }


    public ViewPager selectTempalteViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_selection);

        setTitle("Select a template");

        selectTempalteViewpager = (ViewPager)findViewById(R.id.select_template_viewpager);
        TemplateSelectionViewPagerAdapter tsAdapter = new TemplateSelectionViewPagerAdapter(getSupportFragmentManager());
        selectTempalteViewpager.setAdapter(tsAdapter);

    }




}
