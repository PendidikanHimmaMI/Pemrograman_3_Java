package himma.pendidikan.model;

import java.sql.Timestamp;
import java.util.Date;

public class PenyewaanPlaystation {
    private Date ppsCreatedDate;
    private  Integer kryId;
    private Integer mpbId;
    private Integer id;
    private Karyawan karyawan;
    private MetodePembayaran metodePembayaran;
    private PlayStation playStation;
    private JenisPlayStation jenisPlayStation;
    private String namaPenyewa;
    private String noTeleponPenyewa;
    private Date tanggalSewa;
    private Double totalHarga;
    private String status;
    private String createdby;



    public PenyewaanPlaystation(Integer ppsId, Integer kryId, String kryNama, Integer mpbId, String mpbNama, String ppsNamaPenyewa, String ppsNoHpPenyewa, java.sql.Date ppsTanggalTransaksi, Double ppsTotalHarga, String ppsStatus, String ppsCreatedBy, Timestamp ppsCreatedDate) {
    }

    public PenyewaanPlaystation(Integer ppsId, Integer kryId, Integer mpbId, String ppsNamaPenyewa, String ppsNoHpPenyewa, Date ppsTanggalTransaksi, Double ppsTotalHarga, String ppsStatus, String ppsCreatedBy, Date ppsCreatedDate) {
        this.id = ppsId;
        this.kryId = kryId;
        this.mpbId = mpbId;
        this.namaPenyewa = ppsNamaPenyewa;
        this.noTeleponPenyewa = ppsNoHpPenyewa;
        this.tanggalSewa = ppsTanggalTransaksi;
        this.totalHarga = ppsTotalHarga;
        this.status = ppsStatus;
        this.createdby = ppsCreatedBy;
        this.ppsCreatedDate = ppsCreatedDate;
    }

    public PenyewaanPlaystation(PlayStation playStation, JenisPlayStation jenisPlayStation) {
        this.playStation = playStation;
        this.jenisPlayStation = jenisPlayStation;
    }

    public PenyewaanPlaystation(Integer id, Karyawan karyawan, MetodePembayaran metodePembayaran, String namaPenyewa, String noTeleponPenyewa, Date tanggalSewa, Double totalHarga, String status) {
        this.id = id;
        this.karyawan = karyawan;
        this.metodePembayaran = metodePembayaran;
        this.namaPenyewa = namaPenyewa;
        this.noTeleponPenyewa = noTeleponPenyewa;
        this.tanggalSewa = tanggalSewa;
        this.totalHarga = totalHarga;
        this.status = status;
    }

    public PenyewaanPlaystation(Integer id, Karyawan karyawan, MetodePembayaran metodePembayaran, String namaPenyewa, String noTeleponPenyewa, Date tanggalSewa, Double totalHarga, String status, String createdby) {
        this.id = id;
        this.karyawan = karyawan;
        this.metodePembayaran = metodePembayaran;
        this.namaPenyewa = namaPenyewa;
        this.noTeleponPenyewa = noTeleponPenyewa;
        this.tanggalSewa = tanggalSewa;
        this.totalHarga = totalHarga;
        this.status = status;
        this.createdby = createdby;
    }

    public PenyewaanPlaystation(int karId, int mpbId, String nama, String telepon, Date tanggal, Double totalHarga, String createdBy, Date createDate) {
        this.kryId = karId;
        this.mpbId = mpbId;
        this.namaPenyewa = nama;
        this.noTeleponPenyewa = telepon;
        this.tanggalSewa = tanggal;
        this.totalHarga = totalHarga;
        this.createdby = createdBy;
        this.ppsCreatedDate = createDate;
    }


    public Date getPpsCreatedDate() {
        return ppsCreatedDate;
    }

    public void setPpsCreatedDate(Date ppsCreatedDate) {
        this.ppsCreatedDate = ppsCreatedDate;
    }

    public Integer getKryId() {
        return kryId;
    }

    public void setKryId(Integer kryId) {
        this.kryId = kryId;
    }

    public Integer getMpbId() {
        return mpbId;
    }

    public void setMpbId(Integer mpbId) {
        this.mpbId = mpbId;
    }

    public PlayStation getPlayStation() {
        return playStation;
    }

    public void setPlayStation(PlayStation playStation) {
        this.playStation = playStation;
    }

    public JenisPlayStation getJenisPlayStation() {
        return jenisPlayStation;
    }

    public void setJenisPlayStation(JenisPlayStation jenisPlayStation) {
        this.jenisPlayStation = jenisPlayStation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Karyawan getKaryawan() {
        return karyawan;
    }

    public void setKaryawan(Karyawan karyawan) {
        this.karyawan = karyawan;
    }

    public MetodePembayaran getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(MetodePembayaran metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public String getNamaPenyewa() {
        return namaPenyewa;
    }

    public void setNamaPenyewa(String namaPenyewa) {
        this.namaPenyewa = namaPenyewa;
    }

    public String getNoTeleponPenyewa() {
        return noTeleponPenyewa;
    }

    public void setNoTeleponPenyewa(String noTeleponPenyewa) {
        this.noTeleponPenyewa = noTeleponPenyewa;
    }

    public Date getTanggalSewa() {
        return tanggalSewa;
    }

    public void setTanggalSewa(Date tanggalSewa) {
        this.tanggalSewa = tanggalSewa;
    }

    public Double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }


    public static class RekapTransaksiBulanan {
        private Integer totalPenyewaan;
        private double totalPendapatan;

        public RekapTransaksiBulanan() {
        }

        public RekapTransaksiBulanan(Integer totalPenyewaan, double totalPendapatan) {
            this.totalPenyewaan = totalPenyewaan;
            this.totalPendapatan = totalPendapatan;
        }

        public Integer getTotalPenyewaan() {
            return totalPenyewaan;
        }

        public void setTotalPenyewaan(Integer totalPenyewaan) {
            this.totalPenyewaan = totalPenyewaan;
        }

        public double getTotalPendapatan() {
            return totalPendapatan;
        }

        public void setTotalPendapatan(double totalPendapatan) {
            this.totalPendapatan = totalPendapatan;
        }
    }

    public static class RekapPendapatanHarian {
        private Integer tanggal;
        private double totalPendapatan;

        public RekapPendapatanHarian() {
        }

        public RekapPendapatanHarian(Integer tanggal, double totalPendapatan) {
            this.tanggal = tanggal;
            this.totalPendapatan = totalPendapatan;
        }

        public Integer getTanggal() {
            return tanggal;
        }

        public void setTanggal(Integer tanggal) {
            this.tanggal = tanggal;
        }

        public double getTotalPendapatan() {
            return totalPendapatan;
        }

        public void setTotalPendapatan(double totalPendapatan) {
            this.totalPendapatan = totalPendapatan;
        }
    }

    public static class RekapTransaksiHarian {
        private Integer tanggal;
        private double totalTransaksi;

        public RekapTransaksiHarian() {
        }

        public RekapTransaksiHarian(Integer tanggal, double totalTransaksi) {
            this.tanggal = tanggal;
            this.totalTransaksi = totalTransaksi;
        }

        public Integer getTanggal() {
            return tanggal;
        }

        public void setTanggal(Integer tanggal) {
            this.tanggal = tanggal;
        }

        public double getTotalTransaksi() {
            return totalTransaksi;
        }

        public void setTotalTransaksi(double totalPendapatan) {
            this.totalTransaksi = totalPendapatan;
        }
    }
}
