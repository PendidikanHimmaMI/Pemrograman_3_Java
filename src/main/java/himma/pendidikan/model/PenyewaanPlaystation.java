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

    public PenyewaanPlaystation(Karyawan karyawan, MetodePembayaran metodePembayaran, String namaPenyewa, String noTeleponPenyewa, Date tanggalSewa, Double totalHarga, String createdby) {
        this.karyawan = karyawan;
        this.metodePembayaran = metodePembayaran;
        this.namaPenyewa = namaPenyewa;
        this.noTeleponPenyewa = noTeleponPenyewa;
        this.tanggalSewa = tanggalSewa;
        this.totalHarga = totalHarga;
        this.createdby = createdby;
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
}
