import java.util.List;
public class Andmeväli {
    String nimi;
    String väärtus;
    List<Andmeväli> sisemisedVäljad;

    public Andmeväli(String nimi, String väärtus, List<Andmeväli> sisemisedVäljad) {
        this.nimi = nimi;
        this.väärtus = väärtus;
        this.sisemisedVäljad = sisemisedVäljad;
    }
}
