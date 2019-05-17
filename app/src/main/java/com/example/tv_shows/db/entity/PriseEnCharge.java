package com.example.tv_shows.db.entity;

import android.arch.persistence.room.Ignore;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PriseEnCharge implements Comparable {

    private String id;
    private String date;
    private String date_peser;
    private String nombre;
    private String sorte;
    private String poids;
    private String qualite;
    private String prixKilo;
    private String prixFinale;
    private String reduction;
    private String remarques;
    private String fromagerieName;

    @Ignore
    public PriseEnCharge() {
    }

    public PriseEnCharge(String id, String date, String date_peser,
                         String nombre, String sorte, String poids,
                         String qualite, String prixKilo, String prixFinale,
                         String reduction, String remarques, String fromagerieName) {
        this.id = id;
        this.date =date;
        this.date_peser = date_peser;
        this.nombre = nombre;
        this.sorte = sorte;
        this.poids = poids;
        this.qualite = qualite;
        this.prixKilo = prixKilo;
        this.prixFinale = prixFinale;
        this.reduction = reduction;
        this.remarques = remarques;
        this.fromagerieName = fromagerieName;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_peser() {
        return date_peser;
    }

    public void setDate_peser(String date_peser) {
        this.date_peser = date_peser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSorte() {
        return sorte;
    }

    public void setSorte(String sorte) {
        this.sorte = sorte;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public String getPrixKilo() {
        return prixKilo;
    }

    public void setPrixKilo(String prixKilo) {
        this.prixKilo = prixKilo;
    }

    public String getPrixFinale() {
        return prixFinale;
    }

    public void setPrixFinale(String prixFinale) {
        this.prixFinale = prixFinale;
    }

    public String getReduction() {
        return reduction;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    @Exclude
    public String getFromagerieName() {
        return fromagerieName;
    }

    public void setFromagerieName(String fromagerieName) {
        this.fromagerieName = fromagerieName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof PriseEnCharge)) return false;
        PriseEnCharge o = (PriseEnCharge) obj;
        return (o.getId() == this.getId());
    }

    @Override
    public String toString() {
        return date;
        }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("date", date);
        result.put("date_peser", date_peser);
        result.put("nombre", nombre);
        result.put("sorte", sorte);
        result.put("poids", poids);
        result.put("qualite", qualite);
        result.put("prixKilo", prixKilo);
        result.put("prixFinale", prixFinale);
        result.put("reduction", reduction);
        result.put("remarques", remarques);

        return result;
    }

    @Override
    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }
}
