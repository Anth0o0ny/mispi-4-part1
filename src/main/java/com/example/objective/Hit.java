package com.example.objective;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Table(name = "hits")
@Entity
public class Hit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private String yLine;
    @Column
    private double r = 2d;
    @Column
    private long exTime;
    @Column
    private boolean success;
    @Column
    private String date;

    @Column
    private String successLine;

    public String getSuccessLine() {
        return successLine;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public String getyLine() {
        return yLine;
    }

    public void setyLine(String yLine) {
        this.yLine = yLine;
        setY(Double.MIN_VALUE);
    }
    public double getY() {
        return y;
    }

    public void setY(double y) {
        if (y == Double.MIN_VALUE) {
            try {
                this.y = Double.parseDouble(yLine);

            } catch (NumberFormatException ex) {
                this.y = Double.parseDouble(yLine.replaceAll(",", "."));
            }
        } else {
            this.y = y;
        }
    }

    public double getR() {
        return r;
    }
    public void setR(double r) {
        this.r = r/100;
    }

    public long getExTime() {
        return exTime;
    }

    public void setExTime(long exTime) {
        this.exTime = exTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        this.successLine = success ? "Hit" : "Miss";
    }

    public String getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = date.format(formatter);
    }
}
