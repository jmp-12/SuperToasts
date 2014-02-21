package com.supertoastsdemo.examples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.Wrappers;
import com.supertoastsdemo.R;

import java.util.ArrayList;
import java.util.Collections;


/**
 * This class showcases the usage of a SuperActivityToast to mimic the undo
 * delete bar in the Gmail app.
 */
@SuppressWarnings("UnusedDeclaration")
public class ExampleUndoBar extends Activity {

    private static final String ID_UNDO_WRAPPER = "id_undo_wrapper";
    private static final String ID_LISTNAME = "id_listname";
    private static final String ID_LISTPOSITION = "id_listposition";
    private static final String ID_NAMELIST = "id_namelist";


    ArrayAdapter<String> namesArrayAdapter;
    ArrayList<String> nameArray = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_undobar);

        /* Create a Wrappers object to reattach our OnClickWrapper  */
        Wrappers wrappers = new Wrappers();
        wrappers.add(onClickWrapper);

        /* Recreate and reshow any SuperActivityToasts that were showing before orientation change */
        SuperActivityToast.onRestoreState(savedInstanceState, ExampleUndoBar.this, wrappers);

        /* If list exists in savedInstanceState load that list, otherwise add default names */
        if(savedInstanceState != null) {

            String[] nameList = savedInstanceState.getStringArray(ID_NAMELIST);

            Collections.addAll(nameArray, nameList);

        } else {

            nameArray.add("John");
            nameArray.add("Alyssa");
            nameArray.add("Chris");
            nameArray.add("Sarah");
            nameArray.add("Olaf");
            nameArray.add("Jessica");
            nameArray.add("Alex");
            nameArray.add("Neav");

        }

        /* Create a new ArrayAdapter to show our list in the ListView */
        namesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nameArray);

        /* Get a reference to our ListView in our XML file and attach the ArrayAdapter to it */
        final ListView namesListView = (ListView)
                findViewById(R.id.names_listview);
        namesListView.setAdapter(namesArrayAdapter);

        /* Register the ListView to show a ContextMenu on long press */
        registerForContextMenu(namesListView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getMenuInflater();

        /**
         * If the View sending the request for a ContextMenu is our ListView (it will be)
         * inflate a specified Menu. In this example the menu simply says "Delete".
         */
        if(view.getId() == R.id.names_listview) {

            inflater.inflate(R.menu.contextmenu_undo, menu);

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        /**
         * If the MenuItem being clicked is our delete item (it will be)
         * remove the item and show an undo bar.
         */
        if(item.getItemId() == R.id.delete) {

            /* Get info for the selected item */
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            /* Create a Bundle and put the String being deleted and it's position into the Bundle */
            final Bundle bundle = new Bundle();
            bundle.putString(ID_LISTNAME, namesArrayAdapter.getItem(info.position));
            bundle.putInt(ID_LISTPOSITION, info.position);

            /* Show a SuperActivityToast with a button and OnClickWrapper */
            SuperActivityToast superActivityToast = new SuperActivityToast(ExampleUndoBar.this, SuperToast.Type.BUTTON);
            superActivityToast.setDuration(SuperToast.Duration.LONG);
            superActivityToast.setText("Name deleted.");
            superActivityToast.setBackground(SuperToast.Background.GRAY);
            superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");

            /* This part is important, pass the Bundle we created earlier as a second parameter here */
            superActivityToast.setOnClickWrapper(onClickWrapper, bundle);
            superActivityToast.show();

            /* Remove the item from the adapter */
            namesArrayAdapter.remove(namesArrayAdapter.getItem(info.position));

        }

        return true;

    }

    /**
     * This is the OnClickWrapper for the SuperActivityToast. It gets a reference to the deleted object
     * via a Parcelable (our bundle that we passed earlier). This is particularly useful if an item is deleted
     * and the user rotates the device, the item's data wil not be lost until the SuperActivityToast disappears.
     */
    OnClickWrapper onClickWrapper = new OnClickWrapper(ID_UNDO_WRAPPER, new SuperToast.OnClickListener() {

        @Override
        public void onClick(View view, Parcelable token) {

            if(token != null){

                /* Create a Bundle from the passed Parcelable (the Bundle we passed earlier as a parameter) */
                Bundle bundle = (Bundle) token;
                String name = bundle.getString(ID_LISTNAME);
                int namePosition = bundle.getInt(ID_LISTPOSITION);

                /* Reinsert the item at the correct position */
                namesArrayAdapter.insert(name, namePosition);

            }

        }

    });


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* Save the SuperActivityToast for recreation if showing during orientation change */
        SuperActivityToast.onSaveState(outState);

        /* Create a String[] to save our current list data on orientation change */
        String[] nameList = new String[nameArray.size()];

        for (int i = 0; i < nameList.length; i++) {

            nameList[i] = namesArrayAdapter.getItem(i);

        }

        outState.putStringArray(ID_NAMELIST, nameList);

    }

}
