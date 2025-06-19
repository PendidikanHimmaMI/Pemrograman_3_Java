package himma.pendidikan.controller;

import himma.pendidikan.component.Dropdown;
import himma.pendidikan.controller.event.EvenListener.*;
import himma.pendidikan.model.Karyawan;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import himma.pendidikan.model.MetodePembayaran;
import himma.pendidikan.service.impl.*;
import himma.pendidikan.util.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;


import java.io.IOException;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class MetodePembayaranCtrl extends EvenListenerIndex {
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
    TableView<MetodePembayaran> tbMetodePembayaran;
    @FXML
    TableColumn<MetodePembayaran, Integer> clNo;
    @FXML
    TableColumn<MetodePembayaran, Void> clAksi;
    @FXML
    TableColumn<MetodePembayaran, String> clNama, clDeskripsi, clStatus;

    public static MetodePembayaranSrvcImpl metodePembayaranSrvc = new MetodePembayaranSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();
    private final int rowsPerPage = 15;
    private List<MetodePembayaran> fullDataList;
    private String lastSearch, lastStatus, lastPosisi, lastSortColumn, lastSortOrder;

    public MetodePembayaranCtrl() {}

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
        handleClick();
        loadData(null,"Aktif", "mpb_id", "ASC");
    }

    private <T> void setAlignmentByType(TableColumn<MetodePembayaran, T> column, Pos alignment) {
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
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_metode_pembayaran/create.fxml"));
                loader.setController(new MetodePembayaranCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_metode_pembayaran/edit.fxml"));
                MetodePembayaranEditCtrl controller = new MetodePembayaranEditCtrl(); // Buat controller
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_metode_pembayaran/index.fxml"));
            }
            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String search, String status, String sortColumn, String sortOrder) {
        lastSearch = search;
        lastStatus = status;
        lastSortColumn = sortColumn;
        lastSortOrder = sortOrder;

        fullDataList = metodePembayaranSrvc.getAllData(search, status, sortColumn, sortOrder);
        int pageCount = (int) Math.ceil((double) fullDataList.size() / rowsPerPage);
        pgTabel.setPageCount(pageCount == 0 ? 1 : pageCount);
        pgTabel.setCurrentPageIndex(0);
        pgTabel.setPageFactory(this::createPage);
    }
    public Node createPage(int pageIndex){
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, fullDataList.size());

        List<MetodePembayaran> pageData = fullDataList.subList(fromIndex, toIndex);
        ObservableList<MetodePembayaran> data = FXCollections.observableArrayList(pageData);

        clNo.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tbMetodePembayaran.getItems().indexOf(col.getValue()) + 1));
        clNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        clDeskripsi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeskripsi()));
        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        setAlignmentByType(clNo, Pos.CENTER_RIGHT);
        setAlignmentByType(clNama, Pos.CENTER_LEFT);
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
                MetodePembayaran metodePembayaran = getTableView().getItems().get(getIndex());
                String nama = metodePembayaran.getNama();
                String currentStatus = metodePembayaran.getStatus();
                boolean isAktif = "Aktif".equals(currentStatus);

                FontIcon deleteIcon = new FontIcon(isAktif ? "fas-toggle-on" : "fas-toggle-off");
                deleteIcon.setIconSize(16);
                deleteIcon.setIconColor(Color.WHITE);

                btnDelete.setGraphic(deleteIcon);
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "red" : "green")+";");
                btnEdit.setOnAction(e -> loadSubPage("edit", metodePembayaran.getId()));
                if(currentStatus.equals("Tidak Aktif")) {
                    btnEdit.setVisible(false);
                    btnDelete.setAlignment(Pos.CENTER);
                }
                btnDelete.setOnAction(e -> {
                    String actionText = isAktif ? "menonaktifkan" : "mengaktifkan";
                    boolean confirmed = new SwalAlert().showAlert(
                            CONFIRMATION,
                            "Konfirmasi",
                            "Apakah anda yakin ingin " + actionText + " metodePembayaran dengan nama: " + nama + "?",
                            true
                    );
                    if (confirmed) {
                        metodePembayaranSrvc.setStatus(metodePembayaran.getId());
                        loadData(lastSearch, lastStatus, lastSortColumn, lastSortOrder);
                    }
                });
                setGraphic(pane);
            }
        });
        tbMetodePembayaran.setItems(data);
        clNama.setSortType(TableColumn.SortType.ASCENDING);
        tbMetodePembayaran.getSortOrder().add(clNama);
        tbMetodePembayaran.sort();
        tbMetodePembayaran.setPrefHeight(650);

        tbMetodePembayaran.setFixedCellSize(30);
        tbMetodePembayaran.setMinHeight(650);
        Node parent = tbMetodePembayaran.getParent();
        if (parent instanceof VBox) {
            VBox.setVgrow(tbMetodePembayaran, Priority.ALWAYS);
        }
        return tbMetodePembayaran;
    }

    //    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        loadData(search,status,"mpb_id","ASC");
    }

    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
    }

    public static class MetodePembayaranCreateCtrl extends EvenListenerCreate {
        @FXML
        Button btnBack;
        @FXML
        Label lbActiveUser;
        @FXML
        TextArea taDeskripsi;
        @FXML
        TextField tfNama;

        Validation v = new Validation();
        MetodePembayaranCtrl metodePembayaranCtrl = new MetodePembayaranCtrl();

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
            handleClickBack();
            v.setLetters(tfNama);
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            taDeskripsi.setText("");
        }

        @Override
        public void handleAddData(ActionEvent e) {
            String nama = tfNama.getText();
            String deskripsi = taDeskripsi.getText();
            String createdBy = Session.getCurrentUser().getNama();
            MetodePembayaran mpb = new MetodePembayaran(nama, deskripsi, createdBy);
            if(metodePembayaranSrvc.saveData(mpb)){
                metodePembayaranCtrl.loadSubPage("index",null);
            }
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }
        public void handleClickBack(){
            btnBack.setOnAction(event -> {
                metodePembayaranCtrl.loadSubPage("index",null);
            });
        }
    }

    public static class MetodePembayaranEditCtrl extends EvenListenerUpdate{
        @FXML
        Label lbActiveUser;
        @FXML
        Button btnBack;
        @FXML
        TextArea taDeskripsi;
        @FXML
        TextField tfNama;

        Validation v = new Validation();
        MetodePembayaranCtrl metodePembayaranCtrl = new MetodePembayaranCtrl();
        private Integer id;
        private String currentPassword;

        public void setId(Integer id) {
            this.id = id;
        }

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getNama()+" | "+Session.getCurrentUser().getPosisi());
            loadData();
            handleClickBack();
            v.setLetters(tfNama);
        }

        @Override
        public void loadData() {
            if(id!=null){
                MetodePembayaran mpb = metodePembayaranSrvc.getDataById(id);
                if(mpb!=null){
                    tfNama.setText(mpb.getNama());
                    taDeskripsi.setText(mpb.getDeskripsi());
                }
            }
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            taDeskripsi.setText("");
        }

        @Override
        public void handleUpdateData(ActionEvent e) {
            String nama = tfNama.getText();
            String deskripsi = taDeskripsi.getText();
            String updatedBy = Session.getCurrentUser().getNama();
            MetodePembayaran mpb = new MetodePembayaran(id,nama, deskripsi, updatedBy);
            if(metodePembayaranSrvc.updateData(mpb)){
                metodePembayaranCtrl.loadSubPage("index",null);
            }
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }
        public void handleClickBack(){
            btnBack.setOnAction(event -> {
                metodePembayaranCtrl.loadSubPage("index",null);
            });
        }
    }
}
