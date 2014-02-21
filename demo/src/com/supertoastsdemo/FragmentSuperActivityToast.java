/**
 *  Copyright 2014 John Persano
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */

package com.supertoastsdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.OnDismissWrapper;
import com.github.johnpersano.supertoasts.util.Wrappers;

public class FragmentSuperActivityToast extends SherlockFragment {

    Spinner mAnimationSpinner;
    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;
    RadioGroup typeRadioGroup;
    CheckBox mImageCheckBox;
    CheckBox mDismissCheckBox;

    DummyOperation mDummyOperation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_superactivitytoast,
                container, false);

        Wrappers wrappers = new Wrappers();
        wrappers.add(onClickWrapper);
        wrappers.add(onDismissWrapper);

        SuperActivityToast.onRestoreState(savedInstanceState, getActivity(), wrappers);

        mAnimationSpinner = (Spinner)
                view.findViewById(R.id.animationSpinner);

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

        mImageCheckBox = (CheckBox)
                view.findViewById(R.id.imageCheckBox);

        mDismissCheckBox = (CheckBox)
                view.findViewById(R.id.dismiss_checkbox);

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

        SuperActivityToast.onSaveState(outState);

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

                superActivityToast.setOnClickWrapper(onClickWrapper);

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

                mDummyOperation = new DummyOperation(superActivityToast);
                mDummyOperation.execute();

                break;

            default:

                superActivityToast = new SuperActivityToast(getActivity(),
                        SuperToast.Type.STANDARD);

                break;

        }

        switch (mAnimationSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setAnimations(SuperToast.Animations.FADE);

                break;

            case 1:

                superActivityToast.setAnimations(SuperToast.Animations.FLYIN);

                break;

            case 2:

                superActivityToast.setAnimations(SuperToast.Animations.POPUP);

                break;

            case 3:

                superActivityToast.setAnimations(SuperToast.Animations.SCALE);

                break;

        }

        switch (mDurationSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setDuration(SuperToast.Duration.SHORT);

                break;

            case 1:

                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);

                break;

            case 2:

                superActivityToast.setDuration(SuperToast.Duration.LONG);

                break;

        }

        switch (mBackgroundSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setBackground(SuperToast.Background.BLACK);

                break;

            case 1:

                superActivityToast.setBackground(SuperToast.Background.GRAY);

                break;

            case 2:

                superActivityToast.setBackground(SuperToast.Background.GREEN);

                break;

            case 3:

                superActivityToast.setBackground(SuperToast.Background.BLUE);

                break;

            case 4:

                superActivityToast.setBackground(SuperToast.Background.RED);

                break;

            case 5:

                superActivityToast.setBackground(SuperToast.Background.PURPLE);

                break;

            case 6:

                superActivityToast.setBackground(SuperToast.Background.ORANGE);

                break;

        }

        switch (mTextsizeSpinner.getSelectedItemPosition()) {

            case 0:

                superActivityToast.setTextSize(SuperToast.TextSize.SMALL);

                break;

            case 1:

                superActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);

                break;

            case 2:

                superActivityToast.setTextSize(SuperToast.TextSize.LARGE);

                break;

        }

        if(mImageCheckBox.isChecked()) {

            superActivityToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);

        }

        if(mDismissCheckBox.isChecked()) {

            superActivityToast.setOnDismissWrapper(onDismissWrapper);

        }

        superActivityToast.show();

    }

    private OnClickWrapper onClickWrapper = new OnClickWrapper("onclickwrapper_one", new SuperToast.OnClickListener() {

        @Override
        public void onClick(View v, Parcelable token) {

            SuperToast superToast = new SuperToast(v.getContext());
            superToast.setText("onClick!");
            superToast.setDuration(SuperToast.Duration.VERY_SHORT);
            superToast.setBackground(SuperToast.Background.BLUE);
            superToast.setTextColor(Color.WHITE);
            superToast.show();

        }

    });

    private OnDismissWrapper onDismissWrapper = new OnDismissWrapper("ondismisswrapper_one", new SuperToast.OnDismissListener() {

        @Override
        public void onDismiss(View view) {

            SuperToast superToast = new SuperToast(view.getContext());
            superToast.setText("onDismiss!");
            superToast.setDuration(SuperToast.Duration.VERY_SHORT);
            superToast.setBackground(SuperToast.Background.RED);
            superToast.setTextColor(Color.WHITE);
            superToast.show();

        }
    });

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

        @Override
        protected void onCancelled() {
            super.onCancelled();

            SuperActivityToast.cancelAllSuperActivityToasts();

        }
    }

}