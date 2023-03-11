{
    "2023-04-05": [
        {
        "id": 7
        "kestvus": "50min",
        "nimi": "Jooks",
        "väljad": [
            "nimi": "km",
            "väärtus": "5",
            "väljad": [...] / null
        ],
        },
    ]
}


class AndmeVäli {
    String nimi;
    String väärtus;
    List<AndmeVäli> sisemisedVäljad;
}

class Trenn {
    int id;
    String kestvus;
    String nimi;
    List<AndmeVäli> peamisedVäljad;
}

class Andmebaas {
    d = {"a": "b", "c": 1}
    HashMap<String, List<Trenn>> andmed;

    * lisaTrenn(String kuupäev, String nimi, String kestvus)
    * looUusKuupäev(String kuupäev)
  	* laeAndmebaas()
  	* salvestaAndmebaas()
}

// peaklass
class Graafikaliides {
    void näitaTrenneKuupäeval(List<Trenn> trennid) {
        while(true) {
            if(käsk == "q")
                return;
        }
    }
    void näitaKuupäevi(Andmebaas a) {
        while(true) {
            if(käsk == "q")
                return;
        }
    }
    public static void main(String[] args) {
        // lae andmebaas
    }
}



1 - näita kõiki trenne ([ID: 7] Jooks - 50 min)
2 - vali trenn ID-ga
q -