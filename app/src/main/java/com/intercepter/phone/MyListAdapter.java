package com.intercepter.phone;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter implements ListAdapter {
    private static ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public static boolean isInNumbers(String number) {
        for (String element : list) {
            if (PhoneNumberUtils.compare(element, number))
                return true;
        }
        return false;
    }

    public MyListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rules_list_layout, null);
        }
        //Handle TextView and display string from your list
        EditText listItemText = (EditText) view.findViewById(R.id.list_item_string);

        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) view.findViewById(R.id.delete_btn);
        Button addBtn = (Button) view.findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                if (list.size() > 1)
                    list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View relativeLayout = (View) v.getParent();
                EditText listItemText = (EditText) relativeLayout.findViewById(R.id.list_item_string);
                list.set(position, listItemText.getText().toString());
                list.add("");
                notifyDataSetChanged();
            }
        });

        return view;
    }
}