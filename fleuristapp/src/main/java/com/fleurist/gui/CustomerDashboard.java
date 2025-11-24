package com.fleurist.gui;

import com.fleurist.App;
import com.fleurist.account.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CustomerDashboard {

    public static void show(Stage stage, Customer customer) {
        // 1. Header
        Label lblBrand = new Label("Fleurist 🌸");
        lblBrand.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        lblBrand.setTextFill(Color.web("#D81B60"));

        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> {
            try {
                new App().start(stage);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        VBox topBar = new VBox(10, lblBrand, btnLogout);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #FFDAB9;");

        // 2. Konten
        Label lblWelcome = new Label("Selamat Belanja, " + customer.getUsername());
        VBox center = new VBox(20, lblWelcome);
        center.setAlignment(Pos.CENTER);

        // 3. Layout
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(center);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}