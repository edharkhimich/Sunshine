package com.example.appleeeee.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appleeeee.myapplication.activity.MainActivity;
import com.example.appleeeee.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CityFragment extends Fragment {

//    @BindView(R.id.city_edit_text)
//    EditText cityEditText;

    private String userCity;
    private Unbinder unbinder;
    public static final String KEY = "key";


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_city, container, false);
        unbinder = ButterKnife.bind(this, v);
//        cityEditText.requestFocus();
        return v;
    }

    @Override
    public void onResume() {
//        cityEditText.setText(null);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
//
//    @OnClick(R.id.submit_btn)
//    public void submitClick(View view) {
//        userCity = cityEditText.getText().toString();
//
//        if (MainActivity.isConn(getActivity())) {
//            if (!userCity.isEmpty()) {
//                Bundle bundle = new Bundle();
//                bundle.putString(KEY, userCity);
//                MainFragment mainFragment = new MainFragment();
//                mainFragment.setArguments(bundle);
//                ((MainActivity) getActivity()).changeFragment(mainFragment, true);
//
//            } else {
//                Toast.makeText(getActivity(), getString(R.string.enter_city), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
