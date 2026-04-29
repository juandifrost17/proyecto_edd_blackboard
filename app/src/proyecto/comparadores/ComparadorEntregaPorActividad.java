package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara entregas segun el nombre de su actividad para permitir orden alfabetico
public class ComparadorEntregaPorActividad implements Comparator<Entrega> {
    @Override
    public int compare(Entrega a, Entrega b) {
        //Si son la misma referencia, son iguales
        if (a == b) return 0;
        //Control de nulos
        if (a == null) return 1;
        if (b == null) return -1;
        Actividad x = a.getActividad();
        Actividad y = b.getActividad();
        if (x == null) return 1;
        if (y == null) return -1;
        String n1 = x.getNombre();
        String n2 = y.getNombre();
        if (n1 == null) return 1;
        if (n2 == null) return -1;

        return n1.compareToIgnoreCase(n2);
    }
}
