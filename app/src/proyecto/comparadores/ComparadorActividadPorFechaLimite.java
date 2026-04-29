package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara actividades segun su fecha limite para permitir ordenarlas cronologicamente
public class ComparadorActividadPorFechaLimite implements Comparator<Actividad> {
    private final CompararadorFechaLimite compFecha = new CompararadorFechaLimite();
    @Override
    public int compare(Actividad a1, Actividad a2) {
        //Si son la misma referencia, se consideran iguales
        if (a1 == a2) return 0;
        //Si la primera es null o no tiene fecha, se considera menor
        if (a1 == null || a1.getFechaLimite() == null) return -1;
        //Si la segunda es null o no tiene fecha, se considera mayor
        if (a2 == null || a2.getFechaLimite() == null) return 1;
        //Se compara usando el comparador de fechas
        return compFecha.compare(a1.getFechaLimite(), a2.getFechaLimite());
    }
}
