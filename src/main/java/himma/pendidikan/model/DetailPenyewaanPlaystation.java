package himma.pendidikan.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class DetailPenyewaanPlaystation {
    private int id;
    private int pps_id;
    private int pst_id;
    private Date waktu_mulai;
    private Date waktu_selesai;
    private Double durasi;
    private Double jumlah_harga;
    private String createby;
    private Date create_date;

    public DetailPenyewaanPlaystation(int pps_id, int pst_id, Date waktu_mulai, Date waktu_selesai, Double durasi, Double jumlah_harga, String createby, Date create_date) {
        this.pps_id = pps_id;
        this.pst_id = pst_id;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.durasi = durasi;
        this.jumlah_harga = jumlah_harga;
        this.createby = createby;
        this.create_date = create_date;
    }

    public DetailPenyewaanPlaystation(int id, int pps_id, int pst_id, Date waktu_mulai, Date waktu_selesai, Double durasi, Double jumlah_harga, String createby, Date create_date) {
        this.id = id;
        this.pps_id = pps_id;
        this.pst_id = pst_id;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.durasi = durasi;
        this.jumlah_harga = jumlah_harga;
        this.createby = createby;
        this.create_date = create_date;
    }

    public DetailPenyewaanPlaystation(Integer id, LocalDateTime waktu_mulai, LocalDateTime waktu_selesai, Double jumlah_harga) {
        this.id = id;
        this.waktu_mulai = Timestamp.valueOf(waktu_mulai);
        this.waktu_selesai = Timestamp.valueOf(waktu_selesai);
        this.jumlah_harga = jumlah_harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPps_id() {
        return pps_id;
    }

    public void setPps_id(int pps_id) {
        this.pps_id = pps_id;
    }

    public int getPst_id() {
        return pst_id;
    }

    public void setPst_id(int pst_id) {
        this.pst_id = pst_id;
    }

    public Date getWaktu_mulai() {
        return waktu_mulai;
    }

    public void setWaktu_mulai(Date waktu_mulai) {
        this.waktu_mulai = waktu_mulai;
    }

    public Date getWaktu_selesai() {
        return waktu_selesai;
    }

    public void setWaktu_selesai(Date waktu_selesai) {
        this.waktu_selesai = waktu_selesai;
    }

    public Double getDurasi() {
        return durasi;
    }

    public void setDurasi(Double durasi) {
        this.durasi = durasi;
    }

    public Double getJumlah_harga() {
        return jumlah_harga;
    }

    public void setJumlah_harga(Double jumlah_harga) {
        this.jumlah_harga = jumlah_harga;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
