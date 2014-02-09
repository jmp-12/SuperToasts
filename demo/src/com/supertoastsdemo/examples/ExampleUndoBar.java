package com.supertoastsdemo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.Style;
import com.github.johnpersano.supertoasts.util.Wrappers;
import com.supertoastsdemo.R;


/** This class showcases how to use a SuperActivityToast to mimic the undo bar found in the Gmail app. */
@SuppressWarnings("UnusedDeclaration")
public class ExampleUndoBar extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

        /**
         * Create a wrappers object and add the onclickwrapper to it in order to
         * preserve the on click event on orientation changes.
         */
        final Wrappers wrappers = new Wrappers();
        wrappers.add(onClickWrapper);

        /**
         *  This is used to recreate any showing or pending SuperActivityToasts.
         *
         *  1st parameter: Use bundle from onCreate(). No need for a null check
         *  2nd parameter: The current Activity
         *  3rd parameter: Wrappers of any previously set OnClickWrappers/OnDismissWrappers
         */
        SuperActivityToast.onRestoreState(savedInstanceState, ExampleUndoBar.this, wrappers);

        /** This button will simulate our potentially destructive action */
        final Button showButton = (Button)
                findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 *  Show a BUTTON type SuperActivityToast with an undo icon.
                 *
                 *  1st parameter: The current activity
                 *  2nd parameter: The type of SuperActivityToast to use, in this case one with a button
                 *  3rd parameter: Style (this parameter is not necessary but helps with themes)
                 */
                final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleUndoBar.this,
                        SuperToast.Type.BUTTON, Style.getStyle(Style.STANDARD, SuperToast.Animations.SCALE));
                superActivityToast.setDuration(SuperToast.Duration.EXTRA_LONG);
                superActivityToast.setText("Some action performed.");
                superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");
                superActivityToast.setOnClickWrapper(onClickWrapper);
                superActivityToast.show();

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
     * Instantiates a new OnClickWrapper, this is where you should put your undo action.
     * Think of an OnClickWrapper as an OnClickListener with a tag.
     * The tag is used to reattach the listener on orientation changes.
     *
     * 1st parameter: A tag unique to this listener
     * 2nd parameter: A new OnClickListener
     */
    OnClickWrapper onClickWrapper = new OnClickWrapper("undo_superactivitytoast", new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            /** Show a SuperActivityToast to simulate an undo action */
            SuperActivityToast.createSuperActivityToast(ExampleUndoBar.this, "Undo action performed!",
                    SuperToast.Duration.SHORT, Style.getStyle(Style.GREEN)).show();

        }

    });

}
