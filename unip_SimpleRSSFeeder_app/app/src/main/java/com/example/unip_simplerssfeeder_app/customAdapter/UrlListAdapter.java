package com.example.unip_simplerssfeeder_app.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.unip_simplerssfeeder_app.R;

import java.util.List;

public class UrlListAdapter  extends ArrayAdapter<String> {
    public UrlListAdapter(Context context, List<String> urls) {
        super(context, R.layout.list_view_row_urls_fromdb ,urls);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // default -  return super.getView(position, convertView, parent);
        // add the layout
        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View customView = myCustomInflater.inflate(R.layout.list_view_row_urls_fromdb, parent, false);
        // get references.
        String singleFoodItem = getItem(position);
        TextView itemText = (TextView) customView.findViewById(R.id.row_urls_fromdb);

        // dynamically update the text from the array
        itemText.setText(singleFoodItem);
        //return our custom View or custom item
        return customView;
    }
}
