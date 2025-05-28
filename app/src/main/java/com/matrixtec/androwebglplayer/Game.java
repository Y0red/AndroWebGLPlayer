package com.matrixtec.androwebglplayer;

public class Game {
    private String imageUrl;
    private String name;
    private String description;
    private String link;
    private String orientation;

    public Game(String imageUrl, String name, String description, String link, String orientation) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.link = link;
        this.orientation = orientation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
    public String getOrientation(){
        return orientation;
    }
}