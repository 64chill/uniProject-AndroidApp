package com.example.unip_simplerssfeeder_app.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonHandler {
    public JsonHandler(){}

    public String get_NewsCardArrayList_ToJsonStringFormat(ArrayList<NewsCard> newsCardArrayList){
        Gson gson = new Gson();
        return gson.toJson(newsCardArrayList);
/*
        JSONArray json_array = new JSONArray();

        for (NewsCard card : newsCardArrayList) {
            JSONObject object = new JSONObject();
            try {
                object.put("title", card.getTitle());
                object.put("img" ,card.getImg());
                object.put("postLink" , card.getPostLink());

                json_array.put(object);
            }catch (Exception e) {
                Log.e("JSON_FORMATER" ,e.getMessage() );
            }
        }
        return json_array.toString();*/
    }

    public ArrayList<NewsCard> get_JsonStringFormat_ToNewsCardArrayList(String json_structure){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NewsCard>>(){}.getType();
        ArrayList<NewsCard> contactList = gson.fromJson(json_structure, type);
        return contactList;
    }
}
