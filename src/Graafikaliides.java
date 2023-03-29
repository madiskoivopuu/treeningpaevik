import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// tegeleb käskude töötlemisega ja command line asjadega

//{"nimi":"km","väärtus":"6","väljad":[{"nimi":"km","väärtus":"7","väljad":[],"id":"345"}],"id":"123"}
public class Graafikaliides {
    private static final Scanner kasutajaInput = new Scanner(System.in);
    private static Andmebaas andmebaas = null;

    private static String viimaneKäsk = "";

    public static void setViimaneKäsk(String viimaneKäsk) {
        Graafikaliides.viimaneKäsk = viimaneKäsk;
    }

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

    public static void salvestaJaSulgeProgramm() {
        for (int proov = 0; proov < 3; proov++) {
            try {
                andmebaas.salvestaAndmebaas();
                System.exit(0);
            } catch (java.io.IOException e) {
                System.out.println("Andmebaasi ei suudetud salvestada");
                System.out.println("Java veateade: " + e.getMessage());
            }
        }

        // programm läheb normaalselt for tsükli sees kinni, kui mitte siis järgmine kood läheb käima
        System.out.println("Peale kolme katset ei suudetud andmebaasi ikka faili salvestada.");
        System.out.println("Lugege veateadet ning proovige hiljem uuesti, programm praegu ei sulgu.");
    }

    public static void kuvaRekursiivseltSisemisedAndmeväljad(Andmeväli andmed, int sügavus) {
        System.out.println("--".repeat(sügavus) + "-> " + andmed);
        if (andmed.getSisemisedVäljad() != null && andmed.getSisemisedVäljad().size() != 0)
            for (Andmeväli sisemisedAndmed : andmed.getSisemisedVäljad())
                kuvaRekursiivseltSisemisedAndmeväljad(sisemisedAndmed, sügavus + 1);
    }

