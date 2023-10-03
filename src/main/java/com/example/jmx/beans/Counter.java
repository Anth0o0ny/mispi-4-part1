package com.example.jmx.beans;

import com.example.jmx.util.MBeanServer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.management.*;
import java.util.concurrent.atomic.AtomicLong;

@ManagedBean(name = "counter")
@ApplicationScoped
public class Counter extends NotificationBroadcasterSupport implements CounterMBean {

    private final String name = "counter";
    private final String nameOfObject = "MBeanServer:name=" + name;
    @ManagedProperty("#{mBeanServer}")
    private MBeanServer mBeanServer;
    private final double R_1 = 2.0;
    private final double R_2 = 2.75;
    private final double MAX_COORDINATE_FOR_R2 = 4.225 ;
    private final double MAX_COORDINATE_FOR_R1 = 3.065;

    private double xValue;
    private double yValue;
    private double rValue;

    private long sequenceNumber = 0;
    private final AtomicLong totalHits = new AtomicLong(0);
    private final AtomicLong successfulHits = new AtomicLong(0);

    public Counter() {
    }

    @PostConstruct
    private void init() {
        mBeanServer.register(nameOfObject, this);
    }

    @PreDestroy
    private void destroy() {
        mBeanServer.unregister(nameOfObject);
    }

    public void increment(boolean isSuccess, double xValue, double yValue, double rValue) {
        totalHits.incrementAndGet();
        this.xValue = xValue;
        this.yValue = yValue;
        this.rValue = rValue;
        if (isSuccess) {
            successfulHits.incrementAndGet();
        }
        checkAndNotify();
    }

    public void clear() {
        totalHits.set(0);
        successfulHits.set(0);
        checkAndNotify();
    }

    private void checkAndNotify() {
        if (((rValue % R_1 == 0) && ((xValue > MAX_COORDINATE_FOR_R1) || ( yValue > MAX_COORDINATE_FOR_R1))) ||
                ((rValue % R_2 == 0) && ((xValue > MAX_COORDINATE_FOR_R2) || ( yValue > MAX_COORDINATE_FOR_R2))) ){
            Notification notification = new Notification("Недопустимые координаты", this, ++sequenceNumber, "Координаты (" + xValue + ", " + yValue + ") за пределами отображаемой области");
            sendNotification(notification);
        }
    }

    @Override
    public long getSuccessfulHits() {
        return successfulHits.get();
    }

    @Override
    public long getTotalHits() {
        return totalHits.get();
    }

    public MBeanServer getmBeanServer() {
        return mBeanServer;
    }

    public void setmBeanServer(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }
}

