package himma.pendidikan.service;

import himma.pendidikan.model.Karyawan;
import himma.pendidikan.model.TopMasterData;

import java.sql.*;
import java.util.List;

public interface KaryawanSrvc {
    Karyawan resultKaryawan(ResultSet rs) throws SQLException;
    List<Karyawan> getAllData();
    List<Karyawan> getAllData(String search, String status, String posisi, String sortColumn, String sortOrder);
    List<TopMasterData> getTop5Karyawan(Integer tahun, Integer bulan);
    Karyawan getDataById(Integer id);
    boolean saveData(Karyawan karyawan);
    boolean updateData(Karyawan karyawan);
    boolean setStatus(Integer id);
    Karyawan auth(String username, String password);
}
