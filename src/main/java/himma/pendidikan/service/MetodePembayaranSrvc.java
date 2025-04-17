package himma.pendidikan.service;

import himma.pendidikan.model.MetodePembayaran;

import java.sql.*;
import java.util.List;

public interface MetodePembayaranSrvc {
    MetodePembayaran resultMetodePembayaran(ResultSet rs) throws SQLException;
    List<MetodePembayaran> getAllData();
    List<MetodePembayaran> getAllData(String search, String status, String sortColumn, String sortOrder);
    List<MetodePembayaran.TopMetodePembayaran> getTop5MetodePembayaran(Integer tahun, Integer bulan);
    MetodePembayaran getDataById(Integer id);
    boolean saveData(MetodePembayaran metodePembayaran);
    boolean updateData(MetodePembayaran metodePembayaran);
    boolean setStatus(Integer id);
}
