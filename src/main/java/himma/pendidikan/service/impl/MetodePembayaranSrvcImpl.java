package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.MetodePembayaran;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.service.MetodePembayaranSrvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodePembayaranSrvcImpl implements MetodePembayaranSrvc{
    DBConnect connect = new DBConnect();

    @Override
    public MetodePembayaran resultMetodePembayaran(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public List<MetodePembayaran> getAllData() {
        return null;
    }

    @Override
    public List<MetodePembayaran> getAllData(String search, String status, String sortColumn, String sortOrder) {
        return null;
    }

    @Override
    public List<TopMasterData> getTop5MetodePembayaran(Integer tahun, Integer bulan) {
        List<TopMasterData> metodePembayaranList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_Top5MetodePembayaranPalingSeringDipakai(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                metodePembayaranList.add(new TopMasterData(
                        connect.result.getString("mpb_nama"),
                        connect.result.getInt("jumlah_digunakan"),
                        connect.result.getDouble("persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return metodePembayaranList;
    }

    @Override
    public MetodePembayaran getDataById(Integer id) {
        return null;
    }

    @Override
    public boolean saveData(MetodePembayaran metodePembayaran) {
        return false;
    }

    @Override
    public boolean updateData(MetodePembayaran metodePembayaran) {
        return false;
    }

    @Override
    public boolean setStatus(Integer id) {
        return false;
    }
}