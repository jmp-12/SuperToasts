package com.supertoastsdemo;

import android.app.Fragment;
import android.content.Intent;
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
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.supertoastsdemo.R;

public class FragmentSuperCardToast extends SherlockFragment {

    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;
    Spinner mDismissFunctionSpinner;

    RadioGroup mTypeRadioGroup;

    CheckBox mImageCheckBox;

    DummyOperation mDummyOperation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_supercardtoast,
                container, false);

        SuperCardToast.onRestoreState(savedInstanceState, getActivity());

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

        showButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                 startActivity(new Intent(getActivity(), ActivityTwo.class));

                return false;
            }
        });

        return view;

    }


    @Override
    public void onPause() {
        super.onPause();

        if(mDummyOperation != null){

            if(mDummyOperation.getStatus() == AsyncTask.Status.PENDING ||
                    mDummyOperation.getStatus() == AsyncTask.Status.RUNNING) {

                mDummyOperation.cancel(true);

            }

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        SuperCardToast.onSaveState(outState);

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

                        SuperActivityToast.createSuperActivityToast(view.getContext(), view.getContext().getResources()
                                .getString(R.string.onclick), SuperToast.Duration.MEDIUM).show();

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

                mDummyOperation = new DummyOperation(superCardToast);
                mDummyOperation.execute();

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

                superCardToast.setTextSize(SuperToast.TextSize.SMALL);

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

                break;

            case 2:

                superCardToast.setTouchToDismiss(true);

                break;

        }

        superCardToast.show();

    }

    private class DummyOperation extends AsyncTask<Void, Integer, Void> {

        /** This setup is a little hacky due to the customization in the demo.
         *  Check the examples package in the demo for a proper example */

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
        	
        	if(mSuperCardToast != null) {
        		
                mSuperCardToast.dismiss();

        	} 

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            
        	if(mSuperCardToast != null) {

            mSuperCardToast.setProgress(progress[0]);
            
        	}

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            SuperCardToast.cancelAllSuperCardToasts();

        }
    }

}