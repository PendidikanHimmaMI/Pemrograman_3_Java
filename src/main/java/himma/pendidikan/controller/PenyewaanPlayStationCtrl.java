package himma.pendidikan.controller;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.controller.event.EvenListener.*;
import himma.pendidikan.model.DetailPenyewaanPlaystation;
import himma.pendidikan.model.MetodePembayaran;
import himma.pendidikan.model.PenyewaanPlaystation;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.service.PlayStationSrvc;
import himma.pendidikan.service.impl.PenyewaanPlayStationSrvcImpl;
import himma.pendidikan.service.impl.PlayStationSrvcImpl;
import himma.pendidikan.util.Session;
import himma.pendidikan.util.Validation;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PenyewaanPlayStationCtrl extends EvenListenerIndex {

    @FXML private Button btnCetakLaporan;
    @FXML private VBox cartContent;
    @FXML private Label labelTotalBayar;
    @FXML private ComboBox<String> cbFilterStatus;
    @FXML private FlowPane flowCards;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField tfSearch;
    @FXML private TextField tfUangBayar;
    @FXML private TextField tfPhone;
    @FXML private TextField tfCustomerName;
    @FXML private TextField tfKembalian;
    @FXML private Button btnBayar;
    @FXML ComboBox<String> cbMetodePembayaran;

    private Map<String, Integer> metodePembayaranMap = new HashMap<>();

    private final DBConnect connect = new DBConnect();
    private final Validation v = new Validation();
    private final List<PlayStation> cart = new ArrayList<>();
    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
    public static PenyewaanPlayStationSrvcImpl penyewaanPlayStationSrvc = new PenyewaanPlayStationSrvcImpl();
    public PenyewaanPlayStationCtrl() {}
    private List<DetailPenyewaanPlaystation> cartDetails = new ArrayList<>();


    @FXML
    public void initialize() {
        loadPlayStationCards(null, null);
        loadDataMetodePembayaran();
        btnBayar.setOnAction(this::handleAddData);
        tfUangBayar.setOnAction(e -> updateTotal());
        v.setNumbers(tfPhone);
        v.setNumbers(tfUangBayar);
    }

    private HBox createTimePicker(ComboBox<String> cbJam, ComboBox<String> cbMenit) {
        cbJam.setPromptText("Jam");
        cbMenit.setPromptText("Menit");

        for (int i = 0; i < 24; i++) {
            cbJam.getItems().add(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i += 5) {
            cbMenit.getItems().add(String.format("%02d", i));
        }

        cbJam.setPrefWidth(60);
        cbMenit.setPrefWidth(60);

        return new HBox(5, cbJam, cbMenit);
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

        List<PlayStation> list = srv.getAllData(search, status, null, "pst_id", "ASC");
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
        cartDetails.clear(); // clear old data before repopulating

        for (PlayStation ps : cart) {
//            System.out.println(ps.getIdPS() + " id");
            VBox item = new VBox(8);
            item.setStyle("-fx-background-color:#020A7A; -fx-background-radius:12; -fx-padding:10;");
            item.setPrefWidth(350);

            Label lblTitle = new Label(ps.getSerialNumber());
            lblTitle.setStyle("-fx-font-size:16; -fx-text-fill:white; -fx-font-weight:bold;");

            ComboBox<String> cbJamMulai = new ComboBox<>();
            ComboBox<String> cbMenitMulai = new ComboBox<>();
            ComboBox<String> cbJamSelesai = new ComboBox<>();
            ComboBox<String> cbMenitSelesai = new ComboBox<>();

            HBox timePickerMulai = createTimePicker(cbJamMulai, cbMenitMulai);
            HBox timePickerSelesai = createTimePicker(cbJamSelesai, cbMenitSelesai);


            Label lblPrice = new Label("Rp. 0");
            lblPrice.setId("priceLabel");
            lblPrice.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

            LocalDate today = LocalDate.now();

            Runnable updateCartDetail = () -> {
                try {
                    if (cbJamMulai.getValue() == null || cbMenitMulai.getValue() == null ||
                            cbJamSelesai.getValue() == null || cbMenitSelesai.getValue() == null) {
                        throw new IllegalArgumentException("Waktu belum lengkap");
                    }

                    LocalTime startTime = LocalTime.of(
                            Integer.parseInt(cbJamMulai.getValue()),
                            Integer.parseInt(cbMenitMulai.getValue())
                    );
                    LocalTime endTime = LocalTime.of(
                            Integer.parseInt(cbJamSelesai.getValue()),
                            Integer.parseInt(cbMenitSelesai.getValue())
                    );

                    if (endTime.isBefore(startTime)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Waktu Tidak Valid");
                        alert.setHeaderText("Jam selesai tidak boleh lebih awal dari jam mulai");
                        alert.setContentText("Silakan pilih waktu selesai yang benar.");
                        alert.showAndWait();

                        cartDetails.removeIf(d -> d.getPst_id() == ps.getIdPS());
                        lblPrice.setText(rupiahFormat.format(0));
                        updateTotal();
                        return;
                    }


                    LocalDateTime mulai = LocalDateTime.of(today, startTime);
                    LocalDateTime selesai = LocalDateTime.of(today, endTime);

                    long minutes = Duration.between(mulai, selesai).toMinutes();

                    double hours = minutes / 60.0;
                    double price = hours * ps.getHargaPS();

                    lblPrice.setText(rupiahFormat.format(price));

                    cartDetails.removeIf(d -> d.getPst_id() == ps.getIdPS());
                    cartDetails.add(new DetailPenyewaanPlaystation(ps.getIdPS(), mulai, selesai, price));
                } catch (Exception e) {
                    lblPrice.setText(rupiahFormat.format(0));
                    cartDetails.removeIf(d -> d.getPst_id() == ps.getIdPS());
                } finally {
                    updateTotal();
                }
            };


            ChangeListener<String> listener = (obs, oldVal, newVal) -> updateCartDetail.run();
            cbJamMulai.valueProperty().addListener(listener);
            cbMenitMulai.valueProperty().addListener(listener);
            cbJamSelesai.valueProperty().addListener(listener);
            cbMenitSelesai.valueProperty().addListener(listener);


            Label lblMulai = new Label("Mulai");
            lblMulai.setStyle("-fx-text-fill: white;");
            Label lblSelesai = new Label("Selesai");
            lblSelesai.setStyle("-fx-text-fill: white;");

            HBox timeBox = new HBox(10,
                    new VBox(lblMulai, timePickerMulai),
                    new VBox(lblSelesai, timePickerSelesai),
                    new Button("–") {{
                        setStyle("-fx-background-color:#FC1F1F; -fx-text-fill: #020A7A; -fx-font-size: 14px; -fx-background-radius: 6;");
                        setOnAction(ev -> {
                            cart.remove(ps);

                            cartDetails.removeIf(d -> d.getPst_id() == ps.getIdPS());
                            updateCartUI();
                        });
                    }}
            );

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
        tfUangBayar.getText();

        String akhirText = labelTotalBayar.getText().replaceAll("[^\\d,]", "").replace(",", ".");
        String bayarText = tfUangBayar.getText().replaceAll("[^\\d,]", "").replace(",", ".");

        if (!akhirText.isEmpty() && !bayarText.isEmpty()) {
            double akhir = Double.parseDouble(akhirText);
            double bayar = Double.parseDouble(bayarText);
            double kembalian = bayar - akhir;

            tfKembalian.setText(rupiahFormat.format(kembalian));
        } else {
            tfKembalian.setText("Input belum lengkap");
        }
    }

    private void loadDataMetodePembayaran() {
        try {
            DBConnect db = new DBConnect();
            ResultSet rs = db.stat.executeQuery("SELECT mpb_id, mpb_nama FROM rps_msmetodepembayaran");

            cbMetodePembayaran.getItems().clear();
            metodePembayaranMap.clear(); // clear old data

            while (rs.next()) {
                int id = rs.getInt("mpb_id");
                String nama = rs.getString("mpb_nama");

                cbMetodePembayaran.getItems().add(nama);
                metodePembayaranMap.put(nama, id);
            }
            rs.close();
            db.stat.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("Gagal load data Metode Pembayaran: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddData(ActionEvent e) {
        String totalHargaText = labelTotalBayar.getText().replaceAll("[^\\d,]", "").replace(",", ".");
        String tunaiText = tfUangBayar.getText().replaceAll("[^\\d,]", "").replace(",", ".");
        String selectedNama = cbMetodePembayaran.getValue();
        String nama = tfCustomerName.getText();
        String telepon = tfPhone.getText();

        if (selectedNama == null || selectedNama.trim().isEmpty() ||
                nama == null || nama.trim().isEmpty() ||
                telepon == null || telepon.trim().isEmpty() ||
                totalHargaText.isEmpty() || tunaiText.isEmpty()) {
            showAlert("Data tidak boleh ada yang kosong");
            return;
        }

        if (!metodePembayaranMap.containsKey(selectedNama)) {
            showAlert("Metode pembayaran tidak valid");
            return;
        }

        double totalHarga;
        double tunai;
        try {
            totalHarga = Double.parseDouble(totalHargaText);
            tunai = Double.parseDouble(tunaiText);
        } catch (NumberFormatException ex) {
            showAlert("Format angka tidak valid");
            return;
        }

        if (tunai < totalHarga) {
            showAlert("Uang yang dimasukkan kurang dari total harga yang harus dibayar");
            return;
        }

        try {
            int karId = Session.getCurrentUser().getId();
            int mpbId = metodePembayaranMap.get(selectedNama);
            java.sql.Date tanggal = java.sql.Date.valueOf(LocalDate.now());
            String createdBy = Session.getCurrentUser().getPosisi();
            java.sql.Date createDate = tanggal;

            PenyewaanPlaystation pps = new PenyewaanPlaystation(
                    karId, mpbId, nama, telepon, tanggal, totalHarga, createdBy, createDate
            );

            if (penyewaanPlayStationSrvc.saveData(pps, cartDetails)) {
                showAlert("Penyewaan berhasil!");
                cart.clear();
                updateCartUI();
                cbMetodePembayaran.setValue("");
                tfCustomerName.setText("");
                tfPhone.setText("");
                tfUangBayar.setText("");
                tfKembalian.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Terjadi error saat menyimpan data.");
        }
    }
    @FXML
    public void cetakLaporan(){
        try {
            String url = "jdbc:sqlserver://127.0.0.4:9210;databaseName=Db_RentalPlayStation;encrypt=true;trustServerCertificate=true";
            String user = "Pendidikan";
            String password = "123";
            Connection conn = DriverManager.getConnection(url, user, password);
            JasperReport report = (JasperReport) JRLoader.loadObject(new File("src/main/java/himma/pendidikan/report/rr.jasper"));
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);
            JasperViewer.viewReport(print, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}