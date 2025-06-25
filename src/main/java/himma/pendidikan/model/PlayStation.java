package himma.pendidikan.model;

public class PlayStation {
    private Integer idPS;
    private JenisPlayStation jenisPlaystation;
    private String serialNumber;
    private String merkPs;
    private Double hargaPS;
    private String status;
    private String createdby;
    private String modifby;
    private Integer idJenisPlaystation;

    public PlayStation() {
    }

    public PlayStation(Integer idPS, Integer idJenisPS, String JenisPlayStation, String serialNumber, String merkPs, Double hargaPS, String status, String createdby) {
        this.idPS = idPS;
        this.idJenisPlaystation = idJenisPS;  // Menyimpan ID jenis PS
        this.jenisPlaystation = jenisPlaystation;
        this.serialNumber = serialNumber;
        this.merkPs = merkPs;
        this.hargaPS = hargaPS;
        this.status = status;
        this.createdby = createdby;
    }
    public PlayStation(Integer idPS, JenisPlayStation jenisPlaystation, String serialNumber, String merkPs, Double hargaPS, String status, String createdby) {
        this.idPS = idPS;
        this.jenisPlaystation = jenisPlaystation;
        this.serialNumber = serialNumber;
        this.merkPs = merkPs;
        this.hargaPS = hargaPS;
        this.status = status;
        this.createdby = createdby;
    }

    public PlayStation(String modifby, String status, Double hargaPS, String merkPs, String serialNumber,  JenisPlayStation jenisPlaystation, Integer idPS) {
        this.modifby = modifby;
        this.status = status;
        this.hargaPS = hargaPS;
        this.merkPs = merkPs;
        this.serialNumber = serialNumber;
        this.jenisPlaystation = jenisPlaystation;
        this.idPS = idPS;
    }

    public PlayStation(Integer IdJenisPS,JenisPlayStation jenisPlaystation, String serialNumber, String merkPs, Double hargaPS, String modifby,Integer idPS) {
        this.idPS = idPS;
        this.idJenisPlaystation = IdJenisPS;
        this.jenisPlaystation = jenisPlaystation;
        this.serialNumber = serialNumber;
        this.merkPs = merkPs;
        this.hargaPS = hargaPS;
        this.modifby = modifby;
    }

    public PlayStation(JenisPlayStation jenisPlaystation, String serialNumber, String merkPs, Double hargaPS, String createdby) {
        this.jenisPlaystation = jenisPlaystation;
        this.serialNumber = serialNumber;
        this.merkPs = merkPs;
        this.hargaPS = hargaPS;
        this.createdby = createdby;
    }


    public Integer getIdJenisPlaystation() {
        return idJenisPlaystation;
    }

    public void setIdJenisPlaystation(Integer idJenisPlaystation) {
        this.idJenisPlaystation = idJenisPlaystation;
    }

    public Integer getIdPS() {
        return idPS;
    }

    public void setIdPS(Integer idPS) {
        this.idPS = idPS;
    }

    public JenisPlayStation getJenisPlaystation() {
        return jenisPlaystation;
    }

    public void setString(JenisPlayStation jenisPlaystation) {
        this.jenisPlaystation = jenisPlaystation;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMerkPs() {
        return merkPs;
    }

    public void setMerkPs(String merkPs) {
        this.merkPs = merkPs;
    }

    public Double getHargaPS() {
        return hargaPS;
    }

    public void setHargaPS(Double hargaPS) {
        this.hargaPS = hargaPS;
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