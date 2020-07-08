package co.desofsi.souriapp.models;

import java.io.Serializable;

public class AppointmentDescription implements Serializable {
    int id;
    String color, reason, observation, date, start, end, status, specialty, name_p, last_name_p, ci, name_d, last_name_d;

    public AppointmentDescription() {

    }

    public AppointmentDescription(int id, String color, String reason, String observation, String date, String start, String end, String status, String specialty, String name_p, String last_name_p, String ci, String name_d, String last_name_d) {
        this.id = id;
        this.color = color;
        this.reason = reason;
        this.observation = observation;
        this.date = date;
        this.start = start;
        this.end = end;
        this.status = status;
        this.specialty = specialty;
        this.name_p = name_p;
        this.last_name_p = last_name_p;
        this.ci = ci;
        this.name_d = name_d;
        this.last_name_d = last_name_d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getName_p() {
        return name_p;
    }

    public void setName_p(String name_p) {
        this.name_p = name_p;
    }

    public String getLast_name_p() {
        return last_name_p;
    }

    public void setLast_name_p(String last_name_p) {
        this.last_name_p = last_name_p;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getName_d() {
        return name_d;
    }

    public void setName_d(String name_d) {
        this.name_d = name_d;
    }

    public String getLast_name_d() {
        return last_name_d;
    }

    public void setLast_name_d(String last_name_d) {
        this.last_name_d = last_name_d;
    }
}
