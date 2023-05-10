package com.example.ruhmatoo2;
//Kasutaja klass hoiab endas infot kindla kasutaja nime, parool ja punktiskooride kohta
public class Kasutaja {
    private String kasutajanimi;
    private String parool;

    //konstruktor kasutajanime ja parooliga uue kasutaja loomiseks
    public Kasutaja(String kasutajanimi, String parool) {
        this.kasutajanimi = kasutajanimi;
        this.parool = parool;
    }

    //get- ja set-meetodid info kättesaamiseks ja -muutmiseks
    public String getKasutajanimi() {
        return kasutajanimi;
    }
    public String getParool() {
        return parool;
    }

    @Override
    public String toString() {
        return kasutajanimi;
    }
}