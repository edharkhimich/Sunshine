package com.example.appleeeee.myapplication.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.appleeeee.myapplication.R;
import com.example.appleeeee.myapplication.activity.MainActivity;
import com.github.machinarius.preferencefragment.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref1);

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}

