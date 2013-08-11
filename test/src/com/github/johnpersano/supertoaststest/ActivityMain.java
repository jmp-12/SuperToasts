package com.github.johnpersano.supertoaststest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoaststest.R;

/** This class simulates an error of the library. If a new
 *  Activity is launched while a SuperActivityToast is showing
 *  the SuperActivityToast will not be destroyed until the user
 *  navigates back to the first Activity **/
public class ActivityMain extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Show a SuperActivityToast. Once pressed quickly press the Intent Button. **/
        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SuperActivityToast.createDarkSuperActivityToast(ActivityMain.this,
                        "Hello there!", 3000).show();

            }
        });

        /** Navigate to ActivityTwo. Press this while the SuperActivityToast is showing **/
        findViewById(R.id.intent_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /** Uncomment this to fix issue. Call this before any startActivity()
                 * method or just call it once from onPause(). **/
                // SuperActivityToast.cancelAllSuperActivityToasts();

                startActivity(new Intent(ActivityMain.this, ActivityTwo.class));

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        /** Uncomment this to fix issue. **/
        // SuperActivityToast.cancelAllSuperActivityToasts();

    }
    
}