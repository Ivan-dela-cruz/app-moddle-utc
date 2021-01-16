package co.desofsi.cursosutc.models;

public class Course {
    String title,name,last_name,description,url_image;
    int teacher_id,subject_id;

    public Course(String title, String name, String last_name, String description, String url_image, int teacher_id, int subject_id) {
        this.title = title;
        this.name = name;
        this.last_name = last_name;
        this.description = description;
        this.url_image = url_image;
        this.teacher_id = teacher_id;
        this.subject_id = subject_id;
    }

    public Course() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }
}
