package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara entregas para identificar si su contenido esta incompleto
public class ComparadorEntregaPorContenido implements Comparator<Entrega> {
    @Override
    public int compare(Entrega e1, Entrega e2) {
        String c1 = e1.getContenido();
        boolean e1Incompleta = (c1 == null || c1.isEmpty());

        String c2 = e2.getContenido();
        boolean e2Incompleta = (c2 == null || c2.isEmpty());

        if (e1Incompleta == e2Incompleta) return 0;
        if (e1Incompleta) return -1;
        return 1;
    }
}
