import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// tegeleb käskude töötlemisega ja command line asjadega
// TODO: lisada andmete salvestamine kui programm kinni panna
public class Graafikaliides {
    private static final Scanner kasutajaInput = new Scanner(System.in);
    private static Andmebaas andmebaas = null;

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

    public static void kuvaRekursiivseltSisemisedAndmeväljad(Andmeväli andmed, int sügavus) {
        System.out.println("--".repeat(sügavus) + "-> " + andmed);
        if(andmed.getSisemisedVäljad() != null && andmed.getSisemisedVäljad().size() != 0)
            for(Andmeväli sisemisedAndmed : andmed.getSisemisedVäljad())
                kuvaRekursiivseltSisemisedAndmeväljad(sisemisedAndmed, sügavus+1);
    }
    public static Andmeväli väljastaSisemisteAndmeteInfoEkraanile(List<Andmeväli> andmeväljad, String id){
        //otsib rekursiivselt selle id üles mida sisestati ning väljastab ingo ekraanile ja tagastab selle välja
        for (Andmeväli väli:andmeväljad) {
            if (väli.getId().equals(id)){
                System.out.println(väli);
                return väli;
            }
            else{
                if (!väli.getSisemisedVäljad().isEmpty()) return väljastaSisemisteAndmeteInfoEkraanile(väli.getSisemisedVäljad(), id);
            }
        }
        return null;
    }
    public static void kuvaSisemisteAndmeteInfo(Andmeväli andmeväli) {
        kustutaCommandPromptiTekst();
        System.out.println("*************************************************");
        System.out.println("*                                               *");
        System.out.println("*              Treeningpäevik                   *");
        System.out.println("*                                               *");
        System.out.println("*              Valitud ID: "+andmeväli.getId()+" ".repeat(21-andmeväli.getId().length())+"*");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        System.out.println();
        System.out.println("> Käsud: ");
        //System.out.println("> V ID - valib sisemise info ning näitab selle sisu");
        System.out.println("> K ID - kustutab andmevälja");
        System.out.println("> B - läheb tagasi eelmisele ekraanile");
        System.out.println("> BT - läheb tagasi trenni juurde");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }


