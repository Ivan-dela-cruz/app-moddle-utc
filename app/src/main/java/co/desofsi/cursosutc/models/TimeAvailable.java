package co.desofsi.cursosutc.models;

import java.io.Serializable;

public class TimeAvailable implements Serializable {
    private int id;
    private String end, start, date;

    public TimeAvailable() {

    }

    public TimeAvailable(int id, String end, String start, String date) {
        this.id = id;
        this.end = end;
        this.start = start;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
