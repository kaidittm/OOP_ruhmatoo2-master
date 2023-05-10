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

    //väljastab küsimuse vastusevariandid suvalises järjekorras, vastasel juhul oleks õige vastus alati esimene :)
    public void väljastaVastuseVariandid() {
        int indeks = 1;
        Random suvaline = new Random();
        List<String> suvalineJärjekord = new ArrayList<>(vastused); //loon uue vastuste listi muutmiseks, et algne ei muutuks (vastuste kontrolliks)
        for (int i = vastused.size() - 1; i > 0; i--) { //tahan vastuseid segada, et alati poleks esimene variant õige (nagu etteantult peab olema)
            int suvalineIndeks = suvaline.nextInt(i + 1); // suvaline indeks [0, i]
            String vahetus = suvalineJärjekord.get(suvalineIndeks);
            suvalineJärjekord.set(suvalineIndeks, suvalineJärjekord.get(i));  // vahetab genetud ja i indeksid
            suvalineJärjekord.set(i, vahetus);}
        System.out.println("  Vastusevariandid:");
        for (String vastus: suvalineJärjekord) {
            System.out.println("    " + indeks + ". " + vastus);
            indeks++;
        }
    }

    //küsib kasutajalt vastust ja kontrollib selle õigsust
    public boolean kirjutaVastus(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Kirjuta vastus: "); //laseb kasutajal kooloni järele vastuse kirjutada (tänu järgmisele reale)
        String kasutajaVastus = scanner.nextLine(); //https://www.w3schools.com/java/java_user_input.asp
        if (kasutajaVastus.equalsIgnoreCase(vastused.get(0))){
            System.out.println("Õige vastus!"); // pm saaks siia punktiskoori ka tekitada
            return true;
        }
        else {
            System.out.println("Vale vastus. Õige vastus: " + vastused.get(0));
            return false;
        }
    }
}