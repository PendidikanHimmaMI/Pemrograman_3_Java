package himma.pendidikan.controller;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.SummaryTabel;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardCtrl {
    @FXML
    Text txtTotalKaryawan, txtTotalPlaystation, txtTotalMetodePembayaran, txtTotalJenisPlaystation;

    DBConnect connect = new DBConnect();

    public void initialize(){
        List<SummaryTabel> summaryTabelList = getSummaryTabel();
        txtTotalJenisPlaystation.setText(summaryTabelList.get(0).getTotal().toString());
        txtTotalPlaystation.setText(summaryTabelList.get(1).getTotal().toString());
        txtTotalKaryawan.setText(summaryTabelList.get(2).getTotal().toString());
        txtTotalMetodePembayaran.setText(summaryTabelList.get(3).getTotal().toString());
    }

    public List<SummaryTabel> getSummaryTabel() {
        List<SummaryTabel> summaryTabelList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_GetDataMasterSummary()";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                summaryTabelList.add(new SummaryTabel(
                        connect.result.getString("Kategori"),
                        connect.result.getInt("JumlahAktif"),
                        connect.result.getInt("JumlahTidakAktif"),
                        connect.result.getInt("Total"),
                        connect.result.getDouble("PersenAktif"),
                        connect.result.getDouble("PersenTidakAktif")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return summaryTabelList;
    }

}
