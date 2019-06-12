package com.example.unip_simplerssfeeder_app.utils.xml_parser_classes;

import com.example.unip_simplerssfeeder_app.utils.NewsCard;

public class RssFeedModel {
    private String title;
    private String imgUrl;
    private String link;
    // _____________________________ RssFeedModel __________________________________________________
    public RssFeedModel(String title, String imgUrl, String link) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.link = link;
    }

    /* *********************************************************************************************

    GETERS AND SETERS

    ********************************************************************************************* */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public NewsCard getNewsCardObject(){
        return new NewsCard(this.title, this.imgUrl, this.link);
    }

}
