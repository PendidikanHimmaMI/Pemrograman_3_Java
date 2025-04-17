package himma.pendidikan.model;

public class PlayStation {
    private Integer id;
    private JenisPlayStation jenisPlayStation;
    private String nama;
    private Integer tahunLiris;
    private Integer maxPemain;
    private String deskripsi;
    private String status;
    private String createdby;
    private String modifby;

    public PlayStation() {}

    public PlayStation(JenisPlayStation jenisPlayStation, String nama, Integer tahunLiris, Integer maxPemain, String deskripsi, String createdby) {
        this.jenisPlayStation = jenisPlayStation;
        this.nama = nama;
        this.tahunLiris = tahunLiris;
        this.maxPemain = maxPemain;
        this.deskripsi = deskripsi;
        this.createdby = createdby;
    }

    public PlayStation(Integer id, JenisPlayStation jenisPlayStation, String nama, Integer tahunLiris, Integer maxPemain, String deskripsi, String modifby) {
        this.id = id;
        this.jenisPlayStation = jenisPlayStation;
        this.nama = nama;
        this.tahunLiris = tahunLiris;
        this.maxPemain = maxPemain;
        this.deskripsi = deskripsi;
        this.modifby = modifby;
    }

    public PlayStation(Integer id, JenisPlayStation jenisPlayStation, String nama, Integer tahunLiris, Integer maxPemain, String deskripsi, String status, String createdby, String modifby) {
        this.id = id;
        this.jenisPlayStation = jenisPlayStation;
        this.nama = nama;
        this.tahunLiris = tahunLiris;
        this.maxPemain = maxPemain;
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

    public JenisPlayStation getJenisPlayStation() {
        return jenisPlayStation;
    }

    public void setJenisPlayStation(JenisPlayStation jenisPlayStation) {
        this.jenisPlayStation = jenisPlayStation;
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

    public static class TopPlayStation{
        private String nama;
        private Integer jumlahSewa;
        private Double persen;

        public TopPlayStation(String nama, Integer jumlahSewa, Double persen) {
            this.nama = nama;
            this.jumlahSewa = jumlahSewa;
            this.persen = persen;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public Integer getJumlahSewa() {
            return jumlahSewa;
        }

        public void setJumlahSewa(Integer jumlahSewa) {
            this.jumlahSewa = jumlahSewa;
        }

        public Double getPersen() {
            return persen;
        }

        public void setPersen(Double persen) {
            this.persen = persen;
        }
    }
}
