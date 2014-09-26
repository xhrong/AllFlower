package com.xhr.AllFlower.model;

import java.io.Serializable;

/**
 * Created by xhrong on 2014/9/22.
 */
public class FlowerInfo implements Serializable{

    private String title;
    private String thumbnailUrl;
    private String description;
    private String story;


    public FlowerInfo(){

    }

    public FlowerInfo(String title,String thumbnailUrl,String description,String story){
        this.title=title;
        this.thumbnailUrl=thumbnailUrl;
        this.description=description;
        this.story=story;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
