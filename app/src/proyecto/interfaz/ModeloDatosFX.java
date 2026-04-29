package proyecto.interfaz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyecto.*;

public class ModeloDatosFX {
    private final ObservableList<String> actDisponibles = FXCollections.observableArrayList();
    private final ObservableList<String> calcDisponibles = FXCollections.observableArrayList();

    // Retorna la lista observable de actividades disponibles para mostrar en la interfaz.
    public ObservableList<String> getActDisponibles() { return actDisponibles; }

    // Retorna la lista observable de cálculos disponibles para mostrar en la interfaz.
    public ObservableList<String> getCalcDisponibles() { return calcDisponibles; }

    // Carga y formatea actividades y cálculos desde las estructuras del sistema hacia listas observables de JavaFX.
    public void cargarDesdeTDAs(ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos) {
        actDisponibles.clear();
        calcDisponibles.clear();
        NodoCompuesto<Actividad, Entrega> nAct = actividades.getHeader();
        while (nAct != null) {
            Actividad a = nAct.getData();
            if (a != null && a.getId() != null && !a.getId().equalsIgnoreCase("id")) {
                actDisponibles.add(formatoActividad(a));
            }
            nAct = nAct.getNext();
        }
        NodoCompuesto<Calculo, Object> nCalc = calculos.getHeader();
        while (nCalc != null) {
            Calculo c = nCalc.getData();
            if (c != null && c.getId() != null && !c.getId().equalsIgnoreCase("id")) {
                calcDisponibles.add(formatoCalculo(c));
            }
            nCalc = nCalc.getNext();
        }

        FXListUtils.ordenarPorId(actDisponibles);
        FXListUtils.ordenarPorId(calcDisponibles);
    }

    // Agrega una actividad recién creada a la lista observable y la reordena para reflejar cambios en tiempo real.
    public void agregarActividadEnTiempoReal(Actividad a) {
        String item = formatoActividad(a);
        if (!actDisponibles.contains(item)) {
            actDisponibles.add(item);
            FXListUtils.ordenarPorId(actDisponibles);
        }
    }

    // Convierte una actividad a texto con formato "ID - Nombre" para mostrar en listas.
    private String formatoActividad(Actividad a) {
        return a.getId() + " - " + a.getNombre();
    }

    // Convierte un cálculo a texto con formato "ID - Nombre" para mostrar en listas.
    private String formatoCalculo(Calculo c) {
        return c.getId() + " - " + c.getNombre();
    }
}