package com.example.ruhmatoo2;

//hoiab endas ühte mängu, alustab mängu, suhtleb kasutajaga
import java.io.*;
import java.util.ArrayList;;
import java.util.List;
import java.util.Scanner;

public class Mäng {
    private List<Kasutaja> kasutajad;
    private List<Küsimustik> küsimustikud;
    private Kasutaja praeguneKasutaja; //kasutaja, kes hetkel sisse on logitud


    //kasutaja lisamine mängu ja logifaili (formaat: username;password;score)
    public void lisaKasutaja(Kasutaja kasutaja) {
        kasutajad.add(kasutaja);
        try {
            FileWriter kirjutaja = new FileWriter("src/kasutajad.txt", true);
            kirjutaja.write(kasutaja.getKasutajanimi() + ";" + kasutaja.getParool());
            kirjutaja.write("\r\n"); //reavahetus
            kirjutaja.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //küsimustiku lisamine mängu ja logifaili (formaat: nimi;failiNimi)
    public void lisaKüsimustik(Küsimustik küsimustik) {
        küsimustikud.add(küsimustik);
        try {
            FileWriter kirjutaja = new FileWriter("src/küsimustikud.txt");
            kirjutaja.write(küsimustik.getKüsimustikuNimi() + ";" + küsimustik.getKüsimustikuFailiNimi());
            kirjutaja.write("\r\n"); //reavahetus
            kirjutaja.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //võimaldab kasutajal oma olemasolevasse kasutajasse sisse logida
    public void logiSisse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Kasutajanimi: ");
        String kasutajaVastus = scanner.nextLine();
        if (leiaKasutaja(kasutajaVastus)!=null) {
            Kasutaja kasutaja = leiaKasutaja(kasutajaVastus);

            //kui kasutaja on olemas, siis küsib parooli (kuni sisestatakse õige parool)
            boolean õigeParool = false;
            while (!õigeParool) {
            Scanner parool = new Scanner(System.in);
            System.out.print("Parool: ");
            String kasutajaParool = parool.nextLine();
                if (kasutajaParool.equals(kasutaja.getParool())) {
                    System.out.println("Sisse logitud!");
                    praeguneKasutaja = kasutaja; //logib sisse
                    õigeParool = true;
                } else {
                    System.out.println("Vale parool!");
                }
            }
        }

    }

    //loob kasutajalt küsitud andmete abil uue kasutaja
    public Kasutaja looKasutaja() {
        Scanner kasutajaloomine = new Scanner(System.in);
        System.out.print("Kasutajanimi: ");
        String kasutajanimi = kasutajaloomine.nextLine();
        System.out.print("Parool: ");
        String parool = kasutajaloomine.nextLine();
        Kasutaja kasutaja = new Kasutaja(kasutajanimi, parool);
        praeguneKasutaja = kasutaja;
        lisaKasutaja(kasutaja);
        System.out.println("Kasutaja loodud! Naudi!");
        return kasutaja;
    }

    //tagastab kasutaja (objektina) otsides kasutajanime alusel kasutajate hulgast
    public Kasutaja leiaKasutaja(String kasutajanimi) {
        for (Kasutaja kasutaja : kasutajad) {
            if (kasutaja.getKasutajanimi().equals(kasutajanimi)) {
                return kasutaja;
            }
        }
        return null;
    }

    //tagastab küsimustiku (objektina) otsides küsimustiku nime alusel
    public Küsimustik leiaKüsimustik(String otsiKüsimustik) {
        for (Küsimustik küsimustik : küsimustikud) {
            if (küsimustik.getKüsimustikuNimi().equals(otsiKüsimustik)) {
                return küsimustik;
            }
        }
        return null;
    }
}
