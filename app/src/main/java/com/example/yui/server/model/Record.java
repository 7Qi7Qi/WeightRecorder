package com.example.yui.server.model;

import java.sql.Timestamp;

public class Record {

    private Integer id;

    private Double weight;

    private Timestamp dateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDateTime() {
        return this.dateTime.toString();
    }

    public Timestamp dateTime() {
        return this.dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public Record(Double weight, Timestamp dateTime) {
        this.weight = weight;
        this.dateTime = dateTime;
    }

    public Record(Integer id, Double weight, String dateTime) {
        this.id = id;
        this.weight = weight;
        this.dateTime = Timestamp.valueOf(dateTime);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", weight=" + weight +
                ", dateTime=" + dateTime +
                '}';
    }
}
