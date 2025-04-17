package himma.pendidikan.component;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

public class Table extends AnchorPane {
    private TableView<Map<String, Object>> tableView;

    public Table() {
        tableView = new TableView<>();
        initializeTable();
    }

    private void initializeTable() {
        TableColumn<Map<String, Object>, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Map<String, Object>, String> param) {
                return new SimpleStringProperty(param.getValue().get("Nama").toString());
            }
        });

        TableColumn<Map<String, Object>, String> colUmur = new TableColumn<>("Umur");
        colUmur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Map<String, Object>, String> param) {
                return new SimpleStringProperty(param.getValue().get("Umur").toString());
            }
        });
        tableView.getColumns().add(colNama);
        tableView.getColumns().add(colUmur);
        tableView.setLayoutX(20);
        tableView.setLayoutY(50);
        tableView.setPrefWidth(500);
        tableView.setPrefHeight(400);
    }

    public void setData(List<Map<String, Object>> data) {
        tableView.getItems().setAll(data);
    }
}
