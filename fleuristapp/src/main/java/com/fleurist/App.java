/**
 * Kelas App adalah entry point utama aplikasi JavaFX (GUI).
 * Kelas ini bertanggung jawab untuk memuat scene login dan memulai Stage.
 * @author Kelompok 11
 */
package com.fleurist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Memuat file FXML dari folder resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/fleurist/view/login.fxml"));
        
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 450, 550);
        
        stage.setTitle("Fleurist - Toko Bunga");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
