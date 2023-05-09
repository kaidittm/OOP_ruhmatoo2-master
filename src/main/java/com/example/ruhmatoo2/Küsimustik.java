package com.example.ruhmatoo2;

//Klass küsimustik, kuhu on koondatud kokku kõik ühe küsimustiku alla käivad küsimused (klassidena)
//Küsimustik loetakse sisse failist, formaat on kirjeldatud all pool
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class Küsimustik {
    private List<Küsimus> küsimused = new ArrayList<Küsimus>();
    private String küsimustikuNimi; //nime annab kasutaja
    private String küsimustikuFailiNimi; //faili nimi, kus küsimustik asub

    //konstruktor
    public Küsimustik(String küsimustikuNimi) {
        this.küsimustikuNimi = küsimustikuNimi;
    }

    //get-meetodid
    public String getKüsimustikuNimi() {
        return küsimustikuNimi;
    }
    public String getKüsimustikuFailiNimi() {
        return küsimustikuFailiNimi;
    }

    public List<Küsimus> getKüsimused() {
        return küsimused;
    }

    //koostaKüsimustik võtab argumendiks failinime, kus küsimustiku küsimused ja vastused asuvad ning lisab kõik küsimused listi
    public void koostaKüsimustik(String failiNimi) throws FileNotFoundException { //fail, kus on küsimused ja vastusevariandid, õige vastus peab olema esimene pärast küsimust
        try {
            küsimustikuFailiNimi = failiNimi;
            File fail = new File("src/" + failiNimi);
            Scanner skännitudFail = new Scanner(fail);
            boolean küsimus = true; //et hoida järge, kas failist loetav rida on küsimus või vastusevariant
            boolean õige = false; //et hoida järge, millal tuleb failis õige vastusevariant
            int indeks = küsimused.size() - 1;

            //loome iga küsimus-vastuste ploki kohta ühe objekti ja lisame objekti küsimustikku
            while (skännitudFail.hasNextLine()) {
                //formaat: küsimus ühel real, siis teadmata hulk vastuseid, siis tühi rida
                String rida = skännitudFail.nextLine();
                if (küsimus) {
                    Küsimus praeguneKüsimus = new Küsimus(rida);
                    küsimused.add(praeguneKüsimus);
                    indeks += 1;
                    küsimus = false;
                    õige = true;
                } else if (rida == "") {
                    küsimus = true;
                } else if (õige) {
                    küsimused.get(indeks).lisaVastus(rida);
                    küsimused.get(indeks).määraÕigeVastus(rida);
                    õige = false;
                } else {
                    küsimused.get(indeks).lisaVastus(rida);
                }
            }
            skännitudFail.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }
}