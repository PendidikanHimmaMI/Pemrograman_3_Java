package himma.pendidikan.controller;

import himma.pendidikan.component.Dropdown;
import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.controller.event.EvenListener;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.model.Karyawan;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.service.PlayStationSrvc;
import himma.pendidikan.service.impl.PlayStationSrvcImpl;
import himma.pendidikan.util.Session;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

public class PlayStationCtrl extends EvenListener.EvenListenerIndex {

    @FXML
    Pagination pgTabel;

    @FXML
    private Button btnTambahData;

    @FXML
    ComboBox<String> cbFilterStatus, cbFilterPosisi;

    @FXML
    private TableColumn<PlayStation, Void> clAksi;

    @FXML
    private TableColumn<PlayStation, String> clSerialNumber;

    @FXML
    private TableColumn<PlayStation, Integer> clNo;

    @FXML
    private TableColumn<PlayStation, String> clMerk;

    @FXML
    private TableColumn<PlayStation, String> clHarga;

    @FXML
    private TableColumn<PlayStation, String> clStatus;

    @FXML
    private TableColumn<PlayStation, String> clJnsPS;
    @FXML
    TableView<PlayStation> tbPlayStation;

    @FXML
    private Label lbActiveUser;

    @FXML
    private TextField tfSearch;

    public static PlayStationSrvcImpl playStationSrvc = new PlayStationSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();
    private final int rowsPerPage = 15;
    private List<PlayStation> fullDataList;
    private String lastSearch, lastStatus, lastPosisi, lastSortColumn, lastSortOrder;


