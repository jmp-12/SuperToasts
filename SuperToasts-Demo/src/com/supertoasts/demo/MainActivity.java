package com.supertoasts.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.supertoasts.demo.R;


public class MainActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
                android.R.layout.simple_spinner_item, android.R.id.text1, getResources().getStringArray(R.array.navigation_list));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        actionBar.setListNavigationCallbacks(arrayAdapter, new ActionBar.OnNavigationListener() {

            SherlockFragment fragment;

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {


                switch (itemPosition) {

                    case 0:

                        fragment = new FragmentSuperToast();

                        break;


                    case 1:

                        fragment = new FragmentSuperActivityToast();

                        break;


                    case 2:

                        break;


                    default:

                        fragment = new FragmentSuperToast();

                        break;

                }

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

                return false;

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);


    }


}
