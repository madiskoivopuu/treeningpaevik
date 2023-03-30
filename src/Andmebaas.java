import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

import jdk.jshell.spi.ExecutionControl;
import org.json.*;

// Võimaldab trennide andmebaasi laadida ning salvestada, samuti muuta selle andmeid mingil moel (trenni lisamine, kustutamine jne)
// Mõte oli, et niimoodi lahutab backendi ja frontendi rohkem ära. Ehk siis andmete töötlemise ja salvestamisega tegeleb mingi class, ning graafikaliides ei pea
// midagi teadma, kuidas neid andmeid töödeldakse.
public class Andmebaas {
    public static final String ANDMEBAASI_FAIL = "andmebaas.json";
    private HashMap<String, List<Trenn>> trenniAndmed;

    public Andmebaas() {
        this.trenniAndmed = new HashMap<>();
    }

    public boolean lisaTrenn(String kuupäev, String nimi, String kestvus) {
        if(!this.trenniAndmed.containsKey(kuupäev))
            return false;

        //loome uue random ID et seda trenniga koos kasutada (panin kolmekohalise sest siis on vast piisavalt palju trenne võimalik lisada)
        int id = (int) (Math.random() * 1000);
        //loome juba kasutusel olevate IDde listi
        ArrayList<Integer> trennideIDd = new ArrayList<>();
        //ma pole kindel kas see töötab nii nagu ma plaanisin
        //aga see peaks iga trenni id panema listi:
        this.trenniAndmed.forEach((key, value) -> {
            if (value.isEmpty()) ;
            else {
                for (Trenn mingiTrenn : value) {
                    trennideIDd.add(Integer.parseInt(mingiTrenn.getId()));
                }
            }
        });
        //kontrollime et seda IDd ei oleks seal listis
        while (trennideIDd.contains(id)) id = (int) (Math.random() * 1000);
        //teeme uue trenni isendi
        Trenn uusTrenn = new Trenn(String.valueOf(id), kestvus, nimi);
        //lisame selle selle kuupäeva trennide listi
        this.trenniAndmed.get(kuupäev).add(uusTrenn);
        return true;
    }

    public boolean kustutaTrenn(String kuupäev, String id) {
        if(!this.trenniAndmed.containsKey(kuupäev))
            return false;

        boolean eemaldati = this.trenniAndmed.get(kuupäev).removeIf(trenn -> trenn.getId().equals(id));
        return eemaldati;
    }

    public List<Trenn> tagastaTrennidKuupäeval(String kuupäev) {
        return this.trenniAndmed.get(kuupäev);
    }

    public boolean looUusKuupäev(String kuupäev) {
        if(this.trenniAndmed.containsKey(kuupäev)) // vana kuupäev kirjutatakse üle ilma selleta
            return false;

        ArrayList<Trenn> mingidTrennid = new ArrayList<>();
        this.trenniAndmed.put(kuupäev, mingidTrennid);
        return true;
    }

    public boolean kustutaKuupäev(String kuupäev) {
        return this.trenniAndmed.remove(kuupäev) != null;
    }

    public List<String> tagastaKõikKuupäevad() {
        //kõik kuupäevad uude listi
        List<String> kõikKuupäevad=new ArrayList<>();
        if (this.trenniAndmed.isEmpty()) return kõikKuupäevad;
        this.trenniAndmed.forEach((key, value) -> {
            kõikKuupäevad.add(key);
        });
        return kõikKuupäevad;
    }

    private List<Andmeväli> loeAndmeväljadRekursiivselt(JSONObject praeguseVäljaInfo) {
        ArrayList<Andmeväli> andmeväljad = new ArrayList<>();
        JSONArray andmeväljadJson = praeguseVäljaInfo.getJSONArray("väljad");

        Iterator<Object> it = andmeväljadJson.iterator();
        while (it.hasNext()) {
            JSONObject väljaInfo = (JSONObject) it.next();
            String id = väljaInfo.getString("id");
            String väljaNimi = väljaInfo.getString("nimi");
            String väärtus = väljaInfo.getString("väärtus");

            // rekursiivselt paneme kirja sisemised väljad
            List<Andmeväli> sisemisedVäljad = new ArrayList<>();
            if (!väljaInfo.getJSONArray("väljad").isEmpty())
                sisemisedVäljad = this.loeAndmeväljadRekursiivselt(väljaInfo);

            Andmeväli väli = new Andmeväli(id, väljaNimi, väärtus, sisemisedVäljad);
            andmeväljad.add(väli);
        }

        return andmeväljad;
    }

