package Home.Modules;

public class User {
    private int ID;
    private String name;
    private String email;
    private String country;
    private String picture;

    public User(int ID, String name, String email , String country, String picture){
        this.ID =ID;
        this.name=name;
        this.email=email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
