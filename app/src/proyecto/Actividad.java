package proyecto;
import java.util.Date;

public class Actividad {
    private String id;
    private String nombre;
    private Date fechaLimite;
    private float pesoAcademico;
    private String tipo;

    //Constructor
    public Actividad(String id, String nombre, Date fechaLimite, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.fechaLimite = fechaLimite;
        this.tipo = tipo;
    }

    // Constructor auxiliar (solo id)
    public Actividad(String id) {
        this.id = id;
    }

    // Constructor auxiliar solo (fecha limite))
    public Actividad(Date fechaLimite){
        this.fechaLimite = fechaLimite;
    }

    //Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Date getFechaLimite() { return fechaLimite; }
    public String getTipo() { return tipo; }
    public float getPesoAcademico() { return pesoAcademico; }

    //Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaLimite(Date fechaLimite) { this.fechaLimite = fechaLimite; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setPesoAcademico(float pesoAcademico) { this.pesoAcademico = pesoAcademico; }

    //toString
    @Override
    public String toString() {
        return  "Nombre: " + nombre + "\n" + "Fecha Limite: " + fechaLimite + "\n" + "Tipo de la actividad: " + tipo + "\n";
    }

    //Sobreescritura del metodo equals
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Actividad)) {
            return false;
        }
        Actividad otra = (Actividad) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(otra.id);
    }

    //Sobreescritura del metodo hashCode
    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }
}