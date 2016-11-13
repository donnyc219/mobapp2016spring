package com.gsu.electronicpostcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gsu.fragments.TemplateSelectionFragment;

import java.util.ArrayList;

public class TemplateSelectionActivity extends AppCompatActivity {


    public class TemplateSelectionViewPagerAdapter extends FragmentPagerAdapter {


        private ArrayList<String> datasource;
        public TemplateSelectionViewPagerAdapter(FragmentManager fm, ArrayList<String> ds) {
            super(fm);

            datasource = ds;

        }


        @Override
        public Fragment getItem(int position) {
            return (Fragment)TemplateSelectionFragment.newInstance(datasource.get(position));
        }


        @Override
        public int getCount() {
            return datasource.size();
        }


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.template_selection_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected (MenuItem item){

        switch (item.getItemId()) {


            case R.id.ok_template:
            {
                // when user click ok (top right button)
                Intent i = new Intent(this, ViewCardActivity.class);
                startActivity(i);
            }
            break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public ViewPager selectTempalteViewpager;
    public ArrayList<String> templateFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_selection);

        setTitle("Select a template");

        // this is where i stored the image names
        initTemplateFilenameArray();

        // set the view pager
        selectTempalteViewpager = (ViewPager)findViewById(R.id.select_template_viewpager);
        TemplateSelectionViewPagerAdapter tsAdapter = new TemplateSelectionViewPagerAdapter(getSupportFragmentManager(), templateFilename);
        selectTempalteViewpager.setAdapter(tsAdapter);

    }

    // list of template
    public void initTemplateFilenameArray(){
        templateFilename = new ArrayList<String>();

        // these are the file name without the extension

        templateFilename.add("flag8385");
        templateFilename.add("love");
        templateFilename.add("thanks1");
        templateFilename.add("balloon1");
        templateFilename.add("cake1");
        templateFilename.add("flowers");
        templateFilename.add("flowers1");
        templateFilename.add("land1");
        templateFilename.add("land2");
        templateFilename.add("land3");
        templateFilename.add("land4");
    }




}
