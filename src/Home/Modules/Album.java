package Home.Modules;

import java.sql.Date;

public class Album {
    int ID;
    String name;
    String artist;
    String category;
    Date releaseDate;
    String picture;

    public Album(int ID, String name, String artist, String category, Date releaseDate, String picture) {
        this.ID = ID;
        this.name = name;
        this.artist = artist;
        this.category = category;
        this.releaseDate = releaseDate;
        this.picture = picture;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }



}
