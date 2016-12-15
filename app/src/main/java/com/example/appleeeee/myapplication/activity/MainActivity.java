package com.example.appleeeee.myapplication.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.appleeeee.myapplication.R;
import com.example.appleeeee.myapplication.fragments.CityFragment;
import com.example.appleeeee.myapplication.model.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = new ProgressBar(MainActivity.this);
        progressBar.setCancelable(false);
        toolbar = new Toolbar(getApplicationContext());
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            changeFragment(new CityFragment(), false);
        }
    }

    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    public static boolean isConn(Context c) {
        ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    public void showProgress() {
        progressBar.show();
    }

    public void dismissDialog() {
        if (progressBar != null) {
            progressBar.dismiss();
        }
    }

    public Toolbar getToolbar(){
        return toolbar;
    }
}

