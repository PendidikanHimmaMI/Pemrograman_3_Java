package himma.pendidikan.service.impl;

import himma.pendidikan.model.PenyewaanPlaystation;
import himma.pendidikan.service.PenyewaanPlayStationSrvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PenyewaanPlayStationSrvcImpl implements PenyewaanPlayStationSrvc {
    @Override
    public PenyewaanPlaystation resultPentewaan(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public List<PenyewaanPlaystation> getAllData() {
        return List.of();
    }

    @Override
    public List<PenyewaanPlaystation> getAllData(String search, String status, Integer kry_id, Integer mpb_id, String sortColumn, String sortOrder) {
        return List.of();
    }

    @Override
    public boolean saveData(PenyewaanPlaystation penyewaanPlaystation) {
        return false;
    }
}
