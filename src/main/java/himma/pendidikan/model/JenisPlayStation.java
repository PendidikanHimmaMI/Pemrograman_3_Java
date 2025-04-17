package himma.pendidikan.model;

public class JenisPlayStation {
    private Integer id;
    private String nama;
    private Integer tahunLiris;
    private Integer maxPemain;
    private String deskripsi;
    private String status;
    private String createdby;
    private String modifby;

    public JenisPlayStation() {}

    public JenisPlayStation(String nama, Integer tahunLiris, Integer maxPemain, String deskripsi, String createdby) {
        this.nama = nama;
        this.tahunLiris = tahunLiris;
        this.maxPemain = maxPemain;
        this.deskripsi = deskripsi;
        this.createdby = createdby;
    }

    public JenisPlayStation(Integer id, String nama, Integer tahunLiris, Integer maxPemain, String deskripsi, String status) {
        this.id = id;
        this.nama = nama;
        this.tahunLiris = tahunLiris;
        this.maxPemain = maxPemain;
        this.deskripsi = deskripsi;
        this.status = status;
    }

    public JenisPlayStation(Integer id, String nama, Integer tahunLiris, Integer maxPemain, String deskripsi, String status, String modifby) {
        this.id = id;
        this.nama = nama;
        this.tahunLiris = tahunLiris;
        this.maxPemain = maxPemain;
        this.deskripsi = deskripsi;
        this.status = status;
        this.modifby = modifby;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getTahunLiris() {
        return tahunLiris;
    }

    public void setTahunLiris(Integer tahunLiris) {
        this.tahunLiris = tahunLiris;
    }

    public Integer getMaxPemain() {
        return maxPemain;
    }

    public void setMaxPemain(Integer maxPemain) {
        this.maxPemain = maxPemain;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
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

    public String getModifby() {
        return modifby;
    }

    public void setModifby(String modifby) {
        this.modifby = modifby;
    }
}
