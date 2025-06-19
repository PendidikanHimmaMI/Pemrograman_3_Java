package himma.pendidikan.controller;

import himma.pendidikan.component.Dropdown;
import himma.pendidikan.controller.event.EvenListener.*;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.service.JenisPlayStationSrvc;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import himma.pendidikan.service.impl.*;
import himma.pendidikan.util.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;


public class JenisPlayStationCtrl extends EvenListenerIndex  {
    @FXML
    Pagination pgTabel;
    @FXML
    Button btnTambahData;
    @FXML
    Label lbActiveUser;
    @FXML
    TextField tfSearch;
    @FXML
    ComboBox<String> cbFilterStatus;
    @FXML
    TableView<JenisPlayStation> tbJenisPlayStation;
    @FXML
    TableColumn<JenisPlayStation, Integer> clNo;
    @FXML
    TableColumn<JenisPlayStation, Void> clAksi;
    @FXML
    TableColumn<JenisPlayStation, String> clNama, clDeskripsi, clStatus;
    @FXML
    TableColumn<JenisPlayStation, Integer> clTahunRilis, clMaxPemain;

    static AppCtrl app = AppCtrl.getInstance();
    public static JenisPlayStationSrvcImpl jenisPlayStationSrvc = new JenisPlayStationSrvcImpl();
    private final int rowsPerPage = 15;
    private List<JenisPlayStation> fullDataList;
    private String lastSearch, lastStatus,  lastSortColumn, lastSortOrder;



    public JenisPlayStationCtrl() {}

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
        handleClick();
        loadData(null,"Aktif",  "jps_id", "ASC");
    }

    private <T> void setAlignmentByType(TableColumn<JenisPlayStation, T> column, Pos alignment) {
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
        });
    }

    public static void loadSubPage(String page, Integer id) {
        try {
            FXMLLoader loader;
            Parent pane;
            if ("add".equals(page)) {
                loader = new FXMLLoader(JenisPlayStationCtrl.class.getResource("/himma/pendidikan/views/master_jenis_play_station/create.fxml"));
                loader.setController(new JenisPlayStationCtrl.JenisPlayStationCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(JenisPlayStationCtrl.class.getResource("/himma/pendidikan/views/master_jenis_play_station/edit.fxml"));
                JenisPlayStationCtrl.JenisPlayStationEditCtrl controller = new JenisPlayStationCtrl.JenisPlayStationEditCtrl
                        (); // Buat controller
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(JenisPlayStationCtrl.class.getResource("/himma/pendidikan/views/master_jenis_play_station/index.fxml"));
            }
            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String search, String status,  String sortColumn, String sortOrder){
        lastSearch = search;
        lastStatus = status;
        lastSortColumn = sortColumn;
        lastSortOrder = sortOrder;

        fullDataList = jenisPlayStationSrvc.getAllData(search, status,  sortColumn, sortOrder);
        int pageCount = (int) Math.ceil((double) fullDataList.size() / rowsPerPage);
        pgTabel.setPageCount(pageCount == 0 ? 1 : pageCount);
        pgTabel.setCurrentPageIndex(0);
        pgTabel.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, fullDataList.size());

        List<JenisPlayStation> pageData = fullDataList.subList(fromIndex, toIndex);
        ObservableList<JenisPlayStation> data = FXCollections.observableArrayList(pageData);

        clNo.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(data.indexOf(col.getValue()) + 1 + (pageIndex * rowsPerPage)));
        clNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        clTahunRilis.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTahunLiris()).asObject());
        clMaxPemain.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMaxPemain()).asObject());
        clDeskripsi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeskripsi()));
        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        setAlignmentByType(clNo, Pos.CENTER_RIGHT);
        setAlignmentByType(clNama, Pos.CENTER_LEFT);
        setAlignmentByType(clTahunRilis, Pos.CENTER_RIGHT);
        setAlignmentByType(clMaxPemain, Pos.CENTER_RIGHT);
        setAlignmentByType(clDeskripsi, Pos.CENTER_LEFT);


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

                JenisPlayStation jenisPlayStation = getTableView().getItems().get(getIndex());
                String nama = jenisPlayStation.getNama();
                String currentStatus = jenisPlayStation.getStatus();
                boolean isAktif = "Aktif".equalsIgnoreCase(currentStatus); // case-insensitive

                // Ikon dan tombol delete
                FontIcon deleteIcon = new FontIcon(isAktif ? "fas-toggle-on" : "fas-toggle-off");
                deleteIcon.setIconSize(16);
                deleteIcon.setIconColor(Color.WHITE);
                btnDelete.setGraphic(deleteIcon);
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "green" : "red") + ";");

                // Aktifkan/hilangkan tombol edit
                btnEdit.setVisible(isAktif);   // Hide dari tampilan
                btnEdit.setManaged(isAktif);   // Hide dari layout (tidak menyisakan space)

                btnEdit.setOnAction(e -> loadSubPage("edit", jenisPlayStation.getId()));
                btnDelete.setOnAction(e -> {
                    String actionText = isAktif ? "menonaktifkan" : "mengaktifkan";
                    boolean confirmed = new SwalAlert().showAlert(
                            CONFIRMATION,
                            "Konfirmasi",
                            "Apakah anda yakin ingin " + actionText + " jenis playstation dengan nama: " + nama + "?",
                            true
                    );
                    if (confirmed) {
                        jenisPlayStationSrvc.setStatus(jenisPlayStation.getId());
                        loadData(lastSearch, lastStatus, lastSortColumn, lastSortOrder);
                    }
                });

                setGraphic(pane);
            }

        });
