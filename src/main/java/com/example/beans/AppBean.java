package com.example.beans;

import com.example.database.HitService;
import com.example.objective.Hit;
import com.example.verify.Verifier;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ManagedBean(name="appBean")
@SessionScoped
public class AppBean implements Serializable {
    private final Verifier verifier = new Verifier();
    @ManagedProperty("#{hitService}")
    private HitService hitService;
    private Hit hit = new Hit();
    private List<Hit> hits;


    @PostConstruct
    public void init() {
        hits = hitService.getAll();
    }

    public void add() {
        long start = System.nanoTime();
        boolean success = verifier.verifyHit(hit.getX(), hit.getY(), hit.getR());
        hit.setSuccess(success);
        hit.setDate(LocalDateTime.now());
        hit.setExTime(System.nanoTime() - start);
        hitService.add(hit);
        hits.add(hit);
        hit = new Hit();
    }

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
    }

    public List<Hit> getHitList() {
        return hits;
    }

    public void setHitList(List<Hit> hits) {
        this.hits = hits;
    }

    public void clear() {
        hitService.clear();
        this.hits.clear();
    }

    public HitService getHitService() {
        return hitService;
    }

    public void setHitService(HitService hitService) {
        this.hitService = hitService;
    }
}

