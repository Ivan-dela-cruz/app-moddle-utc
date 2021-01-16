package co.desofsi.cursosutc.models;

public class Level {
    int student_id, level_id;
    String name, description, urlImage;

    public Level(int student_id, int level_id, String name, String description, String urlImage) {
        this.student_id = student_id;
        this.level_id = level_id;
        this.name = name;
        this.description = description;
        this.urlImage = urlImage;
    }

    public Level() {
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
