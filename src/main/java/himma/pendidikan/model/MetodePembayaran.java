package himma.pendidikan.model;

public class MetodePembayaran {
    private Integer id;
    private String nama;
    private String deskripsi;
    private String status;
    private String createdby;
    private String modifby;

    public MetodePembayaran() {}

    public MetodePembayaran(String nama, String deskripsi, String createdby) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.createdby = createdby;
    }


    public MetodePembayaran(Integer id, String nama, String deskripsi, String modifby) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.modifby = modifby;
    }

    public MetodePembayaran(Integer id, String nama, String deskripsi, String status, String createdby, String modifby) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.status = status;
        this.createdby = createdby;
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
