package com.example.unip_simplerssfeeder_app.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.unip_simplerssfeeder_app.R;
import com.example.unip_simplerssfeeder_app.customAdapter.NewsListAdapter;
import com.example.unip_simplerssfeeder_app.utils.DatabaseHelperRSSUrl;
import com.example.unip_simplerssfeeder_app.utils.NewsCard;
import com.example.unip_simplerssfeeder_app.utils.ObjectSerializer;
import com.example.unip_simplerssfeeder_app.utils.xml_parser_classes.RSSFeedParser;
import com.example.unip_simplerssfeeder_app.utils.xml_parser_classes.RssFeedModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainFragment extends Fragment implements View.OnClickListener {
    private View view;
    private DatabaseHelperRSSUrl mDatabaseHelper;
    private ArrayList<NewsCard> newsCardsArray;
    private ListView allNews_listVew;
    private NewsListAdapter customAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mDatabaseHelper = new DatabaseHelperRSSUrl(getActivity()); //set database helper

        view.findViewById(R.id.button_refresh_news).setOnClickListener(this);
        newsCardsArray = this.getArrayList_FromSharedPreferences();
        allNews_listVew = (ListView) view.findViewById(R.id.list_view_all_news);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_refresh_news){
            refreshBttonClick();
        }
        }

    private void refreshBttonClick(){
        // get URLS from out database and put them in an array
        ArrayList<String> urlList = new ArrayList<>();
        Cursor mCoursor = mDatabaseHelper.getURLs();

        while(mCoursor.moveToNext()){
            urlList.add(mCoursor.getString(0));
        }
        /// start to parse, get news and relevant data from the internet
        new grabAndShowNews(urlList).execute();

    }
    /*
     *
     * working with ASYNC TASKSA BELOW
     *
     *
     *
     */
    //___________________________ grabAndShowNews __________________________________________________
    class grabAndShowNews extends AsyncTask<Void, Void, Void> {
        private ArrayList<String> urlList;

        public grabAndShowNews(ArrayList<String> urlList){
            this.urlList = urlList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("dointheback" , "st");
            for (String uniqueUrl: urlList) {
                Log.e("SimpleRSSFeeder" ,uniqueUrl );
                try {
                    /*start ---------------------------------------------------------------------------*/
                    URL url = new URL(uniqueUrl);
                    RSSFeedParser rfp = new RSSFeedParser(url, 5);
                    ArrayList<RssFeedModel> rfmList = rfp.parseFeed();
                    ArrayList<NewsCard> ncList = new ArrayList<>();
                    for (RssFeedModel model : rfmList) {
                        ncList.add(model.getNewsCardObject());
                    }
                    newsCardsArray.addAll(ncList);

                    /*end ---------------------------------------------------------------------------*/

                } catch (Exception e) {
                    Log.e("SimpleRSSFeeder" , e.getMessage());

                }
            } // for end;
            return null;
        } //doInBackGround end;
        @Override
        protected void onPostExecute(Void Void) {
            /*for (NewsCard model : newsCardsArray) {
                Log.e("at the end" , model.getTitle());
            }*/
            Collections.shuffle(newsCardsArray); // randomize news
            customAdapter = new NewsListAdapter(getActivity(), newsCardsArray);
            allNews_listVew.setAdapter(customAdapter);
           return;
        } //onPostExecute end;
    } // class end;

    /*
    *
    * working with shared preferences below
    *
    *
    *
     */
    //___________________________ setSharedPreferences _____________________________________________
    public void setSharedPreferences(ArrayList<NewsCard> arrayL){
        Context context = getActivity();
        //Set the values
        SharedPreferences prefs = context.getSharedPreferences("SimpleRSSFeeder_NewsList", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        try {
            Log.e("add arr list" , this.newsCardsArray.toString());
            editor.putString("nwl", ObjectSerializer.serialize(this.newsCardsArray));
        } catch (Exception e) {
            Log.e("SP_error_write" ,e.getMessage());
        }
        editor.commit();


    }

    //___________________________ sgetArrayList_FromSharedPreferences ______________________________
    public ArrayList<NewsCard> getArrayList_FromSharedPreferences(){
        Context context = getActivity();
        //Retrieve the values
        // load tasks from preference
        SharedPreferences prefs = context.getSharedPreferences("SimpleRSSFeeder_NewsList", Context.MODE_PRIVATE);

        try {
            ArrayList<NewsCard> arrL = (ArrayList<NewsCard>) ObjectSerializer.deserialize(prefs.getString("SimpleRSSFeeder_NewsList", ObjectSerializer.serialize(new ArrayList<NewsCard>())));
            Log.e("arr list" , arrL.toString());
        } catch (Exception e) {
            Log.e("SP_error_read" , e.getMessage());
        }
        return new ArrayList<NewsCard>();

    }

}
