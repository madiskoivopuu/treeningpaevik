import java.util.ArrayList;
import java.util.List;

public class Trenn {
    private String id;
    private String kestvus;
    private String nimi;
    private List<Andmeväli> peamisedVäljad;

    public Trenn(String id, String kestvus, String nimi) {
        this.id = id;
        this.kestvus = kestvus;
        this.nimi = nimi;
        this.peamisedVäljad = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKestvus() {
        return kestvus;
    }

    public void setKestvus(String kestvus) {
        this.kestvus = kestvus;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public List<Andmeväli> getPeamisedVäljad() {
        return peamisedVäljad;
    }

    public void setPeamisedVäljad(List<Andmeväli> peamisedVäljad) {
        this.peamisedVäljad = peamisedVäljad;
    }
}
