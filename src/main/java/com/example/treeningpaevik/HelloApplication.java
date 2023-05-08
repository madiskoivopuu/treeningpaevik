package com.example.treeningpaevik;

import com.example.treeningpaevik.andmebaas.Andmebaas;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Andmebaas andmebaas = new Andmebaas();
        andmebaas.laeAndmebaas();

        BorderPane juur = new BorderPane();

        juur.setLeft(new TrennideList());

        Scene scene = new Scene(juur, 320, 240);
        stage.setTitle("Treeningpäevik");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}