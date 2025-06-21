package himma.pendidikan.service.impl;

import himma.pendidikan.connection.DBConnect;

import himma.pendidikan.model.*;

import himma.pendidikan.service.PenyewaanPlayStationSrvc;
import himma.pendidikan.util.SwalAlert;
import himma.pendidikan.util.Validation;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;


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
    public boolean saveData(PenyewaanPlaystation penyewaanPlaystation, List<DetailPenyewaanPlaystation> detailList) {
        try {
            String sql = "{call rps_createTransaksiPenyewaan(?, ?, ?, ?, ?, ?, ?)}";

            SQLServerDataTable tvp = new SQLServerDataTable();
            tvp.addColumnMetadata("pst_id", java.sql.Types.INTEGER);
            tvp.addColumnMetadata("dps_waktu_mulai", java.sql.Types.TIMESTAMP);
            tvp.addColumnMetadata("dps_waktu_selesai", java.sql.Types.TIMESTAMP);
            tvp.addColumnMetadata("dps_jumlah_harga", java.sql.Types.DECIMAL);

            for (DetailPenyewaanPlaystation detail : detailList) {
                System.out.println(detail.getPst_id() + " ini id");
                System.out.println(detail.getDurasi()+"durasi");
                tvp.addRow(
                        detail.getId(),
                        Timestamp.valueOf(String.valueOf(detail.getWaktu_mulai())),
                        Timestamp.valueOf(String.valueOf(detail.getWaktu_selesai())),
                        detail.getJumlah_harga()
                );
            }

            SQLServerCallableStatement stmt = (SQLServerCallableStatement) connect.conn.prepareCall(sql);
            stmt.setInt(1, penyewaanPlaystation.getKryId());
            stmt.setInt(2, penyewaanPlaystation.getMpbId());
            stmt.setString(3, penyewaanPlaystation.getNamaPenyewa());
            stmt.setString(4, penyewaanPlaystation.getNoTeleponPenyewa());
            stmt.setDouble(5, penyewaanPlaystation.getTotalHarga());
            stmt.setString(6, penyewaanPlaystation.getCreatedby());
            stmt.setStructured(7, "DetailPenyewaanType", tvp); // TVP!

            stmt.execute();
            stmt.close();

            swal.showAlert(INFORMATION, "Berhasil", "Data berhasil disimpan!", false);
            return true;

        } catch (SQLException e) {
            swal.showAlert(ERROR, "Gagal", e.getMessage(), false);
            System.out.println(e.getMessage());
            return false;
        }
    }



}
