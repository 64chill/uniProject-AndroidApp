package com.example.unip_simplerssfeeder_app;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unip_simplerssfeeder_app.fragments.EditNewsFragment;
import com.example.unip_simplerssfeeder_app.fragments.MainFragment;
import com.example.unip_simplerssfeeder_app.fragments.UniqueNewsFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainFragment mp = new MainFragment();
                    FragmentManager m1 = getSupportFragmentManager();
                    m1.beginTransaction().replace(R.id.mainContentLayout, mp, mp.getTag()).commit();
                    return true;

                // -----------------------------------------------------------------------------------------------
                case R.id.navigation_show_unique_news:
                    UniqueNewsFragment unf = new UniqueNewsFragment();
                    FragmentManager fmUnique = getSupportFragmentManager();
                    fmUnique.beginTransaction().replace(R.id.mainContentLayout, unf, unf.getTag()).commit();
                    return true;

                // -----------------------------------------------------------------------------------------------
                case R.id.navigation_edit_news:
                    EditNewsFragment enf = new EditNewsFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.mainContentLayout, enf, enf.getTag()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mp = new MainFragment();
        FragmentManager m1 = getSupportFragmentManager();
        m1.beginTransaction().replace(R.id.mainContentLayout, mp, mp.getTag()).commit();

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
