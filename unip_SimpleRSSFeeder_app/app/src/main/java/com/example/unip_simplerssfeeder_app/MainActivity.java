package com.example.unip_simplerssfeeder_app;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.unip_simplerssfeeder_app.fragments.EditNewsFragment;
import com.example.unip_simplerssfeeder_app.fragments.MainFragment;
import com.example.unip_simplerssfeeder_app.fragments.UniqueNewsFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    // _____________________________ mOnNavigationItemSelectedListener _____________________________
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    set_MainFragment();
                    return true;
                // -----------------------------------------------------------------------------------------------
                case R.id.navigation_show_unique_news:
                    set_UniqueNewsFragment();
                    return true;
                // -----------------------------------------------------------------------------------------------
                case R.id.navigation_edit_news:
                    set_EditNewsFragment();
                    return true;
            }
            return false;
        }
    };

    // _____________________________ onCreate ______________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MainFragment mp = new MainFragment();
        FragmentManager m1 = getSupportFragmentManager();
        m1.beginTransaction().replace(R.id.mainContentLayout, mp, mp.getTag()).commit();

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /*_________________ Check which fragment to show _________________________________________*/
        //TODO check why it is not working!?---------------------------------------------------------
        if(navView.getMenu().findItem(R.id.navigation_home).isChecked())                {set_MainFragment(); Log.e("CHANGE" , "navHome");}
        if(navView.getMenu().findItem(R.id.navigation_show_unique_news).isChecked())    {set_UniqueNewsFragment(); Log.e("CHANGE" , "navUnique");}
        if(navView.getMenu().findItem(R.id.navigation_edit_news).isChecked())           {set_EditNewsFragment(); Log.e("CHANGE" , "navEdit");}

    }

    /* *********************************************************************************************
    Setters
    ********************************************************************************************* */

    /*_________________ set_MainFragment _________________________________________________________*/
    public void set_MainFragment(){
        MainFragment mp = new MainFragment();
        FragmentManager m1 = getSupportFragmentManager();
        m1.beginTransaction().replace(R.id.mainContentLayout, mp, mp.getTag()).commit();
    }

    /*_________________ set_UniqueNewsFragment ___________________________________________________*/
    public void set_UniqueNewsFragment(){
        UniqueNewsFragment unf = new UniqueNewsFragment();
        FragmentManager fmUnique = getSupportFragmentManager();
        fmUnique.beginTransaction().replace(R.id.mainContentLayout, unf, unf.getTag()).commit();
    }

    /*_________________ set_EditNewsFragment _____________________________________________________*/
    public void set_EditNewsFragment(){
        EditNewsFragment enf = new EditNewsFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainContentLayout, enf, enf.getTag()).commit();
    }

}
