package com.github.johnpersano.supertoastsdemo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoastsdemo.R;

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
            public void onClick(View v) {

                dummyTextView.setVisibility(View.VISIBLE);
                superActivityToast.dismiss();

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