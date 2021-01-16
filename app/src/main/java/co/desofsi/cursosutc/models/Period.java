package co.desofsi.cursosutc.models;

import java.io.Serializable;

public class Period implements Serializable {
    private int id;
    private String name,url_image,status,start_date, end_date, color;

    public Period() {
    }

    public Period(int id, String name, String url_image, String status, String start_date, String end_date, String color) {
        this.id = id;
        this.name = name;
        this.url_image = url_image;
        this.status = status;
        this.start_date = start_date;
        this.end_date = end_date;
        this.color = color;
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

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
