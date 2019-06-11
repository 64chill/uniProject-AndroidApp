package com.example.unip_simplerssfeeder_app.customAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unip_simplerssfeeder_app.R;
import com.example.unip_simplerssfeeder_app.utils.NewsCard;

import java.io.InputStream;
import java.util.List;

public class NewsListAdapter extends ArrayAdapter<NewsCard> {

    public NewsListAdapter(Context context, List<NewsCard> newsCards) {
        super(context, R.layout.list_view_row_news ,newsCards);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // default -  return super.getView(position, convertView, parent);
        // add the layout
        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View customView = myCustomInflater.inflate(R.layout.list_view_row_news, parent, false);
        // get references.
        NewsCard item = getItem(position);

        TextView itemTitle = (TextView) customView.findViewById(R.id.list_view_row_news_title);
        TextView itemUrl = (TextView) customView.findViewById(R.id.list_view_row_news_postLink);
        ImageView imageview = (ImageView) customView.findViewById(R.id.list_view_row_news_image);


            // dynamically update the text from the array
            itemTitle.setText(item.getTitle());
            itemUrl.setText(item.getPostLink());
            String imageUrl = item.getImg();
            // if not empty string, if empty = there is no image to show
            if(imageUrl != "") {new DownloadImageTask(imageview).execute(imageUrl);}

            //return our custom View or custom item

        return customView;
    }

    // we need this class in order to pass the url of image and to download and show image to the user
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", ""+e.getMessage());
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