    public PlayStationCtrl() {
    }

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getPosisi());
        handleClick();
        loadData(null, "Aktif", null, "pst_id", "ASC");
    }

    private <T> void setAlignmentByType(TableColumn<PlayStation, T> column, Pos alignment) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setAlignment(alignment);
            }
        });
    }

    public void handleClick() {
        btnTambahData.setOnAction(event -> {
            loadSubPage("add", null);
        });
    }


    public void loadSubPage(String page, Integer id) {
        try {
            FXMLLoader loader;
            Parent pane;
            if ("add".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_play_station/create.fxml"));
                loader.setController(new PlayStationCtrl.PlaytationCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_play_station/edit.fxml"));
                PlaytationEditCtrl controller = new PlaytationEditCtrl();// Buat controller
                System.out.println("id" + id);
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_play_station/index.fxml"));
            }
            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String search, String status, String posisi, String sortColumn, String sortOrder) {
        lastSearch = search;
        lastStatus = status;
        lastPosisi = posisi;
        lastSortColumn = sortColumn;
        lastSortOrder = sortOrder;

        fullDataList = playStationSrvc.getAllData(search, status, posisi, sortColumn, sortOrder);
        int pageCount = (int) Math.ceil((double) fullDataList.size() / rowsPerPage);
        pgTabel.setPageCount(pageCount == 0 ? 1 : pageCount);
        pgTabel.setCurrentPageIndex(0);
        pgTabel.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, fullDataList.size());

        List<PlayStation> pageData = fullDataList.subList(fromIndex, toIndex);
        ObservableList<PlayStation> data = FXCollections.observableArrayList(pageData);

        clNo.setCellValueFactory(col -> {
            int indexInPage = tbPlayStation.getItems().indexOf(col.getValue());
            int globalIndex = pageIndex * rowsPerPage + indexInPage + 1;
            return new ReadOnlyObjectWrapper<>(globalIndex);
        });
        clMerk.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMerkPs()));
        clSerialNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerialNumber()));
        clJnsPS.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisPlaystation()));

        clHarga.setCellValueFactory(cellData -> {
            Double harga = cellData.getValue().getHargaPS();
            String formattedHarga = String.format("Rp %.2f", harga); // or use NumberFormat for currency
            return new SimpleStringProperty(formattedHarga);
        });

        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        tbPlayStation.setItems(data);
        setAlignmentByType(clNo, Pos.CENTER);
        setAlignmentByType(clMerk, Pos.CENTER_LEFT);
        setAlignmentByType(clSerialNumber, Pos.CENTER_LEFT);
        setAlignmentByType(clJnsPS, Pos.CENTER_LEFT);
        setAlignmentByType(clHarga, Pos.CENTER_RIGHT);
        setAlignmentByType(clStatus, Pos.CENTER_RIGHT);
        setAlignmentByType(clAksi, Pos.CENTER);

        clAksi.setCellFactory(param -> new TableCell<>() {
            final Button btnEdit = new Button();
            final Button btnDelete = new Button();
            final HBox pane = new HBox(btnEdit, btnDelete);

            {
                pane.setSpacing(10);
                pane.setAlignment(Pos.CENTER);

                FontIcon editIcon = new FontIcon("fas-pencil-alt");
                editIcon.setIconSize(16);
                editIcon.setIconColor(Color.WHITE);
                btnEdit.setGraphic(editIcon);
                btnEdit.setStyle("-fx-background-color: orange;");
                btnEdit.setCursor(Cursor.HAND);
                btnDelete.setCursor(Cursor.HAND);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                PlayStation playStation = getTableView().getItems().get(getIndex());
                String serialNumber = playStation.getSerialNumber();
                String currentStatus = playStation.getStatus();
                boolean isAktif = "Aktif".equals(currentStatus);

                FontIcon deleteIcon = new FontIcon(isAktif ? "fas-toggle-on" : "fas-toggle-off");
                deleteIcon.setIconSize(16);
                deleteIcon.setIconColor(Color.WHITE);

                btnDelete.setGraphic(deleteIcon);
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "red" : "green") + ";");
                btnEdit.setOnAction(e -> loadSubPage("edit", playStation.getIdPS()));
                btnDelete.setOnAction(e -> {
                    String actionText = isAktif ? "menonaktifkan" : "mengaktifkan";
                    boolean confirmed = new SwalAlert().showAlert(
                            CONFIRMATION,
                            "Konfirmasi",
                            "Apakah anda yakin ingin " + actionText + " Playstation dengan kode: " + serialNumber + "?",
                            true
                    );
                    if (confirmed) {
                        playStationSrvc.setStatus(playStation.getIdPS());
                        loadData(lastSearch, lastStatus, lastPosisi, lastSortColumn, lastSortOrder);
                    }
                });
                setGraphic(pane);
            }
        });
        tbPlayStation.setItems(data);
        clNo.setSortType(TableColumn.SortType.ASCENDING);
        tbPlayStation.getSortOrder().add(clNo);
        tbPlayStation.sort();

        tbPlayStation.setFixedCellSize(30);;
        tbPlayStation.setMinHeight(650);
        Node Parent = tbPlayStation.getParent();
        if (Parent instanceof VBox) {
            VBox.setVgrow(tbPlayStation, Priority.ALWAYS);
        }
        return new Label(""); // atau return null; juga tidak masalah
    }

    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();

        loadData(search,status,null,"pst_id","ASC");
    }

    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
    }

    public static class PlaytationCreateCtrl extends EvenListenerCreate {
        @FXML
        ComboBox<String> cbJenisPS;
        @FXML
        ComboBox<PlayStation> cbCoba;
        @FXML
        Label lbActiveUser;
        @FXML
        TextField tfMerk;
        @FXML
        TextField tfHarga;
        @FXML
        TextField tfSerialNumber;
        @FXML
        Button btnBack;

        Validation v = new Validation();
        PlayStationCtrl playStationCtrl = new PlayStationCtrl();
        DBConnect dbConnect = new DBConnect();


        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getPosisi());
            handleClickBack();
            loadDataJenisPS();
        }

        private void loadDataJenisPS() {
            try {
                DBConnect db = new DBConnect();
                ResultSet rs = db.stat.executeQuery("SELECT jps_nama FROM rps_msjenisplaystation");

                cbJenisPS.getItems().clear();
                while (rs.next()) {
                    String jenisPS = rs.getString("jps_nama");
                    cbJenisPS.getItems().add(jenisPS);
                }
                rs.close();
                db.stat.close();
                db.conn.close();
            } catch (SQLException e) {
                System.out.println("Gagal load data jenis PS: " + e.getMessage());
            }
        }


        @Override
        public void handleAddData(ActionEvent e) {
            String merk = tfMerk.getText();
            String jenisPS = cbJenisPS.getValue();
            String serialNumber = tfSerialNumber.getText();
            Double harga = Double.valueOf(tfHarga.getText());
            String createdBy = Session.getCurrentUser().getNama();

            Integer idJenisPS = playStationCtrl.getIdJenisFromDatabase( jenisPS);
            if(idJenisPS != null){
                PlayStation playStation = new PlayStation(null, idJenisPS, jenisPS, serialNumber, merk, harga, "Aktif", createdBy);

                if(playStationSrvc.saveData(playStation)){
                    playStationCtrl.loadSubPage("index",null);
                }
            }
        }

        @Override
        public void handleClear() {
            tfMerk.setText("");
            tfHarga.setText("");
            tfSerialNumber.setText("");
            cbJenisPS.setValue(null);
            cbJenisPS.setPromptText("Pilih Jenis PS");
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }

        public void handleClickBack() {
            btnBack.setOnAction(event -> {
                playStationCtrl.loadSubPage("index", null);
            });
        }
    }

    private Integer getIdJenisFromDatabase(String jenisPlaystation) {
        try {
            DBConnect db = new DBConnect();
            String query = "SELECT jps_id FROM rps_msjenisplaystation WHERE jps_nama = ?";
            PreparedStatement ps = db.conn.prepareStatement(query);
            ps.setString(1, jenisPlaystation);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("jps_id");  // Mengembalikan ID jenis PlayStation
            }

            rs.close();
            ps.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("Error saat mengambil ID jenis: " + e.getMessage());
        }
        return null;  // Jika ID tidak ditemukan
    }

    public static class PlaytationEditCtrl extends EvenListenerUpdate {
        @FXML
        ComboBox<String> cbJenisPS;
        @FXML
        ComboBox<PlayStation> cbCoba;
        @FXML
        Label lbActiveUser;
        @FXML
        TextField tfMerk, tfHarga, tfSerialNumber;
        @FXML
        Button btnBack;

        Validation v = new Validation();
        PlayStationCtrl playStationCtrl = new PlayStationCtrl();
        private Integer id;

        public void setId(Integer id) {
            this.id = id;
        }

        private void loadDataJenisPS() {
            try {
                DBConnect db = new DBConnect();
                ResultSet rs = db.stat.executeQuery("SELECT jps_nama FROM rps_msjenisplaystation");

                cbJenisPS.getItems().clear();
                while (rs.next()) {
                    String jenisPS = rs.getString("jps_nama");
                    cbJenisPS.getItems().add(jenisPS);
                }
                rs.close();
                db.stat.close();
                db.conn.close();
            } catch (SQLException e) {
                System.out.println("Gagal load data jenis PS: " + e.getMessage());
            }
        }

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getPosisi());
            handleClickBack();
            loadData();
            loadDataJenisPS();
            v.setNumbers(tfHarga);
       }


        @Override
        public void handleUpdateData(ActionEvent e) {
            String merk = tfMerk.getText();
            String jenisPS = cbJenisPS.getValue();
            String serialNumber = tfSerialNumber.getText();
            Double harga = Double.valueOf(tfHarga.getText());
            String updateby = Session.getCurrentUser().getNama();
            System.out.println(Session.getCurrentUser().getNama());
            Integer idJenisPS = playStationCtrl.getIdJenisFromDatabase(jenisPS);
            if(idJenisPS != null){
                PlayStation playStation = new PlayStation(idJenisPS, jenisPS, serialNumber, merk, harga, updateby,id);
                if(playStationSrvc.updateData(playStation)){
                    playStationCtrl.loadSubPage("index",null);
                }
            }
        }

        public void loadData(){
            if(id != null){
                PlayStation pst = playStationSrvc.getDataById(id);
                if(pst != null){
                    tfMerk.setText(pst.getMerkPs());
                    tfSerialNumber.setText(pst.getSerialNumber());
                    tfHarga.setText(String.valueOf(pst.getHargaPS()));
                    cbJenisPS.setValue(pst.getJenisPlaystation());
                }
            }
       }

        @Override
        public void handleClear() {
            tfMerk.setText("");
            tfHarga.setText("");
            tfSerialNumber.setText("");
            cbJenisPS.setValue(null);
            cbJenisPS.setPromptText("Pilih Jenis PS");
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }

        public void handleClickBack() {
            btnBack.setOnAction(event -> {
                playStationCtrl.loadSubPage("index", null);
            });

        }
    }
}
