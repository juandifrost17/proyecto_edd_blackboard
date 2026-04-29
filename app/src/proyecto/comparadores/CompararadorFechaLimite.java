package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;
import java.util.Date;

// Compara fechas para permitir orden cronologico
public class CompararadorFechaLimite implements Comparator<Date> {
    // Compara dos fechas y retorna -1 si es anterior, 1 si es posterior y 0 si son iguales
    @Override
    public int compare(Date fecha1, Date fecha2) {
        if (fecha1.before(fecha2)) {
            return -1;
        }
        else if (fecha1.after(fecha2)) {
            return 1;
        }
        return 0;
    }
}
