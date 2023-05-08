package com.example.treeningpaevik.andmebaas;

import java.util.ArrayList;
import java.util.List;

// Hoiab JSONi andmebaasist saadud trennide infot
// Otse JSONi pealt ei ole kõige parem andmetega töötada, seega on selleks tehtud see klass, kuhu info veidike lihtsamaks kasutamiseks panna saame.
public class Trenn {
    private String id;
    private String kestvus;
    private String nimi;
    private List<Andmeväli> peamisedVäljad;

    public Trenn(String id, String kestvus, String nimi) {
        this.id = id;
        this.kestvus = kestvus;
        this.nimi = nimi;
        this.peamisedVäljad = new ArrayList<>();
    }

    public String toString() {
        return "[ID: " + this.id + "] " + this.nimi + " - " + this.kestvus;
    }

    public String getId() {
        return id;
    }

    public String getKestvus() {
        return kestvus;
    }

    public void setKestvus(String kestvus) {
        this.kestvus = kestvus;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public List<Andmeväli> getPeamisedVäljad() {
        return peamisedVäljad;
    }

    public void setPeamisedVäljad(List<Andmeväli> peamisedVäljad) {
        this.peamisedVäljad = peamisedVäljad;
    }

    public Andmeväli leiaVäliIDga(String id) {
        for(Andmeväli väli : this.peamisedVäljad) {
            if(väli.getNimi() == id)
                return väli;
        }

        return null;
    }
}
