package himma.pendidikan.component;

import javafx.collections.*;
import javafx.scene.control.*;

import java.util.List;
import java.util.function.Function;

public class Dropdown {
    public static void setDropdown(ComboBox<String> comboBox, List<String> list) {
        ObservableList<String> items = FXCollections.observableArrayList(list);
        comboBox.setItems(items);
    }

    public static <T> void setDropdown(ComboBox<T> comboBox, List<T> list, Function<T, String> labelFunction) {
        ObservableList<T> items = FXCollections.observableArrayList(list);
        comboBox.setItems(items);

        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : labelFunction.apply(item));
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : labelFunction.apply(item));
            }
        });
    }
}
