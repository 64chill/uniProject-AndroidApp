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

    public RSSFeedParser(URL rss_url, int howManyItemsToParse){

        rssFeedModelList = new ArrayList<>();
        this.howManyItemsToParse = howManyItemsToParse;

        try{
            this.inputStream = rss_url.openConnection().getInputStream();
        }catch(Exception e){
            Log.e("ERRORR_SSFEEDER" , e.getMessage());
        }


    }

    public ArrayList<RssFeedModel> parseFeed() throws Exception {

        String mTitle = "";
        String mLink = "";
        String mImgUrl = "";
        boolean isItem = false;
        int num_tmp = 0;
        int itemParseCount = 0;

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
                // proveri da li nam tag ne treba---------------------------------------------------
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
                        mTitle = "";
                        mImgUrl = "";
                        mLink = "";
                    }
                }
            } // while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) { end;
        } catch(Exception e){
            Log.e("ERRORR_SSFEEDER" , e.getMessage());

        }
        this.inputStream.close();
        /*ArrayList<NewsCard> ncList = new ArrayList<>();
        for (RssFeedModel model : this.rssFeedModelList) {
            ncList.add(model.getNewsCardObject());
        }
        //remove duplicates
        Set<NewsCard> set = new HashSet<>(ncList);
        ncList.clear();
        ncList.addAll(set);*/
        //
        return rssFeedModelList;
    }



}
/*import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class RSSFeedParser {
    //TODO add comments
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    static final String IMG_URL1 = "content";
    static final String IMG_URL2 = "thumbnail";
    private Integer numberOfItemsToShow;

    final URL url;

    public RSSFeedParser(String feedUrl, Integer numberOfItemsToShow) {
        this.numberOfItemsToShow = numberOfItemsToShow + 1;
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Feed readFeed() {
        Feed feed = null;

        Integer countItemsToShow = 0;
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";
            String img_url = "";


            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                if(countItemsToShow == numberOfItemsToShow) {break;}
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    switch (localPart) {
                        case ITEM:
                            countItemsToShow++;
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language,
                                        copyright, pubdate);
                            }
                            event = eventReader.nextEvent();
                            break;
                        case TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                        case LINK:
                            link = getCharacterData(event, eventReader);
                            break;
                        case GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case LANGUAGE:
                            language = getCharacterData(event, eventReader);
                            break;
                        case AUTHOR:
                            author = getCharacterData(event, eventReader);
                            break;
                        case PUB_DATE:
                            pubdate = getCharacterData(event, eventReader);
                            break;
                        case COPYRIGHT:
                            copyright = getCharacterData(event, eventReader);
                            break;
                        case IMG_URL1:
                        case IMG_URL2:
                            Iterator<Attribute> iterator = event.asStartElement().getAttributes();
                            while (iterator.hasNext())
                            {
                                Attribute attribute = iterator.next();
                                String value = attribute.getValue();
                                String name = attribute.getName().toString();
                                if(name == "url")
                                    img_url = value;
                            }
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                        FeedMessage message = new FeedMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setImgUrl(img_url);
                        message.setTitle(title);
                        feed.getMessages().add(message);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";

        event = eventReader.nextEvent();

        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
*/