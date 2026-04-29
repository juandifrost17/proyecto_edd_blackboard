package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara entregas segun su calificacion para permitir ordenarlas
public class ComparadorEntregaPorCalificacion implements Comparator<Entrega> {
    @Override
    public int compare(Entrega a, Entrega b) {
        if (a == b) return 0;
        if (a == null) return 1;
        if (b == null) return -1;

        double diff = a.getCalificacion() - b.getCalificacion();
        if (diff < 0) return -1;
        if (diff > 0) return 1;
        return 0;
    }
}
