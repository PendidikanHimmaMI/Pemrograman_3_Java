package himma.pendidikan.controller;

import himma.pendidikan.component.Dropdown;
import himma.pendidikan.controller.event.EvenListener.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import himma.pendidikan.model.Karyawan;
import himma.pendidikan.service.impl.*;
import himma.pendidikan.util.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class KaryawanCtrl extends EvenListenerIndex {
    @FXML
    Pagination pgTabel;
    @FXML
    Button btnTambahData;
    @FXML
    Label lbActiveUser;
    @FXML
    TextField tfSearch;
    @FXML
    ComboBox<String> cbFilterStatus, cbFilterPosisi;
    @FXML
    TableView<Karyawan> tbKaryawan;
    @FXML
    TableColumn<Karyawan, Integer> clNo;
    @FXML
    TableColumn<Karyawan, Void> clAksi;
    @FXML
    TableColumn<Karyawan, String> clNama, clPosisi, clNoTelepon, clEmail, clUsername, clStatus;

    AppCtrl app = AppCtrl.getInstance();
    public static KaryawanSrvcImpl karyawanSrvc = new KaryawanSrvcImpl();
    private final int rowsPerPage = 15;
    private List<Karyawan> fullDataList;
    private String lastSearch, lastStatus, lastPosisi, lastSortColumn, lastSortOrder;

    public KaryawanCtrl() {}

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
        handleClick();
        loadData(null,"Aktif", null, "kry_id", "ASC");
    }

    private <T> void setAlignmentByType(TableColumn<Karyawan, T> column, Pos alignment) {
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

    public void loadSubPage(String page, Integer id) {
        try {
            FXMLLoader loader;
            Parent pane;
            if ("add".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_karyawan/create.fxml"));
                loader.setController(new KaryawanCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_karyawan/edit.fxml"));
                KaryawanEditCtrl controller = new KaryawanEditCtrl();
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_karyawan/index.fxml"));
            }
            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String search, String status, String posisi, String sortColumn, String sortOrder){
        lastSearch = search;
        lastStatus = status;
        lastPosisi = posisi;
        lastSortColumn = sortColumn;
        lastSortOrder = sortOrder;

        fullDataList = karyawanSrvc.getAllData(search, status, posisi, sortColumn, sortOrder);
        int pageCount = (int) Math.ceil((double) fullDataList.size() / rowsPerPage);
        pgTabel.setPageCount(pageCount == 0 ? 1 : pageCount);
        pgTabel.setCurrentPageIndex(0);
        pgTabel.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, fullDataList.size());

        List<Karyawan> pageData = fullDataList.subList(fromIndex, toIndex);
        ObservableList<Karyawan> data = FXCollections.observableArrayList(pageData);

        clNo.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(data.indexOf(col.getValue()) + 1 + (pageIndex * rowsPerPage)));
        clNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        clPosisi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosisi()));
        clNoTelepon.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoTelepon()));
        clEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        clUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        setAlignmentByType(clNo, Pos.CENTER_RIGHT);
        setAlignmentByType(clNama, Pos.CENTER_LEFT);
        setAlignmentByType(clPosisi, Pos.CENTER_LEFT);
        setAlignmentByType(clNoTelepon, Pos.CENTER_LEFT);
        setAlignmentByType(clEmail, Pos.CENTER_LEFT);
        setAlignmentByType(clUsername, Pos.CENTER_LEFT);

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

                Karyawan karyawan = getTableView().getItems().get(getIndex());
                String nama = karyawan.getNama();
                String currentStatus = karyawan.getStatus();
                boolean isAktif = "Aktif".equals(currentStatus);

                FontIcon deleteIcon = new FontIcon(isAktif ? "fas-toggle-on" : "fas-toggle-off");
                deleteIcon.setIconSize(16);
                deleteIcon.setIconColor(Color.WHITE);

                btnDelete.setGraphic(deleteIcon);
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "red" : "green")+";");
                btnEdit.setOnAction(e -> loadSubPage("edit", karyawan.getId()));
                btnDelete.setOnAction(e -> {
                    String actionText = isAktif ? "menonaktifkan" : "mengaktifkan";
                    boolean confirmed = new SwalAlert().showAlert(
                            CONFIRMATION,
                            "Konfirmasi",
                            "Apakah anda yakin ingin " + actionText + " karyawan dengan nama: " + nama + "?",
                            true
                    );
                    if (confirmed) {
                        karyawanSrvc.setStatus(karyawan.getId());
                        loadData(lastSearch, lastStatus, lastPosisi, lastSortColumn, lastSortOrder);
                    }
                });

                setGraphic(pane);
            }
        });
