package com.example.ruhmatoo2;
//klass küsimuse, vastuste variantide ja õige vastusega (string kujul (mitte listi indeks), et saaks vastuste listi randomiga muuta)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Küsimus {
    private String küsimus;
    private List<String> vastused = new ArrayList<String>(); //vastusevariandid
    private String õigeVastus;

    //konstruktor uue küsimuse loomiseks (vastusevariantideta, et oleks võimalik lugeda sisse failist)
    public Küsimus(String küsimus) { //konstruktor failist lugemiseks
        this.küsimus = küsimus;
    }
    //konstruktor, kui vastused juba listina olemas
    public Küsimus(String küsimus, List<String> vastused) { //konstruktor ise lisamiseks
        this.küsimus = küsimus;
        this.vastused = vastused;
    }

    // "get"- ja "set"-meetodid
    public void määraÕigeVastus(String vastus) {
        this.õigeVastus = õigeVastus;
    }
    public void lisaVastus(String vastus) {
        vastused.add(vastus);
    }
    public List<String> getVastused() { return vastused; }
    public String getKüsimus() { return küsimus; }
}
