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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.*;

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

            TextField tfMulai = new TextField();
            TextField tfSelesai = new TextField();
            tfMulai.setPromptText("HH:mm");
            tfSelesai.setPromptText("HH:mm");

            Label lblPrice = new Label("Rp. 0");
            lblPrice.setId("priceLabel");
            lblPrice.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

            LocalDate today = LocalDate.now();

            Runnable updateCartDetail = () -> {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime startTime = LocalTime.parse(tfMulai.getText(), formatter);
                    LocalTime endTime = LocalTime.parse(tfSelesai.getText(), formatter);
                    LocalDateTime mulai = LocalDateTime.of(today, startTime);
                    LocalDateTime selesai = LocalDateTime.of(today, endTime);

                    long minutes = Duration.between(mulai, selesai).toMinutes();
                    if (minutes < 0) minutes = 0;

                    double hours = minutes / 60.0;
                    double price = hours * ps.getHargaPS();

                    lblPrice.setText(rupiahFormat.format(price));

                    // Update or replace the detail in cartDetails
//                    System.out.println(ps.getIdPS());
//                    System.out.println(mulai);
                    cartDetails.removeIf(d -> d.getPst_id() == ps.getIdPS());
                    cartDetails.add(new DetailPenyewaanPlaystation(ps.getIdPS(), mulai, selesai, price));
                    System.out.println(cartDetails.get(0).getId()+" ID");
                } catch (Exception e) {
                    lblPrice.setText(rupiahFormat.format(0));
                    cartDetails.removeIf(d -> d.getPst_id() == ps.getIdPS());
                } finally {
                    updateTotal();
                }
            };

            tfMulai.textProperty().addListener((obs, oldVal, newVal) -> updateCartDetail.run());
            tfSelesai.textProperty().addListener((obs, oldVal, newVal) -> updateCartDetail.run());

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
        try {
            String TotalHarga = labelTotalBayar.getText().replaceAll("[^\\d,]", "").replace(",", ".");
            int karId = Session.getCurrentUser().getId();
            String selectedNama = cbMetodePembayaran.getValue();
            int mpbId = metodePembayaranMap.get(selectedNama);
            String nama = tfCustomerName.getText();
            String telepon = tfPhone.getText();
            Double totalHarga = Double.parseDouble(TotalHarga) ;
            java.sql.Date tanggal = java.sql.Date.valueOf(LocalDate.now());
            String createdBy = Session.getCurrentUser().getPosisi();
            java.sql.Date createDate = tanggal;

            PenyewaanPlaystation pps = new PenyewaanPlaystation(karId, mpbId, nama, telepon,tanggal, totalHarga, createdBy, createDate);
//            System.out.println(cartDetails.);
            if(penyewaanPlayStationSrvc.saveData(pps, cartDetails)){
                showAlert("Penyewaan berhasil!");
                cart.clear();
                updateCartUI();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Terjadi error saat menyimpan data.");
        }
    }

    @FXML
    public void cetakLaporan() {
        try {
            InputStream input = getClass().getResourceAsStream("/himma/pendidikan/report/file.jrxml");
            if (input == null) {
                showAlert("File .jrxml tidak ditemukan di classpath!");
                System.out.println("Not found");

                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            Map<String, Object> param = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, connect.conn);

            // Simpan sebagai PDF di folder Downloads (misalnya)
            String userHome = System.getProperty("user.home");
            String outputPath = userHome + "/Downloads/LaporanPenyewaan.pdf";
            JasperExportManager.exportReportToPdfFile(print, outputPath);

            showAlert("Laporan berhasil disimpan di: " + outputPath);
            System.out.println("Laporan berhasil disimpan di: " + outputPath);


        } catch (JRException e) {
            showAlert("Error: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }


//        try {
//            // Path untuk file .jrxml dan .jasper
//            String jrxmlPath = "/himma/pendidikan/report/rr.jrxml";
//            String jasperPath = "src/main/resources/himma/pendidikan/report/rr.jasper";
//
//            JasperReport jasperReport = null;
//
//            // 1. Cek apakah file .jasper sudah ada
//            File jasperFile = new File(jasperPath);
//
//            if (jasperFile.exists()) {
//                // Jika .jasper sudah ada, load langsung
//                System.out.println("Loading pre-compiled .jasper file...");
//                jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
//            } else {
//                // Jika belum ada, compile dan simpan
//                System.out.println("Compiling .jrxml to .jasper...");
//
//                // Compile dari resource stream
//                InputStream jrxmlStream = getClass().getResourceAsStream(jrxmlPath);
//                if (jrxmlStream == null) {
//                    throw new JRException("File .jrxml tidak ditemukan: " + jrxmlPath);
//                }
//
//                jasperReport = JasperCompileManager.compileReport(jrxmlStream);
//
//                // Simpan hasil compile sebagai .jasper file
//                JRSaver.saveObject(jasperReport, jasperPath);
//                System.out.println("File .jasper berhasil disimpan: " + jasperPath);
//            }
//
//            // 2. Ambil path gambar dari resources
//            String imagePath = getClass().getResource("/himma/pendidikan/assets/image/LogoLaporan.png").getPath();
//
//            // 3. Siapkan parameter
//            Map<String, Object> param = new HashMap<>();
//            param.put("IMAGE_PATH", imagePath);
//
//            // 4. Fill report dengan data
//            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, connect.conn);
//
//            // 5. Tampilkan viewer
//            JasperViewer viewer = new JasperViewer(print, false);
//            viewer.setVisible(true);
//
//        } catch (JRException e) {
//            System.err.println("JasperReports Error: " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("General Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    // Method alternatif: Force compile ulang
    public void forceCompileReport() {
        try {
            String jrxmlPath = "/himma/pendidikan/report/rr.jrxml";
            String jasperPath = "src/main/resources/himma/pendidikan/report/rr.jasper";

            System.out.println("Force compiling .jrxml to .jasper...");

            // Compile dari resource
            InputStream jrxmlStream = getClass().getResourceAsStream(jrxmlPath);
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            // Simpan hasil compile
            JRSaver.saveObject(jasperReport, jasperPath);
            System.out.println("File .jasper berhasil di-compile ulang: " + jasperPath);

        } catch (JRException e) {
            System.err.println("Compile Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method untuk hanya tampilkan (jika .jasper sudah ada)
    public void tampilkanLaporanSaja() {
        try {
            String jasperPath = "src/main/resources/himma/pendidikan/report/rr.jasper";

            // Load .jasper yang sudah di-compile
            File jasperFile = new File(jasperPath);
            if (!jasperFile.exists()) {
                throw new JRException("File .jasper tidak ditemukan. Compile dulu dengan forceCompileReport()");
            }

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);

            // Ambil path gambar
            String imagePath = getClass().getResource("/himma/pendidikan/assets/image/LogoLaporan.png").getPath();

            // Parameter
            Map<String, Object> param = new HashMap<>();
            param.put("IMAGE_PATH", imagePath);

            // Fill dan tampilkan
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, connect.conn);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setVisible(true);

        } catch (JRException e) {
            System.err.println("Error menampilkan laporan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //        try {
//            String url = "jdbc:sqlserver://127.0.0.4:9210;databaseName=Db_RentalPlayStation;encrypt=true;trustServerCertificate=true";
//            String user = "Pendidikan";
//            String password = "123";
//            Connection conn = DriverManager.getConnection(url, user, password);
//            JasperReport report = (JasperReport) JRLoader.loadObject(new File("src/main/java/himma/pendidikan/report/rr.jrxml"));
//            HashMap<String, Object> parameters = new HashMap<>();
//            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);
//            JasperViewer.viewReport(print, false);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}