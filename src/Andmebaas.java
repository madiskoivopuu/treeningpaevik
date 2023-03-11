import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jdk.jshell.spi.ExecutionControl;
import org.json.*;

public class Andmebaas {
    public static final String ANDMEBAASI_FAIL = "andmebaas.json";
    private HashMap<String, List<Trenn>> trenniAndmed;

    public Andmebaas() {
        this.trenniAndmed = new HashMap<>();
    }

    //lisaTrenn(String kuupäev, String nimi, String kestvus)
    //looUusKuupäev(String kuupäev)
    private List<Andmeväli> loeAndmeväljadRekursiivselt(JSONObject praeguseVäljaInfo) {
        ArrayList<Andmeväli> andmeväljad = new ArrayList<>();
        JSONArray andmeväljadJson = praeguseVäljaInfo.getJSONArray("väljad");

        Iterator<Object> it = andmeväljadJson.iterator();
        while(it.hasNext()) {
            JSONObject väljaInfo = (JSONObject)it.next();
            String väljaNimi = väljaInfo.getString("nimi");
            String väärtus = väljaInfo.getString("väärtus");

            // rekursiivselt paneme kirja sisemised väljad
            List<Andmeväli> sisemisedVäljad = null;
            if(!väljaInfo.isNull("väljad"))
                sisemisedVäljad = this.loeAndmeväljadRekursiivselt(väljaInfo);

            Andmeväli väli = new Andmeväli(väljaNimi, väärtus, sisemisedVäljad);
            andmeväljad.add(väli);
        }

        return andmeväljad;
    }

  	public void laeAndmebaas() throws FileNotFoundException {
        JSONTokener jsonFail = new JSONTokener(new FileReader(ANDMEBAASI_FAIL));

        // vaatame kõik kuupäevad andmebaasis läbi
        JSONObject kuupäevadKoosTrennidega = new JSONObject(jsonFail);
        for(String kuupäev : kuupäevadKoosTrennidega.keySet()) {
            this.trenniAndmed.put(kuupäev, new ArrayList<>());

            // vaatame läbi kõik trennid, mis sel kuupäeval tegime
            JSONArray treeningud = kuupäevadKoosTrennidega.getJSONArray(kuupäev);
            Iterator<Object> it = treeningud.iterator();
            while(it.hasNext()) {
                JSONObject trennJson = (JSONObject)it.next();
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
        throw new ExecutionControl.NotImplementedException("pole valmis");
    }
    private JSONArray trenniObjektidJsoniks(List<Trenn> trennid) {
        JSONArray trennidJson = new JSONArray();
        for(Trenn trenn : trennid) {
            JSONObject trenniJson = new JSONObject();
            trenniJson.put("id", trenn.getId());
            trenniJson.put("nimi", trenn.getNimi());
            trenniJson.put("kestvus", trenn.getKestvus());
            trenniJson.put("väljad", this.väljaObjektidJsoniks(trenn.getPeamisedVäljad()));

            trennidJson.put(trenniJson);
        }

        return trennidJson;
    }

    public void salvestaAndmebaas() {
        JSONObject uusAndmebaas = new JSONObject();
        for(String kuupäev : this.trenniAndmed.keySet()) {
            List<Trenn> trennid = this.trenniAndmed.get(kuupäev);
            uusAndmebaas.put(kuupäev, this.trenniObjektidJsoniks(trennid));
        }
    }

    // testimise meetod
    public static void main(String[] args) throws Exception {
        Andmebaas andmed = new Andmebaas();
        andmed.laeAndmebaas();
    }
}
