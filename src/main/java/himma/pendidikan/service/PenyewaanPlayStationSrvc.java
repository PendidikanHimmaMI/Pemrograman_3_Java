package himma.pendidikan.service;

import himma.pendidikan.model.PenyewaanPlaystation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface PenyewaanPlayStationSrvc {
    PenyewaanPlaystation resultPentewaan(ResultSet rs) throws SQLException;
    List<PenyewaanPlaystation> getAllData();
    List<PenyewaanPlaystation> getAllData(String search, String status, Integer kry_id, Integer mpb_id, String sortColumn, String sortOrder);
    boolean saveData(PenyewaanPlaystation penyewaanPlaystation);
}