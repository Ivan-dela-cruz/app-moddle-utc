package co.desofsi.souriapp.models;

import java.io.Serializable;

public class Specialty implements Serializable {
    private int id;
    private String name,description,url_image,status,created_at,color;


    public Specialty(int id, String name, String description, String url_image, String status, String created_at, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url_image = url_image;
        this.status = status;
        this.created_at = created_at;
        this.color = color;
    }

    public Specialty() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
