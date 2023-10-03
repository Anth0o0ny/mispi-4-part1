package com.example.jmx.util;

import com.example.jmx.beans.Counter;
import com.example.jmx.beans.Interval;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean(name = "notification")
@ApplicationScoped
public class Notification {

    @ManagedProperty("#{counter}")
    Counter counter;

    @ManagedProperty("#{interval}")
    Interval interval;

    public Notification() {
    }

    public void notifyAdding(boolean isSuccess, String date,double x, double y, double r) {
        counter.increment(isSuccess, x, y, r);
        interval.increment(date);
    }

    public void notifyClearing() {
        counter.clear();
        interval.clear();
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }


    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval intervalBean) {
        this.interval = intervalBean;
    }
}
