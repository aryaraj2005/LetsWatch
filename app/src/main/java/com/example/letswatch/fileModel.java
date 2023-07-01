package com.example.letswatch;

public class fileModel {
    String title, url, cuimgurl, cuid;

    public fileModel() {
    }

    public fileModel(String title, String url, String cuimgurl, String cuid) {
        this.title = title;
        this.url = url;
        this.cuimgurl = cuimgurl;
        this.cuid = cuid;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCuimgurl() {
        return cuimgurl;
    }

    public void setCuimgurl(String cuimgurl) {
        this.cuimgurl = cuimgurl;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }
}