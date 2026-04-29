package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara entregas segun la longitud de su contenido para permitir ordenarlas
public class ComparadorEntregaPorLongitudContenido implements Comparator<Entrega> {
    @Override
    public int compare(Entrega e1, Entrega e2) {
        String c1 = e1.getContenido();
        String c2 = e2.getContenido();

        int l1;
        int l2;

        if (c1 == null) {
            l1 = 0;
        } else {
            l1 = c1.length();
        }
        if (c2 == null) {
            l2 = 0;
        } else {
            l2 = c2.length();
        }

        if (l1 < l2) return -1;
        if (l1 > l2) return 1;
        return 0;
    }
}
