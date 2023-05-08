package com.example.treeningpaevik;

import com.example.treeningpaevik.andmebaas.Trenn;
import javafx.event.Event;
import javafx.event.EventType;

public class TrenniValikMuudetiEvent extends Event {
    private static final EventType<TrenniValikMuudetiEvent> mikssedategemapeab = new EventType<>(ANY);
    public static final EventType<TrenniValikMuudetiEvent> TRENNI_MUUDETI = new EventType<>(mikssedategemapeab, "TRENNI_MUUDETI");
    private Trenn trenn;
    public TrenniValikMuudetiEvent(EventType<? extends Event> eventType, Trenn trenn) {
        super(eventType);
        this.trenn = trenn;
    }

    public Trenn getTrenn() {
        return trenn;
    }
}
