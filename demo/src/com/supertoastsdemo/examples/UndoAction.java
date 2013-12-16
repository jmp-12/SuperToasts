package com.supertoastsdemo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.supertoastsdemo.R;

public class UndoAction extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undoaction);

        final TextView dummyTextView = (TextView)
                findViewById(R.id.dummy_textview);

        final SuperActivityToast superActivityToast = new SuperActivityToast(UndoAction.this,
                SuperToast.Type.BUTTON);
        superActivityToast.setText("Action performed.");
        superActivityToast.setDuration(SuperToast.Duration.LONG);
        superActivityToast.setButtonText("UNDO");
        superActivityToast.setButtonResource(SuperToast.Icon.Dark.UNDO);
        superActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);
        superActivityToast.setButtonOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 * Any Context used in this method should be view.getContext() otherwise there will be
                 * a null pointer exception on orientation change.
                 */

                dummyTextView.setVisibility(View.VISIBLE);

            }
        });

        Button deleteButton = (Button)
                findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dummyTextView.setVisibility(View.GONE);

                superActivityToast.show();

            }
        });

    }

}