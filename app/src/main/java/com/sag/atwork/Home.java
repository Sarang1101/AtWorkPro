package com.sag.atwork;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_fragment,new com.sag.atwork.fragments.home_fragment());
        ft.commit();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
Fragment fragment=null;
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            //fragment= new com.sag.atwork.fragments.profile_fragment();
            startActivity(new Intent(getApplicationContext(),Profile.class));

        } else if (id == R.id.nav_worker) {
            fragment= new com.sag.atwork.fragments.worker_fragment();
        } else if (id == R.id.nav_notification) {
            fragment= new com.sag.atwork.fragments.notify_fragment();
        } else if (id == R.id.nav_settings) {
            fragment= new com.sag.atwork.fragments.settings_fragment();
        }else if (id == R.id.nav_home){
            fragment= new com.sag.atwork.fragments.home_fragment();
        }
        if(fragment!=null){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_fragment,fragment);
        ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
