package com.example.tv_shows.db.entity;

import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Fromagerie implements Comparable {

    private String name;
    private String language;

    @Ignore
    public Fromagerie() {
    }

    public Fromagerie(@NonNull String name) {
        this.name = name;
    }

    @Exclude
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Fromagerie)) return false;
        Fromagerie o = (Fromagerie) obj;
        return o.getName().equals(this.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("language", language);

        return result;
    }
}
