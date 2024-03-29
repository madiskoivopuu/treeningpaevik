import java.util.List;

// Hoiab JSONi andmebaasist saadud andmeväljade (nii välimiste kui ka sisemiste) infot.
// Otse JSONi pealt ei ole kõige parem andmetega töötada, seega on selleks tehtud see klass, kuhu info veidike lihtsamaks kasutamiseks panna saame.
public class Andmeväli {
    private String id;
    private String nimi;
    private String väärtus;
    private List<Andmeväli> sisemisedVäljad;

    public Andmeväli(String id, String nimi, String väärtus, List<Andmeväli> sisemisedVäljad) {
        this.id = id;
        this.nimi = nimi;
        this.väärtus = väärtus;
        this.sisemisedVäljad = sisemisedVäljad;
    }

    public String toString() {
        return this.nimi + " - " + this.väärtus + " (ID: " + this.id + ")";
    }

    public String getId() {
        return id;
    }
    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getVäärtus() {
        return väärtus;
    }

    public void setVäärtus(String väärtus) {
        this.väärtus = väärtus;
    }

    public List<Andmeväli> getSisemisedVäljad() {
        return sisemisedVäljad;
    }
}
