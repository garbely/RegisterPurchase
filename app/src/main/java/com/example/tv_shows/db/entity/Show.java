package com.example.tv_shows.db.entity;

import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Show implements Comparable {

    private String name;
    private String description;

    @Ignore
    public Show() {
    }

    public Show(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Exclude
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Show)) return false;
        Show o = (Show) obj;
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
        result.put("description", description);

        return result;
    }
}
