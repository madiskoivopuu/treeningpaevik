import java.util.List;

// Abiklass andmevälja ja tema asukoha (listi) hoiustamiseks
// selleks, et andmevälja oleks võimalik kasutaja soovil mingist listist ära kustutada
public class AndmeväliJaAsukoht {
    public Andmeväli andmeväli;
    public List<Andmeväli> asukoht;

    public AndmeväliJaAsukoht(Andmeväli andmeväli, List<Andmeväli> asukoht) {
        this.andmeväli = andmeväli;
        this.asukoht = asukoht;
    }
}
