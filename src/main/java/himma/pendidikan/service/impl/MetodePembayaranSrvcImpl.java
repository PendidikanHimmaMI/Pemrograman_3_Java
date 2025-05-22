package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.MetodePembayaran;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.service.MetodePembayaranSrvc;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;

import java.util.List;
import java.sql.*;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class MetodePembayaranSrvcImpl implements MetodePembayaranSrvc {
    DBConnect connect = new DBConnect();
    Validation v = new Validation();
    SwalAlert swal = new SwalAlert();

    @Override
    public MetodePembayaran resultMetodePembayaran(ResultSet rs) throws SQLException {
        return new MetodePembayaran(
                v.getInt(rs, "mpb_id"),
                v.getString(rs, "mpb_nama"),
                v.getString(rs,"mpb_deskripsi"),
                v.getString(rs, "mpb_status"),
                v.getString(rs, "mpb_created_by"),
                v.getString(rs, "mpb_modif_by")
        );
    }

    @Override
    public List<MetodePembayaran> getAllData() {
        return getAllData(null, null, "mpb_id", "ASC");
    }

    @Override
    public List<MetodePembayaran> getAllData(String search, String status, String sortColumn, String sortOrder) {
        List<MetodePembayaran> metodePembayaranList = new ArrayList<>();
        try {
            String query = "EXEC rps_getListMetodePembayaran ?, ?, ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, search);
            connect.pstat.setString(2, status);
            connect.pstat.setString(3, sortColumn);
            connect.pstat.setString(4, sortOrder);

            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                metodePembayaranList.add(resultMetodePembayaran(connect.result));
            }
            connect.result.close();
            connect.pstat.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return metodePembayaranList;
    }

    @Override
    public List<TopMasterData> getTop5MetodePembayaran(Integer tahun, Integer bulan) {
        List<TopMasterData> metodePembayaranList = new ArrayList<>();
        try {
            String query = "SELECT * FROM fn_Top5MetodePembayaranPalingSeringDipakai(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                metodePembayaranList.add(new TopMasterData(
                   connect.result.getString("mpb_nama"),
                   connect.result.getInt("jumlah_digunakan"),
                   connect.result.getDouble( "persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return metodePembayaranList;
    }

    @Override
    public MetodePembayaran getDataById(Integer id) {
        try {
            String query = "SELECT * FROM rps_getByIdMetodePembayaran(?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, id);
            connect.result = connect.pstat.executeQuery();
            if(connect.result.next()) {
                return resultMetodePembayaran(connect.result);
            }
            connect.result.close();
            connect.pstat.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveData(MetodePembayaran metodePembayaran) {
        try{
            String query = "{call rps_createMetodePembayaran(?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setString(1, metodePembayaran.getNama());
            connect.cstat.setString(2, metodePembayaran.getDeskripsi());
            connect.cstat.setString(3, metodePembayaran.getCreatedby());
//            connect.cstat.setString(4, metodePembayaran.getModifby());
            connect.cstat.execute();

            connect.cstat.close();
            swal.showAlert(INFORMATION, "Berhasil", "Data berhasil ditambahkan!", false);
            return true;
        }
        catch (SQLException e){
            swal.showAlert(ERROR, "Gagal", e.getMessage(), false);
            return false;
        }
    }

    @Override
    public boolean updateData(MetodePembayaran metodePembayaran) {
        try {
            String query = "{call rps_updateMetodePembayaran(?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, metodePembayaran.getId());
            connect.cstat.setString(2, metodePembayaran.getNama());
            connect.cstat.setString(3, metodePembayaran.getDeskripsi());
//            connect.cstat.setString(4, metodePembayaran.getCreatedby());
            connect.cstat.setString(4, metodePembayaran.getModifby());
            connect.cstat.execute();

            connect.cstat.close();
            swal.showAlert(INFORMATION, "Berhasil", "Data berhasil diubah!", false);
            return true;
        }
        catch (SQLException e){
                System.out.println(e.getMessage()   );
                swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
                return false;
        }
    }

    @Override
    public boolean setStatus(Integer id) {
        try {
            String query = "{call rps_setStatusMetodePembayaran(?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, id);
            connect.cstat.executeUpdate();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Status berhasil diubah",false);
            return true;
        }
        catch (SQLException e){
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }
}