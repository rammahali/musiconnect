package Home.Modules;

public class Artist {
    private int ID;
    private String name;
    private String country;
    private String picture;

    public Artist(int ID, String name, String country, String picture){
        this.ID =ID;
        this.name=name;

        this.country=country;
        this.picture=picture;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
