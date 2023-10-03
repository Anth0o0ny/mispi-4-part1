package com.example.jmx.beans;

import com.example.jmx.util.MBeanServer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@ManagedBean(name = "interval")
@ApplicationScoped
public class Interval implements IntervalMBean {

    private final String name = "interval";
    private final String nameOfObject = "MBeanServer:name=" + name;

    @ManagedProperty("#{mBeanServer}")
    private MBeanServer mBeanServer;

    private final AtomicLong totalInterval = new AtomicLong(0);
    private final AtomicLong totalHits = new AtomicLong(0);
    private String intervalCalculated;
    private double lastHitTime = 0;

    public Interval() {
    }

    @PostConstruct
    private void init() {
        mBeanServer.register(nameOfObject, this);
    }

    @PreDestroy
    private void destroy() {
        mBeanServer.unregister(nameOfObject);
    }

    public void increment(String date) {
        totalHits.incrementAndGet();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

        if (totalHits.get() == 1) {
            lastHitTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else {
            long currentTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long intervalInMillis = (long) (currentTime - lastHitTime);
            long intervalInSeconds = intervalInMillis / 1000;
            totalInterval.addAndGet(intervalInSeconds);
            lastHitTime = currentTime;
        }

        updateInterval();
    }

    public void clear() {
        totalInterval.set(0);
        totalHits.set(0);
        lastHitTime = 0;
        updateInterval();
    }

    @Override
    public String getTotalInterval() {
        return intervalCalculated;
    }

    public void updateInterval() {
        intervalCalculated = ((totalHits.get() > 0) ? (double) totalInterval.get() / totalHits.get() : 0) + "s";
    }

    public MBeanServer getmBeanServer() {
        return mBeanServer;
    }

    public void setmBeanServer(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }
}
