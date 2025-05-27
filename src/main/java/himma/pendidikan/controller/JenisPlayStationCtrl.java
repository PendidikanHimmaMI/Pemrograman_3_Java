package himma.pendidikan.controller;

import himma.pendidikan.component.Dropdown;
import himma.pendidikan.controller.event.EvenListener.*;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.service.JenisPlayStationSrvc;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import himma.pendidikan.service.impl.*;
import himma.pendidikan.util.*;

import java.io.IOException;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;


public class JenisPlayStationCtrl extends EvenListenerIndex {
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

    public static JenisPlayStationSrvcImpl jenisPlaystationSrvc = new JenisPlayStationSrvcImpl();
    static AppCtrl app = AppCtrl.getInstance();

    public JenisPlayStationCtrl() {}

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getPosisi());
        handleClick();
        loadData(null,"Aktif",  "jps_id", "ASC");
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

    public void loadData(String search, String status, String sortColumn, String sortOrder){
        List<JenisPlayStation> jenisPlayStationList = jenisPlaystationSrvc.getAllData(search,status, sortColumn, sortOrder);
        ObservableList<JenisPlayStation> data = FXCollections.observableArrayList(jenisPlayStationList);

        clNo.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tbJenisPlayStation.getItems().indexOf(col.getValue()) + 1));
        clNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));

        clTahunRilis.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTahunLiris())
        );    clMaxPemain.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getMaxPemain())
        );
        clDeskripsi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeskripsi()));
        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        clAksi.setCellFactory(param -> new TableCell<>() {
            final Button btnEdit = new Button("Edit");
            final Button btnDelete = new Button("Hapus");
            final HBox pane = new HBox(btnEdit, btnDelete);
            {
                pane.setSpacing(10);
                btnEdit.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
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
                boolean isAktif = "Aktif".equals(currentStatus);
                btnDelete.setText(isAktif ? "Hapus" : "Pulihkan");
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "red" : "green") + "; -fx-text-fill: white;");
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
                        jenisPlaystationSrvc.setStatus(jenisPlayStation.getId());
                        loadData(search,status, sortColumn, sortOrder);
                    }
                });
                setGraphic(pane);
            }
        });
        tbJenisPlayStation.setItems(data);
    }

    //    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        loadData(search,status,"jps_id","ASC");
    }


    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
    }


    public static class JenisPlayStationCreateCtrl extends EvenListenerCreate {

        @FXML
        Label lbActiveUser;
        @FXML
        TextField tfNama, tfTahunRilis, tfMaxPemain, tfDeskripsi;

        Validation v = new Validation();
        JenisPlayStationCtrl jenisPlayStationCtrl = new JenisPlayStationCtrl();

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getPosisi());

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
            String nama = tfNama.getText();
            Integer tahunRilis = Integer.valueOf(tfTahunRilis.getText());
            Integer maxPemain = Integer.valueOf(tfMaxPemain.getText());
            String deskripsi = tfDeskripsi.getText();
            String createdBy = Session.getCurrentUser().getNama();
            JenisPlayStation jpls = new JenisPlayStation(nama, tahunRilis, maxPemain, deskripsi,  createdBy);
            if(jenisPlaystationSrvc.saveData(jpls)){
                jenisPlayStationCtrl.loadSubPage("index",null);
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

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getStatus());
            loadData();

        }

        @Override
        public void loadData() {
            if(id!=null){
                JenisPlayStation jpls = jenisPlaystationSrvc.getDataById(id);
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
            tfNama.setText("");
            tfTahunRilis.setText("");
            tfMaxPemain.setText("");
            tfDeskripsi.setText("");
        }

        @Override
        public void handleUpdateData(ActionEvent e) {
            String nama = tfNama.getText();
            Integer tahunRilis = Integer.valueOf(tfTahunRilis.getText());
            Integer maxPemain = Integer.valueOf(tfMaxPemain.getText());
            String deskripsi = tfDeskripsi.getText();
            String modifby = Session.getCurrentUser().getNama();
//            System.out.println(modifby);
            JenisPlayStation jpls = new JenisPlayStation(id,nama, tahunRilis, maxPemain, deskripsi,null,  modifby);
            if(jenisPlaystationSrvc.updateData(jpls)){
                JenisPlayStationCtrl.loadSubPage("index",null);
            }
        }


        public void handleBack() {
            loadSubPage("index",null);
        }
    }
}
