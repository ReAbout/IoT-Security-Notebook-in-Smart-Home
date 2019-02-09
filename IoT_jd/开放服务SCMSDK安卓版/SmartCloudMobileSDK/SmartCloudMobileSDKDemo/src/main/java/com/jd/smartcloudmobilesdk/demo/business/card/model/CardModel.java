package com.jd.smartcloudmobilesdk.demo.business.card.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2018/5/2.
 */
public class CardModel implements Serializable {

    private int device_total;
    private int scene_total;
    private String cache_time;
    private String bg_img_url;
    private List<CardCell> cards;

    public int getDevice_total() {
        return device_total;
    }

    public void setDevice_total(int device_total) {
        this.device_total = device_total;
    }

    public int getScene_total() {
        return scene_total;
    }

    public void setScene_total(int scene_total) {
        this.scene_total = scene_total;
    }

    public String getCache_time() {
        return cache_time;
    }

    public void setCache_time(String cache_time) {
        this.cache_time = cache_time;
    }

    public String getBg_img_url() {
        return bg_img_url;
    }

    public void setBg_img_url(String bg_img_url) {
        this.bg_img_url = bg_img_url;
    }

    public List<CardCell> getCards() {
        return cards;
    }

    public void setCards(List<CardCell> cards) {
        this.cards = cards;
    }
}
