package himma.pendidikan.controller;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.MetodePembayaran;
import himma.pendidikan.model.PenyewaanPlaystation;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.service.PlayStationSrvc;
import himma.pendidikan.service.impl.PenyewaanPlayStationSrvcImpl;
import himma.pendidikan.service.impl.PlayStationSrvcImpl;
import himma.pendidikan.util.Session;
import himma.pendidikan.util.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import java.time.LocalDate;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static himma.pendidikan.controller.MetodePembayaranCtrl.metodePembayaranSrvc;

public class PenyewaanPlayStationCtrl {
    @FXML
    private VBox cartContent;
    @FXML
    private Label labelTotalBayar;
    @FXML
    private ComboBox<String> cbFilterStatus;
    @FXML
    private ComboBox<MetodePembayaran> cbMetodePembayaran;
    @FXML
    private FlowPane flowCards;
    @FXML
    private ScrollPane scrollCard;
    @FXML
    TextField tfSearch;
    @FXML
    private ScrollPane scrollCart;
    @FXML
    private TextField tfUangBayar;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfKembalian;

    @FXML
    private Button btnBayar;

    DBConnect connect = new DBConnect();
    Validation v = new Validation();

    public void initialize() {
        loadPlayStationCards();
        tfUangBayar.setOnAction(e -> handleBayar());
        handleClick();
    }

