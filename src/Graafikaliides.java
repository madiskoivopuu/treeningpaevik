import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

// tegeleb käskude töötlemisega ja command line asjadega
public class Graafikaliides {
    public enum Ekraan {
        VÄLJU,
        KÄIVITUSEKRAAN,
        TRENNIEKRAAN,
        VÄLJAEKRAAN;
    }
    private static Ekraan praeguneEkraan = Ekraan.KÄIVITUSEKRAAN;

    public static void käivitusEkraan(Andmebaas andmed) {
        System.out.println();
        System.out.println();
        System.out.println("Oled trenni teinud kuupäevadel: ");
        for(String kuupäev : andmed.tagastaKõikKuupäevad()) {
            System.out.print(kuupäev + "  ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("*************************************************");
        System.out.println("*                                               *");
        System.out.println("*              Treeningpäevik                   *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        Andmebaas andmed = new Andmebaas();
        try {
            andmed.laeAndmebaas();
        } catch (FileNotFoundException e) {
            System.out.println("Andmebaasi faili ei eksisteerinud, programm loob automaatselt uue.");
            File fail = new File(Andmebaas.ANDMEBAASI_FAIL);
            try {
                fail.createNewFile();
            } catch(Exception e2) {
                System.out.println("Faili loomine ebaõnnestus.");
                // TODO: sleep lisada vms
            }
        }

        while(true) {
            switch (praeguneEkraan) {
                case VÄLJU:
                    return;
                case KÄIVITUSEKRAAN:
                    käivitusEkraan(andmed);
                    break;
            }
        }
    }
}
