package com.supertoastsdemo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.extlibsupertoasts.SuperActivityToast;
import com.extlibsupertoasts.SuperToast;
import com.supertoastsdemo.R;

public class UndoActionWithCallBack extends Activity implements View.OnClickListener {

    private TextView mDummyTextView;
    private SuperActivityToast mSuperActivityToast;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undoaction);

        mDummyTextView = (TextView)
                findViewById(R.id.dummy_textview);

        mSuperActivityToast = new SuperActivityToast(UndoActionWithCallBack.this,
                SuperToast.Type.BUTTON);
        mSuperActivityToast.setText("Action performed.");
        mSuperActivityToast.setDuration(SuperToast.DURATION_LONG);
        mSuperActivityToast.setButtonText("UNDO");
        mSuperActivityToast.setButtonResource(SuperToast.BUTTON_DARK_UNDO);
        mSuperActivityToast.setTextSize(SuperToast.TEXTSIZE_MEDIUM);

        Button deleteButton = (Button)
                findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mDummyTextView.setVisibility(View.GONE);

                if(!mSuperActivityToast.isShowing()) {

                    mSuperActivityToast.show();

                }

            }
        });

    }


    @Override
    public void onClick(View v) {

        mDummyTextView.setVisibility(View.VISIBLE);
        mSuperActivityToast.dismiss();

    }

}