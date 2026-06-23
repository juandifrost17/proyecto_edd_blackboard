package proyecto;
import proyecto.comparadores.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServicioConsultas {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private final ListaCompuesta<Estudiante, Entrega> estudiantes;
    private final ListaCompuesta<Actividad, Entrega> actividades;
    private final ListaCompuesta<Calculo, Object> calculos;

    // Constructor
    public ServicioConsultas(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos) {
        this.estudiantes = estudiantes;
        this.actividades = actividades;
        this.calculos = calculos;
    }

    // Consulta 1: lista actividades cuya fecha limite ya vencio
    public String c1_ActividadesExpiradas() {
        StringBuilder out = new StringBuilder();
        Date fechaReferencia = fechaActualSinHora();
        out.append("\n--- Actividades Expiradas al ").append(SDF.format(fechaReferencia)).append(" ---\n");
        Actividad referenciaFecha = new Actividad(fechaReferencia);
        ListaCompuesta<Actividad, Entrega> res = actividades.filtrarPorCriterioPrincipal(new ComparadorActividadPorFechaLimite(), referenciaFecha, -1);
        NodoCompuesto<Actividad, Entrega> b = res.getHeader();
        if (b == null) {
            out.append("No hay actividades vencidas.\n");
            return out.toString();
        }
        while (b != null) {
            out.append(b.getData().getNombre()).append(" - ").append(SDF.format(b.getData().getFechaLimite())).append("\n");
            b = b.getNext();
        }
        return out.toString();
    }

    // Consulta 2: actividades con entregas incompletas segun umbral de caracteres
    public String c2_ActividadesIncompletas(int umbralContenido) {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Actividades con entregas incompletas (< ").append(umbralContenido).append(" caracteres) ---\n");
        Entrega dummy = new Entrega(null, null, repetir(';', umbralContenido), 0.0, null, true);
        ListaCompuesta<Actividad, Entrega> res = actividades.filtrarPorCriterioSecundario(new ComparadorEntregaPorLongitudContenido(), dummy, -1);
        if (res == null || res.isEmpty()) {
            out.append("¡Excelente! No se encontraron entregas incompletas.\n");
            return out.toString();}
        NodoCompuesto<Actividad, Entrega> nodoAct = res.getHeader();
        while (nodoAct != null) {
            Actividad act = nodoAct.getData();
            out.append("\n* ").append(act.getId()).append(" - ").append(act.getNombre()).append("\n");
            NodoCompuesto<Entrega, Object> nodoEnt = nodoAct.getSecundaria().getHeader();
            while (nodoEnt != null) {
                Entrega ent = nodoEnt.getData();
                String contenido = "";
                if (ent.getContenido() != null) {contenido = ent.getContenido();}
                if (contenido.length() < umbralContenido) {
                    String estudianteInfo = "Estudiante Desconocido";
                    if (ent.getEstudiante() != null) {
                        estudianteInfo = ent.getEstudiante().getNombre() + " " + ent.getEstudiante().getApellido() + " (ID: " + ent.getEstudiante().getId() + ")";
                    }

                    out.append(String.format("   [!] %s\n       Contenido: \"%s\" (Longitud: %d)\n",
                            estudianteInfo, contenido, contenido.length()));
                }
                nodoEnt = nodoEnt.getNext();
            }
            nodoAct = nodoAct.getNext();
        }

        return out.toString();
    }

    // Consulta 3: actividades con al menos una entrega con nota menor a un umbral
    public String c3_CalificacionesMenoresA(double umbralNota) {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Actividades con al menos una nota menor a ").append(umbralNota).append(" ---\n");
        Entrega entregaUmbral = new Entrega(umbralNota);
        ListaCompuesta<Actividad, Entrega> res = actividades.filtrarPorCriterioSecundario(new ComparadorEntregaPorCalificacion(), entregaUmbral, -1);
        NodoCompuesto<Actividad, Entrega> b = res.getHeader();
        if (b == null) {
            out.append("No se encontraron actividades con notas menores a ese umbral.\n");
            return out.toString();
        }
        while (b != null) {
            out.append("- ").append(b.getData().getNombre()).append("\n");
            b = b.getNext();
        }
        return out.toString();
    }

    // Consulta 4: entregas luego de cierta fecha y no calificadas
    public String c4_EntregasTardiasSinCalificar(Date fechaLimite) {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Entregas despues de ").append(fechaLimite).append(" y no calificadas ---\n");
        Entrega refFecha = new Entrega(null, null, "", 0.0, fechaLimite, true);
        ListaCompuesta<Entrega, Object> entregasDespues = actividades.obtenerSecundariaFiltrada(new ComparadorEntregaPorFechaLimite(), refFecha, 1);
        Entrega dummyCalificada = new Entrega(null, null, "REF", 0.0, null, true);
        ListaCompuesta<Entrega, Object> entregasNoCalificadas = actividades.obtenerSecundariaFiltrada(new ComparadorEntregaNoCalificada(), dummyCalificada, 0);
        ListaCompuesta<Entrega, Object> resFinal = entregasDespues.interseccionSinRepetidos(entregasNoCalificadas);
        out.append(resFinal.toString());
        return out.toString();
    }

    // Consulta 5: estudiantes con porcentaje de entregas mayor a un valor
    public String c5_EstudiantesPorcentajeEntregas(double porcentaje) {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Estudiantes con mas del ").append(porcentaje).append("% de entregas ---\n");
        ListaCompuesta<Estudiante, Entrega> res = estudiantes.filtrarPorcentajeSecundariosMayor(porcentaje, actividades.getSize());
        out.append(res.toString());
        return out.toString();
    }

    // Consulta 6: estudiantes que no han respondido al menos una actividad vencida
    public String c6_EstudiantesFaltanExpiradas() {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Estudiantes que NO han respondido al menos una actividad vencida ---\n");
        Actividad referenciaFecha = new Actividad(fechaActualSinHora());
        ListaCompuesta<Actividad, Entrega> actividadesVencidas = actividades.filtrarPorCriterioPrincipal(new ComparadorActividadPorFechaLimite(), referenciaFecha, -1);
        ListaCompuesta<Entrega, Object> refsVencidas = new ListaCompuesta<>();
        for (NodoCompuesto<Actividad, Entrega> a = actividadesVencidas.getHeader(); a != null; a = a.getNext()) {
            Actividad act = a.getData();
            Entrega ref = new Entrega(null, act, "REF", 0.0, null, true);
            refsVencidas.anadir(new NodoCompuesto<>(ref));
        }
        ListaCompuesta<Estudiante, Entrega> res = estudiantes.filtrarPrincipalesConFaltantesEnReferencias(refsVencidas, new ComparadorEntregaPorActividadId());
        if (res == null || res.isEmpty()) {
            out.append("Excelente, ningun estudiante tiene actividades expiradas pendientes.\n");
            return out.toString();
        }
        NodoCompuesto<Estudiante, Entrega> nodoEst = res.getHeader();
        while (nodoEst != null) {
            Estudiante est = nodoEst.getData();
            out.append("\nEstudiante: ").append(est.getNombre()).append(" ").append(est.getApellido()).append(" (ID: ").append(est.getId()).append(")\n");
            NodoCompuesto<Actividad, Entrega> nodoActVencida = actividadesVencidas.getHeader();
            while (nodoActVencida != null) {
                Actividad actVencida = nodoActVencida.getData();
                boolean laEntrego = false;
                NodoCompuesto<Entrega, Object> nodoEntrega = nodoEst.getSecundaria().getHeader();
                while (nodoEntrega != null) {
                    Entrega e = nodoEntrega.getData();
                    if (e.getActividad().getId().equals(actVencida.getId())) {
                        if (e.getContenido() != null && !e.getContenido().isEmpty()) laEntrego = true;
                        break;
                    }
                    nodoEntrega = nodoEntrega.getNext();
                }
                if (!laEntrego) {
                    out.append("  [!] Falto entregar: ").append(actVencida.getNombre()).append(" (Vencio el: ").append(SDF.format(actVencida.getFechaLimite())).append(")\n");
                }
                nodoActVencida = nodoActVencida.getNext();
            }
            nodoEst = nodoEst.getNext();
        }

        return out.toString();
    }

    // Consulta 7: estudiantes con la misma nota en dos actividades diferentes
    public String c7_EstudiantesMismaNota() {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Estudiantes con la misma nota en dos actividades diferentes ---\n");
        ListaCompuesta<Estudiante, Entrega> res = estudiantes.filtrarPrincipalesPorCoincidenciaDoble(new ComparadorEntregaPorCalificacion(), new ComparadorEntregaPorActividad());
        out.append(res.toString());
        return out.toString();
    }

    // Consulta 8: calculos no ejecutables (faltan calificaciones)
    public String c8_CalculosNoEjecutables() {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Calculos que NO se pueden ejecutar ---\n");
        NodoCompuesto<Calculo, Object> nCalc = calculos.getHeader();
        if (nCalc == null) {
            out.append("No hay calculos cargados.\n");
            return out.toString();
        }
        boolean alguno = false;
        while (nCalc != null) {
            Calculo c = nCalc.getData();
            boolean ejecutableParaTodos = true;
            NodoCompuesto<Estudiante, Entrega> ne = estudiantes.getHeader();
            while (ne != null) {
                if (!EvaluadorCalculos.calculoEjecutable(c.getFormulaPosfija(), ne.getSecundaria())) {
                    ejecutableParaTodos = false;
                    break;
                }
                ne = ne.getNext();
            }
            if (!ejecutableParaTodos) {
                alguno = true;
                out.append("- ").append(c.getId()).append(" | ").append(c.getNombre()).append(" | ").append(c.getFormulaPosfija()).append("\n");
            }

            nCalc = nCalc.getNext();
        }
        if (!alguno) out.append("Todos los calculos son ejecutables para todos los estudiantes.\n");
        return out.toString();
    }

    // Consulta 9: calculos que involucren una actividad dada por id
    public String c9_CalculosPorActividad(String idAct) {
        StringBuilder out = new StringBuilder();
        out.append("\n--- Calculos que involucran la actividad: ").append(idAct).append(" ---\n");
        String tipoActividad = null;
        NodoCompuesto<Actividad, Entrega> na = actividades.getHeader();
        while (na != null) {
            if (na.getData().getId().equalsIgnoreCase(idAct)) {
                tipoActividad = na.getData().getTipo();
                break;
            }
            na = na.getNext();
        }

        NodoCompuesto<Calculo, Object> nCalc = calculos.getHeader();
        boolean encontro = false;
        while (nCalc != null) {
            Calculo c = nCalc.getData();
            if (EvaluadorCalculos.calculoInvolucraActividad(c.getFormulaPosfija(), idAct, tipoActividad)) {
                encontro = true;
                out.append("- ").append(c.getId()).append(" | ").append(c.getNombre()).append("\n");
                out.append("  Formula: ").append(c.getFormulaPosfija()).append("\n");
            }
            nCalc = nCalc.getNext();
        }
        if (!encontro) out.append("No se encontraron calculos para esa actividad.\n");
        return out.toString();
    }

    // Consulta 10: ordenar estudiantes por un calculo definido
    public String c10_OrdenarEstudiantesPorCalculo(String idCalculo, boolean desc) {
        StringBuilder out = new StringBuilder();

        Calculo calculoElegido = buscarCalculoPorId(idCalculo);

        if (calculoElegido == null) {
            out.append("Calculo no encontrado: ").append(idCalculo).append("\n");
            return out.toString();
        }
        estudiantes.ordenar(new ComparadorEstudiantePorCalculo(calculoElegido, desc));
        out.append("\nOrdenados por: ").append(calculoElegido.getNombre());

        if (desc) {
            out.append(" (DESC)\n");
        } else {
            out.append(" (ASC)\n");
        }
        out.append("--------------------------------------------------\n");
        NodoCompuesto<Estudiante, Entrega> ne = estudiantes.getHeader();

        while (ne != null) {
            Estudiante est = ne.getData();
            double val = EvaluadorCalculos.evaluarFormula(calculoElegido.getFormulaPosfija(), ne.getSecundaria());

            out.append(est.getNombre()).append(" ").append(est.getApellido()).append(" (ID: ").append(est.getId()).append(") -> ").append(String.format("%.2f", val)).append("\n");
            ne = ne.getNext();
        }
        return out.toString();
    }

    // Devuelve el id del calculo ubicado en el indice indicado (1..n)
    public String obtenerIdCalculoPorIndice(int indice) {
        if (indice <= 0) {
            return null;
        }
        int i = 1;
        NodoCompuesto<Calculo, Object> actual = calculos.getHeader();

        while (actual != null) {
            if (i == indice) {
                return actual.getData().getId();
            }
            actual = actual.getNext();
            i++;
        }
        return null;
    }

    // Busca un calculo por id dentro de la lista
    public Calculo buscarCalculoPorId(String id) {
        NodoCompuesto<Calculo, Object> c = calculos.getHeader();
        while (c != null) {
            if (c.getData().getId().equalsIgnoreCase(id)) return c.getData();
            c = c.getNext();
        }
        return null;
    }

    // Construye un string con un caracter repetido n veces
    private String repetir(char c, int n) {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < n; i++) texto.append(c);
        return texto.toString();
    }

    // Normaliza la fecha actual para comparar solo yyyy-MM-dd, sin hora del sistema.
    private Date fechaActualSinHora() {
        try {
            return SDF.parse(SDF.format(new Date()));
        } catch (java.text.ParseException e) {
            return new Date();
        }
    }
}
