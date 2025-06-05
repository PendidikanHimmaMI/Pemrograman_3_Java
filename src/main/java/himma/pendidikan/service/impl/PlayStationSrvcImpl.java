package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.service.PlayStationSrvc;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.*;

public class PlayStationSrvcImpl implements PlayStationSrvc {
    DBConnect connect = new DBConnect();
    Validation v = new Validation();
    SwalAlert swal = new SwalAlert();

    @Override
    public PlayStation resultPlayStation(ResultSet rs) throws SQLException {
        return new PlayStation(
                v.getInt(rs, "pst_id"),
                v.getString(rs, "jps_nama"),
                v.getString(rs, "pst_serial_number"),
                v.getString(rs, "pst_merk"),
                v.getDouble(rs, "pst_harga_per_jam"),
                v.getString(rs, "pst_status"),
                v.getString(rs, "pst_created_by")
        );
    }

    @Override
    public List<PlayStation> getAllData(String search, String status, String sortColumn, String sortOrder) {
        return getAllData(search, status, null, "pst_id", sortOrder);
    }

    @Override
    public List<PlayStation> getAllData(String search, String status, String idJenisPlayStation, String sortColumn, String sortOrder) {
        List<PlayStation> playStationList = new ArrayList<>();
        try {
            String query = "EXEC rps_getListPlayStation ?, ?, ?, ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, search);
            connect.pstat.setString(2, status);
            connect.pstat.setString(3, idJenisPlayStation);
            connect.pstat.setString(4, sortColumn);
            connect.pstat.setString(5, sortOrder);

            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                playStationList.add(resultPlayStation(connect.result));
            }

            connect.result.close();
            connect.pstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playStationList;
    }

    @Override
    public List<TopMasterData> getTop5PlayStation(Integer tahun, Integer bulan) {
        List<TopMasterData> playStationList = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playStationList;
    }

    @Override
    public PlayStation getDataById(Integer id) {
        try {
            String query = "SELECT * FROM rps_getByIdPlayStation(?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, id);
            connect.result = connect.pstat.executeQuery();

            if (connect.result.next()) {
                return resultPlayStation(connect.result);
            }

            connect.result.close();
            connect.pstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveData(PlayStation playStation) {
        try {
            String query = "{call rps_createPlayStation(?, ?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, playStation.getIdJenisPlaystation());
            connect.cstat.setString(2, playStation.getSerialNumber());
            connect.cstat.setString(3, playStation.getMerkPs());
            connect.cstat.setDouble(4, playStation.getHargaPS());
            connect.cstat.setString(5, playStation.getCreatedby());

            connect.cstat.execute();
            connect.cstat.close();
            swal.showAlert(INFORMATION, "Berhasil", "Data berhasil ditambahkan!", false);
            return true;
        } catch (SQLException e) {
            swal.showAlert(ERROR, "Gagal", e.getMessage(), false);
            return false;
        }
    }

    @Override
    public boolean updateData(PlayStation playStation) {
        try {
            String query = "{call rps_updatePlayStation(?, ?, ?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, playStation.getIdPS());
            connect.cstat.setInt(2, playStation.getIdJenisPlaystation());
            connect.cstat.setString(3, playStation.getSerialNumber());
            connect.cstat.setString(4, playStation.getMerkPs());
            connect.cstat.setDouble(5, playStation.getHargaPS());
            connect.cstat.setString(6, playStation.getModifby());

            connect.cstat.execute();
            connect.cstat.close();
            swal.showAlert(INFORMATION, "Berhasil", "Data berhasil diubah!", false);
            return true;
        } catch (SQLException e) {
            swal.showAlert(ERROR, "Gagal", e.getMessage(), false);
            return false;
        }
    }

    @Override
    public boolean setStatus(Integer id) {
        try {
            String query = "{call rps_setStatusPlayStation(?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, id);
            connect.cstat.executeUpdate();
            connect.cstat.close();
            swal.showAlert(INFORMATION, "Berhasil", "Status berhasil diubah", false);
            return true;
        } catch (SQLException e) {
            swal.showAlert(ERROR, "Gagal", e.getMessage(), false);
            return false;
        }
    }
}
