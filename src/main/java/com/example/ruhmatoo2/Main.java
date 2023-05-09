package com.example.ruhmatoo2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.shuffle;
import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_LEFT;

public class Main extends Application {

    @Override
    public void start(Stage peaLava) throws Exception {
        BorderPane piiriPaan = new BorderPane();
        mäng();

        avaleht(piiriPaan, peaLava);

        Scene stseen1 = new Scene(piiriPaan, 500, 400);

        peaLava.setTitle("Avaleht");
        peaLava.setScene(stseen1);
        peaLava.show();
    }

    public void avaleht(BorderPane piiriPaan, Stage peaLava) {
        VBox vBox = new VBox(); //paigutuse lihtsustamiseks

        Label kasutajanimi = new Label("Kasutajanimi: ");
        TextField kasutajanimiVäli = new TextField("Kasutajanimi");
        Label salasõna = new Label("Parool: ");
        PasswordField salasõnaVäli = new PasswordField();
        Label probleem = new Label("");

        HBox nupudHBox = new HBox();
        Button lisa = new Button("Registreeri");
        Button logi = new Button("Logi sisse");
        nupudHBox.getChildren().addAll(lisa,logi);

        lisa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                looKasutaja(kasutajanimiVäli.getCharacters().toString(), salasõnaVäli.getCharacters().toString());
            }
        });
        logi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    logiSisse(kasutajanimiVäli.getCharacters().toString(), salasõnaVäli.getCharacters().toString());
                    küsimustikuLeht(peaLava, piiriPaan);
                } catch (ValeParoolErind valeParoolErind) {
                    probleem.setText("Vale parool!");
                } catch (KasutajatEiLeiduErind kasutajatEiLeiduErind) {
                    probleem.setText("Vale kasutajanimi!");
                }
            }
        });
        salasõnaVäli.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                try {
                    logiSisse(kasutajanimiVäli.getCharacters().toString(), salasõnaVäli.getCharacters().toString());
                    küsimustikuLeht(peaLava, piiriPaan);
                } catch (ValeParoolErind valeParoolErind) {
                    probleem.setText("Vale parool!");
                } catch (KasutajatEiLeiduErind kasutajatEiLeiduErind) {
                    probleem.setText("Vale kasutajanimi!");
                }
            }
        } );

        GridPane ruudustik = new GridPane(); //ruudustik teksti ja väljade paigustamise lihtsustamiseks
        ruudustik.setAlignment(CENTER);
        ruudustik.setPadding(new Insets(5)); //äärtesse ruumi

        ruudustik.add(kasutajanimi, 0, 0);
        ruudustik.add(kasutajanimiVäli, 1, 0);
        ruudustik.add(salasõna, 0, 1);
        ruudustik.add(salasõnaVäli, 1, 1);
        //vertikaalne ja horisontaalne ruum
        ruudustik.setVgap(5);
        ruudustik.setHgap(5);

        vBox.getChildren().addAll(ruudustik, nupudHBox, probleem); //ruudustik ja nupud kokku

        vBox.setSpacing(5); //lisab ruudustiu ja nuppude vahele hingamisruumi
        nupudHBox.setSpacing(2.5); // nuppude vahele ruumi
        nupudHBox.setAlignment(Pos.BASELINE_CENTER); //nupud keskele
        vBox.setAlignment(CENTER); //lisab vBoxi, kus on kogu sisu, keskele

        piiriPaan.setCenter(vBox);
    }

    public Küsimus küsiKüsimus() {
        if (küsida == null || küsida.isEmpty()) return null;
        Küsimus küs = küsida.get(0);
        küsida.remove(küs);
        return küs;
    }

    public void küsimuseLeht(Stage peaLava, Küsimus küsimus, BorderPane piiriPaan) {
        piiriPaan.getChildren().clear();
        Label küs = new Label(küsimus.getKüsimus());
        Label tulemus= new Label();

        Button kinnitusnupp= new Button("Kinnita vastus");
        ToggleGroup vastused= new ToggleGroup();
        VBox vBox = new VBox(); //teeb pärast paigutamise lihtsamaks
        VBox vBoxKüsimus= new VBox();
        VBox vBoxTulemus = new VBox();

        vBoxKüsimus.getChildren().add(küs);
        List<RadioButton> valikvastused = new ArrayList<>();
        kinnitusnupp.setDisable(true);

        RadioButton õigevalik = new RadioButton(küsimus.getVastused().get(0));
        õigevalik.setOnAction(e -> kinnitusnupp.setDisable(false) );
        õigevalik.setToggleGroup(vastused);
        valikvastused.add(õigevalik);

        for (int i = 1; i < küsimus.getVastused().size(); i ++) {
            RadioButton valik = new RadioButton(küsimus.getVastused().get(i));
            valik.setOnAction(e -> kinnitusnupp.setDisable(false) );
            valik.setToggleGroup(vastused);
            valikvastused.add(valik);
        }

        shuffle(valikvastused);

        for (RadioButton nupp: valikvastused) {
            vBoxKüsimus.getChildren().add(nupp);
        }

        vBoxTulemus.getChildren().addAll(kinnitusnupp, tulemus);

        kinnitusnupp.setOnAction(e ->
                {
                    if (kinnitusnupp.getText().equals("Järgmine küsimus")) {
                        Küsimus küsida = küsiKüsimus();
                        if (küsida != null) küsimuseLeht(peaLava, küsida, piiriPaan);
                        else lõppLeht(peaLava, vBox);
                    }
                    if (õigevalik.isSelected()) {
                        tulemus.setText("Õige vastus!");
                        if (!kinnitusnupp.getText().equals("Järgmine küsimus")) skoor += 1;
                        kinnitusnupp.setText("Järgmine küsimus");
                        for (RadioButton valik:valikvastused) valik.setDisable(true);
                    }
                    else {
                        tulemus.setText("Vale vastus!" +'\n'+ "Õige vastus: " + õigevalik.getText());
                        kinnitusnupp.setText("Järgmine küsimus");
                        for (RadioButton valik:valikvastused) valik.setDisable(true);
                    }

                }
        );

        GridPane ruudustik = new GridPane();
        ruudustik.setAlignment(CENTER); //ruudustik keskele
        vBoxKüsimus.setAlignment(CENTER_LEFT); //küsimused vasakjoondusga
        tulemus.setTextAlignment(TextAlignment.CENTER); //tulemus (õige/vale vastus) keskjoondusega
        vBoxTulemus.setAlignment(CENTER); //tulemuse osa keskjoondusesse
        vBoxKüsimus.setSpacing(5);
        vBoxTulemus.setSpacing(5);

        vBox.getChildren().addAll(vBoxKüsimus, vBoxTulemus); //kaks osa kokku
        vBox.setSpacing(10);

        ruudustik.add(vBox, 1,1); //lisan ruudustiku keskele (1,1 koordinaadid)

        Scene stseen2 = new Scene(ruudustik, 500, 400);  // luuakse stseen
        peaLava.setTitle("Küsimus");  // lava tiitelribale pannakse tekst
        peaLava.setScene(stseen2);  // lavale lisatakse stseen
        peaLava.show();  // lava tehakse nähtavaks
    }


    public void lõppLeht(Stage peaLava, VBox layout) {
        layout.getChildren().clear();
        Label tekst = new Label("Lõpp!");
        Label skoor = new Label("Sinu skoor: " + String.valueOf(this.skoor));
        Button välju = new Button("Välju");
        Button algusesse = new Button("Algusesse");
        VBox vBox = new VBox();
        HBox hBox = new HBox(algusesse, välju);
        vBox.getChildren().addAll(tekst, skoor, hBox);
        BorderPane piiriPaan = new BorderPane();

        piiriPaan.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        vBox.setSpacing(5);
        hBox.setSpacing(5);
        välju.setOnAction(e -> { Platform.exit(); });
        algusesse.setOnAction(e -> { avaleht(piiriPaan, peaLava); });

        Scene stseen3 = new Scene(piiriPaan, 500, 400);  // luuakse stseen
        peaLava.setTitle("Lõpp");  // lava tiitelribale pannakse tekst
        peaLava.setScene(stseen3);  // lavale lisatakse stseen
        vBox.setMinWidth(125); //min laius kui vee nuppe näha
        peaLava.show();  // lava tehakse nähtavaks
    }


    public void küsimustikuLeht(Stage peaLava, BorderPane piiriPaan) {
        VBox vBox = new VBox();
        Label tekst = new Label("Vali küsimustik!");

        Button kinnitusnupp= new Button("Kinnita vastus");
        ToggleGroup küsimustikuValik = new ToggleGroup();

        List<RadioButton> valikvastused = new ArrayList<>();

        for (int i = 0; i < küsimustikud.size(); i ++) {
            RadioButton valik = new RadioButton(küsimustikud.get(i).getKüsimustikuNimi());
            valik.setOnAction(e -> kinnitusnupp.setDisable(false) );
            valik.setToggleGroup(küsimustikuValik);
            valikvastused.add(valik);
        }

        vBox.getChildren().add(tekst);
        for (RadioButton nupp: valikvastused) {
            vBox.getChildren().add(nupp);
        }

        HBox nupud = new HBox();
        nupud.getChildren().add(kinnitusnupp);
        Button uusKüsimustik = new Button("Uus küsimustik");
        nupud.getChildren().add(uusKüsimustik);

        VBox suur = new VBox(); //et pärast kõik kokku panna

        kinnitusnupp.setOnAction(e ->
                {
                    String küsimustikuNimi = küsimustikuValik.getSelectedToggle().toString();;
                    Küsimustik küsimustik = leiaKüsimustik(küsimustikuNimi.split("\\'")[1]);
                    this.küsida = new ArrayList<>();
                    for (Küsimus küs: küsimustik.getKüsimused()) this.küsida.add(küs);
                    Küsimus küs = küsiKüsimus();
                    küsimuseLeht(peaLava, küs, piiriPaan);
                }
        );
        uusKüsimustik.setOnAction(e ->
                {
                    lisaUusKüsimustik(peaLava, piiriPaan);
                }
        );

        GridPane ruudustik = new GridPane();
        ruudustik.setPadding(new Insets(10, 10, 10, 10)); //(https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Insets.html)
        ruudustik.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        nupud.setSpacing(5);
        nupud.setAlignment(CENTER);
        suur.getChildren().addAll(vBox,nupud);
        suur.setSpacing(10);
        ruudustik.add(suur, 1,1);

        Scene stseen4 = new Scene(ruudustik, 500, 400);  // luuakse stseen
        peaLava.setTitle("Küsimustik");  // lava tiitelribale pannakse tekst
        peaLava.setScene(stseen4);  // lavale lisatakse stseen
        peaLava.show();  // lava tehakse nähtavaks
    }

    public void lisaUusKüsimustik(Stage peaLava, BorderPane piiriPaan) {
        piiriPaan.getChildren().clear();
        TextField küsimustikuNimi = new TextField("Küsimustiku nimi");
        TextField küsimustikuFail = new TextField("Küsimustiku fail");
        Button kinnitusnupp = new Button("Lisa");
        Label probleem = new Label("");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(küsimustikuNimi, küsimustikuFail, kinnitusnupp, probleem);

        kinnitusnupp.setOnAction(e ->
                {
                    try {
                        Küsimustik uusKüsimustik = new Küsimustik(küsimustikuNimi.getText());
                        uusKüsimustik.koostaKüsimustik(küsimustikuFail.getText());
                        lisaKüsimustik(uusKüsimustik);
                        küsimustikuLeht(peaLava, piiriPaan);
                    } catch (FileNotFoundException erind) {
                        probleem.setText("Sellist faili ei leidu! Proovi mõne teise failiga uuesti.");
                    }
                }
        );

        piiriPaan.setCenter(vBox);
        vBox.setSpacing(5); //lisab vertikaalsete osade vahele hingamisruumi
        vBox.setAlignment(Pos.CENTER); //lisab vBoxi, kus on kogu sisu, keskele
        piiriPaan.setPadding(new Insets(0, 100, 0, 100)); //paremale ja vasakule ruumi

        Scene stseen5 = piiriPaan.getScene();
        peaLava.setTitle("Lisa küsimustik");  // lava tiitelribale pannakse tekst
        peaLava.setScene(stseen5);  // lavale lisatakse stseen
        peaLava.show();  // lava tehakse nähtavaks
    }

    private List<Kasutaja> kasutajad;
    private List<Küsimustik> küsimustikud;
    private Kasutaja praeguneKasutaja; //kasutaja, kes hetkel sisse on logitud
    private List<Küsimus> küsida;
    private int skoor;

    //mängu alustades loetakse logifailist sisse juba loodud kasutajad ja küsimustikud ning lisatakse need objektidena mängu
    public void mäng() throws Exception {
        kasutajad = new ArrayList<>();
        küsimustikud = new ArrayList<>();
        try {
            File kasutajateFail = new File("src/kasutajad.txt");
            Scanner skännitudKasutajad = new Scanner(kasutajateFail);
            File küsimustikeFail = new File("src/küsimustikud.txt");
            Scanner skännitudKüsimustikud = new Scanner(küsimustikeFail);
            while (skännitudKasutajad.hasNextLine()) {
                //formaat: "username;password"
                String rida = skännitudKasutajad.nextLine();
                String[] tükid = rida.split(";");
                String kasutajanimi = tükid[0];
                String parool = tükid[1];
                Kasutaja uuskasutaja = new Kasutaja(kasutajanimi, parool);
                kasutajad.add(uuskasutaja);
            }
            skännitudKasutajad.close();
            while (skännitudKüsimustikud.hasNextLine()) {
                //formaat: küsimustikunimi;küsimustikufail
                String rida = skännitudKüsimustikud.nextLine();
                String[] tükid = rida.split(";");
                String küsimustikunimi = tükid[0];
                Küsimustik uusküsimustik = new Küsimustik(küsimustikunimi);
                String küsimustikufail = tükid[1];
                uusküsimustik.koostaKüsimustik(küsimustikufail);
                küsimustikud.add(uusküsimustik);
            }
            skännitudKüsimustikud.close();
        } catch (FileNotFoundException e) {

        }
    }

    //kasutaja lisamine mängu ja logifaili (formaat: username;password;score)
    public void lisaKasutaja(Kasutaja kasutaja) {
        kasutajad.add(kasutaja);
        try {
            //Here true is to append the content to file
            FileWriter fw = new FileWriter("src/kasutajad.txt",true);
            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(kasutaja.getKasutajanimi() + ";" + kasutaja.getParool());
            bw.write("\r\n"); //reavahetus
            //Closing BufferedWriter Stream
            bw.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //küsimustiku lisamine mängu ja logifaili (formaat: nimi;failiNimi)
    public void lisaKüsimustik(Küsimustik küsimustik) {
        küsimustikud.add(küsimustik);
        try {
            FileWriter kirjutaja = new FileWriter("src/küsimustikud.txt", true);
            kirjutaja.write(küsimustik.getKüsimustikuNimi() + ";" + küsimustik.getKüsimustikuFailiNimi());
            kirjutaja.write("\r\n"); //reavahetus
            kirjutaja.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //võimaldab kasutajal oma olemasolevasse kasutajasse sisse logida
    public void logiSisse(String kasutajanimi, String parool) throws KasutajatEiLeiduErind, ValeParoolErind {
        if (leiaKasutaja(kasutajanimi) != null) {
            Kasutaja kasutaja = leiaKasutaja(kasutajanimi);
            if (parool.equals(kasutaja.getParool())) {
                System.out.println("Sisse logitud!");
                praeguneKasutaja = kasutaja; //logib sisse
            }
            else throw new ValeParoolErind("Vale parool!");
        }
        else throw new KasutajatEiLeiduErind("Vale kasutajanimi!");
    }

    //loob kasutajalt küsitud andmete abil uue kasutaja
    public Kasutaja looKasutaja(String kasutajanimi, String parool) {
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

    public static void main(String[] args) {
        launch();
    }
}