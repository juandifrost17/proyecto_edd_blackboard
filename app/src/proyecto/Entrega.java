package proyecto;
import java.util.Date;

public class Entrega {
    private Estudiante estudiante;
    private Actividad actividad;
    private String contenido;
    private double calificacion;
    private Date fechaEntrega;
    private boolean calificada;

    //Constructor
    public Entrega(Estudiante estudiante, Actividad actividad, String contenido, double calificacion, Date fechaEntrega, boolean calificada) {
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.contenido = contenido;
        this.calificacion = calificacion;
        this.fechaEntrega = fechaEntrega;
        this.calificada = calificada;
    }

    // Constructor auxiliar (solo calificacion)
    public Entrega(double calificacion) {
        this.calificacion = calificacion;
    }

    //Getters
    public Estudiante getEstudiante() { return estudiante; }
    public Actividad getActividad() { return actividad; }
    public String getContenido() { return contenido; }
    public double getCalificacion() { return calificacion; }
    public Date getFechaEntrega() { return fechaEntrega; }
    public boolean isCalificada() { return calificada; }

    //Setters
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }
    public void setCalificada(boolean calificada) { this.calificada = calificada; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    //toString
    @Override
    public String toString() {
        String idEstudiante = "N/A";
        if (estudiante != null && estudiante.getId() != null) {
            idEstudiante = estudiante.getId();
        }
        String nombreActividad = "N/A";
        if (actividad != null && actividad.getNombre() != null) {
            nombreActividad = actividad.getNombre();
        }
        return "ID: " + idEstudiante + "\n" + "Actividad: " + nombreActividad + "\n" + "Nota: " + calificacion + "\n" + "Fecha de la entrega: " + fechaEntrega + "\n" + "Contenido: " + contenido;
    }

    //Sobreescritura del metodo equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrega)) return false;
        Entrega e = (Entrega) o;
        if (estudiante == null || actividad == null || e.estudiante == null || e.actividad == null) return false;
        return estudiante.equals(e.estudiante) && actividad.equals(e.actividad);
    }

    //Sobreescritura del metodo hashCode
    @Override
    public int hashCode() {
        if (estudiante == null || actividad == null) {
            return 0;
        }
        return estudiante.hashCode() + actividad.hashCode();
    }
}