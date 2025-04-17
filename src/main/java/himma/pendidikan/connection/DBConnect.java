package himma.pendidikan.connection;


import himma.pendidikan.model.Karyawan;
import himma.pendidikan.model.TopMasterData;
import himma.pendidikan.service.impl.KaryawanSrvcImpl;

import java.sql.*;
import java.util.List;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;
    public CallableStatement cstat;

    public DBConnect() {
        try {
            String url = "jdbc:sqlserver://127.0.0.4:9210;database=Db_RentalPlayStation;user=Pendidikan;password=123;encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Error saat connect database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DBConnect db = new DBConnect();
        KaryawanSrvcImpl karyawanSrvc = new KaryawanSrvcImpl();

        List<TopMasterData> topList = karyawanSrvc.getTop5Karyawan(2025, 4);

        if (topList.isEmpty()) {
            System.out.println("Tidak ada data ditemukan.");
        } else {
            for (TopMasterData k : topList) {
                System.out.println("Nama: " + k.getNama());
                System.out.println("Jumlah Transaksi: " + k.getJumlah());
                System.out.println("Persen: " + k.getPersen() + "%");
                System.out.println("-----------------------------");
            }
        }
        System.out.println("Connected to database");
    }
}
