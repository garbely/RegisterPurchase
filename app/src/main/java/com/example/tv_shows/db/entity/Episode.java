package com.example.tv_shows.db.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Episode implements Comparable {

    private String id;
    private String name;
    private String length;
    private String showName;

    public Episode() {
    }

    public Episode(String id, String name, String length, String showName) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.showName = showName;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Exclude
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    @Exclude
    public int getNumber() {
        return Integer.valueOf(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Episode)) return false;
        Episode o = (Episode) obj;
        return (o.getId() == this.getId());
    }

    @Override
    public String toString() {
        return id + ":     " + name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("length", length);

        return result;
    }

    @Override
    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }
}
