package com.tubes.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tubes/main.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600); // Set ukuran window

        stage.setScene(scene);
        stage.setTitle("That's Not My Neighbor");
        stage.setResizable(false); // Window tidak bisa di-resize
        stage.centerOnScreen();    // Tampilkan di tengah layar
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
