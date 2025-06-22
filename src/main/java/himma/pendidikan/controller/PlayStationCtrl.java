package himma.pendidikan.controller;

import himma.pendidikan.component.Dropdown;
import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.controller.event.EvenListener;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.service.impl.JenisPlayStationSrvcImpl;
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

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

public class PlayStationCtrl extends EvenListener.EvenListenerIndex {
    @FXML
    Pagination pgTabel;
    @FXML
    private Button btnTambahData;

    @FXML
    ComboBox<String> cbFilterStatus;

    @FXML
    ComboBox<JenisPlayStation> cbFilterJenisPlayStation;

    @FXML
    private TableColumn<PlayStation, Void> clAksi;

    @FXML
    private TableColumn<PlayStation, String> clSerialNumber;

    @FXML
    private TableColumn<PlayStation, Integer> clNo;

    @FXML
    private TableColumn<PlayStation, String > clMerk;

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

    private String lastSearch, lastStatus, lastSortColumn, lastSortOrder;
    private Integer lastidJeniPlayStation;
    private List<PlayStation> fullDataList;
    public static PlayStationSrvcImpl playStationSrvc = new PlayStationSrvcImpl();
    public static JenisPlayStationSrvcImpl jenisPlayStationSrvc = new JenisPlayStationSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();

    private final int rowsPerPage = 15;

    public PlayStationCtrl(){}

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
        List<JenisPlayStation> jenisPlayStationList = jenisPlayStationSrvc.getAllData(null, "Aktif", "jps_id", "ASC");
        Dropdown.setDropdown(cbFilterJenisPlayStation, jenisPlayStationList, JenisPlayStation::getNama);
        handleClick();
        loadData(null,"Aktif", null, "pst_id", "ASC");
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


    public void handleClick(){
        btnTambahData.setOnAction(event -> {
            loadSubPage("add",null);
        });}



    public void loadSubPage(String page, Integer id) {
        try {
            FXMLLoader loader;
            Parent pane;
            if ("add".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_play_station/create.fxml"));
                loader.setController(new PlaytationCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_play_station/edit.fxml"));
                PlaytationEditCtrl controller = new PlaytationEditCtrl();// Buat controller
                System.out.println("id"+id);
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

    public void loadData(String search, String status, Integer idJeniPlayStation, String sortColumn, String sortOrder) {
        lastSearch = search;
        lastStatus = status;
        lastidJeniPlayStation = idJeniPlayStation;
        lastSortColumn = sortColumn;
        lastSortOrder = sortOrder;

        fullDataList = playStationSrvc.getAllData(search, status, idJeniPlayStation, sortColumn, sortOrder);
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
        clJnsPS.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisPlaystation().getNama()));

        clHarga.setCellValueFactory(cellData -> {
            Double harga = cellData.getValue().getHargaPS();
            String formattedHarga = String.format("Rp %.2f", harga); // or use NumberFormat for currency
            return new SimpleStringProperty(formattedHarga);
        });

        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        tbPlayStation.setItems(data);
        setAlignmentByType(clNo, Pos.CENTER_LEFT);
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
                boolean isAktif = "Aktif".equalsIgnoreCase(currentStatus);

                FontIcon deleteIcon = new FontIcon(isAktif ? "fas-toggle-on" : "fas-toggle-off");
                deleteIcon.setIconSize(16);
                deleteIcon.setIconColor(Color.WHITE);

                btnDelete.setGraphic(deleteIcon);
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "green" : "red") + ";");
                btnEdit.setVisible(isAktif);
                btnEdit.setManaged(isAktif);

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
                        loadData(lastSearch, lastStatus, lastidJeniPlayStation, lastSortColumn, lastSortOrder);
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
        return new Label("");
    }
    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        JenisPlayStation jenisPlayStationId = cbFilterJenisPlayStation.getSelectionModel().getSelectedItem();
        Integer idJenisPS = (jenisPlayStationId != null && jenisPlayStationId.getId() != null)
                ? jenisPlayStationId.getId()
                : null;

        loadData(search, status, idJenisPS, "pst_id", "ASC");

    }

    @Override
    public void handleClear() {
        tfSearch.clear();
        cbFilterStatus.setValue("");
        cbFilterJenisPlayStation.setValue(null);
        loadData(null,"Aktif", null, "pst_id", "ASC");
    }

    public static class PlaytationCreateCtrl extends EvenListenerCreate {
        @FXML
        ComboBox<JenisPlayStation> cbJenisPS;
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
            List<JenisPlayStation> list = jenisPlayStationSrvc.getAllData(null,"Aktif", "jps_id", "ASC");
            lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
            handleClickBack();
            v.setNumbers(tfHarga);
            Dropdown.setDropdown(cbJenisPS,list, k -> k.getNama());
        }

        @Override
        public void handleAddData(ActionEvent e) {
            String merk = tfMerk.getText();
            JenisPlayStation selectedJenis = cbJenisPS.getValue();
            Integer jenisPS = (selectedJenis != null) ? selectedJenis.getId() : null;
            Double harga = tfHarga.getText().isEmpty() ? null : Double.parseDouble(tfHarga.getText());

            String serialNumber = tfSerialNumber.getText();
            String createdBy = Session.getCurrentUser().getNama();

            System.out.println(harga);
            if(jenisPS != null){
                PlayStation playStation = new PlayStation(null, jenisPS, null, serialNumber, merk, harga, "Aktif", createdBy);
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

    public static class PlaytationEditCtrl extends EvenListenerUpdate {
        @FXML
        ComboBox<JenisPlayStation> cbJenisPS;
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

        @FXML
        public void initialize() {
            List<JenisPlayStation> list = jenisPlayStationSrvc.getAllData(null,"Aktif", "jps_id", "ASC");
            lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
            handleClickBack();
            v.setNumbers(tfHarga);
            Dropdown.setDropdown(cbJenisPS,list, k -> k.getNama());
            loadData();
        }


        @Override
        public void handleUpdateData(ActionEvent e) {
            String merk = tfMerk.getText();
            JenisPlayStation selectedJenis = cbJenisPS.getValue();
            Integer jenisPS = (selectedJenis != null) ? selectedJenis.getId() : null;
            Double harga = tfHarga.getText().isEmpty() ? null : Double.parseDouble(tfHarga.getText());
            String serialNumber = tfSerialNumber.getText();
            String updateby = Session.getCurrentUser().getNama();
            if(jenisPS != null){
                PlayStation playStation = new PlayStation(jenisPS, null, serialNumber, merk, harga, updateby,id);
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
                    tfHarga.setText(pst.getHargaPS().toString());
                    for (JenisPlayStation jenis : cbJenisPS.getItems()) {
                        if (jenis.getNama().equals(pst.getJenisPlaystation().getNama())) {
                            cbJenisPS.setValue(jenis);
                            break;
                        }
                    }
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