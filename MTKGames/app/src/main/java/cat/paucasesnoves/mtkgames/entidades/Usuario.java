package cat.paucasesnoves.mtkgames.entidades;

public class Usuario {

    private int id;
    private String nombre;
    private String nickName;
    private String email;
    private String password;
    private String fechaNacimiento;
    private int fotoPerfil;
    private String ultimaConexion;
    private int saldo;
    private int login;
    private int idBiblioteca;

    public Usuario(String nickName, String password) {
        this.nickName = nickName;
        this.password = password;
    }

    public Usuario(int id, String nombre, String nickName, String email, String password, String fechaNacimiento,
                   int fotoPerfil, String ultimaConexion, int saldo, int login, int idBiblioteca) {
        this.id = id;
        this.nombre = nombre;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.ultimaConexion = ultimaConexion;
        this.saldo = saldo;
        this.login = login;
        this.idBiblioteca = idBiblioteca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(String ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getIdBiblioteca() {
        return idBiblioteca;
    }

    public void setIdBiblioteca(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", fotoPerfil=" + fotoPerfil +
                ", ultimaConexion='" + ultimaConexion + '\'' +
                ", saldo=" + saldo +
                ", login=" + login +
                ", idBiblioteca=" + idBiblioteca +
                '}';
    }
}
