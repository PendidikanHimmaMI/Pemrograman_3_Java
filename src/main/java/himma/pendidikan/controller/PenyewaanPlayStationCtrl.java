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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PenyewaanPlayStationCtrl {

    @FXML private VBox cartContent;
    @FXML private Label labelTotalBayar;
    @FXML private ComboBox<String> cbFilterStatus;
    @FXML private ComboBox<MetodePembayaran> cbMetodePembayaran;
    @FXML private FlowPane flowCards;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField tfSearch;
    @FXML private TextField tfUangBayar;
    @FXML private TextField tfPhone;
    @FXML private TextField tfCustomerName;
    @FXML private TextField tfKembalian;
    @FXML private Button btnBayar;

    private final DBConnect connect = new DBConnect();
    private final Validation v = new Validation();
    private final List<PlayStation> cart = new ArrayList<>();
    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    @FXML
    public void initialize() {
        loadPlayStationCards(null, null);
        btnBayar.setOnAction(this::handleAddData);
        tfUangBayar.setOnAction(e -> updateTotal());
    }

    public void handleSearch() {
        String search = tfSearch.getText().trim();
        String status = cbFilterStatus.getValue();
        loadPlayStationCards(search, status);
    }

    public void handleClear() {
        cbFilterStatus.setValue(null);
    }

    PlayStation ps = new PlayStation();
    private void loadPlayStationCards(String search, String status) {
        flowCards.getChildren().clear();
        PlayStationSrvc srv = new PlayStationSrvcImpl();

        List<PlayStation> list = srv.getAllData(search, status, (Integer) null, "pst_id", "ASC");
        System.out.println("Data loaded: " + list.size());

        for (PlayStation ps : list) {
            VBox card = createCard(ps);
            flowCards.getChildren().add(card);
        }
    }


    private VBox createCard(PlayStation ps) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color: #020A7A; -fx-background-radius:12; -fx-padding:10;");
        card.setPrefSize(350, 350);
        card.setCursor(Cursor.HAND);
        Label title = new Label( ps.getSerialNumber());
        title.setStyle("-fx-font-size:20; -fx-font-weight:bold; -fx-text-fill:white;");
        Label jenis = new Label("- " + ps.getJenisPlaystation());
        Label merk = new Label("- Merk: " + ps.getMerkPs());
        Label status = new Label("- Status: " + ps.getStatus());
        Label harga = new Label("- " + rupiahFormat.format(ps.getHargaPS()) + "/Jam");
        for (Label lbl : List.of(jenis, merk, status, harga)) {
            lbl.setStyle("-fx-text-fill:white; -fx-font-size:16;");
        }

        card.getChildren().addAll(title, jenis, merk, status, harga);
        card.setOnMouseClicked(e -> addToCart(ps));
        return card;
    }

    private void addToCart(PlayStation ps) {
        if (!cart.contains(ps)) {
            cart.add(ps);
            updateCartUI();
        }
    }

    private void updateCartUI() {
        cartContent.getChildren().clear();
        for (PlayStation ps : cart) {
            VBox item = new VBox(8);
            item.setStyle("-fx-background-color:#020A7A; -fx-background-radius:12; -fx-padding:10;");
            item.setPrefWidth(350);

            Label lblTitle = new Label(ps.getSerialNumber());
            lblTitle.setStyle("-fx-font-size:16; -fx-text-fill:white; -fx-font-weight:bold;");

            TextField tfMulai = new TextField();
            TextField tfSelesai = new TextField();
            tfMulai.setPromptText("HH:mm");
            tfSelesai.setPromptText("HH:mm");


            Label lblPrice = new Label("Rp. 0");
            lblPrice.setId("priceLabel");
            lblPrice.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

            Runnable calc = () -> {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime start = LocalTime.parse(tfMulai.getText(), formatter);
                    LocalTime end = LocalTime.parse(tfSelesai.getText(), formatter);

                    long minutes = Duration.between(start, end).toMinutes();
                    if (minutes < 0) minutes = 0; // Avoid negative durations

                    double hours = minutes / 60.0;
                    double price = hours * ps.getHargaPS();

                    lblPrice.setText(rupiahFormat.format(price));
                } catch (Exception e) {
                    lblPrice.setText(rupiahFormat.format(0)); // reset on invalid input
                } finally {
                    updateTotal(); // Always update total
                }
            };


            tfMulai.textProperty().addListener((o, oldV, newV) -> calc.run());
            tfSelesai.textProperty().addListener((o, oldV, newV) -> calc.run());


            Label lblMulai = new Label("Mulai");
            lblMulai.setStyle("-fx-text-fill: white;");

            Label lblSelesai = new Label("Selesai");
            lblSelesai.setStyle("-fx-text-fill: white;");

            HBox timeBox = new HBox(10,
                    new VBox(lblMulai, tfMulai),
                    new VBox(lblSelesai, tfSelesai),
                    new Button("–") {{
                        setStyle("-fx-background-color:#FC1F1F; -fx-text-fill: #020A7A; -fx-font-size: 14px; -fx-background-radius: 6;");
                        setOnAction(ev -> {
                            cart.remove(ps);
                            updateCartUI();
                        });}});
            timeBox.setAlignment(Pos.CENTER_LEFT);

            item.getChildren().addAll(lblTitle, timeBox, lblPrice);
            cartContent.getChildren().add(item);
        }
        updateTotal();
    }

    private void updateTotal() {
        double total = cartContent.getChildren().stream().mapToDouble(node -> {
            Label lbl = (Label) node.lookup("#priceLabel");
            if (lbl != null) {
                String val = lbl.getText().replace("Rp", "").replace(".", "").replace(",", ".");
                try { return Double.parseDouble(val); } catch (NumberFormatException e) {}
            }
            return 0.0;
        }).sum();
        labelTotalBayar.setText(rupiahFormat.format(total));
    }

    private void handleAddData(ActionEvent event) {
        try {
            int karId = Session.getCurrentUser().getId();
            String nama = tfCustomerName.getText();
            String telepon = tfPhone.getText();
            java.sql.Date tanggal = java.sql.Date.valueOf(LocalDate.now());
            String createdBy = Session.getCurrentUser().getNama();
            java.sql.Date createDate = tanggal;

            showAlert("Penyewaan berhasil!");
            cart.clear();
            updateCartUI();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Terjadi error saat menyimpan data.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}