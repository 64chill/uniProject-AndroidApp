package com.example.unip_simplerssfeeder_app.utils;

public class NewsCard {
    private String title;
    private String img;
    private String postLink;

    //constructor ----------------------------------------------------------------------------------
    public NewsCard(String title, String img, String link) {
        this.title = title;
        this.img = img; // url of the image
        this.postLink = link;
    }
    //getters and setters --------------------------------------------------------------------------
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String link) {
        this.postLink = link;
    }

}
