package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.service.PlayStationSrvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayStationSrvcImpl implements PlayStationSrvc {
    DBConnect connect = new DBConnect();

    @Override
    public PlayStation resultPlayStation(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public List<PlayStation> getAllData() {
        return null;
    }

    @Override
    public List<PlayStation> getAllData(String search, String status, Integer idJenisPlayStation, String sortColumn, String sortOrder) {
        return null;
    }

    @Override
    public List<TopMasterData> getTop5PlayStation(Integer tahun, Integer bulan) {
        List<TopMasterData> playStationList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_Top5PlaystationPalingSeringDisewa(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                playStationList.add(new TopMasterData(
                        connect.result.getString("pst_serial_number"),
                        connect.result.getInt("jumlah_disewa"),
                        connect.result.getDouble("persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return playStationList;
    }

    @Override
    public PlayStation getDataById(Integer id) {
        return null;
    }

    @Override
    public boolean saveData(PlayStation playStation) {
        return false;
    }

    @Override
    public boolean updateData(PlayStation playStation) {
        return false;
    }

    @Override
    public boolean setStatus(Integer id) {
        return false;
    }
}
