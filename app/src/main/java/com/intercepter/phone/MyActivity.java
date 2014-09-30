package com.intercepter.phone;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MyActivity extends Activity {

    /**
     * Items entered by the user is stored in this ArrayList variable
     */
    public static ArrayList<String> list = new ArrayList<String>();

    /**
     * Declaring an ArrayAdapter to set items to ListView
     */
    private MyListAdapter adapter;

    public static final String PREFS_NAME = "blocked_numbers";
    public static final String BLOCKED_NUMBERS_PREF_NAME = "blocked_numbers";

    public static boolean isInNumbers(String number) {
        for (String element : list) {
            if (PhoneNumberUtils.compare(element, number))
                return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> stringSet = new HashSet<String>(list);

        editor.putStringSet(BLOCKED_NUMBERS_PREF_NAME, stringSet);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Button addButton = (Button) findViewById(R.id.add_button);
        if (adapter == null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            Set<String> stringSet = settings.getStringSet(BLOCKED_NUMBERS_PREF_NAME, new HashSet<String>());
            list.addAll(stringSet);
            adapter = new MyListAdapter(this, list);
        }
        View.OnClickListener addButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText listItemText = (EditText) ((View) v.getParent()).findViewById(R.id.phone_number);
                String number = listItemText.getText().toString();
                if (number.isEmpty())
                    return;
                listItemText.setText("");
                list.add(number);
                adapter.notifyDataSetChanged();
            }
        };
        addButton.setOnClickListener(addButtonListener);
        ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
