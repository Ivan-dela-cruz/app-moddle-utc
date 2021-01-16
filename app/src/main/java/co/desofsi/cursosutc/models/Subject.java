package co.desofsi.cursosutc.models;

public class Subject {
    int period_id, student_id,subject_id;
    String name, description, urlImage;

    public Subject(int period_id, int student_id, int subject_id, String name, String description, String urlImage) {
        this.period_id = period_id;
        this.student_id = student_id;
        this.subject_id = subject_id;
        this.name = name;
        this.description = description;
        this.urlImage = urlImage;
    }

    public Subject() {
    }

    public int getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(int period_id) {
        this.period_id = period_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
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
