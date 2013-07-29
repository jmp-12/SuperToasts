package com.supertoasts.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.extlibsupertoasts.SuperActivityToast;
import com.extlibsupertoasts.SuperCardToast;
import com.extlibsupertoasts.SuperToast;

public class FragmentSuperCardToast extends SherlockFragment {

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

        SuperCardToast superCardToast;

        switch (mTypeRadioGroup.getCheckedRadioButtonId()) {

            case R.id.toast_radiobutton:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

            case R.id.button_radiobutton:

                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.BUTTON);

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

                superCardToast.setDuration(SuperToast.DURATION_SHORT);

                break;

            case 1:

                superCardToast.setDuration(SuperToast.DURATION_MEDIUM);

                break;

            case 2:

                superCardToast.setDuration(SuperToast.DURATION_LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_BLACKTRANSLUCENT);

                break;

            case 1:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_GREYTRANSLUCENT);

                break;

            case 2:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_GREENTRANSLUCENT);

                break;

            case 3:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_BLUETRANSLUCENT);

                break;

            case 4:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_REDTRANSLUCENT);

                break;

            case 5:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_PURPLETRANSLUCENT);

                break;

            case 6:

                superCardToast.setBackgroundResource(SuperToast.BACKGROUND_ORANGETRANSLUCENT);

                break;

        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                superCardToast.setTextSize(SuperToast.TEXTSIZE_SMALL);

                break;

            case 1:

                superCardToast.setTextSize(SuperToast.TEXTSIZE_MEDIUM);

                break;

            case 2:

                superCardToast.setTextSize(SuperToast.TEXTSIZE_LARGE);

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
                            getActivity().getResources().getString(R.string.error_prehoneycomb), SuperToast.DURATION_MEDIUM).show();

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

        SuperCardToast mSuperCardToast;

        public DummyOperation(SuperCardToast superCardToast) {

            this.mSuperCardToast = superCardToast;

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

            mSuperCardToast.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            mSuperCardToast.setProgress(progress[0]);

        }

    }

}