package com.skrb7f16.trashtocash.models;

import java.util.List;

public class Feeds {
    String id;
    String title;
    String city;
    String address;
    String desc;
    String type;
    String by;
    String byId;
    String at;
    Boolean donate;
    int price;
    List<String> pics;
    Boolean taken;



    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getById() {
        return byId;
    }

    public void setById(String byId) {
        this.byId = byId;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getDonate() {
        return donate;
    }

    public void setDonate(Boolean donate) {
        this.donate = donate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }


    public Feeds(String id, String name, String city, String address, String desc, String type, String by, Boolean donate, int price, List<String> pics) {
        this.id = id;
        this.title = name;
        this.city = city;
        this.address = address;
        this.desc = desc;
        this.type = type;
        this.by = by;
        this.donate = donate;
        this.price = price;
        this.pics = pics;

    }

    public Feeds() {
        this.taken=false;

    }
}
