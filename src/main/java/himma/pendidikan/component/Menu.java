package himma.pendidikan.component;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import himma.pendidikan.controller.AppCtrl;
import himma.pendidikan.util.Session;

import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static void CreateMenu(VBox menu, AppCtrl appCtrl){
        menu.getChildren().clear();

        List<MenuItem> menus = MenuList.getMenuForRole(Session.getCurrentUser().getPosisi());
        Button firstButton = null;

        for(MenuItem item : menus){
            Button btn = new Button(item.title());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.TOP_LEFT);
            btn.setStyle("-fx-background-color: #020A7A; -fx-font-weight: bold;-fx-font-size: 16px;;-fx-padding: 8 0 8 20; -fx-text-fill: #FFFFFF;");
            btn.setCursor(Cursor.HAND);
            btn.setOnAction(e -> {
                if (item.title().equalsIgnoreCase("Logout")) {
                    Session.setCurrentUser(null); // atau Session.clear(); tergantung implementasi kamu
                    appCtrl.loadLoginPage(); // method ini akan ganti scene utama ke login
                } else {
                    appCtrl.loadPage(item.fxmlPath);
                    setActiveButton(btn, menu);
                }
            });

            if (firstButton == null && item.title().equalsIgnoreCase("Dashboard")) {
                firstButton = btn;
            }
            menu.getChildren().add(btn);
        }
        if (firstButton != null) {
            firstButton.fire();
        }
    }

    private static void setActiveButton(Button activeButton, VBox menu) {
        for (var node : menu.getChildren()) {
            if (node instanceof Button btn) {
                btn.setStyle("-fx-background-color: #020A7A; -fx-font-weight: bold; -fx-font-size: 16px;-fx-padding: 8 0 8 20; -fx-text-fill: #FFFFFF;");
            }
        }
        activeButton.setStyle("-fx-background-color: #FFFFFF; -fx-font-weight: bold;-fx-font-size: 16px;;-fx-padding: 8 0 8 20;-fx-text-fill: #020A7A;");
    }

    record MenuItem(String title, String fxmlPath) { }

    class MenuList {
        public static List<MenuItem> getMenuForRole(String role){
            List<MenuItem> list = new ArrayList<>();
            list.add(new MenuItem("Dashboard", "/himma/pendidikan/views/dashboard/index.fxml"));

            switch (role.toLowerCase()) {
                case "admin" -> {
                    list.add(new MenuItem("Play Station", "/himma/pendidikan/views/master_play_station/index.fxml"));
                    list.add(new MenuItem("Jenis Play Station", "/himma/pendidikan/views/master_jenis_play_station/index.fxml"));
                    list.add(new MenuItem("Karyawan", "/himma/pendidikan/views/master_karyawan/index.fxml"));
                    list.add(new MenuItem("Metode Pembayaran", "/himma/pendidikan/views/master_metode_pembayaran/index.fxml"));
                }
                case "kasir" -> {
                    list.add(new MenuItem("Transaksi Penyewaan", "/himma/pendidikan/views/Transaksi.fxml"));
                }
                case "manager" -> {
                    list.add(new MenuItem("Laporan Penyewaan", "/himma/pendidikan/views/Laporan.fxml"));
                }
            }
            list.add(new MenuItem("Logout", "/himma/pendidikan/views/login/index.fxml"));
            return list;
        }
    }
}
