package co.desofsi.cursosutc.models;

public class Doctor {
    private int id;
    private String name,last_name,academy_title,biography,url_image;

    public Doctor() {

    }

    public Doctor(int id, String name, String last_name, String academy_title, String biography, String url_image) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.academy_title = academy_title;
        this.biography = biography;
        this.url_image = url_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAcademy_title() {
        return academy_title;
    }

    public void setAcademy_title(String academy_title) {
        this.academy_title = academy_title;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
