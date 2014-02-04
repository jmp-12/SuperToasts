package com.supertoastsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;

/** Use this class to play around with the library. Simply run this activity
 *  in a specified run configuration */
public class Playground extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

        SuperActivityToast.onRestoreState(savedInstanceState, this);

        final Button showButton = (Button)
                findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SuperActivityToast.createSuperActivityToast(Playground.this, "Time to play!",
                        SuperToast.Duration.SHORT).show();

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /** This method is used to save any showing or pending SuperActivityToasts */
        SuperActivityToast.onSaveState(outState);

    }

}
