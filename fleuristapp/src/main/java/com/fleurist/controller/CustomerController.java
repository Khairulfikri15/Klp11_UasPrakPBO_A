/**
 * Kelas CustomerController menangani semua event dan logika tampilan 
 * untuk Dashboard Customer (Katalog, Keranjang, dan History Transaksi).
 * @author Kelompok 11
 */

package com.fleurist.controller;

import com.fleurist.App;
import com.fleurist.account.Customer;
import com.fleurist.driver.Driver;
import com.fleurist.model.Barang;
import com.fleurist.model.Keranjang.ItemKeranjang;
import com.fleurist.model.Transaksi;
import com.fleurist.model.Diskon;
import com.fleurist.model.pembayaran.Bank;
import com.fleurist.model.pembayaran.COD;
import com.fleurist.model.pembayaran.Pembayaran;
import com.fleurist.model.pembayaran.QRIS;
import com.fleurist.model.Invoice;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea; 
import javafx.scene.control.Dialog; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList; 
import java.util.List; 
import java.util.Optional;

public class CustomerController {

    @FXML private Label lblWelcome;
    @FXML private Label lblTotalCart;
    @FXML private ComboBox<String> cbJenis;
    @FXML private ComboBox<String> cbTipe;

    @FXML private FlowPane catalogContainer;
    @FXML private TableView<ItemKeranjang> tableCart;
    @FXML private TableColumn<ItemKeranjang, String> colCartNama;
    @FXML private TableColumn<ItemKeranjang, Double> colCartHarga;
    @FXML private TableColumn<ItemKeranjang, Integer> colCartJumlah;
    @FXML private TableColumn<ItemKeranjang, Double> colCartSubtotal;
    @FXML private TableView<Transaksi> tableHistory;
    @FXML private TableColumn<Transaksi, String> colHistId;
    @FXML private TableColumn<Transaksi, String> colHistTanggal;
    @FXML private TableColumn<Transaksi, Double> colHistTotal;
    @FXML private TableColumn<Transaksi, String> colHistStatus;

    private Customer loggedUser;

