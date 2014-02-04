package com.supertoastsdemo.examples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.DefaultStyle;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.OnDismissListener;
import com.github.johnpersano.supertoasts.util.OnDismissWrapper;
import com.supertoastsdemo.R;

import java.util.ArrayList;
import java.util.List;

/** This class showcases some different uses for the SuperActivityToast */
@SuppressWarnings("UnusedDeclaration")
public class ExampleSuperActivityToast extends Activity {

    private DefaultStyle mDefaultStyle;

    private int mCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

        /** Create a list containing any OnClickWrappers for SuperActivityToast recreation */
        List<OnClickWrapper> onClickWrapperList = new ArrayList<OnClickWrapper>();
        onClickWrapperList.add(onClickWrapper);
        onClickWrapperList.add(onClickWrapperTwo);

        /** Create a list containing any OnDismissWrappers for SuperActivityToast recreation */
        List<OnDismissWrapper> onDismissWrapperList = new ArrayList<OnDismissWrapper>();
        onDismissWrapperList.add(onDismissWrapper);

        /** This is used to recreate any showing or pending SuperActivityToasts.
         *
         *  1st parameter: Use bundle from onCreate(). No need for a null check
         *  2nd parameter: The current Activity
         *  3rd parameter: List of any previously set OnClickWrappers
         *  4th parameter: List of any previously set OnDismissWrappers
         */
        SuperActivityToast.onRestoreState(savedInstanceState, this, onClickWrapperList, onDismissWrapperList);

        /**
         *  Set up a default style to be used by all SuperActivityToasts in this activity. This is not
         *  necessary, it is used to make your life easier if you have a lot of SuperActivityToasts to theme.
         */
        mDefaultStyle = new DefaultStyle();
        mDefaultStyle.animations = SuperToast.Animations.FLYIN;
        mDefaultStyle.background = SuperToast.Background.TRANSLUCENT_PURPLE;
        mDefaultStyle.duration = SuperToast.Duration.LONG;
        mDefaultStyle.textColor = Color.WHITE;

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
                final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, SuperToast.Type.BUTTON, mDefaultStyle);
                superActivityToast.setText("Hello!");
                superActivityToast.setButtonText("CLICK");
                superActivityToast.setButtonIcon(SuperToast.Icon.Dark.INFO);
                superActivityToast.setOnDismissWrapper(onDismissWrapper);

                /** We are using two different OnClickWrappers based on the amount of
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
            final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, mDefaultStyle);
            superActivityToast.setText("On click wrapper one!");
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
            SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, mDefaultStyle);
            superActivityToast.setText("On click wrapper two!");
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

