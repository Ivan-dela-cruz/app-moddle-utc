package co.desofsi.souriapp.models;

import java.io.Serializable;

public class DetailPayment implements Serializable {
    private int id, dues ,is_check;
    private String description,date_pay,status;

    private double total;

    public DetailPayment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDues() {
        return dues;
    }

    public void setDues(int dues) {
        this.dues = dues;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_pay() {
        return date_pay;
    }

    public void setDate_pay(String date_pay) {
        this.date_pay = date_pay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