//        for (TableColumn<Karyawan, ?> col : tbKaryawan.getColumns()) {
//            col.setText(col.getText() + " ▲");
//        }

        tbJenisPlayStation.setItems(data);
        clTahunRilis.setSortType(TableColumn.SortType.DESCENDING);
        tbJenisPlayStation.getSortOrder().add(clTahunRilis);
        tbJenisPlayStation.sort();
        tbJenisPlayStation.setPrefHeight(650);

        tbJenisPlayStation.setFixedCellSize(30);
        tbJenisPlayStation.setMinHeight(650);
        Node parent = tbJenisPlayStation.getParent();
        if (parent instanceof VBox) {
            VBox.setVgrow(tbJenisPlayStation, Priority.ALWAYS);
        }
        return tbJenisPlayStation;
    }


    //    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        loadData(search,status,"jps_id","ASC");
    }


    @Override
    public void handleClear() {
        tfSearch.clear(); // Bersihkan kolom pencarian
        cbFilterStatus.setValue("Aktif"); // Set ulang ke filter default
        loadData(null, "Aktif", "jps_id", "ASC"); // Muat ulang data default
        cbFilterStatus.setValue("");
    }




    public static class JenisPlayStationCreateCtrl extends EvenListenerCreate {

        @FXML
        Label lbActiveUser;
        @FXML
        TextField tfNama, tfTahunRilis, tfMaxPemain, tfDeskripsi;

        Validation v = new Validation();
        JenisPlayStationCtrl jenisPlayStationCtrl = new JenisPlayStationCtrl();

        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getPosisi());
            v.setNumbers(tfTahunRilis);
            v.setNumbers(tfMaxPemain);
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            tfTahunRilis.setText("");
            tfMaxPemain.setText("");
            tfDeskripsi.setText("");
        }

        @Override
        public void handleAddData(ActionEvent e) {
            initialize();
            String nama = tfNama.getText();
            String tahunRilisStr = tfTahunRilis.getText();
            String maxPemainStr = tfMaxPemain.getText();
            String deskripsi = tfDeskripsi.getText();
            String createdBy = Session.getCurrentUser().getNama();

            if (nama.isEmpty() || tahunRilisStr.isEmpty() || maxPemainStr.isEmpty() ) {
                new SwalAlert().showAlert(ERROR, "Validasi", "Semua data harus diisi.", false);
                return;
            }

            Integer tahunRilis, maxPemain;
            try {
                tahunRilis = Integer.parseInt(tahunRilisStr);
            } catch (NumberFormatException ex) {
                new SwalAlert().showAlert(ERROR, "Validasi", "Tahun rilis harus berupa angka.", false);
                return;
            }

            try {
                maxPemain = Integer.parseInt(maxPemainStr);
            } catch (NumberFormatException ex) {
                new SwalAlert().showAlert(ERROR, "Validasi", "Max pemain harus berupa angka.", false);
                return;
            }



            JenisPlayStation jpls = new JenisPlayStation(nama, tahunRilis, maxPemain, deskripsi, createdBy);
            if (jenisPlayStationSrvc.saveData(jpls)) {
                jenisPlayStationCtrl.loadSubPage("index", null);
            }
        }

        //        @Override
        public void handleBack() {
            loadSubPage("index",null);
        }
    }

    public static class JenisPlayStationEditCtrl extends EvenListenerUpdate{

        @FXML
        Label lbActiveUser;


        @FXML
        TextField tfNama, tfTahunRilis, tfMaxPemain, tfDeskripsi;

        Validation v = new Validation();
        JenisPlayStationCtrl playStationCtrl = new JenisPlayStationCtrl();
        private Integer id;

        public void setId(Integer id) {
            this.id = id;
        }

        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getStatus());
            loadData();
            v.setNumbers(tfTahunRilis);
            v.setNumbers(tfMaxPemain);
        }

        @Override
        public void loadData() {
            if(id!=null){
                JenisPlayStation jpls = jenisPlayStationSrvc.getDataById(id);
                if(jpls!=null){
                    tfNama.setText(jpls.getNama());
                    tfTahunRilis.setText(jpls.getTahunLiris().toString());
                    tfMaxPemain.setText((jpls.getMaxPemain().toString()));
                    tfDeskripsi.setText(jpls.getDeskripsi());
                }
            }

        }

        @Override
        public void handleClear() {
            tfTahunRilis.setText("");
            tfMaxPemain.setText("");
            tfDeskripsi.setText("");
        }

        @Override
        public void handleUpdateData(ActionEvent e) {
            String nama = tfNama.getText();
            String tahunRilisStr = tfTahunRilis.getText();
            String maxPemainStr = tfMaxPemain.getText();
            String deskripsi = tfDeskripsi.getText();
            String modifby = Session.getCurrentUser().getNama();

            if (nama.isEmpty() || tahunRilisStr.isEmpty() || maxPemainStr.isEmpty()) {
                new SwalAlert().showAlert(ERROR, "Validasi", "Semua data harus diisi.", false);
                return;
            }

            Integer tahunRilis, maxPemain;
            try {
                tahunRilis = Integer.parseInt(tahunRilisStr);
            } catch (NumberFormatException ex) {
                new SwalAlert().showAlert(ERROR, "Validasi", "Tahun rilis harus berupa angka.", false);
                return;
            }

            try {
                maxPemain = Integer.parseInt(maxPemainStr);
            } catch (NumberFormatException ex) {
                new SwalAlert().showAlert(ERROR, "Validasi", "Max pemain harus berupa angka.", false);
                return;
            }

            JenisPlayStation jpls = new JenisPlayStation(id, nama, tahunRilis, maxPemain, deskripsi, null, modifby);
            if (jenisPlayStationSrvc.updateData(jpls)) {
                JenisPlayStationCtrl.loadSubPage("index", null);
            }
        }


        public void handleBack() {
            loadSubPage("index",null);
        }
    }
}