package com.supertoastsdemo.examples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.*;
import com.supertoastsdemo.R;

/** This class showcases some different uses for the SuperActivityToast */
@SuppressWarnings("UnusedDeclaration")
public class ExampleSuperActivityToast extends Activity {

    private Style mStyle;

    private int mCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

        /**
         * Create a wrappers object with any OnClickWrappers/OnDismissWrappers to be
         * reattached on orientation change
         */
        Wrappers wrappers = new Wrappers();
        wrappers.add(onClickWrapper);
        wrappers.add(onClickWrapperTwo);
        wrappers.add(onDismissWrapper);

        /**
         *  This is used to recreate any showing or pending SuperActivityToasts.
         *
         *  1st parameter: Use bundle from onCreate(). No need for a null check
         *  2nd parameter: The current Activity
         *  3rd parameter: Wrappers of any previously set OnClickWrappers/OnDismissWrappers
         */
        SuperActivityToast.onRestoreState(savedInstanceState, this, wrappers);

        /**
         *  Set up a default style to be used by all SuperActivityToasts in this activity. This is not
         *  necessary, it is used to make your life easier if you have a lot of SuperActivityToasts to theme.
         */
        mStyle = new Style();
        mStyle.animations = SuperToast.Animations.FLYIN;
        mStyle.background = SuperToast.Background.TRANSLUCENT_PURPLE;
        mStyle.textColor = Color.WHITE;
        mStyle.buttonTextColor = Color.LTGRAY;
        mStyle.dividerColor = Color.WHITE;

        final Button showButton = (Button)
                findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 *  Show a BUTTON type SuperActivityToast.
                 *
                 *  1st parameter: The current activity
                 *  2nd parameter: The type of SuperActivityToast to use, in this case one with a button
                 *  3rd parameter: Previously defined default style
                 */
                final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, SuperToast.Type.BUTTON, mStyle);
                superActivityToast.setText("Hello!");
                superActivityToast.setDuration(SuperToast.Duration.LONG);
                superActivityToast.setButtonText("CLICK");
                superActivityToast.setButtonIcon(SuperToast.Icon.Dark.INFO);
                superActivityToast.setOnDismissWrapper(onDismissWrapper);

                /**
                 * We are using two different OnClickWrappers based on the amount of
                 * times the user has clicked the button for demonstration. Rotate the device
                 * for each condition to see the app recreate the SuperActivityToast and reassign the
                 * appropriate OnClickWrapper.
                 */
                if(mCount >= 1) {

                    superActivityToast.setOnClickWrapper(onClickWrapperTwo);

                } else  {

                    superActivityToast.setOnClickWrapper(onClickWrapper);

                }

                superActivityToast.show();

                mCount++;

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /** This method is used to save any showing or pending SuperActivityToasts */
        SuperActivityToast.onSaveState(outState);

    }

    /**
     * Instantiates a new OnClickWrapper. Think of an OnClickWrapper as an OnClickListener with a
     * tag. The tag is used to reattach the listener on orientation changes.
     *
     * 1st parameter: A tag unique to this listener
     * 2nd parameter: A new OnClickListener
     */
    OnClickWrapper onClickWrapper = new OnClickWrapper("onclick_1", new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            /**
             *  Show a SuperActivityToast.
             *
             *  1st parameter: The current activity
             *  2nd parameter: Previously defined default style
             */
            final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, mStyle);
            superActivityToast.setText("On click wrapper one!");
            superActivityToast.setDuration(SuperToast.Duration.SHORT);
            superActivityToast.show();

        }
    });

    /**
     * Instantiates a new OnClickWrapper. Think of an OnClickWrapper as an OnClickListener with a
     * tag. The tag is used to reattach the listener on orientation changes.
     *
     * 1st parameter: A tag unique to this listener
     * 2nd parameter: A new OnClickListener
     */
    OnClickWrapper onClickWrapperTwo = new OnClickWrapper("onclick_2", new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            /**
             *  Show a SuperActivityToast.
             *
             *  1st parameter: The current activity
             *  2nd parameter: Previously defined default style
             */
            SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, mStyle);
            superActivityToast.setText("On click wrapper two!");
            superActivityToast.setDuration(SuperToast.Duration.SHORT);
            superActivityToast.show();

        }
    });

    /**
     * Instantiates a new OnDismissWrapper. Think of an OnDismissWrapper as an OnDismissListener with a
     * tag. The tag is used to reattach the listener on orientation changes.
     *
     * 1st parameter: A tag unique to this listener
     * 2nd parameter: A new OnDismissListener
     */
    OnDismissWrapper onDismissWrapper = new OnDismissWrapper("dismiss_1", new OnDismissListener() {

        @Override
        public void onDismiss(View view) {

            Log.d(ExampleSuperActivityToast.this.getClass().toString(), "On Dismiss");

        }
    });

}

