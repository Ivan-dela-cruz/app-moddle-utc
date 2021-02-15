package co.desofsi.cursosutc.models;

public class Education {
    int id,academic_period_id,level_id,subject_id,user_id,status;
    String name,description,career,url_image,content,created_at;

    public Education(int id, int academic_period_id, int level_id, int subject_id, int user_id, int status, String name, String description, String career, String url_image, String content, String created_at) {
        this.id = id;
        this.academic_period_id = academic_period_id;
        this.level_id = level_id;
        this.subject_id = subject_id;
        this.user_id = user_id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.career = career;
        this.url_image = url_image;
        this.content = content;
        this.created_at = created_at;
    }

    public Education() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAcademic_period_id() {
        return academic_period_id;
    }

    public void setAcademic_period_id(int academic_period_id) {
        this.academic_period_id = academic_period_id;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
