package himma.pendidikan.model;

public class TopMasterData {
    private String nama;
    private Integer jumlah;
    private Double persen;

    public TopMasterData() {
    }

    public TopMasterData(String nama, Integer jumlah, Double persen) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.persen = persen;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Double getPersen() {
        return persen;
    }

    public void setPersen(Double persen) {
        this.persen = persen;
    }
}