    public static String andmeväljadeEkraan(Andmeväli andmeväli){
        kuvaSisemisteAndmeteInfo(andmeväli);
        while (true){
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaSisemisteAndmeteInfo(andmeväli);
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }

            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                //siin pole nagu vaja V-d sest juba sai valitud see mida sooviti
                //ning kui mitte siis läheb lis ühe võrra tagasi ja valib mis tahtis?

                /*case "V" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaSisemisteAndmeteInfo(andmeväljad);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }
                    //otsib üles valitud IDga andmevälja
                    for (Andmeväli andmeväli:andmeväljad) {
                        if (andmeväli.getId().equals(käskJaArgumendid[1])){
                            //muul juhul hakkab rekursiivselt(?) järjest sisemisi välju kuvama kui neid leidub ja inimene neid valib
                            String viimaneKäsk = andmeväljadeEkraan(andmeväli.getSisemisedVäljad());
                            if(viimaneKäsk.equals("BT")) {
                                return "BT"; // väljub igast tsüklist niikaua kuni jõuame tagasi trennini
                            }
                            continue;
                        }
                    }
                }*/

                case "B" -> {
                    return "B";
                }
                case "BT" -> {
                    return "BT";
                }
                case "Q" -> {
                    System.exit(0);
                }
                default -> {
                    kuvaSisemisteAndmeteInfo(andmeväli);
                    System.out.println("Sellist käsku ei eksisteeri.");
                }
            }
        }
    }

    public static void kuvaKindlaTrenniEkraaniInfo(Trenn trenn) {
        System.out.println("*************************************************");
        System.out.println("*                                               *");
        System.out.println("*                                               *");
        System.out.println("*              Treeningpäevik                   *");
        System.out.println("*                                               *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        System.out.println(trenn);
        if(trenn.getPeamisedVäljad().size() == 0)
            System.out.println("Sellel trennil pole rohkem infot.");
        else
            for(Andmeväli andmed : trenn.getPeamisedVäljad())
                kuvaRekursiivseltSisemisedAndmeväljad(andmed, 0);
        System.out.println();
        System.out.println("> V ID - vaata mingit kindlat andmevälja");
        System.out.println("> MN uusNimi - muuda trenni nime");
        System.out.println("> MK uusKestvus - muuda trenni kestvust (ajaühik sisesta ise)");
        System.out.println("> K ID - kustutab andmevälja");
        System.out.println("> K trenniNimi - kustutab praeguse trenni andmed");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }
    public static void kindlaTrenniEkraan(String kuupäev, Trenn trenn) {
        kuvaKindlaTrenniEkraaniInfo(trenn);

        while (true) {
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaKindlaTrenniEkraaniInfo(trenn);
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }
            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                case "V" -> {
                    List<Andmeväli> trenniPeamisedAndmeväljad=trenn.getPeamisedVäljad();
                    Andmeväli valitudVäli=väljastaSisemisteAndmeteInfoEkraanile(trenniPeamisedAndmeväljad, käskJaArgumendid[1]);
                    if (valitudVäli==null){
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Sellist IDd ei leidu.");
                    }
                    andmeväljadeEkraan(valitudVäli);
                }
                case "MN" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Käsul MN peab olema täpselt üks uue nime argument.");
                        continue;
                    }
                }
                case "MK" -> {
                    if (käskJaArgumendid.length < 2) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Käsul MK peab olema täpselt üks uue kestvuse argument.");
                        continue;
                    }
                    // uus nimi võib koosneda mitmest sõnast, peame stringi kokku ühendama
                    //String uusNimi = String.join(" ", " ", " ", käskJaArgumendid, käskJaArgumendid.length); // retarded compileri error
                    //trenn.setNimi(käskJaArgumendid);
                }
                case "K" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }

                    if(käskJaArgumendid[1].equals(trenn.getNimi())) { // tahetakse kustutada trenni
                        // ...
                        // siin vast lihtsalt returnib tagasi trennide ekraanile
                    } else {

                    }
                }
                case "Q" -> {
                    System.exit(0);
                }
            }
        }
    }

    public static void kuvaTrennideEkraaniInfo(String kuupäev, List<Trenn> trennid) {
        kustutaCommandPromptiTekst();

        // arvutused kuupäeva info keskele panemiseks
        // https://stackoverflow.com/questions/16629476/how-to-center-a-print-statement-text
        String kuupäevaInfo = "Valitud kuupäev: " + kuupäev;
        String rida = "                                               ";
        int nihe = (rida.length()-kuupäevaInfo.length())/2;
        String kuupäevaPrint = "*%" + nihe + "s%s%" + nihe + "s*\n";

        System.out.println("*************************************************");
        System.out.println("*                                               *");
        System.out.println("*              Treeningpäevik                   *");
        System.out.println("*                                               *");
        System.out.printf(kuupäevaPrint, " ", kuupäevaInfo, " ");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        System.out.println("Oled teinud järgmiseid trenne:");
        for (Trenn trenn : trennid) {
            System.out.println(trenn);
        }
        System.out.println();

        System.out.println("> Käsud: ");
        System.out.println("> V ID - valib trenni ning näitab selle sisu");
        System.out.println("> L nimi ||| kestvus  - lisab trenni, kindlasti pange nime ja kestvuse vahele |||");
        System.out.println("> B - läheb tagasi eelmisele ekraanile");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();

    }

    public static void trennideEkraan(String kuupäev, List<Trenn> trennid) {
        kuvaTrennideEkraaniInfo(kuupäev, trennid);

        while (true) {
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaTrennideEkraaniInfo(kuupäev, trennid);
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }
            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                case "V" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaTrennideEkraaniInfo(kuupäev, trennid);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }

                    for (Trenn trenn : trennid) {
                        //otsib listist õige IDga trenni üles
                        if (trenn.getId().equals(käskJaArgumendid[1])) {
                            kindlaTrenniEkraan(kuupäev, trenn);
                            continue;
                        }
                    }

                    kuvaTrennideEkraaniInfo(kuupäev, trennid);
                    System.out.println("Sellise ID-ga trenni ei leitud.");
                    continue;
                }
                case "B" -> {
                    return; // naaseb tagasi kuupäevade ekraanile
                }
                case "Q" -> {
                    System.exit(0);
                }
                default -> {
                    kuvaTrennideEkraaniInfo(kuupäev, trennid);
                    System.out.println("Sellist käsku ei eksisteeri.");
                }
            }
        }
    }

    public static void kuvaKuupäevaEkraaniInfo() {
        // https://stackoverflow.com/questions/2979383/how-to-clear-the-console
        kustutaCommandPromptiTekst();

        System.out.println("*************************************************");
        System.out.println("*                                               *");
        System.out.println("*                                               *");
        System.out.println("*              Treeningpäevik                   *");
        System.out.println("*                                               *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        System.out.println();
        System.out.println();
        System.out.println("Oled trenni teinud kuupäevadel: ");
        for (String kuupäev : andmebaas.tagastaKõikKuupäevad()) {
            System.out.print(kuupäev + "  ");
        }
        System.out.println();

        System.out.println("> Käsud: ");
        System.out.println("> V kuupäev - valib kuupäeva ning näitab trenne sellel kuupäeval");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }

    public static void kuupäevadeEkraan() {
        kuvaKuupäevaEkraaniInfo();

        while (true) {
            System.out.print("Sisesta käsk: ");
            String käsk = kasutajaInput.nextLine();
            System.out.println();

            if (käsk.equals("")) {
                kuvaKuupäevaEkraaniInfo();
                System.out.println("Tühja käsku ei eksisteeri.");
                continue;
            }

            String[] käskJaArgumendid = käsk.split(" ");
            switch (käskJaArgumendid[0].toUpperCase()) {
                case "V" -> {
                    if (käskJaArgumendid.length != 2) {
                        kuvaKuupäevaEkraaniInfo();
                        System.out.println("Käsul V peab olema täpselt üks kuupäeva argument.");
                        continue;
                    }

                    List<Trenn> trennid = andmebaas.tagastaTrennidKuupäeval(käskJaArgumendid[1]);
                    if (trennid == null) {
                        kuvaKuupäevaEkraaniInfo();
                        System.out.println("Sellist kuupäeva ei eksisteeri.");
                        continue;
                    }

                    trennideEkraan(käskJaArgumendid[1], trennid);
                    kuvaKuupäevaEkraaniInfo(); // selleks, et peale sellele ekraanile tagasi tulekut oleks kuupäevade ekraani käsud nähtaval
                }
                case "Q" -> {
                    System.exit(0);
                }
                default -> {
                    kuvaKuupäevaEkraaniInfo();
                    System.out.println("Sellist käsku ei eksisteeri.");
                }
            }
        }
    }

    public static void main(String[] args) {
        andmebaas = new Andmebaas();
        try {
            andmebaas.laeAndmebaas();
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

        kuupäevadeEkraan();
    }
}
