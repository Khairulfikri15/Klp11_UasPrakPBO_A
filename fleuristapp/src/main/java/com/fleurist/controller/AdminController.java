package com.fleurist.controller;

import com.fleurist.App;
import com.fleurist.driver.Driver;
import com.fleurist.model.Barang;
import com.fleurist.model.Diskon;
import com.fleurist.model.Transaksi;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AdminController {

    @FXML private Label lblWelcome;

    // --- TAB 1: BARANG ---
    @FXML private TableView<Barang> tableBarang;
    @FXML private TableColumn<Barang, String> colId;
    @FXML private TableColumn<Barang, String> colNama;
    @FXML private TableColumn<Barang, String> colJenis;
    @FXML private TableColumn<Barang, String> colWarna;
    @FXML private TableColumn<Barang, String> colTipe;
    @FXML private TableColumn<Barang, Integer> colStok;
    @FXML private TableColumn<Barang, Double> colHarga;

    // --- TAB 2: DISKON ---
    @FXML private TableView<Diskon> tableDiskon;
    @FXML private TableColumn<Diskon, String> colKodeDiskon;
    @FXML private TableColumn<Diskon, String> colNamaDiskon;
    @FXML private TableColumn<Diskon, Double> colPersenDiskon;
    @FXML private TableColumn<Diskon, String> colStatusDiskon;

    // --- TAB 3: TRANSAKSI ---
    @FXML private TableView<Transaksi> tableTransaksi;
    @FXML private TableColumn<Transaksi, String> colIdTrx;
    @FXML private TableColumn<Transaksi, Double> colTotalTrx;
    @FXML private TableColumn<Transaksi, String> colWaktuTrx;
    @FXML private TableColumn<Transaksi, String> colStatusTrx;

    @FXML
    public void initialize() {
        setupTableBarang();
        setupTableDiskon();
        setupTableTransaksi();
    }

    // ========================= SETUP TABEL =========================

    private void setupTableBarang() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colWarna.setCellValueFactory(new PropertyValueFactory<>("warna"));
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipeProduk"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        refreshData();
    }

    private void setupTableDiskon() {
        // Perhatikan: PropertyValueFactory harus sesuai nama getter (getIdDiskon -> "idDiskon")
        colKodeDiskon.setCellValueFactory(new PropertyValueFactory<>("idDiskon"));
        colNamaDiskon.setCellValueFactory(new PropertyValueFactory<>("namaDiskon"));
        colPersenDiskon.setCellValueFactory(new PropertyValueFactory<>("persen"));
        
        // Custom Cell untuk Status Aktif/Nonaktif
        colStatusDiskon.setCellValueFactory(cellData -> {
            boolean aktif = cellData.getValue().isAktif();
            return new SimpleStringProperty(aktif ? "AKTIF" : "NONAKTIF");
        });
    }

    private void setupTableTransaksi() {
        colIdTrx.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colTotalTrx.setCellValueFactory(new PropertyValueFactory<>("totalAkhir"));
        colWaktuTrx.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggal().toString())); // Sederhana
        
        colStatusTrx.setCellValueFactory(cellData -> {
            boolean diterima = cellData.getValue().isDiterimaAdmin();
            return new SimpleStringProperty(diterima ? "SELESAI" : "MENUNGGU");
        });
    }

    private void refreshData() {
        tableBarang.setItems(FXCollections.observableArrayList(Driver.getCatalog().getBarangList()));
        tableDiskon.setItems(FXCollections.observableArrayList(Driver.getCatalog().getDiskonList()));
        tableTransaksi.setItems(FXCollections.observableArrayList(Driver.getTransaksiList()));
    }

    // ========================= FITUR BARANG (CRUD) =========================

    @FXML
    private void handleTambah() {
        Dialog<Barang> dialog = new Dialog<>();
        dialog.setTitle("Tambah Barang");
        dialog.setHeaderText("Isi detail produk:");
        ButtonType saveBtn = new ButtonType("Simpan", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNama = new TextField(); txtNama.setPromptText("Nama");
        TextField txtHarga = new TextField(); txtHarga.setPromptText("Harga");
        TextField txtStok = new TextField(); txtStok.setPromptText("Stok");
        TextField txtWarna = new TextField(); txtWarna.setPromptText("Warna");
        TextField txtJenis = new TextField(); txtJenis.setPromptText("Jenis");
        TextField txtTipe = new TextField(); txtTipe.setPromptText("Tipe");
        TextField txtImage = new TextField(); txtImage.setPromptText("File Gambar"); 

        grid.add(new Label("Nama:"), 0, 0); grid.add(txtNama, 1, 0);
        grid.add(new Label("Harga:"), 0, 1); grid.add(txtHarga, 1, 1);
        grid.add(new Label("Stok:"), 0, 2); grid.add(txtStok, 1, 2);
        grid.add(new Label("Warna:"), 0, 3); grid.add(txtWarna, 1, 3);
        grid.add(new Label("Jenis:"), 0, 4); grid.add(txtJenis, 1, 4);
        grid.add(new Label("Tipe:"), 0, 5); grid.add(txtTipe, 1, 5);
        grid.add(new Label("File Gambar:"), 0, 6); grid.add(txtImage, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    String idBaru = "P" + String.format("%03d", Driver.getCatalog().getBarangList().size() + 1);
                    return new Barang(idBaru, txtNama.getText(), Double.parseDouble(txtHarga.getText()),
                            Integer.parseInt(txtStok.getText()), txtWarna.getText(), txtJenis.getText(), txtTipe.getText(), txtImage.getText());
                } catch (Exception e) { showAlert("Error", "Input Harga/Stok/Tipe harus angka/teks!"); return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(b -> {
            Driver.getCatalog().tambahBarang(b);
            refreshData();
        });
    }

    // METHOD EDIT BARANG
    @FXML
    private void handleEdit() {
        Barang selected = tableBarang.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Warning", "Pilih barang dulu!"); return; }

        Dialog<Barang> dialog = new Dialog<>();
        dialog.setTitle("Edit Barang");
        dialog.setHeaderText("Edit data untuk: " + selected.getNama());
        ButtonType saveBtn = new ButtonType("Update", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        // PRE-FILL DATA LAMA (8 Argumen)
        TextField txtNama = new TextField(selected.getNama());
        TextField txtHarga = new TextField(String.valueOf(selected.getHarga()));
        TextField txtStok = new TextField(String.valueOf(selected.getStok()));
        TextField txtWarna = new TextField(selected.getWarna());
        TextField txtJenis = new TextField(selected.getJenis());
        TextField txtTipe = new TextField(selected.getTipeProduk());
        TextField txtImage = new TextField(selected.getImageFile()); // File Gambar

        grid.add(new Label("ID:"), 0, 0); grid.add(new Label(selected.getId()), 1, 0); // ID Tetap
        grid.add(new Label("Nama:"), 0, 1); grid.add(txtNama, 1, 1);
        grid.add(new Label("Harga:"), 0, 2); grid.add(txtHarga, 1, 2);
        grid.add(new Label("Stok:"), 0, 3); grid.add(txtStok, 1, 3);
        grid.add(new Label("Warna:"), 0, 4); grid.add(txtWarna, 1, 4);
        grid.add(new Label("Jenis:"), 0, 5); grid.add(txtJenis, 1, 5);
        grid.add(new Label("Tipe:"), 0, 6); grid.add(txtTipe, 1, 6);
        grid.add(new Label("File Gambar:"), 0, 7); grid.add(txtImage, 1, 7); 

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    Barang updatedBarang = new Barang(
                        selected.getId(),
                        txtNama.getText(),
                        Double.parseDouble(txtHarga.getText()),
                        Integer.parseInt(txtStok.getText()),
                        txtWarna.getText(),
                        txtJenis.getText(),
                        txtTipe.getText(),
                        txtImage.getText()
                    );
                    return updatedBarang;
                } catch (Exception e) { showAlert("Error", "Input Harga/Stok harus angka!"); return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedBarang -> {
            Driver.getCatalog().editBarang(updatedBarang.getId(), updatedBarang);
            refreshData(); 
            showAlert("Sukses", "Data barang berhasil diperbarui!");
        });
    }


    @FXML
    private void handleHapus() {
        Barang selected = tableBarang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Driver.getCatalog().hapusBarang(selected.getId());
            refreshData();
            showAlert("Sukses", "Barang berhasil dihapus.");
        }
    }

    // ========================= FITUR DISKON =========================

    @FXML
    private void handleTambahDiskon() {
        Dialog<Diskon> dialog = new Dialog<>();
        dialog.setTitle("Tambah Voucher");
        dialog.setHeaderText("Buat kode diskon baru:");
        ButtonType saveBtn = new ButtonType("Simpan", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField txtKode = new TextField(); txtKode.setPromptText("Contoh: LEBARAN");
        TextField txtNama = new TextField(); txtNama.setPromptText("Contoh: Diskon Hari Raya");
        TextField txtPersen = new TextField(); txtPersen.setPromptText("Contoh: 20");

        grid.add(new Label("Kode Unik:"), 0, 0); grid.add(txtKode, 1, 0);
        grid.add(new Label("Nama Promo:"), 0, 1); grid.add(txtNama, 1, 1);
        grid.add(new Label("Persen (%):"), 0, 2); grid.add(txtPersen, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    double p = Double.parseDouble(txtPersen.getText());
                    return new Diskon(txtKode.getText().toUpperCase(), txtNama.getText(), p, true);
                } catch (Exception e) { return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(d -> {
            Driver.getCatalog().tambahDiskon(d);
            refreshData();
        });
    }

    @FXML
    private void handleToggleDiskon() {
        Diskon selected = tableDiskon.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setAktif(!selected.isAktif());
            tableDiskon.refresh(); 
            showAlert("Sukses", "Status diskon berhasil diubah.");
        } else {
            showAlert("Warning", "Pilih diskon dulu!");
        }
    }
    
    @FXML
    private void handleHapusDiskon() {
        Diskon selected = tableDiskon.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Driver.getCatalog().hapusDiskon(selected.getIdDiskon());
            refreshData();
        }
    }

    // ========================= FITUR TRANSAKSI =========================

    @FXML
    private void handleTerimaTransaksi() {
        Transaksi selected = tableTransaksi.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih transaksi di tabel dulu!");
            return;
        }

        if (!selected.isDiterimaAdmin()) {
            // 1. Update status di objek
            selected.terimaTransaksi();
            
            // 2. Refresh tampilan tabel secara paksa (INI KUNCINYA)
            tableTransaksi.refresh(); 
            
            showAlert("Sukses", "Transaksi ID " + selected.getIdTransaksi() + " telah diselesaikan. Status diperbarui.");
        } else {
            showAlert("Info", "Transaksi ini sudah selesai (Diterima).");
        }
    }

    // ========================= UMUM =========================

    @FXML
    private void handleLogout() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/fleurist/view/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) lblWelcome.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
