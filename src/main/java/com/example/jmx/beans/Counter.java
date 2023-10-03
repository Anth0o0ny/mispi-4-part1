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
    private final int MULTIPLIER = 5;
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

    public void increment(boolean isSuccess) {
        totalHits.incrementAndGet();
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
        long currentTotal = totalHits.get();
        if (currentTotal % MULTIPLIER == 0) {
            Notification notification = new Notification("Кратность", this, sequenceNumber++, "Кратно " + MULTIPLIER);
            notification.setUserData(currentTotal);
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

