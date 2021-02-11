package co.desofsi.cursosutc.models;

public class File {
    int id;
    String name, filename,url_file;

    public File(int id, String name, String filename, String url_file) {
        this.id = id;
        this.name = name;
        this.filename = filename;
        this.url_file = url_file;
    }

    public File() {
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl_file() {
        return url_file;
    }

    public void setUrl_file(String url_file) {
        this.url_file = url_file;
    }
}
