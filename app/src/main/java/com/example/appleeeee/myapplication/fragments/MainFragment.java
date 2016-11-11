package com.example.appleeeee.myapplication.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appleeeee.myapplication.activity.MainActivity;
import com.example.appleeeee.myapplication.R;
import com.example.appleeeee.myapplication.adapter.ForecastAdapter;
import com.example.appleeeee.myapplication.api.Api;
import com.example.appleeeee.myapplication.model.Forecast;
import com.example.appleeeee.myapplication.model.Model;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    @BindView(R.id.location_text_view)
    TextView locationTv;
    @BindView(R.id.condition_text_view)
    TextView conditionTv;
    @BindView(R.id.temperature_text_view)
    TextView temperatureTv;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.collapsing_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private String conditionText;
    private String city;
    private String temperatureF;
    private int temperature;
    private String lastBuildDate;
    private String cityBundle;
    private String query;
    private List<Forecast> list;
    private ForecastAdapter adapter;
    View rootView;
    private static Bitmap bitmap;
    private static File file;
    private static final String TYPE = "image/*";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static boolean isCelsiuos;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).showProgress();
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        JodaTimeAndroid.init(getActivity());
        ButterKnife.bind(this, v);

        rootView = getActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content);

        Bundle bundle = this.getArguments();
        cityBundle = bundle.getString(CityFragment.KEY);
        query = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + cityBundle + "\")";

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent)); // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF")); //Color of your title
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        AppBarLayout appBarLayout = (AppBarLayout) v.findViewById(R.id.weather_holder);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    toolbar.setTitle("Title");

                    setHasOptionsMenu(true);
                } else if (isShow) {
                    isShow = false;
                    setHasOptionsMenu(false);
                }
            }
        });

        makingRequest();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String temp = prefs.getString("list1", "default");

        isCelsiuos = temp.equals("1");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ortions, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
            case R.id.refresh:
                makingRequest();
                break;
            case R.id.settings:
                ((MainActivity) getActivity()).changeFragment(new SettingsFragment(), true);
                break;
            case R.id.share:
                getScreenShot(rootView);
                store(bitmap, "weather screenshoot");
                shareImage(file);

        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(TYPE);

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }



