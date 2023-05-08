package com.example.treeningpaevik;

import com.calendarfx.view.YearMonthView;
import com.example.treeningpaevik.andmebaas.Trenn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;



import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class TrennideList extends Pane {
    List<Trenn> trennid;
    YearMonthView kalender;
    ListView<String> trenniNimekiri;
    public TrennideList() {
        // TODO: lugeda andmed, või siis
        this.trennid = new ArrayList<>();
        this.trenniNimekiri = new ListView<String>();

        this.kalender = new YearMonthView();
        this.kalender.setShowTodayButton(false); // eemaldab veidra nupu kalendri alt

        VBox vertikaalneJagaja = new VBox();
        vertikaalneJagaja.setPadding(new Insets(10, 10, 10, 50));
        vertikaalneJagaja.setSpacing(10); // iga elemendi vahel 10 px

        vertikaalneJagaja.getChildren().add(this.kalender);
        vertikaalneJagaja.getChildren().add(new Label("Trennid"));
        vertikaalneJagaja.getChildren().add(this.trenniNimekiri);

        // lisame trennid listi
        // TODO: tegelikud trennid ka lisada
        for (int i = 0; i < 20; i++) {
            this.trenniNimekiri.getItems().add("Jooks - 35 min");
        }

        // seame eventide kuulajad üles
        this.trenniNimekirjaKuulaja();
        this.kalendriValikuKuulaja();

        this.getChildren().add(vertikaalneJagaja);
    }

    // Kuulab valitud elemendi muudatusi trennide nimekirja listis, saadab muudatuse koos valitud trenniga edasi TrenniValikMuudetiEvent kaudu
    public void trenniNimekirjaKuulaja() {
        this.trenniNimekiri.setOnMouseClicked(mouseEvent -> {
            Trenn trenn = this.trennid.get(
                    this.trenniNimekiri.getSelectionModel().getSelectedIndex()
            );

            this.fireEvent(new TrenniValikMuudetiEvent(TrenniValikMuudetiEvent.TRENNI_MUUDETI, trenn));
        });
    }

    public void kalendriValikuKuulaja() {
        // TODO: implementeerida
    }
}
