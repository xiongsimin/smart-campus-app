package com.example.smartcampus.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class News implements Serializable {
    private String newsTitle;
    private String newsTime;
    private String href;

    public News() {
        super();
    }

    public News(String newsTitle, String newsTime, String href) {
        this.newsTitle = newsTitle;
        this.newsTime = newsTime;
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsTitle='" + newsTitle + '\'' +
                ", newsTime='" + newsTime + '\'' +
                ", href='" + href + '\'' +
                '}';
    }

}
