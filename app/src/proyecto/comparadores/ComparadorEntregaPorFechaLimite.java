package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara entregas segun su fecha de entrega para permitir ordenarlas
public class ComparadorEntregaPorFechaLimite implements Comparator<Entrega> {
    private final CompararadorFechaLimite compFecha = new CompararadorFechaLimite();
    @Override
    public int compare(Entrega e1, Entrega e2) {
        if (e1 == e2) return 0;
        if (e1 == null || e1.getFechaEntrega() == null) return -1;
        if (e2 == null || e2.getFechaEntrega() == null) return 1;
        return compFecha.compare(e1.getFechaEntrega(), e2.getFechaEntrega());
    }
}