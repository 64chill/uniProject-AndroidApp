package com.example.unip_simplerssfeeder_app.utils.xml_parser_classes;
/*
*   CODE TAKEN FROM https://www.androidauthority.com/simple-rss-reader-full-tutorial-733245/
*       THEN ADDAPTED TO SUIT THE NEED OF THIS APP
*
 */

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class RSSFeedParser {
    private InputStream inputStream;
    private ArrayList<RssFeedModel> rssFeedModelList;
    private int howManyItemsToParse;

    // _____________________________ RSSFeedParser _________________________________________________
    public RSSFeedParser(URL rss_url, int howManyItemsToParse){

        rssFeedModelList = new ArrayList<>();
        this.howManyItemsToParse = howManyItemsToParse;

        try{
            this.inputStream = rss_url.openConnection().getInputStream();
        }catch(Exception e){
            Log.e("ERRORR_SSFEEDER" , e.getMessage());
        }
    }

    // _____________________________ parseFeed _____________________________________________________
    public ArrayList<RssFeedModel> parseFeed() throws Exception {

        String  mTitle         = "";
        String  mLink          = "";
        String  mImgUrl        = "";
        boolean isItem         = false;
        int     num_tmp        = 0;
        int     itemParseCount = 0;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(this.inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                // stop the loop depending on howManyItemsToParse
                if (this.howManyItemsToParse <= itemParseCount ){
                        break;
                }
                int eventType = xmlPullParser.getEventType();
                // check if we do not need a tag---------------------------------------------------
                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        num_tmp = 0;
                        continue;
                    }
                }
                // ---------------------------------------------------------------------------------
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    mTitle = result;
                } else if (name.equalsIgnoreCase("link")) {
                    mLink = result;
                } else if (name.equalsIgnoreCase("media:content") ||
                        name.equalsIgnoreCase("media:thumbnail") ||
                        name.equalsIgnoreCase("thumbnail")) {

                    mImgUrl = xmlPullParser.getAttributeValue(null, "url");
                }
                if (mTitle != "" && mImgUrl != "" && mLink != "") {
                    if (isItem && num_tmp==0) {
                        num_tmp++; itemParseCount++;
                        RssFeedModel item = new RssFeedModel(mTitle, mImgUrl, mLink);
                        this.rssFeedModelList.add(item);
                        mTitle  = "";
                        mImgUrl = "";
                        mLink   = "";
                    }
                }
            } // while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) { end;
        } catch(Exception e){
            Log.e("ERRORR_SSFEEDER" , e.getMessage());
        }
        this.inputStream.close();
        return rssFeedModelList;
    }
}
