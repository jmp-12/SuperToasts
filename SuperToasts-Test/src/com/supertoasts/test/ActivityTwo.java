package com.supertoasts.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class ActivityTwo extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("Activity two");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(48);

        setContentView(textView);

    }

}