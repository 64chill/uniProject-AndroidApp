package com.example.unip_simplerssfeeder_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unip_simplerssfeeder_app.customAdapter.UrlListAdapter;
import com.example.unip_simplerssfeeder_app.utils.DatabaseHelperRSSUrl;

import java.util.ArrayList;

public class ViewRssUrlsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelperRSSUrl mDatabaseHelper;
    private ListView mListView;
    public static Context context;

    /*** onCreate *********************************************************************************/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rss_urls);

        findViewById(R.id.back_btn_view_rss_urls).setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelperRSSUrl(this);
        mDatabaseHelper.getURLs();

        mListView = this.findViewById(R.id.list_view_urls_fromdb);
        context = getApplicationContext();
        showListViewUrls();
    }
    /*** onClick **********************************************************************************/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn_view_rss_urls) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
    /*** showListViewUrls *************************************************************************/
    private void showListViewUrls(){
        final Context context = ViewRssUrlsActivity.this;
        ArrayList<String> tmpList = new ArrayList<>();
        Cursor mCoursor = mDatabaseHelper.getURLs();

        while(mCoursor.moveToNext()){
            tmpList.add(mCoursor.getString(0));
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new UrlListAdapter(context,tmpList );
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i , long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                dialogRemove(context, name);
            }
        });

    }

    /*** DialogRemove *****************************************************************************/
    public void dialogRemove(final Context context, final String recordName){
        recordName.trim();
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
        View v1 = getLayoutInflater().inflate(R.layout.dialog_remove_url, null);
        final Button btnCancel       = v1.findViewById(R.id.button_dialog_remove_url_cancel);
        final Button btnRemove       = v1.findViewById(R.id.button_dialog_remove_url_remove);
        final TextView urlTextHolder = v1.findViewById(R.id.dialog_remove_url_urlText);
        urlTextHolder.setText(recordName);

        myBuilder.setView(v1);
        final AlertDialog dialog= myBuilder.create();
        dialog.show();
        // remove listener
        btnRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteURL(recordName);
                showListViewUrls();  // update our view that record is deleted from the databse
                Toast.makeText(context, "Removed." , Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        // cancel listener
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}
