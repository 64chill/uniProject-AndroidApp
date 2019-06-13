package com.example.unip_simplerssfeeder_app.fragments;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unip_simplerssfeeder_app.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.example.unip_simplerssfeeder_app.ViewRssUrlsActivity;
import com.example.unip_simplerssfeeder_app.utils.DatabaseHelperRSSUrl;
import com.example.unip_simplerssfeeder_app.utils.xml_parser_classes.RSSFeedParser;
import com.example.unip_simplerssfeeder_app.utils.xml_parser_classes.RssFeedModel;


public class EditNewsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private DatabaseHelperRSSUrl mDatabaseHelper;//new DatabaseHelperRSSUrl(view.getContext());
    private boolean asyncTaskAddCheck = false;

    public EditNewsFragment() {
        // Required empty public constructor
    }
// ---------------------------------------------------------------------------------------------------- onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_news, container, false);

        view.findViewById(R.id.button_add_rss_url).setOnClickListener(this); // add button
        view.findViewById(R.id.button_view_rss_urls).setOnClickListener(this); // view button
        mDatabaseHelper = new DatabaseHelperRSSUrl(view.getContext());
        return view;
    }
    // ---------------------------------------------------------------------------------------------------- onClick
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add_rss_url){
            String input_url = ((EditText) view.findViewById(R.id.input_rss_url)).getText().toString();
            input_url = input_url.trim();

            //check if this URL exists
            // add to database - show Toast
            if(checkIfRSSIsValid(input_url)){
                addUrlToDB(input_url);
            }
        }

        if (v.getId() == R.id.button_view_rss_urls){
            Intent i = new Intent(v.getContext(), ViewRssUrlsActivity.class);
            startActivity(i);
        }
    }
    // ---------------------------------------------------------------------------------------------------- checkIfRSSIsValid
    private boolean checkIfRSSIsValid(String user_input){
        String myPattern = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
        boolean bb = Pattern.matches(myPattern, user_input);

        if (bb){
                return true;
        }
        Toast.makeText(view.getContext(), "URL is invalid : Please make sure to add http:// or https:// at the beginning", Toast.LENGTH_LONG).show();
        return false;
    }
    // ---------------------------------------------------------------------------------------------------- addUrlToDB
    private boolean addUrlToDB(String input_url){
        boolean checker = mDatabaseHelper.checkIfurlExist(input_url);
        if(checker) {
            Toast.makeText(view.getContext(), "You already added this URL! Please enter the new one!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!doesURLContainRSSfeed(input_url)){
            Toast.makeText(view.getContext(), "This is not a valid RSS FEED!", Toast.LENGTH_LONG).show();
            return false;
        }
        boolean insertData = mDatabaseHelper.addURLToDB(input_url);

        if(insertData){
            Toast.makeText(view.getContext(),"Your URL was added", Toast.LENGTH_LONG).show();
            return true;
        } else{
            Toast.makeText(view.getContext(),"There was a problem with adding Your URL", Toast.LENGTH_LONG).show();
        }
        return false;

    }
    // ---------------------------------------------------------------------------------------------------- doesURLContainRSSfeed
    public boolean doesURLContainRSSfeed(String stringUrl) {

        new testIfValidRss(stringUrl);
        try {
            new testIfValidRss(stringUrl).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.asyncTaskAddCheck;
    }

    class testIfValidRss extends AsyncTask<Void, Void, Void> {
        private String stringUrl;
        public testIfValidRss(String stringUrl){
            super();
            this.stringUrl = stringUrl;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                URL url = new URL(stringUrl);
                RSSFeedParser rfp = new RSSFeedParser(url, 5);
                ArrayList<RssFeedModel> rfmList = rfp.parseFeed();
                if(!rfmList.isEmpty()) {
                    if(rfmList.get(0).getTitle() !=null) {
                        asyncTaskAddCheck = true;
                    }
                }
            }catch(Exception e ){
                e.printStackTrace();
                Log.e("EXCEPTION" ,""+e.getMessage() );
            }
           return null;
        } //doInBackGround end;
    } // class end;
}
