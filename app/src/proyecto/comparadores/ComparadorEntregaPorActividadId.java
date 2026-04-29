package proyecto.comparadores;
import proyecto.Entrega;
import java.util.Comparator;

// Compara entregas para verificar si pertenecen a la misma actividad por id
public class ComparadorEntregaPorActividadId implements Comparator<Entrega> {
    @Override
    public int compare(Entrega e1, Entrega ref) {
        //Validacion de nulos
        if (e1 == null || ref == null) return 1;
        if (e1.getActividad() == null || ref.getActividad() == null) return 1;
        if (e1.getActividad().getId() == null || ref.getActividad().getId() == null) return 1;
        String id1 = e1.getActividad().getId();
        String id2 = ref.getActividad().getId();
        //Si los ID son iguales, cumple el criterio
        if (id1.equals(id2)) return 0;
        return 1;
    }
}
