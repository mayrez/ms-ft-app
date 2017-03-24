package com.example.may.msocial.models;

import java.io.Serializable;

/**
 * Created by May on 17/04/2015.
 */
public class MsRow implements Serializable{
    private String title;
    private String subtitle;
    private int nqueries;
    private boolean checked;
    private String url;

    public MsRow(){

    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;

    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getNqueries() {
        return nqueries;
    }

    public void setNqueries(int nqueries) {
        this.nqueries = nqueries;
    }

}
