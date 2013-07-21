package com.supertoaststest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.extlibsupertoasts.SuperToast;

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

        return view;

    }

    @Override
    public void onPause() {
        super.onPause();

        /* Don't let the SuperToast linger */
        SuperToast.cancelAllSuperToasts();

    }


    private void showSuperToast() {

        final SuperToast superToast = new SuperToast(getActivity());

        switch (mAnimationSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setAnimation(SuperToast.ANIMATION_FADE);

                break;

            case 1:

                superToast.setAnimation(SuperToast.ANIMATION_FLYIN);

                break;

            case 2:

                superToast.setAnimation(SuperToast.ANIMATION_POPUP);

                break;

            case 3:

                superToast.setAnimation(SuperToast.ANIMATION_SCALE);

                break;

        }

        switch (mDurationSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setDuration(SuperToast.DURATION_SHORT);

                break;

            case 1:

                superToast.setDuration(SuperToast.DURATION_MEDIUM);

                break;

            case 2:

                superToast.setDuration(SuperToast.DURATION_LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_BLACKTRANSLUCENT);

                break;

            case 1:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_GREYTRANSLUCENT);

                break;

            case 2:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_GREENTRANSLUCENT);

                break;

            case 3:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_BLUETRANSLUCENT);

                break;

            case 4:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_REDTRANSLUCENT);

                break;

            case 5:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_PURPLETRANSLUCENT);

                break;

            case 6:

                superToast.setBackgroundResource(SuperToast.BACKGROUND_ORANGETRANSLUCENT);

                break;

        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                superToast.setTextSize(SuperToast.TEXTSIZE_SMALL);

                break;

            case 1:

                superToast.setTextSize(SuperToast.TEXTSIZE_MEDIUM);

                break;

            case 2:

                superToast.setTextSize(SuperToast.TEXTSIZE_LARGE);

                break;

        }

        if(mImageCheckBox.isChecked()) {

            superToast.setIconResource(R.drawable.icon_message, SuperToast.IconPosition.LEFT);

        }

        superToast.show();

    }

}