//    private void formatTime() {
//        DateTime dt = new DateTime();
//        DateTimeFormatter formatter = DateTimeFormat
//                .forPattern("EEE, dd MMM yyyy HH:mm a z");
//        if (lastBuildDate.contains("EEST")) {
//            dt = formatter.parseDateTime(lastBuildDate.replace("EEST", "EST"));
//        } else if (lastBuildDate.contains("BST")) {
//            dt = formatter.parseDateTime(lastBuildDate.replace("BST", "GMT"));
//        } else if (lastBuildDate.contains("CEST")) {
//            dt = formatter.parseDateTime(lastBuildDate.replace("CEST", "PDT"));
//        }
//        dt = formatter.parseDateTime(lastBuildDate);
//        }
//        DateTimeFormatter formatter2 = DateTimeFormat
//                .forPattern("HH:mm");
//
//        lastBuildDate = formatter2.print(dt);
//    }

    private void setAdapter() {
        adapter = new ForecastAdapter(getActivity());
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void makingRequest() {
        Api.getInstance().getApiInterface().getWeather(query, "json")
                .enqueue(new Callback<Model>() {
                             @Override
                             public void onResponse(Call<Model> call, Response<Model> response) {
                                 if (response.code() == 200) {
                                     if (response.body().getQuery().getResults() != null) {
                                         city = response.body().getQuery().getResults().getChannel().getLocation().getCity();
                                         temperatureF = response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                                         conditionText = response.body().getQuery().getResults().getChannel().getItem().getCondition().getText();
                                         lastBuildDate = response.body().getQuery().getResults().getChannel().getLastBuildDate();
                                         list = response.body().getQuery().getResults().getChannel().getItem().getForecast();
//                                         formatTime();
                                         if (isCelsiuos) {
                                             temperature = (int) ((Integer.valueOf(temperatureF) - 32) / 1.8000);
                                             temperatureTv.setText((temperature) + " \u00B0C");
                                         } else {
                                             temperature = (Integer.valueOf(temperatureF));
                                             temperatureTv.setText((temperature) + " \u00B0F");

                                         }

                                         if (city != null) {
                                             locationTv.setText(city);
                                         } else {
                                             locationTv.setText(R.string.unknown_location);
                                         }

                                         if (conditionText != null) {
                                             if (conditionText.equalsIgnoreCase("Scattered Showers")) {
                                                 weatherIcon.setImageResource(R.drawable.scattered_showers);
                                             } else if (conditionText.equalsIgnoreCase("Showers")
                                                     || conditionText.equalsIgnoreCase("Rain")
                                                     || conditionText.equalsIgnoreCase("Mixed Rain And Hail")) {
                                                 weatherIcon.setImageResource(R.drawable.rain);
                                             } else if (conditionText.equalsIgnoreCase("Tornado")
                                                     || conditionText.equalsIgnoreCase("Tropical Storm")
                                                     || conditionText.equalsIgnoreCase("Hurricane")
                                                     || conditionText.equalsIgnoreCase("Severe Thunderstorms")
                                                     || conditionText.equalsIgnoreCase("Thunderstorms")) {
                                                 weatherIcon.setImageResource(R.drawable.tornado);
                                             } else if (conditionText.equalsIgnoreCase("Isolated Thunderstorms")
                                                     || conditionText.equalsIgnoreCase("Scattered Thunderstorms")
                                                     || conditionText.equalsIgnoreCase("Scattered Showers")) {
                                                 weatherIcon.setImageResource(R.drawable.isolated_thunderstorms);
                                             } else if (conditionText.equalsIgnoreCase("Partly Cloudy")) {
                                                 weatherIcon.setImageResource(R.drawable.partly_cloudy);
                                             } else if (conditionText.equalsIgnoreCase("Mostly Cloudy")
                                                     || conditionText.equalsIgnoreCase("Cloudy")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy);
                                             } else if (conditionText.equalsIgnoreCase("Mostly Cloudy (Day)")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy_day);
                                             } else if (conditionText.equalsIgnoreCase("Mostly Cloudy (Night)")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy_night);
                                             } else if (conditionText.equalsIgnoreCase("Partly Cloudy (Night)")) {
                                                 weatherIcon.setImageResource(R.drawable.partly_cloudy_night);
                                             } else if (conditionText.equalsIgnoreCase("Clear (Night)")
                                                     || conditionText.equalsIgnoreCase("Fair (Night)")) {
                                                 weatherIcon.setImageResource(R.drawable.clear_night);
                                             } else if (conditionText.equalsIgnoreCase("Sunny")
                                                     || conditionText.equalsIgnoreCase("Fair (Day)")
                                                     || conditionText.equalsIgnoreCase("Hot")
                                                     || conditionText.equalsIgnoreCase("Clear")) {
                                                 weatherIcon.setImageResource(R.drawable.sunny);
                                             } else if (conditionText.equalsIgnoreCase("Breezy")
                                                     || conditionText.equalsIgnoreCase("Partly Cloudy (Day)")) {
                                                 weatherIcon.setImageResource(R.drawable.breeze);
                                             } else if (conditionText.equalsIgnoreCase("Mixed Rain And Snow")
                                                     || conditionText.equalsIgnoreCase("Mixed Rain And Sleet")
                                                     || conditionText.equalsIgnoreCase("Mixed Snow And Sleet")) {
                                                 weatherIcon.setImageResource(R.drawable.mixed_rain_and_snow);
                                             } else if (conditionText.equalsIgnoreCase("Freezing Drizzle")) {
                                                 weatherIcon.setImageResource(R.drawable.freezy);
                                             } else if (conditionText.equalsIgnoreCase("Drizzle")
                                                     || conditionText.equalsIgnoreCase("Freezing Rain")) {
                                                 weatherIcon.setImageResource(R.drawable.drizzle);
                                             } else if (conditionText.equalsIgnoreCase("Foggy")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy);
                                             } else if (conditionText.equalsIgnoreCase("Hail")) {
                                                 weatherIcon.setImageResource(R.drawable.hail);
                                             } else if (conditionText.equalsIgnoreCase("Dust")
                                                     || conditionText.equalsIgnoreCase("Foggy")
                                                     || conditionText.equalsIgnoreCase("Haze")
                                                     || conditionText.equalsIgnoreCase("Smoky")) {
                                                 weatherIcon.setImageResource(R.drawable.dust);
                                             } else if (conditionText.equalsIgnoreCase("Scattered Snow Showers")) {
                                                 weatherIcon.setImageResource(R.drawable.scattered_snow_showers);
                                             } else if (conditionText.equalsIgnoreCase("Blustery")
                                                     || conditionText.equalsIgnoreCase("Windy")
                                                     || conditionText.equalsIgnoreCase("Cold")) {
                                                 weatherIcon.setImageResource(R.drawable.blustery);
                                             } else if (conditionText.equalsIgnoreCase("Snow")
                                                     || conditionText.equalsIgnoreCase("Snow Showers")
                                                     || conditionText.equalsIgnoreCase("Heavy Snow")
                                                     || conditionText.equalsIgnoreCase("Snow Flurries")
                                                     || conditionText.equalsIgnoreCase("Light Snow Showers")
                                                     || conditionText.equalsIgnoreCase("Blowing Snow")
                                                     || conditionText.equalsIgnoreCase("Sleet")
                                                     || conditionText.equalsIgnoreCase("Rain and Snow")) {
                                                 weatherIcon.setImageResource(R.drawable.snow);
                                             } else {
                                                 weatherIcon.setImageResource(R.drawable.na);
                                             }
                                             conditionTv.setText(conditionText);
                                         } else {
                                             conditionTv.setText(R.string.unknown_condition);
                                         }
                                         setAdapter();
                                     } else {
                                         Toast.makeText(getActivity(), R.string.please_enter_correct_city, Toast.LENGTH_LONG).show();
                                         ((MainActivity) getActivity()).changeFragment(new CityFragment(), false);
                                     }
                                 } else {
                                     Toast.makeText(getActivity(), R.string.please_check_all_details, Toast.LENGTH_LONG).show();
                                 }
                                 ((MainActivity) getActivity()).dismissDialog();
                             }

                             @Override
                             public void onFailure(Call<Model> call, Throwable t) {
                                 Toast.makeText(getActivity(), R.string.error_in_response, Toast.LENGTH_LONG).show();
                             }
                         }
                );
    }
}
