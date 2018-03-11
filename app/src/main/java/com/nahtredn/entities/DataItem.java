package com.nahtredn.entities;

/**
 * Created by Me on 10/03/2018.
 */

public class DataItem {

    private String title;
    private String description;

    public DataItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
