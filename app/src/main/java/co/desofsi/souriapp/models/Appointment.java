package co.desofsi.souriapp.models;

import java.io.Serializable;

public class Appointment implements Serializable {
    private int id, id_specialty, id_doctor, id_patient;
    private String observation, reason, date, start, end, color;

    public Appointment(int id, int id_specialty, int id_doctor, int id_patient, String observation, String reason, String date, String start, String end, String color) {
        this.id = id;
        this.id_specialty = id_specialty;
        this.id_doctor = id_doctor;
        this.id_patient = id_patient;
        this.observation = observation;
        this.reason = reason;
        this.date = date;
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public Appointment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_specialty() {
        return id_specialty;
    }

    public void setId_specialty(int id_specialty) {
        this.id_specialty = id_specialty;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public int getId_patient() {
        return id_patient;
    }

    public void setId_patient(int id_patient) {
        this.id_patient = id_patient;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