    public void laeAndmebaas() throws FileNotFoundException {
        JSONTokener jsonFail = new JSONTokener(new FileReader(ANDMEBAASI_FAIL));

        // vaatame kõik kuupäevad andmebaasis läbi
        JSONObject kuupäevadKoosTrennidega = new JSONObject(jsonFail);
        for (String kuupäev : kuupäevadKoosTrennidega.keySet()) {
            this.trenniAndmed.put(kuupäev, new ArrayList<>());

            // vaatame läbi kõik trennid, mis sel kuupäeval tegime
            JSONArray treeningud = kuupäevadKoosTrennidega.getJSONArray(kuupäev);
            for (Object o : treeningud) {
                JSONObject trennJson = (JSONObject) o;
                String trenniNimi = trennJson.getString("nimi");
                String trenniKestvus = trennJson.getString("kestvus");
                String id = trennJson.getString("id");

                // lisame trenni kindlasse kuupäeva
                Trenn trenn = new Trenn(id, trenniKestvus, trenniNimi);
                List<Trenn> trennidKuupäeval = this.trenniAndmed.get(kuupäev);
                trennidKuupäeval.add(trenn);

                // loeme trenni andmeväljad eraldi objekti, siis uuendame trenni infot
                List<Andmeväli> andmeväljad = this.loeAndmeväljadRekursiivselt(trennJson);
                trenn.setPeamisedVäljad(andmeväljad);
            }
        }
    }

    private JSONArray väljaObjektidJsoniks(List<Andmeväli> väljad) {
        JSONArray väljadJson = new JSONArray();
        for (Andmeväli andmed : väljad) {
            JSONObject andmedJson = new JSONObject();
            andmedJson.put("id", andmed.getId());
            andmedJson.put("nimi", andmed.getNimi());
            andmedJson.put("väärtus", andmed.getVäärtus());
            if (andmed.getSisemisedVäljad().size() != 0)
                andmedJson.put("väljad", this.väljaObjektidJsoniks(andmed.getSisemisedVäljad()));
            else
                andmedJson.put("väljad", new JSONArray());

            // lisame andmete Jsoni listi
            väljadJson.put(andmedJson);
        }

        return väljadJson;
    }

    private JSONArray trenniObjektidJsoniks(List<Trenn> trennid) {
        JSONArray trennidJson = new JSONArray();
        for (Trenn trenn : trennid) {
            JSONObject trenniJson = new JSONObject();
            trenniJson.put("id", trenn.getId());
            trenniJson.put("nimi", trenn.getNimi());
            trenniJson.put("kestvus", trenn.getKestvus());
            trenniJson.put("väljad", this.väljaObjektidJsoniks(trenn.getPeamisedVäljad()));

            trennidJson.put(trenniJson);
        }

        return trennidJson;
    }

    public void salvestaAndmebaas() throws java.io.IOException {
        // Java objektide muundamine JSONiks
        JSONObject uusAndmebaas = new JSONObject();
        for (String kuupäev : this.trenniAndmed.keySet()) {
            List<Trenn> trennid = this.trenniAndmed.get(kuupäev);
            uusAndmebaas.put(kuupäev, this.trenniObjektidJsoniks(trennid));
        }

        // faili salvestamine
        File andmebaasiFail = new File(ANDMEBAASI_FAIL);
        try (FileWriter salvestus = new FileWriter(andmebaasiFail)) {
            salvestus.write(uusAndmebaas.toString());
            salvestus.flush();
        }
    }

    // testimise meetod
    public static void main(String[] args) throws Exception {
        Andmebaas andmed = new Andmebaas();
        andmed.laeAndmebaas();

        //need andmed on nüüd andmebaasis:
        //andmed.looUusKuupäev("2023-03-18");
        //andmed.lisaTrenn("2023-03-18", "Jalutamine", "80min");
        //andmed.looUusKuupäev("2023-02-28");
        //andmed.lisaTrenn("2023-02-28", "Jõutrenn", "60min");
        //andmed.lisaTrenn("2023-04-05", "Jooks", "30min");
        System.out.println(andmed.tagastaKõikKuupäevad());
        andmed.salvestaAndmebaas();
    }
}
