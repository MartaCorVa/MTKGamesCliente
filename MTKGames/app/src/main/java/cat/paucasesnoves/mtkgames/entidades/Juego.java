package cat.paucasesnoves.mtkgames.entidades;

public class Juego {

    private int id;
    private String nombre;
    private String descripcion;
    private byte[] imagenJuego;
    private byte[] iconoJuego;
    private String empresaDesarrollo;
    private double nota;
    private int precio;
    private int numeroVentas;
    private int idCategoria;

    public Juego(int id, String nombre, String descripcion, byte[] imagenJuego, byte[] iconoJuego,
                 String empresaDesarrollo, double nota, int precio, int numeroVentas, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenJuego = imagenJuego;
        this.iconoJuego = iconoJuego;
        this.empresaDesarrollo = empresaDesarrollo;
        this.nota = nota;
        this.precio = precio;
        this.numeroVentas = numeroVentas;
        this.idCategoria = idCategoria;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagenJuego() {
        return imagenJuego;
    }

    public void setImagenJuego(byte[] imagenJuego) {
        this.imagenJuego = imagenJuego;
    }

    public byte[] getIconoJuego() {
        return iconoJuego;
    }

    public void setIconoJuego(byte[] iconoJuego) {
        this.iconoJuego = iconoJuego;
    }

    public String getEmpresaDesarrollo() {
        return empresaDesarrollo;
    }

    public void setEmpresaDesarrollo(String empresaDesarrollo) {
        this.empresaDesarrollo = empresaDesarrollo;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getNumeroVentas() {
        return numeroVentas;
    }

    public void setNumeroVentas(int numeroVentas) {
        this.numeroVentas = numeroVentas;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "Juego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", empresaDesarrollo='" + empresaDesarrollo + '\'' +
                ", nota=" + nota +
                ", precio=" + precio +
                ", numeroVentas=" + numeroVentas +
                ", idCategoria=" + idCategoria +
                '}';
    }
}