    public static AndmeväliJaAsukoht leiaRekursiivseltAndmeväliIDga(List<Andmeväli> andmeväljad, String id) {
        //otsib rekursiivselt selle id üles mida sisestati ning väljastab info ekraanile ja tagastab selle välja
        for (Andmeväli väli : andmeväljad) {
            if (väli.getId().equals(id)) {
                return new AndmeväliJaAsukoht(väli, andmeväljad);
            } else {
                if (!väli.getSisemisedVäljad().isEmpty())
                    return leiaRekursiivseltAndmeväliIDga(väli.getSisemisedVäljad(), id);
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
        System.out.println("*              Valitud ID: " + andmeväli.getId() + " ".repeat(21 - andmeväli.getId().length()) + "*");
        System.out.println("*                                               *");
        System.out.println("*************************************************");
        System.out.println();

        if (andmeväli.getSisemisedVäljad().size() == 0)
            System.out.println("Sellel väljal pole rohkem infot.");
        else {
            System.out.println("Sellel väljal on sisu:");
            for (Andmeväli andmeväli1 : andmeväli.getSisemisedVäljad())
                kuvaRekursiivseltSisemisedAndmeväljad(andmeväli1, 0);
        }


        System.out.println();
        System.out.println("> Käsud: ");
        System.out.println("> V ID - valib sisemise info ning näitab selle sisu");
        System.out.println("> K ID - kustutab andmevälja");
        System.out.println("> B - läheb tagasi eelmisele ekraanile");
        System.out.println("> BT - läheb tagasi trenni juurde");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }


    public static String andmeväljadeEkraan(Andmeväli andmeväli) {
        kuvaSisemisteAndmeteInfo(andmeväli);
        while (true) {
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
                case "V" -> {
                    setViimaneKäsk("V");
                    if (käskJaArgumendid.length != 2) {
                        kuvaSisemisteAndmeteInfo(andmeväli);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }
                    List<Andmeväli> andmeteVäljad = andmeväli.getSisemisedVäljad();

                    AndmeväliJaAsukoht valik = leiaRekursiivseltAndmeväliIDga(andmeteVäljad, käskJaArgumendid[1]);
                    if (valik == null) {
                        andmeväljadeEkraan(andmeväli);
                        System.out.println("Sellise ID-ga andmevälja ei leidu.");
                        continue;
                    }
                    //System.out.println(valik.andmeväli.getId());
                    andmeväljadeEkraan(valik.andmeväli);

                    if (Objects.equals(viimaneKäsk, "B")) {
                        andmeväljadeEkraan(andmeväli);
                        return "B";
                    }
                    if (Objects.equals(viimaneKäsk, "BT")) return "BT";
                }

                case "K" -> {
                    setViimaneKäsk("K");
                    AndmeväliJaAsukoht valik = leiaRekursiivseltAndmeväliIDga(andmeväli.getSisemisedVäljad(), käskJaArgumendid[1]);
                    if (valik != null) {
                        valik.asukoht.remove(valik.andmeväli);
                        kuvaSisemisteAndmeteInfo(andmeväli);
                        System.out.println("Andmeväli kustutatud!");

                    } else {
                        kuvaSisemisteAndmeteInfo(andmeväli);
                        System.out.println("Andmevälja ID-ga " + käskJaArgumendid[1] + " ei leitud.");
                    }
                }
                case "B" -> {
                    setViimaneKäsk("B");
                    return "B";
                }
                case "BT" -> {
                    setViimaneKäsk("BT");
                    return "BT";
                }
                case "Q" -> {
                    salvestaJaSulgeProgramm();
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
        if (trenn.getPeamisedVäljad().size() == 0)
            System.out.println("Sellel trennil pole rohkem infot.");
        else
            for (Andmeväli andmed : trenn.getPeamisedVäljad())
                kuvaRekursiivseltSisemisedAndmeväljad(andmed, 0);
        System.out.println();
        System.out.println("> V ID - vaata mingit kindlat andmevälja");
        System.out.println("> MN uusNimi - muuda trenni nime");
        System.out.println("> MK uusKestvus - muuda trenni kestvust (ajaühik sisesta ise)");
        System.out.println("> K ID - kustutab andmevälja");
        System.out.println("> K trenniNimi - kustutab praeguse trenni andmed");
        System.out.println("> B - läheb tagasi eelmisele ekraanile");
        System.out.println("> Q - sulgeb programmi");
        System.out.println();
    }

    public static String kindlaTrenniEkraan(String kuupäev, Trenn trenn) {
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
                    setViimaneKäsk("V");
                    List<Andmeväli> trenniPeamisedAndmeväljad = trenn.getPeamisedVäljad();
                    AndmeväliJaAsukoht valik = leiaRekursiivseltAndmeväliIDga(trenniPeamisedAndmeväljad, käskJaArgumendid[1]);
                    if (valik == null) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Sellist IDd ei leidu.");
                        continue;
                    }
                    //System.out.println(valik.andmeväli.getId());
                    andmeväljadeEkraan(valik.andmeväli);
                    if (Objects.equals(viimaneKäsk, "BT")) return "BT";
                    if (Objects.equals(viimaneKäsk, "B")) {
                        kindlaTrenniEkraan(kuupäev, trenn);
                        return "B";
                    }
                }
                case "MN" -> {
                    setViimaneKäsk("MN");
                    if (käskJaArgumendid.length < 2) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Käsul MN peab olema vähemalt üks sõna uue nimena.");
                        continue;
                    }

                    // uus nimi võib koosneda mitmest sõnast, peame stringi kokku ühendama ja käsu eemaldama
                    String uusNimi = String.join(" ", käskJaArgumendid);
                    uusNimi = uusNimi.substring(2);
                    trenn.setNimi(uusNimi);

                    // väljastame uue info ekraanile
                    kuvaKindlaTrenniEkraaniInfo(trenn);
                }
                case "MK" -> {
                    setViimaneKäsk("MK");
                    if (käskJaArgumendid.length < 2) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Käsul MK peab olema vähemalt üks sõna/number kestvuse jaoks.");
                        continue;
                    }
                    // sama mis MN käsus
                    String uusKestvus = String.join(" ", käskJaArgumendid);
                    uusKestvus = uusKestvus.substring(2);
                    trenn.setKestvus(uusKestvus);

                    // väljastame uue info ekraanile
                    kuvaKindlaTrenniEkraaniInfo(trenn);
                }
                case "K" -> {
                    setViimaneKäsk("K");
                    if (käskJaArgumendid.length != 2) {
                        kuvaKindlaTrenniEkraaniInfo(trenn);
                        System.out.println("Käsul V peab olema täpselt üks ID argument.");
                        continue;
                    }

                    if (käskJaArgumendid[1].equals(trenn.getNimi())) { // tahetakse kustutada trenni
                        andmebaas.kustutaTrenn(kuupäev, trenn.getId());
                        return "K";
                    } else {
                        AndmeväliJaAsukoht valik = leiaRekursiivseltAndmeväliIDga(trenn.getPeamisedVäljad(), käskJaArgumendid[1]);
                        if (valik != null) {
                            valik.asukoht.remove(valik.andmeväli);
                            kuvaKindlaTrenniEkraaniInfo(trenn);
                        } else {
                            kuvaKindlaTrenniEkraaniInfo(trenn);
                            System.out.println("Andmevälja ID-ga " + käskJaArgumendid[1] + " ei leitud.");
                        }
                    }
                }
                case "B" -> {
                    setViimaneKäsk("B");
                    return "B";
                }
                case "Q" -> {
                    salvestaJaSulgeProgramm();
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
        int nihe = (rida.length() - kuupäevaInfo.length()) / 2;
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

        käsk:
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
                            String tagastus = kindlaTrenniEkraan(kuupäev, trenn);

                            if (viimaneKäsk.equals("B")) {
                                trennideEkraan(kuupäev, trennid);
                                return;
                            }
                            // kui siit tagasi tulla, siis peame ekraani infot uuesti näitama
                            if (Objects.equals(viimaneKäsk, "BT")) kuvaTrennideEkraaniInfo(kuupäev, trennid);
                            continue käsk;
                        }
                    }

                    kuvaTrennideEkraaniInfo(kuupäev, trennid);
                    System.out.println("Sellise ID-ga trenni ei leitud.");
                }
                case "L" -> {
                    String[] nimiJaKestvus=käsk.substring(2).split(" \\|"+"\\|"+"\\| ");
                    String nimi=nimiJaKestvus[0];
                    String kestvus=nimiJaKestvus[1];
                    andmebaas.lisaTrenn(kuupäev, nimi, kestvus);

                    kuvaTrennideEkraaniInfo(kuupäev, trennid);
                    System.out.println("Kuupäev lisatud!");
                }
                case "B" -> {
                    setViimaneKäsk("B");
                    return; // naaseb tagasi kuupäevade ekraanile
                }
                case "Q" -> {
                    salvestaJaSulgeProgramm();
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
                    salvestaJaSulgeProgramm();
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
