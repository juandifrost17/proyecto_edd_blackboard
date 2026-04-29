package proyecto;

public class GestorCalculos {
    // Recorre cada calculo y determina que estudiantes no lo pueden ejecutar
    public static void mostrarCalculosNoEjecutables(ListaCompuesta<Calculo, Object> calculos, ListaCompuesta<Estudiante, Entrega> estudiantes) {
        if (calculos == null || calculos.getHeader() == null) { return; }
        NodoCompuesto<Calculo, Object> nodoCalc = calculos.getHeader();
        while (nodoCalc != null) {
            Calculo calculo = nodoCalc.getData();
            ListaCompuesta<Estudiante, Object> estudiantesBloqueados = new ListaCompuesta<>();
            NodoCompuesto<Estudiante, Entrega> nodoEst = estudiantes.getHeader();
            while (nodoEst != null) {
                boolean ejecutable = EvaluadorCalculos.calculoEjecutable(calculo.getFormulaPosfija(), nodoEst.getSecundaria());
                if (!ejecutable) {
                    estudiantesBloqueados.anadir(new NodoCompuesto<>(nodoEst.getData()));
                }
                nodoEst = nodoEst.getNext();
            }
            if (estudiantesBloqueados.isEmpty()) {
                System.out.println("  " + calculo.getNombre() + ": EJECUTABLE para todos");
            } else {
                System.out.println("   " + calculo.getNombre() + ": NO EJECUTABLE para " + estudiantesBloqueados.getSize() + " estudiante(s)");
                NodoCompuesto<Estudiante, Object> nodoBloq = estudiantesBloqueados.getHeader();
                while (nodoBloq != null) {
                    Estudiante est = nodoBloq.getData();
                    System.out.println("     • " + est.getNombre() + " " + est.getApellido());
                    nodoBloq = nodoBloq.getNext();
                }
            }
            nodoCalc = nodoCalc.getNext();
        }
    }

    // Muestra que calculos usan una actividad especifica (por id o por tipo)
    public static void mostrarCalculosPorActividad(ListaCompuesta<Calculo, Object> calculos, ListaCompuesta<Actividad, Entrega> actividades, String idActividad) {
        if (calculos == null || calculos.getHeader() == null) {
            System.out.println("No hay cálculos.");
            return;
        }
        String tipoActividad = null;
        NodoCompuesto<Actividad, Entrega> nodoAct = actividades.getHeader();
        while (nodoAct != null) {
            if (nodoAct.getData().getId().equalsIgnoreCase(idActividad)) {
                tipoActividad = nodoAct.getData().getTipo();
                break;
            }
            nodoAct = nodoAct.getNext();
        }
        if (tipoActividad == null) {
            System.out.println("  Advertencia: La actividad " + idActividad + " no existe en el catálogo.");
        }
        boolean encontro = false;
        NodoCompuesto<Calculo, Object> nodoCalc = calculos.getHeader();
        while (nodoCalc != null) {
            Calculo calculo = nodoCalc.getData();
            if (EvaluadorCalculos.calculoInvolucraActividad(calculo.getFormulaPosfija(), idActividad, tipoActividad)) {
                System.out.print("  • " + calculo.getNombre() + " (");
                if (calculo.getFormulaPosfija().contains(idActividad)) {
                    System.out.print("ID Directo");
                } else {
                    System.out.print("Categoría " + tipoActividad);
                }
                System.out.println(")");
                encontro = true;
            }
            nodoCalc = nodoCalc.getNext();
        }
        if (!encontro) {
            System.out.println("  No se encontraron cálculos que involucren: " + idActividad);
        }
    }
}
