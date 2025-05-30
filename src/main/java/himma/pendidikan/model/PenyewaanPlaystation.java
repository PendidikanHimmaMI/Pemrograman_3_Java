package himma.pendidikan.model;

import java.util.Date;

public class PenyewaanPlaystation {
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



    public PenyewaanPlaystation() {
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
