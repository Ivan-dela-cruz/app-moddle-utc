package co.desofsi.souriapp.models;

public class User {
    private int id;
    private String name,last_name,status,ci;

    public User(int id, String name, String last_name, String status, String ci) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.status = status;
        this.ci = ci;
    }

    public User() {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
}
