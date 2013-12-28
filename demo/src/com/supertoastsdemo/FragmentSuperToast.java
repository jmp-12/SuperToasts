package com.supertoastsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.johnpersano.supertoasts.SuperToast;


public class FragmentSuperToast extends SherlockFragment {

    Spinner mAnimationSpinner;
    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;

    CheckBox mImageCheckBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_supertoast,
                container, false);

        mAnimationSpinner = (Spinner)
                view.findViewById(R.id.animationSpinner);

        mDurationSpinner = (Spinner)
                view.findViewById(R.id.durationSpinner);

        mBackgroundSpinner = (Spinner)
                view.findViewById(R.id.backgroundSpinner);

        mTextsizeSpinner = (Spinner)
                view.findViewById(R.id.textsizeSpinner);

        mImageCheckBox = (CheckBox)
                view.findViewById(R.id.imageCheckBox);

        Button showButton = (Button)
                view.findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showSuperToast();

            }

        });

        showButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                startActivity(new Intent(getActivity(), ActivityTwo.class));

                return false;
            }
        });

        return view;

    }



    private void showSuperToast() {

        final SuperToast superToast = new SuperToast(getActivity());

        switch (mAnimationSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setAnimations(SuperToast.Animations.FADE);

                break;

            case 1:

                superToast.setAnimations(SuperToast.Animations.FLYIN);

                break;

            case 2:

                superToast.setAnimations(SuperToast.Animations.POPUP);

                break;

            case 3:

                superToast.setAnimations(SuperToast.Animations.SCALE);

                break;

        }

        switch (mDurationSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setDuration(SuperToast.Duration.SHORT);

                break;

            case 1:

                superToast.setDuration(SuperToast.Duration.MEDIUM);

                break;

            case 2:

                superToast.setDuration(SuperToast.Duration.LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_BLACK);

                break;

            case 1:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GRAY);

                break;

            case 2:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GREEN);

                break;

            case 3:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_BLUE);

                break;

            case 4:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_RED);

                break;

            case 5:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_PURPLE);

                break;

            case 6:

                superToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_ORANGE);

                break;


        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setTextSize(SuperToast.TextSize.SMALL);

                break;

            case 1:

                superToast.setTextSize(SuperToast.TextSize.MEDIUM);

                break;

            case 2:

                superToast.setTextSize(SuperToast.TextSize.LARGE);

                break;

        }

        if(mImageCheckBox.isChecked()) {

            superToast.setIconResource(R.drawable.icon_message, SuperToast.IconPosition.LEFT);

        }

        superToast.show();

    }

}
