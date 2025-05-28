package himma.pendidikan.service;

import himma.pendidikan.model.PlayStation;
import himma.pendidikan.model.TopMasterData;

import java.sql.*;
import java.util.List;

public interface PlayStationSrvc {
    PlayStation resultPlayStation(ResultSet rs) throws SQLException;
    List<PlayStation> getAllData();
    List<PlayStation> getAllData(String search, String status, String idJenisPlayStation, String sortColumn, String sortOrder);
    List<TopMasterData> getTop5PlayStation(Integer tahun, Integer bulan);
    PlayStation getDataById(Integer id);
    boolean saveData(PlayStation playStation);
    boolean updateData(PlayStation playStation);
    boolean setStatus(Integer id);
}
