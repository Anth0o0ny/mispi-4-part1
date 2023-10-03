package com.example.database;

import com.example.jmx.util.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.example.objective.Hit;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "hitService")
@ApplicationScoped

public class HitService implements Serializable, HitDao {

    private final SessionFactory manager = ConnectionManager.getSessionFactory();

    @ManagedProperty("#{notification}")
    private Notification notification;

    public HitService() {
    }

    @Override
    public void add(Hit hit) {
        Session session = manager.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(hit);
            transaction.commit();
            String formattedDate = hit.getDate();
            notification.notifyAdding(hit.isSuccess(), formattedDate);
        }
        catch (Exception e){
            if (transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK || transaction.isActive())
                transaction.rollback();
        }
    }

    @Override
    public List<Hit> getAll() {
        Session currentSession = manager.getCurrentSession();
        currentSession.beginTransaction();
        List<Hit> listAnswer = currentSession.createQuery( "FROM Hit ").list();
        currentSession.getTransaction().commit();
        for (Hit hit : listAnswer) {
            String formattedDate = hit.getDate();
            notification.notifyAdding(hit.isSuccess(), formattedDate);
        }
        return listAnswer;
    }

    @Override
    public void clear() {
        Session currentSession = manager.getCurrentSession();
        currentSession.beginTransaction();
        currentSession.createQuery("delete from Hit").executeUpdate();
        currentSession.getTransaction().commit();
        notification.notifyClearing();
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}