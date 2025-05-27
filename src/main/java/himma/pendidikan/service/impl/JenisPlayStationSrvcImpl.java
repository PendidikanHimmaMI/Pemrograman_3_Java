package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.service.JenisPlayStationSrvc;

import java.sql.*;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class JenisPlayStationSrvcImpl implements JenisPlayStationSrvc {
    DBConnect connect = new DBConnect();
    Validation v = new Validation();
    SwalAlert swal = new SwalAlert();
    @Override
    public JenisPlayStation resultJenisPlayStation(ResultSet rs) throws SQLException {
        return new JenisPlayStation(
                v.getInt(rs,"jps_id"),
                v.getString(rs,"jps_nama"),
                v.getInt(rs,"jps_tahun_rilis"),
                v.getInt(rs,"jps_max_pemain"),
                v.getString(rs,"jps_deskripsi"),
                v.getString(rs,"jps_status"));
    }

    @Override
    public List<JenisPlayStation> getAllData() {
        return getAllData(null, null, "jps_id", "ASC");
    }

//    @Override
//    public List<JenisPlayStation> getAllData(String search, String status, String sortColumn, String sortOrder) {
//        return List.of();
//    }

    @Override
    public List<JenisPlayStation> getAllData(String search, String status, String sortColumn, String sortOrder) {
        List<JenisPlayStation> jenisPlayStationList = new ArrayList<>();
        try {
            String query = "EXEC rps_getListJenisPlayStation ?, ?, ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, search);
            connect.pstat.setString(2, status);
            connect.pstat.setString(3, sortColumn);
            connect.pstat.setString(4, sortOrder);

            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                jenisPlayStationList.add(resultJenisPlayStation(connect.result));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jenisPlayStationList;
    }

    @Override
    public List<TopMasterData> getTop5JenisPlayStation(Integer tahun, Integer bulan) {
        List<TopMasterData> jenisPlayStationList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_Top5JenisPlayStationPalingSeringTransaksi(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                jenisPlayStationList.add(new TopMasterData(
                        connect.result.getString("jps_nama"),
                        connect.result.getInt("jumlah_transaksi"),
                        connect.result.getDouble("persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jenisPlayStationList;
    }

    @Override
    public JenisPlayStation getDataById(Integer id) {
        try {
            String query = "SELECT * FROM rps_getByIdJenisPlayStation(?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, id);
            connect.result = connect.pstat.executeQuery();
            if(connect.result.next()) {
                return resultJenisPlayStation(connect.result);
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveData(JenisPlayStation jps) {
        try{
            String query = "{call rps_createJenisPlayStation(?, ?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setString(1, jps.getNama());
            connect.cstat.setInt(2, jps.getTahunLiris());
            connect.cstat.setInt(3, jps.getMaxPemain());
            connect.cstat.setString(4, jps.getDeskripsi());
            connect.cstat.setString(5, jps.getCreatedby());
            connect.cstat.execute();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Data berhasil ditambahkan!",false);
            return true;
        }catch (SQLException e){
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }

    @Override
    public boolean updateData(JenisPlayStation jps) {
        try{
            String query = "{call rps_updateJenisPlayStation(?, ?, ?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, jps.getId());
            connect.cstat.setString(2, jps.getNama());
            connect.cstat.setInt(3, jps.getTahunLiris());
            connect.cstat.setInt(4, jps.getMaxPemain());
            connect.cstat.setString(5, jps.getDeskripsi());
            connect.cstat.setString(6, jps.getModifby());
            System.out.println(jps.getModifby());
            connect.cstat.execute();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Data berhasil diubah!",false);
            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage()   );
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }

    @Override
    public boolean setStatus(Integer id) {
        try {
            String query = "{call rps_setStatusJenisPlayStation(?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, id);
            connect.cstat.executeUpdate();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Status berhasil diubah",false);
            return true;
        }catch (SQLException e){
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }

}
