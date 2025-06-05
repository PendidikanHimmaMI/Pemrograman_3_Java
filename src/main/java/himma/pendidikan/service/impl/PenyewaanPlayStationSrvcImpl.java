package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;
import himma.pendidikan.model.JenisPlayStation;
import himma.pendidikan.model.MetodePembayaran;
import himma.pendidikan.model.PenyewaanPlaystation;
import himma.pendidikan.model.PlayStation;
import himma.pendidikan.service.PenyewaanPlayStationSrvc;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;

import java.util.List;
import java.sql.*;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class PenyewaanPlayStationSrvcImpl implements PenyewaanPlayStationSrvc {
    DBConnect connect = new DBConnect();
    Validation v = new Validation();
    SwalAlert swal = new SwalAlert();
    @Override
    public PenyewaanPlaystation resultPenyewaan(ResultSet rs) throws SQLException {
        PlayStation playStation = new PlayStation(
                v.getInt(rs, "pst_id"),
                v.getString(rs, "jps_nama"),
                v.getString(rs, "pst_serial_number"),
                v.getString(rs, "pst_merk"),
                v.getDouble(rs, "pst_harga_per_jam"),
                v.getString(rs, "pst_status"),
                v.getString(rs, "pst_created_by")
        );
        JenisPlayStation jenisPlayStation = new JenisPlayStation(
                v.getInt(rs, "jps_id"),
                v.getString(rs, "jps_nama"),
                null,
                v.getInt(rs, "jps_max_pemain"),
                null,
                null,
                null
        );
        return new PenyewaanPlaystation(
                v.getInt(rs, "pps_id"),
                v.getInt(rs, "kry_id"),
                v.getString(rs, "kry_nama"),
                v.getInt(rs, "mpb_id"),
                v.getString(rs, "mpb_nama"),
                v.getString(rs, "pps_nama_penyewa"),
                v.getString(rs, "pps_no_hp_penyewa"),
                v.getDate(rs, "pps_tanggal_transaksi"),
                v.getDouble(rs, "pps_total_harga"),
                v.getString(rs, "pps_status"),
                v.getString(rs, "pps_created_by"),
                v.getTimestamp(rs, "pps_created_date")
        );
    }

    @Override
    public List<PenyewaanPlaystation> getAllData() {
        return getAllData(null, null, null, "pst_id", "ASC");
    }

    @Override
    public List<PenyewaanPlaystation> getAllData(String search, String status, Integer jps_id, String sortColumn, String sortOrder) {
        List<PenyewaanPlaystation> penyewaanPlaystations = new ArrayList<>();
        try {
            String query = "EXEC rps_getgetListPlayStation ?, ?, ?, ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, search);
            connect.pstat.setString(2, status);
            connect.pstat.setInt(3, jps_id);
            connect.pstat.setString(4, sortColumn);
            connect.pstat.setString(5, sortOrder);

            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                penyewaanPlaystations.add(resultPenyewaan(connect.result));
            }
            connect.result.close();
            connect.pstat.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return penyewaanPlaystations;
    }

    @Override
    public boolean saveData(PenyewaanPlaystation penyewaanPlaystation) {
        return false;
    }


}
