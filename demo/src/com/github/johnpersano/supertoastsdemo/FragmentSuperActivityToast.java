package com.github.johnpersano.supertoastsdemo;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.concurrent.ThreadPoolExecutor;

public class FragmentSuperActivityToast extends Fragment {

    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;

    RadioGroup typeRadioGroup;

    CheckBox mImageCheckBox;

    DummyOperation mDummyOperation;

    SuperActivityToast mSuperActivityToast;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_superactivitytoast,
                container, false);

        SuperActivityToast.onRestoreState(savedInstanceState, getActivity());

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        SuperActivityToast.onSaveState(outState);

    }

    @Override
    public void onPause() {
        super.onPause();

        if(mDummyOperation != null) {

            if(mDummyOperation.getStatus() == AsyncTask.Status.PENDING ||
                    mDummyOperation.getStatus() == AsyncTask.Status.RUNNING) {

                mDummyOperation.cancel(true);

            }

        }
    }

    private void showSuperToast() {

        switch (typeRadioGroup.getCheckedRadioButtonId()) {

            case R.id.toast_radiobutton:

                mSuperActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

            case R.id.button_radiobutton:

                mSuperActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.BUTTON);
                mSuperActivityToast.setTouchToDismiss(true);
                mSuperActivityToast.setIndeterminate(false);
                mSuperActivityToast.setButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SuperToast.cancelAllSuperToasts();
                         SuperToast.createDarkSuperToast(v.getContext(), "On Click!", SuperToast.Duration.VERY_SHORT).show();

                    }
                });

                break;

            case R.id.progress_radiobutton:

                mSuperActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.PROGRESS);

                break;

            case R.id.hprogress_radiobutton:

                mSuperActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.PROGRESS_HORIZONTAL);

                /** Since this SuperActivityToast will show actual
                 *  progress from a background ASyncTask the duration of the
                 *  SuperActivityToast must be indeterminate **/
                mSuperActivityToast.setIndeterminate(true);

                /** This dummy ASyncTask will dismiss the SuperActivityToast
                 *  when finished **/
                mDummyOperation = new DummyOperation(mSuperActivityToast);
                mDummyOperation.execute();

                break;

            default:

                mSuperActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

        }

        switch (mDurationSpinner.getSelectedItemPosition()) {

            case 0:

                mSuperActivityToast.setDuration(SuperToast.Duration.SHORT);

                break;

            case 1:

                mSuperActivityToast.setDuration(SuperToast.Duration.MEDIUM);

                break;

            case 2:

                mSuperActivityToast.setDuration(SuperToast.Duration.LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_BLACK);

                break;

            case 1:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GRAY);

                break;

            case 2:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GREEN);

                break;

            case 3:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_BLUE);

                break;

            case 4:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_RED);

                break;

            case 5:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_PURPLE);

                break;

            case 6:

                mSuperActivityToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_ORANGE);

                break;

        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                mSuperActivityToast.setTextSize(SuperToast.TextSize.SMALL);

                break;

            case 1:

                mSuperActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);

                break;

            case 2:

                mSuperActivityToast.setTextSize(SuperToast.TextSize.LARGE);

                break;

        }

        if (mImageCheckBox.isChecked()) {

            mSuperActivityToast.setIconResource(R.drawable.icon_message, SuperToast.IconPosition.LEFT);

        }

        mSuperActivityToast.show();

    }

    private class DummyOperation extends AsyncTask<Void, Integer, Void> {

        SuperActivityToast mSuperActivityToast;

        public DummyOperation(SuperActivityToast superActivityToast) {

            this.mSuperActivityToast = superActivityToast;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if(mSuperActivityToast != null) {

                SuperActivityToast.cancelAllSuperActivityToasts();

            }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 1; i <= 11; i++) {

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