    private void loadPlayStationCards() {
        try {
            String query = "EXEC rps_getListPlayStation ?, ?, ?, ?, ?";
            PreparedStatement pstat = connect.conn.prepareStatement(query);

            // set null or default values for now
            pstat.setString(1, null); // search
            pstat.setString(2, null); // status
            pstat.setString(3, null); // idJenisPlayStation
            pstat.setString(4, "pst_id");
            pstat.setString(5, "ASC");

            ResultSet rs = pstat.executeQuery();

            while (rs.next()) {
                PlayStation ps = new PlayStation(
                        v.getInt(rs, "pst_id"),
                        v.getString(rs, "jps_nama"),
                        v.getString(rs, "pst_serial_number"),
                        v.getString(rs, "pst_merk"),
                        v.getDouble(rs, "pst_harga_per_jam"),
                        v.getString(rs, "pst_status"),
                        v.getString(rs, "pst_created_by")
                );

                flowCards.getChildren().add(createCard(ps));
            }

            rs.close();
            pstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSearch() {
        String search = tfSearch.getText().trim();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem() != null
                ? cbFilterStatus.getSelectionModel().getSelectedItem().toString()
                : null;

        loadCards(search, status, "pst_id", "ASC");
    }

    public void loadCards(String search, String status, String sortColumn, String sortOrder) {
        PlayStationSrvc playStationService = new PlayStationSrvcImpl();
        List<PlayStation> playStationList = playStationService.getAllData(search, status, sortColumn, sortOrder);

        FlowPane flow = new FlowPane();
        flow.setHgap(10);
        flow.setVgap(10);

        for (PlayStation metode : playStationList) {
            VBox card = createCard(metode);
            flow.getChildren().add(card);
        }

        scrollCard.setContent(flow);
    }
    public void handleClear() {
        cbFilterStatus.setValue("");
    }

    private VBox createCard(PlayStation ps) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #020A7A; -fx-background-radius: 12; -fx-padding: 10;");
        card.setPrefWidth(350);
        card.setPrefHeight(350);
        card.setSpacing(5);

        Label title = new Label("PlayStation - " + ps.getSerialNumber());
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white;");

        Label jenis = new Label("-  "+ps.getJenisPlaystation());
        Label pemain = new Label("- Max 2 Pemain");
        Label merk = new Label("- Merk: " + ps.getMerkPs());
        Label status = new Label("- Status: " + ps.getStatus());
        Label harga = new Label("-  Rp. " + String.format("%,.0f", ps.getHargaPS()) + " / Jam");

        for (Label label : List.of(jenis, pemain, merk, status, harga)) {
            label.setStyle("-fx-font-size: 16; -fx-text-fill: white;");
        }

        card.getChildren().addAll(title, jenis, pemain, merk, status, harga);

        // Add mouse click event handler on the entire card
        card.setOnMouseClicked(event -> {
            System.out.println("Clicked on: " + ps.getSerialNumber());
            addToCart(ps);  // <-- your method to add this item to the cart
        });

        return card;
    }

    PlayStation ps = new PlayStation();
    PenyewaanPlayStationCtrl penyewaanPlayStationCtrl = new PenyewaanPlayStationCtrl();

    public void handleAddData() {
        try {
            Integer karId = Session.getCurrentUser().getId();
            String nama = tfCustomerName.getText();
            String telepon = tfPhone.getText();
            java.sql.Date tanggal = java.sql.Date.valueOf(LocalDate.now());
            String createdBy = Session.getCurrentUser().getNama();
            java.sql.Date createDate = java.sql.Date.valueOf(LocalDate.now());

            double totalBayar = 0;
            for (Node node : cartContent.getChildren()) {
                Label priceLabel = (Label) node.lookup("#priceLabel");
                if (priceLabel != null) {
                    String text = priceLabel.getText().replace("Rp.", "").replace(".", "").replace(",", ".").trim();
                    totalBayar += Double.parseDouble(text);
                }
            }

            for (PlayStation ps : cart) {
                String status = "Sedang Disewa";

                PenyewaanPlaystation penyewaan = new PenyewaanPlaystation(
                        1, karId, 1, nama, telepon, tanggal, ps.getHargaPS(), status, createdBy, createDate
                );

                PenyewaanPlayStationSrvcImpl penyewaanService = new PenyewaanPlayStationSrvcImpl();
                penyewaanService.saveData(penyewaan); // <-- Make sure your service implementation works
            }

            System.out.println("Data berhasil ditambahkan.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Gagal menambahkan data penyewaan.");
        }
    }



    private final List<PlayStation> cart = new ArrayList<>();

    private void addToCart(PlayStation ps) {
        if (!cart.contains(ps)) {
            cart.add(ps);
            updateCartUI();
        } else {
            System.out.println("This item is already in the cart!");
        }
    }

    private void updateCartUI() {
        cartContent.getChildren().clear();

        for (PlayStation ps : cart) {
            VBox cartItem = new VBox(5);
            cartItem.setStyle("-fx-background-color: #020A7A; -fx-background-radius: 12; -fx-padding: 10;");
            cartItem.setPrefWidth(350);

            Label title = new Label("PlayStation - " + ps.getSerialNumber());
            title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            VBox jamMulaiBox = new VBox(2);
            Label lblMulai = new Label("Jam Mulai:");
            lblMulai.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            TextField tfMulai = new TextField("");
            tfMulai.setStyle("-fx-background-radius: 20; -fx-alignment: center;");
            tfMulai.setMaxWidth(100);
            jamMulaiBox.getChildren().addAll(lblMulai, tfMulai);

            VBox jamSelesaiBox = new VBox(2);
            Label lblSelesai = new Label("Jam Selesai:");
            lblSelesai.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            TextField tfSelesai = new TextField("");
            tfSelesai.setStyle("-fx-background-radius: 20; -fx-alignment: center;");
            tfSelesai.setMaxWidth(100);
            jamSelesaiBox.getChildren().addAll(lblSelesai, tfSelesai);

            Label lblHarga = new Label("Rp. 0");
            lblHarga.setId("priceLabel");

            lblHarga.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            Runnable updateHarga = () -> {
                String start = tfMulai.getText().trim();
                String end = tfSelesai.getText().trim();
                if (start.matches("\\d{2}:\\d{2}") && end.matches("\\d{2}:\\d{2}")) {
                    double harga = calculateHarga(start, end, ps.getHargaPS());
                    lblHarga.setText("Rp. " + String.format("%,.0f", harga));
                    updateTotalBayar();
                }
            };

            tfMulai.textProperty().addListener((obs, oldVal, newVal) -> updateHarga.run());
            tfSelesai.textProperty().addListener((obs, oldVal, newVal) -> updateHarga.run());

            Button removeBtn = new Button("-");
            removeBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-background-radius: 8;");
            removeBtn.setOnAction(e -> {
                cart.remove(ps);
                updateCartUI();
            });

            VBox removeBox = new VBox(removeBtn);
            removeBox.setAlignment(Pos.TOP_RIGHT);

            HBox waktuBox = new HBox(20, jamMulaiBox, jamSelesaiBox, removeBox);
            waktuBox.setStyle("-fx-padding: 5 0 5 0;");
            waktuBox.setAlignment(Pos.CENTER_LEFT);

            updateHarga.run();

            cartItem.getChildren().addAll(title, waktuBox, lblHarga);
            cartContent.getChildren().add(cartItem);
        }
    }


    private double calculateHarga(String startTime, String endTime, double hargaPerJam) {
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);

        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        int totalStartMinutes = startHour * 60 + startMinute;
        int totalEndMinutes = endHour * 60 + endMinute;

        int durationMinutes = totalEndMinutes - totalStartMinutes;
        if (durationMinutes <= 0) return 0;

        double hours = durationMinutes / 60.0;
        return hargaPerJam * hours;
    }

    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public void updateTotalBayar() {
        double total = 0;

        for (Node node : cartContent.getChildren()) {
            Label priceLabel = (Label) node.lookup("#priceLabel");
            if (priceLabel != null) {
                String text = priceLabel.getText().replace("Rp.", "").replace(".", "").replace(",", ".").trim();
                try {
                    total += Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format: " + text);
                }
            }
        }

        labelTotalBayar.setText(rupiahFormat.format(total));
    }


    public void handleClick(){
        btnBayar.setOnAction(event -> {
            handleBayar();
        });
    }
    @FXML
    public void handleBayar() {
        String bayarText = tfUangBayar.getText().trim();
        if (bayarText.isEmpty()) {
            tfKembalian.setText("Masukkan jumlah uang");
            return;
        }


        double totalBayar = 0;
        for (Node node : cartContent.getChildren()) {
            Label priceLabel = (Label) node.lookup("#priceLabel");
            if (priceLabel != null) {
                try {
                    String text = priceLabel.getText().replace("Rp.", "").replace(".", "").replace(",", ".").trim();
                    totalBayar += Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format: " + priceLabel.getText());
                }
            }
        }

        try {
            double uangDibayar = Double.parseDouble(bayarText);
            if (uangDibayar < totalBayar) {
                tfKembalian.setText("Uang tidak cukup!");
            } else {
                double kembalian = uangDibayar - totalBayar;
                tfKembalian.setText(rupiahFormat.format(kembalian));
            }
        } catch (NumberFormatException e) {
            tfKembalian.setText("Format uang salah");
        }
    }

}
