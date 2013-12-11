package com.github.johnpersano.supertoastsdemo;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

public class FragmentSuperCardToast extends Fragment {

    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;
    Spinner mDismissFunctionSpinner;

    RadioGroup mTypeRadioGroup;

    CheckBox mImageCheckBox;

    private int mSdkVersion = android.os.Build.VERSION.SDK_INT;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_supercardtoast,
                container, false);

        mTypeRadioGroup = (RadioGroup)
                view.findViewById(R.id.type_radiogroup);

        mDurationSpinner = (Spinner)
                view.findViewById(R.id.duration_spinner);

        mBackgroundSpinner = (Spinner)
                view.findViewById(R.id.background_spinner);

        mTextsizeSpinner = (Spinner)
                view.findViewById(R.id.textsize_spinner);

        mDismissFunctionSpinner = (Spinner)
                view.findViewById(R.id.dismissfunction_spinner);

        Button showButton = (Button)
                view.findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showSuperCardToast();

            }

        });

        return view;

    }


    private void showSuperCardToast() {

        final SuperCardToast superCardToast;

        switch (mTypeRadioGroup.getCheckedRadioButtonId()) {

            case R.id.toast_radiobutton:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

            case R.id.button_radiobutton:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.BUTTON);
                superCardToast.setButtonOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        superCardToast.dismiss();

                        SuperActivityToast.createDarkSuperActivityToast(getActivity(), getActivity().getResources().getString(R.string.onclick),
                                SuperToast.Duration.MEDIUM).show();

                    }
                });

                break;

            case R.id.progress_radiobutton:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.PROGRESS);

                break;

            case R.id.hprogress_radiobutton:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.PROGRESS_HORIZONTAL);

                /** Since this SuperCardToast will show actual
                 *  progress from a background ASyncTask the duration of the
                 *  SuperCardToast must be indeterminate **/
                superCardToast.setIndeterminate(true);

                /** This dummy ASyncTask will dismiss the SuperCardToast
                 *  when finished **/
                new DummyOperation(superCardToast).execute();

                break;

            default:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

        }

        switch (mDurationSpinner.getSelectedItemPosition()) {

            case 0:

                superCardToast.setDuration(SuperToast.Duration.SHORT);

                break;

            case 1:

                superCardToast.setDuration(SuperToast.Duration.MEDIUM);

                break;

            case 2:

                superCardToast.setDuration(SuperToast.Duration.LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_BLACK);

                break;

            case 1:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GRAY);

                break;

            case 2:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GREEN);

                break;

            case 3:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_BLUE);

                break;

            case 4:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_RED);

                break;

            case 5:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_PURPLE);

                break;

            case 6:

                superCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_ORANGE);

                break;

        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                superCardToast.setTextSize(SuperToast.TextSize.SMALL);

                break;

            case 1:

                superCardToast.setTextSize(SuperToast.TextSize.MEDIUM);

                break;

            case 2:

                superCardToast.setTextSize(SuperToast.TextSize.LARGE);

                break;

        }

        switch (mDismissFunctionSpinner.getSelectedItemPosition()) {

            case 0:

                /** Do nothing, this is the stock behaviour **/

                break;

            case 1:

                /** No need to check Android version for this call. The library does this automatically. **/
                superCardToast.setSwipeToDismiss(true);

                if (mSdkVersion < android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {

                    /** Notify user that this does nothing on pre-honeycomb devices **/
                    SuperActivityToast.createDarkSuperActivityToast(getActivity(),
                            getActivity().getResources().getString(R.string.error_prehoneycomb), SuperToast.Duration.MEDIUM).show();

                }

                break;

            case 2:

                superCardToast.setTouchToDismiss(true);

                break;

        }

        superCardToast.show();

    }


    @Override
    public void onPause() {
        super.onPause();

        /* Don't let a SuperActivityToast linger */
        SuperActivityToast.cancelAllSuperActivityToasts();

    }

    private class DummyOperation extends AsyncTask<Void, Integer, Void> {

        /**
         * This setup is a little hacky due to the customization in the demo.
         * Check the examples package in the demo for a proper example
         */

        SuperCardToast mSuperCardToast;

        public DummyOperation(SuperCardToast superCardToast) {

            this.mSuperCardToast = superCardToast;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < 11; i++) {

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

            if (mSuperCardToast != null) {

                mSuperCardToast.dismiss();

            }

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            if (mSuperCardToast != null) {

                mSuperCardToast.setProgress(progress[0]);

            }

        }

    }

}