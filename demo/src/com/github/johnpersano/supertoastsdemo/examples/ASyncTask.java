package com.github.johnpersano.supertoastsdemo.examples;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoastsdemo.R;

public class ASyncTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        Button showButton = (Button)
                findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /** Executes the ASyncTask that displays the SuperCardToast. **/
                new DummyOperation().execute();

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

        /** Don't let the SuperActivityToast linger on other screens **/
        SuperActivityToast.cancelAllSuperActivityToasts();

    }


    private class DummyOperation extends AsyncTask<Void, Integer, Void> {

        private SuperCardToast mSuperCardToast;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /** Create a new SuperCardToast that displays a horizontal progressbar. **/
            mSuperCardToast = new SuperCardToast(ASyncTask.this,
                    SuperToast.Type.PROGRESS_HORIZONTAL);
            mSuperCardToast.setIndeterminate(true);
            mSuperCardToast.setBackgroundResource(SuperToast.Background.TRANSLUCENT_GRAY);
            mSuperCardToast.setTextColor(Color.WHITE);

            /** Allow the user to dismiss the SuperCardToast. **/
            mSuperCardToast.setSwipeToDismiss(true);
            mSuperCardToast.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            /** Sleep the thread to simulate backgound activity. **/
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

            mSuperCardToast.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            mSuperCardToast.setProgress(progress[0]);

        }

    }

}
