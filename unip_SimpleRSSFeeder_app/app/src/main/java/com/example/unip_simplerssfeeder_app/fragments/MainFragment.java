package com.example.unip_simplerssfeeder_app.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.unip_simplerssfeeder_app.R;
import com.example.unip_simplerssfeeder_app.customAdapter.NewsListAdapter;
import com.example.unip_simplerssfeeder_app.utils.DatabaseHelperRSSUrl;
import com.example.unip_simplerssfeeder_app.utils.JsonHandler;
import com.example.unip_simplerssfeeder_app.utils.NewsCard;
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
        newsCardsArray = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        /* _________________________ init */
        mDatabaseHelper = new DatabaseHelperRSSUrl(getActivity()); //set database helper

        view.findViewById(R.id.button_refresh_news).setOnClickListener(this);
        allNews_listVew = (ListView) view.findViewById(R.id.list_view_all_news);
        /*setting saved news----------------------------------------------------------------------*/
        newsCardsArray = getArrayList_FromSharedPreferences(getActivity());
        if (newsCardsArray !=null || !newsCardsArray.isEmpty()) {
            customAdapter = new NewsListAdapter(getActivity(), newsCardsArray);
            allNews_listVew.setAdapter(customAdapter);

            /*____________________________________ list view onClick() ___________________________*/
            allNews_listVew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    NewsCard nc = (NewsCard) parent.getItemAtPosition(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nc.getPostLink()));
                    startActivity(browserIntent);
                }
            });
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        //////////////////////////////////////////
        if (v.getId() == R.id.button_refresh_news){
            this.newsCardsArray.clear();
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
        try{
            urlList.get(0);
            new grabAndShowNews(urlList).execute();
        } catch (Exception e){
            Toast.makeText(view.getContext(), "You can't do that!, please insert at least one URL", Toast.LENGTH_LONG).show();
        }
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
            for (String uniqueUrl: urlList) {
                try {
                    /*start ----------------------------------------------------------------------*/
                    URL url = new URL(uniqueUrl);
                    RSSFeedParser rfp = new RSSFeedParser(url, 5);
                    ArrayList<RssFeedModel> rfmList = rfp.parseFeed();
                    ArrayList<NewsCard> ncList = new ArrayList<>();
                    for (RssFeedModel model : rfmList) {
                        ncList.add(model.getNewsCardObject());
                    }
                    newsCardsArray.addAll(ncList);

                    /*end ------------------------------------------------------------------------*/

                } catch (Exception e) {
                    Log.e("SimpleRSSFeeder" , e.getMessage());

                }
            } // for end;
            return null;
        } //doInBackGround end;
        @Override
        protected void onPostExecute(Void Void) {
            Collections.shuffle(newsCardsArray); // randomize news
            customAdapter = new NewsListAdapter(getActivity(), newsCardsArray);
            allNews_listVew.setAdapter(customAdapter);
            setSharedPreferences(newsCardsArray, getActivity());
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
    public void setSharedPreferences(ArrayList<NewsCard> arrayL  , Context context){

        //Set the values
        SharedPreferences prefs = context.getSharedPreferences("SimpleRSSFeeder_NewsList", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        try {
            prefs.edit().remove("nwl").commit(); // remove specific key from our shared preferences
            editor.putString("nwl" , new JsonHandler().get_NewsCardArrayList_ToJsonStringFormat(arrayL));
        } catch (Exception e){
            Log.e("SP_error_write1111111" ,e.getMessage());
        }
        editor.commit();
    }

    //___________________________ getArrayList_FromSharedPreferences ______________________________
    public ArrayList<NewsCard> getArrayList_FromSharedPreferences(Context context){
        //Retrieve the values
        // load tasks from preference
        ArrayList<NewsCard> arrL = new ArrayList<>();

        try {
            SharedPreferences prefs = context.getSharedPreferences("SimpleRSSFeeder_NewsList", Context.MODE_PRIVATE);
            String jsonString = prefs.getString("nwl" , "[{}]");
            arrL = new JsonHandler().get_JsonStringFormat_ToNewsCardArrayList(jsonString);

        } catch (Exception e) {
            Log.e("SP_error_read" , e.getMessage());
        }
        return arrL;
    }
}
