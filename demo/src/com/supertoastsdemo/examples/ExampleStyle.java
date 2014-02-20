package com.supertoastsdemo.examples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.supertoastsdemo.R;


/**
 * This class showcases the use of the Style class.
 */
@SuppressWarnings("UnusedDeclaration")
public class ExampleStyle extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

        /**
         *  This is used to recreate any showing or pending SuperActivityToasts.
         *
         *  1st parameter: Use bundle from onCreate(). No need for a null check
         *  2nd parameter: The current Activity
         */
        SuperActivityToast.onRestoreState(savedInstanceState, ExampleStyle.this);

        /**
         *  This is used to recreate any showing or pending SuperCardToasts.
         *
         *  1st parameter: Use bundle from onCreate(). No need for a null check
         *  2nd parameter: The current Activity
         */
        SuperCardToast.onRestoreState(savedInstanceState, ExampleStyle.this);

        /* Create a style object and modify it's references directly. */
        final Style customStyle = new Style();
        customStyle.animations = SuperToast.Animations.POPUP;
        customStyle.background = SuperToast.Background.PURPLE;
        customStyle.textColor = Color.WHITE;
        customStyle.buttonTextColor = Color.LTGRAY;
        customStyle.dividerColor = Color.WHITE;

        /* Create a default style object defined in the Style class. */
        final Style defaultStyle = Style.getStyle(Style.GREEN);

        /* Create a default style object defined in the Style class with specified animations. */
        final Style defaultStyleAnimation = Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN);

        final Button showButton = (Button)
                findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 * Create a SuperActivityToast that uses the custom style.
                 *
                 * 1st parameter: The current activity
                 * 2nd parameter: The style object
                 */
                final SuperActivityToast superActivityToast = new SuperActivityToast(ExampleStyle.this, customStyle);
                superActivityToast.setDuration(SuperToast.Duration.SHORT);
                superActivityToast.setText("This is a custom style");
                superActivityToast.show();

                /**
                 * Create a SuperActivityToast that uses a default style. The creation of the style object
                 * in the above code is for demonstration, just use Style.getStyle(); directly as a parameter.
                 *
                 * 1st parameter: The current activity
                 * 2nd parameter: The style object
                 */
                final SuperCardToast superCardToast = new SuperCardToast(ExampleStyle.this, defaultStyle);
                superCardToast.setDuration(SuperToast.Duration.SHORT);
                superCardToast.setText("This is a default style");
                superCardToast.show();

                /**
                 * Create a SuperActivityToast that uses a default style with specified animations. The creation of the style object
                 * in the above code is for demonstration, just use Style.getStyle(); directly as a parameter.
                 *
                 * 1st parameter: The current activity
                 * 2nd parameter: The style object
                 */
                final SuperCardToast superCardToastAnimation = new SuperCardToast(ExampleStyle.this, defaultStyleAnimation);
                superCardToastAnimation.setDuration(SuperToast.Duration.SHORT);
                superCardToastAnimation.setText("This is a default style with specified animations");
                superCardToastAnimation.show();

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* This method is used to save any showing or pending SuperActivityToasts */
        SuperActivityToast.onSaveState(outState);

        /* This method is used to save any showing or pending SuperCardToasts */
        SuperCardToast.onSaveState(outState);

    }

}
