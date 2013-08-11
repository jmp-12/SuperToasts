package com.github.johnpersano.supertoastsdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoastsdemo.R;

public class FragmentSuperActivityToast extends SherlockFragment {

    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;

    RadioGroup typeRadioGroup;

    CheckBox mImageCheckBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_superactivitytoast,
                container, false);

        typeRadioGroup = (RadioGroup)
                view.findViewById(R.id.type_radiogroup);

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

        /* Don't let the SuperActivityToast linger */
        SuperActivityToast.cancelAllSuperActivityToasts();

    }


    private void showSuperToast() {

        final SuperActivityToast superActivityToast;

        switch (typeRadioGroup.getCheckedRadioButtonId()) {

            case R.id.toast_radiobutton:

                superActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

            case R.id.button_radiobutton:

                superActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.BUTTON);
                superActivityToast.setButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        superActivityToast.dismiss();

                        SuperActivityToast.createDarkSuperActivityToast(getActivity(), getActivity().getResources().getString(R.string.onclick),
                                SuperToast.DURATION_MEDIUM).show();

                    }
                });

                break;

            case R.id.progress_radiobutton:

                superActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.PROGRESS);

                break;

            case R.id.hprogress_radiobutton:

                superActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.PROGRESS_HORIZONTAL);

                /** Since this SuperActivityToast will show actual
                 *  progress from a background ASyncTask the duration of the
                 *  SuperActivityToast must be indeterminate **/
                superActivityToast.setIndeterminate(true);

                /** This dummy ASyncTask will dismiss the SuperActivityToast
                 *  when finished **/
                new DummyOperation(superActivityToast).execute();

                break;

            default:

                superActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

        }

        switch (mDurationSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setDuration(SuperToast.DURATION_SHORT);

                break;

            case 1:

                superActivityToast.setDuration(SuperToast.DURATION_MEDIUM);

                break;

            case 2:

                superActivityToast.setDuration(SuperToast.DURATION_LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_BLACKTRANSLUCENT);

                break;

            case 1:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_GREYTRANSLUCENT);

                break;

            case 2:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_GREENTRANSLUCENT);

                break;

            case 3:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_BLUETRANSLUCENT);

                break;

            case 4:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_REDTRANSLUCENT);

                break;

            case 5:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_PURPLETRANSLUCENT);

                break;

            case 6:

                superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_ORANGETRANSLUCENT);

                break;

        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setTextSize(SuperToast.TEXTSIZE_SMALL);

                break;

            case 1:

                superActivityToast.setTextSize(SuperToast.TEXTSIZE_MEDIUM);

                break;

            case 2:

                superActivityToast.setTextSize(SuperToast.TEXTSIZE_LARGE);

                break;

        }

        if(mImageCheckBox.isChecked()) {

            superActivityToast.setIconResource(R.drawable.icon_message, SuperToast.IconPosition.LEFT);

        }

        superActivityToast.show();

    }

    private class DummyOperation extends AsyncTask<Void, Integer, Void> {

        SuperActivityToast mSuperActivityToast;

        public DummyOperation(SuperActivityToast superActivityToast) {

            this.mSuperActivityToast = superActivityToast;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i < 11 ; i++) {

                try {

                    Thread.sleep(250);

                    onProgressUpdate(i * 10);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {

            mSuperActivityToast.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            mSuperActivityToast.setProgress(progress[0]);

        }

    }

}