package com.fleurist.gui;

import com.fleurist.App;
import com.fleurist.account.Admin;
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

public class AdminDashboard {

    public static void show(Stage stage, Admin admin) {
        // 1. Header
        Label lblHeader = new Label("ADMIN DASHBOARD");
        lblHeader.setTextFill(Color.WHITE);
        lblHeader.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label lblUser = new Label("Login sebagai: " + admin.getUsername());
        lblUser.setTextFill(Color.WHITE);

        VBox header = new VBox(5, lblHeader, lblUser);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #2E8B57;");

        // 2. Menu
        Button btnLogout = new Button("Log Out");
        btnLogout.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            try {
                new App().start(stage); // Kembali ke Login
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        VBox menuBox = new VBox(15, new Label("Fitur Admin (Coming Soon)"), btnLogout);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(30));

        // 3. Layout
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(menuBox);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
