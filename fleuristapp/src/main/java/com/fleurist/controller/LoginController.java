/**
 * Kelas LoginController mengontrol logika login dan registrasi 
 * pengguna baru pada tampilan awal aplikasi.
 * @author Kelompok 11
 */

package com.fleurist.controller;

import com.fleurist.App;
import com.fleurist.account.Akun;
import com.fleurist.account.Admin;
import com.fleurist.account.Customer;
import com.fleurist.driver.Driver;
import com.fleurist.gui.CustomerDashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField txtUser; 

    @FXML
    private PasswordField txtPass; 

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    public void initialize() {
        Driver.init();

        // Isi ComboBox Role
        cbRole.getItems().addAll("Admin", "Customer");
        cbRole.getSelectionModel().selectFirst();
    }
    

    @FXML
    private void handleLogin() {
        String username = txtUser.getText();
        String password = txtPass.getText();
        String selectedRole = cbRole.getValue();

        if (selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, "Gagal", "Harap pilih role (Admin/Customer).");
            return;
        }

        Akun user = Driver.login(username, password);

        if (user != null) {
            String actualRole = (user instanceof Admin) ? "Admin" : "Customer";
            
            // Cek Konsistensi Role
            if (!actualRole.equals(selectedRole)) {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Role yang dipilih (" + selectedRole + ") tidak sesuai dengan akun Anda.");
                return;
            }
            
            //Lanjut ke Dshboard
            Stage currentStage = (Stage) txtUser.getScene().getWindow();

            if (user instanceof Admin) {
                openDashboard("/com/fleurist/view/admin_dashboard.fxml", currentStage, null);
            } else {
                openDashboard("/com/fleurist/view/customer_dashboard.fxml", currentStage, (Customer) user);
            }

        } else {
            // FIX: Login Gagal menggunakan tipe ERROR
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau Password salah!");
        }
    }

    // --- FITUR REGISTRASI ---
    @FXML
    private void handleRegistration() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Registrasi Akun Baru");
        dialog.setHeaderText("Isi data untuk membuat akun customer:");

        ButtonType registerButtonType = new ButtonType("Daftar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(); nameField.setPromptText("Nama Lengkap");
        TextField usernameField = new TextField(); usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField(); passwordField.setPromptText("Password");

        grid.add(new Label("Nama Lengkap:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Username:"), 0, 1); grid.add(usernameField, 1, 1);
        grid.add(new Label("Password:"), 0, 2); grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String name = nameField.getText().trim();

                if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Gagal", "Semua kolom wajib diisi."); // Peringatan
                    return null;
                }
                
                for (Akun a : Driver.getAkunList()) {
                    if (a.getUsername().equalsIgnoreCase(username)) {
                        showAlert(Alert.AlertType.ERROR, "Gagal", "Username sudah digunakan.");
                        return null;
                    }
                }
                
                return new Customer(username, password, name);
            }
            return null;
        });

        Optional<Customer> result = dialog.showAndWait();

        result.ifPresent(newCustomer -> {
            Driver.getAkunList().add(newCustomer);
            // FIX: Menggunakan tipe INFORMATION untuk sukses
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Registrasi berhasil! Silakan login."); 
            System.out.println("Akun baru terdaftar: " + newCustomer.getUsername());
        });
    }
    // --- AKHIR FITUR REGISTRASI ---


    private void openDashboard(String fxmlPath, Stage stage, Customer customerData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (customerData != null) {
                CustomerController controller = loader.getController();
                controller.setCustomer(customerData);
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error System", "Gagal memuat halaman: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
