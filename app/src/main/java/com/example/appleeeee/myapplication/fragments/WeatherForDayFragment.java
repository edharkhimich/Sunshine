package com.example.appleeeee.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appleeeee.myapplication.R;
import com.example.appleeeee.myapplication.adapter.ForecastAdapter;
import com.example.appleeeee.myapplication.model.Forecast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherForDayFragment extends Fragment {

    @BindView(R.id.tv_high_temp_info_day)
    TextView tvHighTemp;
    @BindView(R.id.tv_low_temp_info_day)
    TextView tvLowTemp;

    private String tempHigh;
    private String tempLow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weather_for_day, container, false);

        Bundle bundle = getArguments();
        tempHigh = String.valueOf(bundle.getString(ForecastAdapter.HIGH_TEMP_KEY));
        tempLow = String.valueOf(bundle.getString(ForecastAdapter.LOW_TEMP_KEY));

        ButterKnife.bind(this, v);

        tvHighTemp.setText(tempHigh);
        tvLowTemp.setText(tempLow);

        return v;
    }
}
