package com.supertoastsdemo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnDismissWrapper;
import com.github.johnpersano.supertoasts.util.Style;
import com.github.johnpersano.supertoasts.util.Wrappers;
import com.supertoastsdemo.R;

/** This class showcases the correct usage for a standard SuperActivityToast */
@SuppressWarnings("UnusedDeclaration")
public class ExampleSuperActivityToast extends Activity {

    private static final String ID_ONDISMISSWRAPPER = "id_ondismisswrapper";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

        /**
         * Create a wrappers object with any OnClickWrappers/OnDismissWrappers to be
         * reattached on orientation change. If you do not do this than any OnClickWrappers/OnDismissWrappers
         * set will NOT work if the SuperActivityToast is recreated after an orientation change.
         */
        final Wrappers wrappers = new Wrappers();
        wrappers.add(onDismissWrapper);

        /**
         *  This is used to recreate any showing or pending SuperActivityToasts.
         *
         *  1st parameter: Use bundle from onCreate(). No need for a null check
         *  2nd parameter: The current Activity
         *  3rd parameter: Wrappers object of any previously set OnClickWrappers/OnDismissWrappers
         */
        SuperActivityToast.onRestoreState(savedInstanceState, this, wrappers);

        final Button showButton = (Button)
                findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 * Create a standard SuperActivityToast with an OnDismissListener.
                 *
                 * 1st parameter: The current Activity
                 * 2nd parameter: A preset red style
                 */
                final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleSuperActivityToast.this, Style.getStyle(Style.RED));
                superActivityToast.setText("Hello world!");
                superActivityToast.setDuration(SuperToast.Duration.LONG);
                superActivityToast.setOnDismissWrapper(onDismissWrapper);
                superActivityToast.show();

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* This method is used to save any showing or pending SuperActivityToasts */
        SuperActivityToast.onSaveState(outState);

    }

    /**
     * Instantiates a new OnDismissWrapper. Think of an OnDismissWrapper as an OnDismissListener with a
     * tag. The tag is used to reattach the listener on orientation changes.
     *
     * 1st parameter: A String tag unique to this listener
     * 2nd parameter: A new OnDismissListener
     */
    OnDismissWrapper onDismissWrapper = new OnDismissWrapper(ID_ONDISMISSWRAPPER, new SuperToast.OnDismissListener() {

        @Override
        public void onDismiss(View view) {

            Log.d(ExampleSuperActivityToast.this.getClass().toString(), "On Dismiss");

        }
    });

}

