package com.example.unip_simplerssfeeder_app.fragments;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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


public class UniqueNewsFragment extends Fragment  implements View.OnClickListener{
    private View view;
    private Spinner inputUniqueNews;
    private Spinner newsNum;
    private DatabaseHelperRSSUrl mDatabaseHelper;
    private ListView uniqueLVistView;
    private Button buttonGrabNews;

    private ArrayList<NewsCard> newsCardsArray;
    private NewsListAdapter customAdapter;

    // _____________________________ UniqueNewsFragment ____________________________________________
    public UniqueNewsFragment() {
        // Required empty public constructor
        newsCardsArray = new ArrayList<>();
    }

    // _____________________________ onActivityCreated _____________________________________________
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //load fragment state
        if (savedInstanceState != null) {
            String jsonString= savedInstanceState.getString("STATE_LIST_VIEW");
            newsCardsArray = new JsonHandler().get_JsonStringFormat_ToNewsCardArrayList(jsonString);

            Collections.shuffle(newsCardsArray); // randomize news
            customAdapter = new NewsListAdapter(getActivity(), newsCardsArray);
            uniqueLVistView.setAdapter(customAdapter);
        }
    }

    // _____________________________ onSaveInstanceState ___________________________________________
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save fragment state
        outState.putString("STATE_LIST_VIEW", new JsonHandler().get_NewsCardArrayList_ToJsonStringFormat(newsCardsArray));
        super.onSaveInstanceState(outState);
    }

    // _____________________________ onCreateView __________________________________________________
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_unique_news, container, false);
        initComponents();

        return view;
    }

    // _____________________________ initComponents ________________________________________________
    public void initComponents(){
        mDatabaseHelper      = new DatabaseHelperRSSUrl(getActivity()); //set database helper
        this.inputUniqueNews = (Spinner) view.findViewById(R.id.spinner_url_to_fetch);
        this.newsNum         = (Spinner) view.findViewById(R.id.spinner_number_of_news_to_show);

        ArrayList<String> inputUniqueNewsList = new ArrayList<>();
        ArrayList<String> newsNumList         = new ArrayList<>();

        newsNumList.add("5");
        newsNumList.add("6");
        newsNumList.add("7");
        newsNumList.add("8");
        newsNumList.add("9");
        newsNumList.add("10");

        Cursor mCoursor = mDatabaseHelper.getURLs();

        while(mCoursor.moveToNext()){
            inputUniqueNewsList.add(mCoursor.getString(0));
        }
        try{
            inputUniqueNewsList.get(0);
            ArrayAdapter<String> adapterUniqueNews = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, inputUniqueNewsList);
            this.inputUniqueNews.setAdapter(adapterUniqueNews);

        } catch (Exception e){
            Toast.makeText(view.getContext(), "You can't do that!, please insert at least one URL", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter<String> adapterNewsNums   = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, newsNumList);
        this.newsNum.setAdapter(adapterNewsNums);

        this.uniqueLVistView    = (ListView) view.findViewById(R.id.list_view_unique_news);
        this.buttonGrabNews     = (Button) view.findViewById(R.id.button_grab_unique_news);

        buttonGrabNews.setOnClickListener(this);
        this.uniqueLVistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                NewsCard nc = (NewsCard) parent.getItemAtPosition(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nc.getPostLink()));
                startActivity(browserIntent);
            }
        });
    }// initComponents end;

    // _____________________________ onClick _______________________________________________________
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_grab_unique_news:
                String rss_url   = (String)((Spinner) view.findViewById(R.id.spinner_url_to_fetch)).getSelectedItem();
                String numToShow = (String)((Spinner) view.findViewById(R.id.spinner_number_of_news_to_show)).getSelectedItem();

                new grabAndShowUniqueNews(rss_url , Integer.parseInt(numToShow)).execute();
                break;
        }

    } // end onClick;


    //___________________________ grabAndShowNews __________________________________________________
    class grabAndShowUniqueNews extends AsyncTask<Void, Void, Void> {
        private String rss_url;
        private Integer numToShow;

        public grabAndShowUniqueNews(String rss_url, Integer numToShow){
            this.rss_url = rss_url;
            this.numToShow = numToShow;
            newsCardsArray.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
                try {
                    /*start ---------------------------------------------------------------------------*/
                    URL url = new URL(rss_url);
                    RSSFeedParser rfp = new RSSFeedParser(url, numToShow);
                    ArrayList<RssFeedModel> rfmList = rfp.parseFeed();
                    ArrayList<NewsCard> ncList = new ArrayList<>();
                    for (RssFeedModel model : rfmList) {
                        ncList.add(model.getNewsCardObject());
                    }
                    newsCardsArray.addAll(ncList);
                    /*end ---------------------------------------------------------------------------*/
                } catch (Exception e) {
                    Log.e("SimpleRSSFeeder", e.getMessage());
                }
            return null;
        } //doInBackGround end;
        @Override
        protected void onPostExecute(Void Void) {
            Collections.shuffle(newsCardsArray); // randomize news
            customAdapter = new NewsListAdapter(getActivity(), newsCardsArray);
            uniqueLVistView.setAdapter(customAdapter);
            return;
        } //onPostExecute end;
    } // class end;
}
