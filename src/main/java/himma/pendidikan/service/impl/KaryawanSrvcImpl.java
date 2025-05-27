package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;
import himma.pendidikan.model.Karyawan;
import himma.pendidikan.service.KaryawanSrvc;

import java.sql.*;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class KaryawanSrvcImpl implements KaryawanSrvc{
    DBConnect connect = new DBConnect();
    Validation v = new Validation();
    SwalAlert swal = new SwalAlert();
    @Override
    public Karyawan resultKaryawan(ResultSet rs) throws SQLException {
        return new Karyawan(
        v.getInt(rs,"kry_id"),
        v.getString(rs,"kry_nama"),
        v.getString(rs,"kry_posisi"),
        v.getString(rs,"kry_alamat"),
        v.getString(rs,"kry_no_hp"),
        v.getString(rs,"kry_email"),
        v.getString(rs,"kry_username"),
        v.getString(rs,"kry_password"),
        v.getString(rs,"kry_status"));
    }

    @Override
    public List<Karyawan> getAllData() {
        return getAllData(null, null, null, "kry_id", "ASC");
    }

    @Override
    public List<Karyawan> getAllData(String search, String status, String posisi, String sortColumn, String sortOrder) {
        List<Karyawan> karyawanList = new ArrayList<>();
        try {
            String query = "EXEC rps_getListKaryawan ?, ?, ?, ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, search);
            connect.pstat.setString(2, status);
            connect.pstat.setString(3, posisi);
            connect.pstat.setString(4, sortColumn);
            connect.pstat.setString(5, sortOrder);

            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                karyawanList.add(resultKaryawan(connect.result));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return karyawanList;
    }

    @Override
    public List<TopMasterData> getTop5Karyawan(Integer tahun, Integer bulan) {
        List<TopMasterData> karyawanList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_Top5KaryawanPalingSeringTransaksi(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                karyawanList.add(new TopMasterData(
                        connect.result.getString("kry_nama"),
                        connect.result.getInt("jumlah_transaksi"),
                        connect.result.getDouble("persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return karyawanList;
    }

    @Override
    public Karyawan getDataById(Integer id)  {
        try {
            String query = "SELECT * FROM rps_getByIdKaryawan(?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, id);
            connect.result = connect.pstat.executeQuery();
            if(connect.result.next()) {
                return resultKaryawan(connect.result);
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveData(Karyawan kry) {
        try{
            String query = "{call rps_createKaryawan(?, ?, ?, ?, ?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setString(1, kry.getNama());
            connect.cstat.setString(2, kry.getPosisi());
            connect.cstat.setString(3, kry.getAlamat());
            connect.cstat.setString(4, kry.getNoTelepon());
            connect.cstat.setString(5, kry.getEmail());
            connect.cstat.setString(6, kry.getUsername());
            connect.cstat.setString(7, kry.getPassword());
            connect.cstat.setString(8, kry.getCreatedby());
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
    public boolean updateData(Karyawan kry) {
        try{
            String query = "{call rps_updateKaryawan(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, kry.getId());
            connect.cstat.setString(2, kry.getNama());
            connect.cstat.setString(3, kry.getPosisi());
            connect.cstat.setString(4, kry.getAlamat());
            connect.cstat.setString(5, kry.getNoTelepon());
            connect.cstat.setString(6, kry.getEmail());
            connect.cstat.setString(7, kry.getUsername());
            connect.cstat.setString(8, kry.getPassword());
            connect.cstat.setString(9, kry.getModifby());
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
            String query = "{call rps_setStatusKaryawan(?)}";
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

    @Override
    public Karyawan auth(String username, String password) {
        try {
            String query = "EXEC rps_loginKaryawan ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, username);
            connect.pstat.setString(2, password);
            connect.result = connect.pstat.executeQuery();
            if(connect.result.next()) {
                return resultKaryawan(connect.result);
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
