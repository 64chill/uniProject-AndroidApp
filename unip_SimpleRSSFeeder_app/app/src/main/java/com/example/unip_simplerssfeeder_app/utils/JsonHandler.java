package com.example.unip_simplerssfeeder_app.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class JsonHandler {
    public JsonHandler(){}

    public String get_NewsCardArrayList_ToJsonStringFormat(ArrayList<NewsCard> newsCardArrayList){
        Gson gson = new Gson();
        return gson.toJson(newsCardArrayList);
    }

    public ArrayList<NewsCard> get_JsonStringFormat_ToNewsCardArrayList(String json_structure){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NewsCard>>(){}.getType();
        ArrayList<NewsCard> contactList = gson.fromJson(json_structure, type);
        return contactList;
    }
}
