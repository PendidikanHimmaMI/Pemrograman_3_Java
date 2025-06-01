package himma.pendidikan.model;

public class SummaryTabel {
    private String kategori;
    private Integer jumlahAktif;
    private Integer jumlahTidakAktif;
    private Integer total;
    private Double persenAktif;
    private Double persenTidakAktif;

    public SummaryTabel() {
    }

    public SummaryTabel(String kategori, Integer jumlahAktif, Integer jumlahTidakAktif, Integer total, Double persenAktif, Double persenTidakAktif) {
        this.kategori = kategori;
        this.jumlahAktif = jumlahAktif;
        this.jumlahTidakAktif = jumlahTidakAktif;
        this.total = total;
        this.persenAktif = persenAktif;
        this.persenTidakAktif = persenTidakAktif;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Integer getJumlahAktif() {
        return jumlahAktif;
    }

    public void setJumlahAktif(Integer jumlahAktif) {
        this.jumlahAktif = jumlahAktif;
    }

    public Integer getJumlahTidakAktif() {
        return jumlahTidakAktif;
    }

    public void setJumlahTidakAktif(Integer jumlahTidakAktif) {
        this.jumlahTidakAktif = jumlahTidakAktif;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getPersenAktif() {
        return persenAktif;
    }

    public void setPersenAktif(Double persenAktif) {
        this.persenAktif = persenAktif;
    }

    public Double getPersenTidakAktif() {
        return persenTidakAktif;
    }

    public void setPersenTidakAktif(Double persenTidakAktif) {
        this.persenTidakAktif = persenTidakAktif;
    }
}
