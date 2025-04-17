package himma.pendidikan.service;

import himma.pendidikan.model.JenisPlayStation;

import java.sql.*;
import java.util.List;

public interface JenisPlayStationSrvc {
    JenisPlayStation resultJenisPlayStation(ResultSet rs) throws SQLException;
    List<JenisPlayStation> getAllData();
    List<JenisPlayStation> getAllData(String search, String status, String sortColumn, String sortOrder);
    List<JenisPlayStation.TopJenisPlayStation> getTop5JenisPlayStation(Integer tahun, Integer bulan);
    JenisPlayStation getDataById(Integer id);
    boolean saveData(JenisPlayStation jenisPlayStation);
    boolean updateData(JenisPlayStation jenisPlayStation);
    boolean setStatus(Integer id);
}
