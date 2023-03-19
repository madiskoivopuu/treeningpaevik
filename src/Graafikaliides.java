import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// tegeleb käskude töötlemisega ja command line asjadega
public class Graafikaliides {
    private static final Scanner kasutajaInput = new Scanner(System.in);
    private static Trenn viimatiValitudTrenn = null;

    public static void kustutaCommandPromptiTekst() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
        }
    }

    public static void kuvaTrennideEkraaniInfo(List<Trenn> trennid) {
        kustutaCommandPromptiTekst();
        System.out.println("Oled teinud järgmiseid trenne:");
        for (Trenn trenn : trennid) {
            System.out.println(trenn.getNimi() + ", ID: " + trenn.getId());
        }
        System.out.println();

        System.out.println("> Käsud: ");
        System.out.println("> V ID - valib trenni ning näitab selle sisu");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();

    }

    public static void trennideEkraan(List<Trenn> trennid) {
        kuvaTrennideEkraaniInfo(trennid);

        while (true) {
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaTrennideEkraaniInfo(trennid);
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }
            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                case "V" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaTrennideEkraaniInfo(trennid);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }

                    for (Trenn trenn : trennid) {
                        //otsib listist õige IDga trenni üles
                        if (Objects.equals(trenn.getId(), käskJaArgumendid[1])) {
                            System.out.println("Valisite trenni:");
                            System.out.println(trenn.getNimi() + ", kestvus: " + trenn.getKestvus() + ", ID: " + trenn.getId());
                            //teeb sisemiste andmete listi
                            List<Andmeväli> selleTrenniAndmeväljad = trenn.getPeamisedVäljad();
                            //kui see on tühi siis annab sellest teada ja viib esialgsete trennide juurde tagasi
                            if (selleTrenniAndmeväljad.isEmpty()) {
                                System.out.println("Sellel trennil pole rohkem infot.");
                                System.out.println();
                                System.out.println("Viime su valitud kuupäeva trennide juurde tagasi.");
                                kuvaTrennideEkraaniInfo(trennid);
                            } else {
                                //muul juhul näitab mis andmed sellel trennil veel on
                                andmeväljadeEkraan(selleTrenniAndmeväljad);
                            }
                        }
                    }
                }
                case "Q" -> {
                    System.exit(0);
                }
                default -> {
                    kuvaTrennideEkraaniInfo(trennid);
                    System.out.println("Sellist käsku ei eksisteeri.");
                }
            }
        }
    }


    public static void kuvaSisemisteAndmeteInfo(List<Andmeväli> andmeväljad) {
        kustutaCommandPromptiTekst();
        System.out.println("Sellel trennil on sisemised andmed:");
        for (Andmeväli andmeväli : andmeväljad) {
            System.out.println(andmeväli.getNimi() + ", " + andmeväli.getVäärtus() + ", ID: " + andmeväli.getId());
        }
        System.out.println();
        System.out.println("> Käsud: ");
        System.out.println("> V ID - valib sisemise info ning näitab selle sisu");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }
    public static void andmeväljadeEkraan(List<Andmeväli> andmeväljad){
        kuvaSisemisteAndmeteInfo(andmeväljad);
        while (true){
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaSisemisteAndmeteInfo(andmeväljad);
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }

            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                case "V" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaSisemisteAndmeteInfo(andmeväljad);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }
                    //otsib üles valitud IDga andmevälja
                    for (Andmeväli andmeväli:andmeväljad) {
                        if (andmeväli.getId().equals(käskJaArgumendid[1])){
                            //kui sellel andmeväljal rohkem infot pole annab teada ja viib sama trenni eelmise sisu juurde tagasi
                            if (andmeväli.getSisemisedVäljad().isEmpty()){
                                System.out.println("Sellel trennil ei ole rohkem infot");
                                System.out.println();
                                System.out.println("Viime su trenni eelmise sisu juurde tagasi.");
                                System.out.println();
                                andmeväljadeEkraan(andmeväljad);
                            }
                            //muul juhul hakkab rekursiivselt(?) järjest sisemisi välju kuvama kui neid leidub ja inimene neid valib
                            andmeväljadeEkraan(andmeväli.getSisemisedVäljad());
                        }
                    }
                }
                case "Q" -> {
                    System.exit(0);
                }
                default -> {
                    kuvaSisemisteAndmeteInfo(andmeväljad);
                    System.out.println("Sellist käsku ei eksisteeri.");
                }
            }
        }
    }

    public static void kuvaKuupäevaEkraaniInfo(Andmebaas andmed) {
        // https://stackoverflow.com/questions/2979383/how-to-clear-the-console
        kustutaCommandPromptiTekst();

        System.out.println("*************************************************");
        System.out.println("*                                               *");
        System.out.println("*              Treeningpäevik                   *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        System.out.println();
        System.out.println();
        System.out.println("Oled trenni teinud kuupäevadel: ");
        for (String kuupäev : andmed.tagastaKõikKuupäevad()) {
            System.out.print(kuupäev + "  ");
        }
        System.out.println();

        System.out.println("> Käsud: ");
        System.out.println("> V kuupäev - valib kuupäeva ning näitab trenne sellel kuupäeval");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }

    public static void kuupäevadeEkraan(Andmebaas andmed) {
        kuvaKuupäevaEkraaniInfo(andmed);

        while (true) {
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaKuupäevaEkraaniInfo(andmed);
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }

            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                case "V" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaKuupäevaEkraaniInfo(andmed);
                        System.out.println("Käsul V peab olema täpselt üks kuupäeva argument.");
                        continue;
                    }

                    List<Trenn> trennid = andmed.tagastaTrennidKuupäeval(käskJaArgumendid[1]);
                    if (trennid == null) {
                        kuvaKuupäevaEkraaniInfo(andmed);
                        System.out.println("Sellist kuupäeva ei eksisteeri.");
                        continue;
                    }

                    trennideEkraan(trennid);
                }
                case "Q" -> {
                    return;
                }
                default -> {
                    kuvaKuupäevaEkraaniInfo(andmed);
                    System.out.println("Sellist käsku ei eksisteeri.");
                }
            }
        }
    }

    public static void main(String[] args) {
        Andmebaas andmed = new Andmebaas();
        try {
            andmed.laeAndmebaas();
        } catch (FileNotFoundException e) {
            System.out.println("HOIATUS: Andmebaasi faili ei eksisteerinud, programm loob automaatselt uue.");
            File fail = new File(Andmebaas.ANDMEBAASI_FAIL);
            try {
                fail.createNewFile();
                FileWriter w = new FileWriter(fail);
                BufferedWriter b = new BufferedWriter(w);
                b.write("{}");
                b.close();
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("VIGA: Faili loomine ebaõnnestus. Vajuta ENTER klahvi et programm sulgeda.");
                kasutajaInput.nextLine();
            }
        }

        kuupäevadeEkraan(andmed);
    }
}
