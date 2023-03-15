import java.util.List;
public class Andmeväli {
    private String nimi;
    private String väärtus;
    private List<Andmeväli> sisemisedVäljad;

    public Andmeväli(String nimi, String väärtus, List<Andmeväli> sisemisedVäljad) {
        this.nimi = nimi;
        this.väärtus = väärtus;
        this.sisemisedVäljad = sisemisedVäljad;
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
