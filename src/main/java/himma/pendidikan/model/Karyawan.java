package himma.pendidikan.model;

public class Karyawan {
    private Integer id;
    private String nama;
    private String posisi;
    private String alamat;
    private String noTelepon;
    private String email;
    private String username;
    private String password;
    private String status;
    private String createdby;
    private String modifby;

    public Karyawan() {}

    public Karyawan(Integer id, String nama, String posisi, String username, String status) {
        this.id = id;
        this.nama = nama;
        this.posisi = posisi;
        this.username = username;
        this.status = status;
    }

    public Karyawan(String nama, String posisi, String alamat, String noTelepon, String email, String username, String password, String createdby) {
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdby = createdby;
    }

    public Karyawan(Integer id, String nama, String posisi, String alamat, String noTelepon, String email, String username, String password, String status) {
        this.id = id;
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public Karyawan(String nama, String posisi, String alamat, String noTelepon, String email, String username, String password, String modifby, Integer id) {
        this.id = id;
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.username = username;
        this.password = password;
        this.modifby = modifby;
    }

    public Karyawan(String nama, String posisi, String alamat, String noTelepon, String email, String username, String status) {
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.username = username;
        this.status = status;
    }

    public Karyawan(Integer id, String nama, String posisi, String alamat, String noTelepon, String email, String username, String password, String status, String createdby, String modifby) {
        this.id = id;
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.username = username;
        this.password = password;
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

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

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
