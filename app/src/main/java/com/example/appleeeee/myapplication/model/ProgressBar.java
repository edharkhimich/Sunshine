package com.example.appleeeee.myapplication.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.example.appleeeee.myapplication.R;

public class ProgressBar extends Dialog {

    public ProgressBar(Context context) {
        super(context);
    }

    {
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setCancelable(false);
        setContentView(R.layout.progress_bar);
    }
}
