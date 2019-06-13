package com.example.unip_simplerssfeeder_app;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.example.unip_simplerssfeeder_app.fragments.EditNewsFragment;
import com.example.unip_simplerssfeeder_app.fragments.MainFragment;
import com.example.unip_simplerssfeeder_app.fragments.UniqueNewsFragment;

public class MainActivity extends AppCompatActivity {
    Fragment mContent;

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
    // _____________________________ onSaveInstanceState ___________________________________________
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save fragment instance
        getSupportFragmentManager().putFragment(outState, "myFragmentName", this.mContent);
    }

    // _____________________________ onCreate ______________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            this.mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        } else {
            set_MainFragment();
        } // end if restore;


        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /* *********************************************************************************************
    Setters
    ********************************************************************************************* */

    /*_________________ set_MainFragment _________________________________________________________*/
    public void set_MainFragment(){
        this.mContent = new MainFragment();
        FragmentManager m1 = getSupportFragmentManager();
        m1.beginTransaction().replace(R.id.mainContentLayout, this.mContent, this.mContent.getTag()).commit();
    }

    /*_________________ set_UniqueNewsFragment ___________________________________________________*/
    public void set_UniqueNewsFragment(){
        this.mContent = new UniqueNewsFragment();
        FragmentManager fmUnique = getSupportFragmentManager();
        fmUnique.beginTransaction().replace(R.id.mainContentLayout, this.mContent, this.mContent.getTag()).commit();
    }

    /*_________________ set_EditNewsFragment _____________________________________________________*/
    public void set_EditNewsFragment(){
        this.mContent = new EditNewsFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainContentLayout, this.mContent, this.mContent.getTag()).commit();
    }

}