//        for (TableColumn<Karyawan, ?> col : tbKaryawan.getColumns()) {
//            col.setText(col.getText() + " ▲");
//        }

        tbKaryawan.setItems(data);
        clNama.setSortType(TableColumn.SortType.ASCENDING);
        tbKaryawan.getSortOrder().add(clNama);
        tbKaryawan.sort();
        tbKaryawan.setPrefHeight(650);

        tbKaryawan.setFixedCellSize(30);
        tbKaryawan.setMinHeight(650);
        Node parent = tbKaryawan.getParent();
        if (parent instanceof VBox) {
            VBox.setVgrow(tbKaryawan, Priority.ALWAYS);
        }
        return tbKaryawan;
    }


    //    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        String posisi = cbFilterPosisi.getSelectionModel().getSelectedItem();
        loadData(search,status,posisi,"kry_id","ASC");
    }

    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
        cbFilterPosisi.setValue("");
    }

    public static class KaryawanCreateCtrl extends EvenListenerCreate {
        @FXML
        ComboBox<String> cbPosisi;
        @FXML
        Label lbActiveUser;
        @FXML
        TextArea taAlamat;
        @FXML
        PasswordField tfPassword;
        @FXML
        TextField tfNama, tfNoTelepon, tfEmail, tfUsername;

        Validation v = new Validation();
        KaryawanCtrl karyawanCtrl = new KaryawanCtrl();

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
            Dropdown.setDropdown(cbPosisi, List.of("Admin", "Manager", "Staff"));
            v.setNumbers(tfNoTelepon);
            v.setLetters(tfNama);
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            tfNoTelepon.setText("");
            tfEmail.setText("");
            tfUsername.setText("");
            tfPassword.setText("");
            taAlamat.setText("");
            cbPosisi.setValue(null);
            cbPosisi.setPromptText("Pilih Posisi");
        }

        @Override
        public void handleAddData(ActionEvent e) {
            String nama = tfNama.getText();
            String noTelepon = tfNoTelepon.getText();
            String email = tfEmail.getText();
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            String alamat = taAlamat.getText();
            String posisi = cbPosisi.getValue();
            String createdBy = Session.getCurrentUser().getNama();
            Karyawan kry = new Karyawan(nama, posisi, alamat, noTelepon, email, username, password, createdBy);
            if(karyawanSrvc.saveData(kry)){
                karyawanCtrl.loadSubPage("index",null);
            }
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }
    }

    public static class KaryawanEditCtrl extends EvenListenerUpdate{
        @FXML
        ComboBox<String> cbPosisi;
        @FXML
        Label lbActiveUser;
        @FXML
        TextArea taAlamat;
        @FXML
        PasswordField tfPassword;
        @FXML
        TextField tfNama, tfNoTelepon, tfEmail, tfUsername;

        Validation v = new Validation();
        KaryawanCtrl karyawanCtrl = new KaryawanCtrl();
        private Integer id;
        private String currentPassword;

        public void setId(Integer id) {
            this.id = id;
        }

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
            loadData();
            Dropdown.setDropdown(cbPosisi, List.of("Admin", "Manager", "Staff"));
            v.setNumbers(tfNoTelepon);
            v.setLetters(tfNama);
        }

        @Override
        public void loadData() {
            if(id!=null){
                Karyawan kry = karyawanSrvc.getDataById(id);
                if(kry!=null){
                    tfNama.setText(kry.getNama());
                    tfNoTelepon.setText(kry.getNoTelepon());
                    tfEmail.setText(kry.getEmail());
                    tfUsername.setText(kry.getUsername());
                    taAlamat.setText(kry.getAlamat());
                    cbPosisi.setValue(kry.getPosisi());
                }
            }
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            tfNoTelepon.setText("");
            tfEmail.setText("");
            tfUsername.setText("");
            taAlamat.setText("");
            cbPosisi.setValue(null);
            cbPosisi.setPromptText("Pilih Posisi");
        }

        @Override
        public void handleUpdateData(ActionEvent e) {
            String nama = tfNama.getText();
            String noTelepon = tfNoTelepon.getText();
            String email = tfEmail.getText();
            String username = tfUsername.getText();
            String newPassword = tfPassword.getText();
            String password = newPassword.isEmpty() ? null : newPassword;
            String alamat = taAlamat.getText();
            String posisi = cbPosisi.getValue();
            String updatedBy = Session.getCurrentUser().getNama();
            System.out.println(currentPassword);
            System.out.println(password);
            Karyawan kry = new Karyawan(nama, posisi, alamat, noTelepon, email, username, password, updatedBy, id);
            if(karyawanSrvc.updateData(kry)){
                karyawanCtrl.loadSubPage("index",null);
            }
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }
    }
}
