package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.model.Karyawan;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.service.JenisPlayStationSrvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JenisPlayStationSrvcImpl implements JenisPlayStationSrvc {
    DBConnect connect = new DBConnect();

    @Override
    public JenisPlayStation resultJenisPlayStation(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public List<JenisPlayStation> getAllData() {
        return null;
    }

    @Override
    public List<JenisPlayStation> getAllData(String search, String status, String sortColumn, String sortOrder) {
        return null;
    }

    @Override
    public List<TopMasterData> getTop5JenisPlayStation(Integer tahun, Integer bulan) {
        List<TopMasterData> jenisPlaystationList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_Top5JenisPlaystationPalingSeringDisewa(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                jenisPlaystationList.add(new TopMasterData(
                        connect.result.getString("jps_name"),
                        connect.result.getInt("jumlah_disewa"),
                        connect.result.getDouble("persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jenisPlaystationList;
    }

    @Override
    public JenisPlayStation getDataById(Integer id) {
        return null;
    }

    @Override
    public boolean saveData(JenisPlayStation jenisPlayStation) {
        return false;
    }

    @Override
    public boolean updateData(JenisPlayStation jenisPlayStation) {
        return false;
    }

    @Override
    public boolean setStatus(Integer id) {
        return false;
    }
}