    // --- INITIALIZE & SETUP ---
    public void setCustomer(Customer customer) {
        this.loggedUser = customer;
        lblWelcome.setText("Halo, " + customer.getUsername());
        setupTableColumns();
        loadCatalogFilterData(); 
        loadCart();
        loadHistory();

        // Saat baris History di-double click, panggil handleViewInvoice
                tableHistory.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) { 
                        handleViewInvoice();
                    }
                });

    }
    
    private void setupTableColumns() {
        // Setup Tabel Keranjang
        colCartNama.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarang().getNama()));
        colCartHarga.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getBarang().getHarga()));
        colCartJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        tableCart.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // Setup Tabel History
        colHistId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colHistTotal.setCellValueFactory(new PropertyValueFactory<>("totalAkhir"));
        
        // 1. FORMAT HARGA (Menambah Rp dan pemisah ribuan)
        colHistTotal.setCellFactory(column -> new TableCell<Transaksi, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %,.0f", item));
                }
            }
        });

        // 2. FORMAT TANGGAL
        colHistTanggal.setCellValueFactory(cell -> new SimpleStringProperty(
            java.time.LocalDateTime.parse(cell.getValue().getTanggal().toString())
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        ));
        
        // 3. COLOR CODE STATUS
        colHistStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isDiterimaAdmin() ? "DITERIMA" : "MENUNGGU"));
        colHistStatus.setCellFactory(column -> new TableCell<Transaksi, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("status-selesai", "status-menunggu");
                
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("DITERIMA")) {
                        getStyleClass().add("status-selesai");
                    } else if (item.equals("MENUNGGU")) {
                        getStyleClass().add("status-menunggu");
                    }
                }
            }
        });
    }

    // --- LOGIKA FILTER ---

    private void loadCatalogFilterData() {
        cbJenis.getItems().clear();
        cbTipe.getItems().clear();
        
        cbJenis.getItems().add("Semua Jenis"); 
        cbJenis.getItems().addAll(Driver.getCatalog().getJenisUnik());
        cbTipe.getItems().add("Semua Tipe"); 
        cbTipe.getItems().addAll(Driver.getCatalog().getTipeUnik());
        
        cbJenis.getSelectionModel().selectFirst();
        cbTipe.getSelectionModel().selectFirst();
        
        refreshCatalogGrid(); 
    }
    
    @FXML
    private void handleFilter() {
        refreshCatalogGrid();
    }
    
    private void refreshCatalogGrid() {
        String selectedJenis = cbJenis.getValue();
        String selectedTipe = cbTipe.getValue();
        
        String jenisFilter = (selectedJenis == null || selectedJenis.equals("Semua Jenis")) ? null : selectedJenis;
        String tipeFilter = (selectedTipe == null || selectedTipe.equals("Semua Tipe")) ? null : selectedTipe;
        
        List<Barang> filteredList = Driver.getCatalog().getBarangByFilter(jenisFilter, tipeFilter);
        
        catalogContainer.getChildren().clear(); 

        for (Barang b : filteredList) {
            VBox card = createProductCard(b);
            catalogContainer.getChildren().add(card);
        }
    }

    private VBox createProductCard(Barang b) {
        VBox card = new VBox(10);
        card.setPrefSize(180, 260);
        card.getStyleClass().add("product-card");

        // GAMBAR
        ImageView imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        
        try {
            String specificImage = "/com/fleurist/images/" + b.getImageFile();
            String placeholderImage = "/com/fleurist/images/bunga.jpg"; 
            
            java.net.URL urlSpecific = getClass().getResource(specificImage);
            java.net.URL urlFinal;

            if (urlSpecific != null) {
                urlFinal = urlSpecific;
            } else {
                urlFinal = getClass().getResource(placeholderImage);
                if (urlFinal == null) { throw new Exception("Placeholder not found."); }
            }
            
            Image img = new Image(urlFinal.toExternalForm(), true); 
            imageView.setImage(img);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: Gagal memuat gambar untuk " + b.getNama() + ". Cek file.");
            imageView.setStyle("-fx-image: null;"); 
            card.setStyle("-fx-background-color: #f0f0f0;");
        }

        // TEKS
        Label nameLabel = new Label(b.getNama());
        nameLabel.getStyleClass().add("product-title");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label("Rp " + String.format("%,.0f", b.getHarga()));
        priceLabel.getStyleClass().add("product-price");

        Label stockLabel = new Label("Stok: " + b.getStok());
        stockLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");

        // TOMBOL BELI
        Button btnBuy = new Button("Tambah Keranjang");
        btnBuy.getStyleClass().addAll("button", "btn-customer");
        btnBuy.setMaxWidth(Double.MAX_VALUE);
        btnBuy.setOnAction(e -> handleAddToCart(b));

        card.getChildren().addAll(imageView, nameLabel, priceLabel, stockLabel, btnBuy);
        return card;
    }


    private void loadCart() {
        colCartNama.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarang().getNama()));
        colCartHarga.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getBarang().getHarga()));
        colCartJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        if (loggedUser != null) {
            tableCart.setItems(FXCollections.observableArrayList(loggedUser.getKeranjang().getItems()));
            lblTotalCart.setText("Total: Rp " + String.format("%,.0f", loggedUser.getKeranjang().totalKeranjang()));
        }
    }
    
    private void loadHistory() {
        colHistId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colHistTotal.setCellValueFactory(new PropertyValueFactory<>("totalAkhir"));
        colHistTanggal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTanggal().toString()));
        colHistStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isDiterimaAdmin() ? "DITERIMA" : "DIPROSES"));
        tableHistory.setItems(FXCollections.observableArrayList(Driver.getTransaksiList()));
    }
    
    // --- ACTIONS ---
    
    private void handleAddToCart(Barang selected) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Beli Barang");
        dialog.setHeaderText("Beli " + selected.getNama());
        dialog.setContentText("Jumlah:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(jumlahStr -> {
            try {
                int jumlah = Integer.parseInt(jumlahStr);
                if (jumlah > 0 && jumlah <= selected.getStok()) {
                    loggedUser.tambahKeKeranjang(selected, jumlah);
                    loadCart(); 
                    showAlert("Sukses", "✅ Berhasil masuk keranjang!");
                } else {
                    showAlert("Peringatan", "Stok tidak cukup.");
                }
            } catch (Exception e) { showAlert("Error", "Input harus angka."); }
        });
    }

    @FXML private void handleAddToCart() {}
    
    @FXML
    private void handleCheckout() {
        if (loggedUser.getKeranjang().getItems().isEmpty()) {
            showAlert("Peringatan", "Keranjang kosong!");
            return;
        }

        ObservableList<ItemKeranjang> selectedItems = tableCart.getSelectionModel().getSelectedItems();
        
        List<ItemKeranjang> itemsToProcess;
        if (selectedItems.isEmpty()) {
            Alert confirmAll = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAll.setTitle("Konfirmasi Checkout");
            confirmAll.setHeaderText("Anda belum memilih item.");
            confirmAll.setContentText("Apakah Anda ingin memproses SEMUA item dalam keranjang?");
            
            Optional<ButtonType> result = confirmAll.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                itemsToProcess = loggedUser.getKeranjang().getItems();
            } else {
                return;
            }
        } else {
            itemsToProcess = new ArrayList<>(selectedItems);
        }
        
        // 2. Kloning dan Swap Cart (Workaround)
        List<ItemKeranjang> originalCartItems = new ArrayList<>(loggedUser.getKeranjang().getItems());
        loggedUser.getKeranjang().getItems().clear(); 
        loggedUser.getKeranjang().getItems().addAll(itemsToProcess); 
        
        double subtotal = loggedUser.getKeranjang().totalKeranjang(); 

        // 3. LOGIKA DIALOG CHECKOUT
        Dialog<Transaksi> dialog = new Dialog<>();
        dialog.setTitle("Langkah Akhir: Checkout");
        
        Label lblSubtotal = new Label("Subtotal Pembelian Anda: Rp " + String.format("%,.0f", subtotal));
        lblSubtotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        dialog.setHeaderText("Konfirmasi Pembayaran");

        ButtonType checkoutButtonType = new ButtonType("Bayar Sekarang", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(checkoutButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 30, 10, 10));

        ComboBox<String> cbMetode = new ComboBox<>();
        cbMetode.getItems().addAll("1. COD", "2. Bank Transfer", "3. QRIS");
        cbMetode.getSelectionModel().selectFirst();
        
        TextField txtDiskon = new TextField();
        txtDiskon.setPromptText("Kode voucher (jika ada)");
        txtDiskon.setMaxWidth(180);

        Label lblTotalAkhir = new Label("TOTAL AKHIR: Rp " + String.format("%,.0f", subtotal));
        lblTotalAkhir.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #D81B60;");

        grid.add(lblSubtotal, 0, 0, 2, 1);
        grid.add(new Separator(), 0, 1, 2, 1);
        grid.add(new Label("Metode Bayar:"), 0, 2); grid.add(cbMetode, 1, 2);
        grid.add(new Label("Kode Diskon:"), 0, 3); grid.add(txtDiskon, 1, 3);
        grid.add(new Separator(), 0, 4, 2, 1);
        grid.add(new Label("TOTAL HARGA:"), 0, 5); grid.add(lblTotalAkhir, 1, 5);

        dialog.getDialogPane().setContent(grid);
        
        Runnable updateDisplay = () -> {
            double currentTotal = subtotal;
            String kode = txtDiskon.getText().trim().toUpperCase();
            Diskon d = Driver.getCatalog().cariDiskon(kode);
            
            if (d != null && d.isAktif()) {
                double persen = d.getPersen() / 100;
                currentTotal = subtotal - (subtotal * persen);
                lblTotalAkhir.setText("TOTAL AKHIR (Diskon " + d.getPersen() + "%): Rp " + String.format("%,.0f", currentTotal));
            } else {
                lblTotalAkhir.setText("TOTAL AKHIR: Rp " + String.format("%,.0f", subtotal));
            }
        };

        txtDiskon.textProperty().addListener((obs, oldV, newV) -> updateDisplay.run());
        updateDisplay.run(); // Initial calculation

        // 4. LOGIKA AKSI (Saat Tombol Bayar Ditekan)
        dialog.setResultConverter(btn -> {
            if (btn == checkoutButtonType) {
                // Ambil diskon lagi untuk final check
                String finalKode = txtDiskon.getText().trim().toUpperCase();
                Diskon finalDiskon = Driver.getCatalog().cariDiskon(finalKode);
                
                // --- LOGIKA INTERUPSI JIKA VOUCHER GAGAL ---
                if (!finalKode.isEmpty() && (finalDiskon == null || !finalDiskon.isAktif())) {
                    Alert confirmNormalPrice = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmNormalPrice.setTitle("Peringatan Voucher Gagal");
                    confirmNormalPrice.setHeaderText("Voucher Gagal Diterapkan.");
                    confirmNormalPrice.setContentText("Anda yakin ingin melanjutkan checkout dengan harga normal (Rp " + String.format("%,.0f", subtotal) + ")?");
                    
                    Optional<ButtonType> resultConfirmation = confirmNormalPrice.showAndWait();
                    if (resultConfirmation.isPresent() && resultConfirmation.get() != ButtonType.OK) {
                        return null; // CANCEL TRANSACTION (Menghentikan ResultConverter)
                    }
                }
                
                // Ambil data pembayaran
                Pembayaran p;
                String metodePilih = cbMetode.getValue();
                if (metodePilih.contains("COD")) p = new COD();
                else if (metodePilih.contains("Bank")) p = new Bank();
                else p = new QRIS();

                // Set diskon final (hanya jika valid/aktif)
                Diskon diskonAkhir = (finalDiskon != null && finalDiskon.isAktif()) ? finalDiskon : null;
                
                // Proses checkout backend
                Transaksi transaksi = loggedUser.checkout(p, diskonAkhir);
                return transaksi;
            }
            return null;
        });

        // 5. Tampilkan Dialog & Proses Akhir
        Optional<Transaksi> result = dialog.showAndWait();

        result.ifPresent(transaksi -> {
            // --- LOGIKA PENGURANGAN STOK ---
            transaksi.getKeranjang().getItems().forEach(item -> {
                Barang barangDibeli = item.getBarang();
                int jumlahDibeli = item.getJumlah();
                // Bug Fix: Gunakan 'jumlahDibeli' bukan 'jumlahDibali'
                barangDibeli.setStok(barangDibeli.getStok() - jumlahDibeli); 
            });
            
            // Logika Restore Cart
            originalCartItems.removeAll(itemsToProcess); 
            loggedUser.getKeranjang().getItems().clear();
            loggedUser.getKeranjang().getItems().addAll(originalCartItems); 
            
            Driver.getTransaksiList().add(transaksi);
            loadHistory(); 
            loadCart();    
            refreshCatalogGrid(); 
            
            showAlert("Sukses", "Transaksi berhasil dibuat.\nTotal Bayar: Rp " + String.format("%,.0f", transaksi.getTotalAkhir()));
        });
    }

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

    @FXML
    private void handleViewInvoice() {
        Transaksi selectedTrx = tableHistory.getSelectionModel().getSelectedItem();
        
        // Cek apakah ada yang dipilih
        if (selectedTrx == null) {
            showAlert("Peringatan", "Pilih transaksi yang ingin dilihat (double click pada baris)!");
            return;
        }

        // --- PERBAIKAN LOGIKA BISNIS UTAMA ---
        // Invoice hanya bisa dicetak jika statusnya sudah DITERIMA (isDiterimaAdmin = true)
        if (!selectedTrx.isDiterimaAdmin()) {
            showAlert("Peringatan", "Invoice belum bisa dicetak! Transaksi masih berstatus MENUNGGU konfirmasi Admin.");
            return; 
        }
        // -------------------------------------

        // Panggil logika Invoice yang sudah ada di model (Hanya jika lolos cek status)
        com.fleurist.model.Invoice inv = new com.fleurist.model.Invoice(selectedTrx);
        String invoiceText = inv.generate();

        // 1. Buat Dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Invoice: " + selectedTrx.getIdTransaksi());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        
        // 2. Gunakan TextArea untuk Menampilkan Invoice
        TextArea textArea = new TextArea(invoiceText);
        textArea.setEditable(false);
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(450);
        
        // Penting: Gunakan font monospace agar format spasi/tabel tidak rusak
        textArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 13px;");

        dialog.getDialogPane().setContent(textArea);
        dialog.showAndWait();
    }

    // --- Metode CustomerController.java ---
    @FXML
    private void handleRemoveFromCart() {
        // 1. Ambil item yang sedang dipilih di tabel keranjang
        ObservableList<ItemKeranjang> selectedItems = tableCart.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            showAlert("Peringatan", "Pilih item dari keranjang yang ingin dihapus.");
            return;
        }

        // 2. Konfirmasi penghapusan (Mencegah salah klik)
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Konfirmasi Hapus");
        confirmDialog.setHeaderText("Hapus " + selectedItems.size() + " item terpilih dari keranjang?");
        confirmDialog.setContentText("Tindakan ini tidak bisa dibatalkan.");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 3. Hapus item dari list Keranjang milik user
            loggedUser.getKeranjang().getItems().removeAll(selectedItems);

            // 4. Reload tabel dan total harga
            loadCart();
            showAlert("Sukses", "Item berhasil dihapus dari keranjang.");
        }
    }
}
