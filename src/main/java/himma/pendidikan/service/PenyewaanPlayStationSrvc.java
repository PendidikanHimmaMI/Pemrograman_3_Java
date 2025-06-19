package himma.pendidikan.service;

import himma.pendidikan.model.DetailPenyewaanPlaystation;
import himma.pendidikan.model.PenyewaanPlaystation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface PenyewaanPlayStationSrvc {
    PenyewaanPlaystation resultPenyewaan(ResultSet rs) throws SQLException;
    List<PenyewaanPlaystation> getAllData();
    List<PenyewaanPlaystation> getAllData(String search, String status, Integer jps_id, String sortColumn, String sortOrder);

    boolean saveData(PenyewaanPlaystation penyewaanPlaystation, List<DetailPenyewaanPlaystation> detailList);
}