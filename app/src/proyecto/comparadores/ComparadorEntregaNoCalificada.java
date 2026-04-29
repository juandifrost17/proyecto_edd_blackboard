package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara entregas para identificar aquellas que no estan calificadas
public class ComparadorEntregaNoCalificada implements Comparator<Entrega> {
    // Retorna 0 si la entrega no esta calificada y 1 en caso contrario
    @Override
    public int compare(Entrega e1, Entrega ref) {
        if (e1 == null) return 1;
        if (!e1.isCalificada()) return 0;
        return 1;
    }
}
