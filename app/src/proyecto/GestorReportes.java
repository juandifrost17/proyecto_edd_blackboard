package proyecto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GestorReportes {
    // Muestra el menu principal para configurar y generar reportes
    public static void menuReporte(Scanner sc, ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos) {
        ListaCompuesta<String, Object> actSeleccionadas = new ListaCompuesta<>();
        ListaCompuesta<String, Object> calcSeleccionados = new ListaCompuesta<>();
        while (true) {
            System.out.println("\n--- CONFIGURACIÓN DE REPORTE ---");
            System.out.println("Actividades elegidas: " + actSeleccionadas.getSize());
            System.out.println("Cálculos elegidos:    " + calcSeleccionados.getSize());
            System.out.println("1. Seleccionar Actividades");
            System.out.println("2. Seleccionar Cálculos");
            System.out.println("3. Generar Reporte en Pantalla");
            System.out.println("4. Descargar Reporte (TXT / CSV)");
            System.out.println("exit. Salir al Menú Principal");
            System.out.print("Ingrese opción: ");

            String input = sc.nextLine().trim().toLowerCase();
            if (cancelado(input)) return;

            switch (input) {
                case "1":
                    seleccionarMultiples(sc, "Actividad", actividades, null, actSeleccionadas);
                    break;
                case "2":
                    seleccionarMultiples(sc, "Cálculo", null, calculos, calcSeleccionados);
                    break;
                case "3":
                    if (actSeleccionadas.isEmpty() && calcSeleccionados.isEmpty()) {
                        System.out.println("\n[!] Error: Debe seleccionar al menos 1 Actividad o 1 Cálculo.");
                    } else {
                        imprimirTablaReporte(estudiantes, actividades, calculos, actSeleccionadas, calcSeleccionados);
                    }
                    break;
                case "4":
                    if (actSeleccionadas.isEmpty() && calcSeleccionados.isEmpty()) {
                        System.out.println("\n[!] Error: No hay nada seleccionado para exportar.");
                        break;
                    }
                    System.out.println("\n¿En qué formato desea descargar el reporte?");
                    System.out.println("1. Archivo de Texto (.txt)");
                    System.out.println("2. Archivo CSV (.csv)");
                    System.out.print("Elige una opción (1 o 2): ");
                    String opcionFormato = sc.nextLine().trim();

                    if (opcionFormato.equals("1")) {
                        exportarReporteTxt(estudiantes, actividades, calculos, actSeleccionadas, calcSeleccionados, "reporte_calificaciones.txt");
                    } else if (opcionFormato.equals("2")) {
                        exportarReporteCsv(estudiantes, actividades, calculos, actSeleccionadas, calcSeleccionados, "reporte_calificaciones.csv");
                    } else {
                        System.out.println("[!] Opción inválida. Exportación cancelada.");
                    }
                    break;
                default:
                    System.out.println("[!] Opción inválida.");
            }
        }
    }

    // Permite seleccionar multiples actividades o calculos y guardarlos en la lista seleccionada
    private static void seleccionarMultiples(Scanner sc, String tipo, ListaCompuesta<Actividad, Entrega> listaAct, ListaCompuesta<Calculo, Object> listaCalc, ListaCompuesta<String, Object> seleccionados) {
        int totalElementos = 0;
        String titulo = "";

        if (tipo != null && tipo.equals("Actividad")) {
            totalElementos = listaAct.getSize();
            titulo = "ACTIVIDADES";
        } else {
            totalElementos = listaCalc.getSize();
            titulo = "CÁLCULOS";
        }
        System.out.println("\n--- SELECCIÓN DE " + titulo + " ---\n");

        int i = 1;
        if (tipo != null && tipo.equals("Actividad")) {
            NodoCompuesto<Actividad, Entrega> actual = listaAct.getHeader();
            while (actual != null) {
                System.out.println(i + ") " + actual.getData().getId() + " - " + actual.getData().getNombre());
                actual = actual.getNext();
                i++;
            }
        } else {
            NodoCompuesto<Calculo, Object> actual = listaCalc.getHeader();
            while (actual != null) {
                System.out.println(i + ") " + actual.getData().getId() + " - " + actual.getData().getNombre());
                actual = actual.getNext();
                i++;
            }
        }

        System.out.println();
        System.out.print("Ingrese los números separados por comas (exit para cancelar): ");
        String input = sc.nextLine().trim();
        if (cancelado(input)) {
            return;
        }

        String[] partes = input.split(",");
        int agregados = 0;

        for (String p : partes) {
            try {
                int indice = Integer.parseInt(p.trim());

                if (indice >= 1 && indice <= totalElementos) {
                    String idReal = obtenerIdPorIndice(tipo, listaAct, listaCalc, indice);

                    if (!contieneId(seleccionados, idReal)) {
                        seleccionados.anadir(new NodoCompuesto<>(idReal));
                        agregados++;
                    }
                } else {
                    System.out.println(" [!] Advertencia: El número " + indice + " está fuera de rango.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" [!] Advertencia: '" + p.trim() + "' no es un número válido.");
            }
        }
        System.out.println("[+] Se agregaron " + agregados + " elementos a su selección.");
    }

    // Imprime en pantalla una tabla con estudiantes, actividades y calculos seleccionados
    private static void imprimirTablaReporte(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos, ListaCompuesta<String, Object> actSeleccionadas, ListaCompuesta<String, Object> calcSeleccionados) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("REPORTE GENERADO");
        System.out.println("=".repeat(100));
        System.out.printf("%-20s", "Estudiante");
        NodoCompuesto<String, Object> nodoActStr = actSeleccionadas.getHeader();
        while (nodoActStr != null) {
            String nombreAct = obtenerNombreActividad(actividades, nodoActStr.getData());
            String nombreCorto = nombreAct.length() > 8 ? nombreAct.substring(0, 8) : nombreAct;
            System.out.printf("| %-8s", nombreCorto);
            nodoActStr = nodoActStr.getNext();
        }
        NodoCompuesto<String, Object> nodoCalcStr = calcSeleccionados.getHeader();
        while (nodoCalcStr != null) {
            String nombreCalc = obtenerNombreCalculo(calculos, nodoCalcStr.getData());
            String nombreCorto = nombreCalc.length() > 12 ? nombreCalc.substring(0, 12) : nombreCalc;
            System.out.printf("| %-12s", nombreCorto);
            nodoCalcStr = nodoCalcStr.getNext();
        }
        System.out.println(" |");
        System.out.println("-".repeat(100));
        NodoCompuesto<Estudiante, Entrega> nodoEst = estudiantes.getHeader();
        while (nodoEst != null) {
            Estudiante est = nodoEst.getData();
            String nombreEst = est.getNombre() + " " + est.getApellido();
            System.out.printf("%-20s", nombreEst.length() > 18 ? nombreEst.substring(0, 18) : nombreEst);

            nodoActStr = actSeleccionadas.getHeader();
            while (nodoActStr != null) {
                double nota = buscarNotaDirecta(nodoEst.getSecundaria(), nodoActStr.getData());
                System.out.printf("| %-8.1f", nota == -1 ? 0.0 : nota);
                nodoActStr = nodoActStr.getNext();
            }
            nodoCalcStr = calcSeleccionados.getHeader();
            while (nodoCalcStr != null) {
                Calculo c = obtenerObjetoCalculo(calculos, nodoCalcStr.getData());
                if (c != null) {
                    double resultado = EvaluadorCalculos.evaluarFormula(c.getFormulaPosfija(), nodoEst.getSecundaria());
                    System.out.printf("| %-12.2f", resultado);
                }
                nodoCalcStr = nodoCalcStr.getNext();
            }
            System.out.println(" |");
            nodoEst = nodoEst.getNext();
        }
        System.out.println("=".repeat(100));
    }

    // Exporta el reporte generado en formato de archivo de texto
    public static void exportarReporteTxt(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos, ListaCompuesta<String, Object> actSeleccionadas, ListaCompuesta<String, Object> calcSeleccionados, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            bw.write("=== REPORTE DE CALIFICACIONES ===\n");
            bw.write("--------------------------------------------------\n");
            NodoCompuesto<Estudiante, Entrega> nodoEst = estudiantes.getHeader();
            while (nodoEst != null) {
                Estudiante est = nodoEst.getData();
                bw.write("Estudiante: " + est.getNombre() + " " + est.getApellido() + " (ID: " + est.getId() + ")\n");
                if (!actSeleccionadas.isEmpty()) {
                    bw.write("  >> Actividades Evaluadas:\n");
                    NodoCompuesto<String, Object> nodoActStr = actSeleccionadas.getHeader();
                    while (nodoActStr != null) {
                        String idAct = nodoActStr.getData();
                        String nombreAct = obtenerNombreActividad(actividades, idAct);
                        double nota = buscarNotaDirecta(nodoEst.getSecundaria(), idAct);
                        String notaStr = nota == -1 ? "0.00 (Sin entrega)" : String.format("%.2f", nota);
                        bw.write(String.format("    - %-20s : %s\n", nombreAct, notaStr));
                        nodoActStr = nodoActStr.getNext();
                    }
                }
                if (!calcSeleccionados.isEmpty()) {
                    bw.write("  >> Cálculos Evaluados:\n");
                    NodoCompuesto<String, Object> nodoCalcStr = calcSeleccionados.getHeader();
                    while (nodoCalcStr != null) {
                        Calculo calculo = obtenerObjetoCalculo(calculos, nodoCalcStr.getData());
                        if (calculo != null) {
                            double nota = EvaluadorCalculos.evaluarFormula(calculo.getFormulaPosfija(), nodoEst.getSecundaria());
                            if (calculo.getId().equals("C8") && nota > 100.0) {
                                bw.write(String.format("    - %-20s : ¡REPROBADO! (Requiere %.2f)\n", calculo.getNombre(), nota));
                            } else if (calculo.getId().equals("C8") && nota <= 0.0) {
                                bw.write(String.format("    - %-20s : ¡APROBADO! (No necesita)\n", calculo.getNombre()));
                            } else {
                                bw.write(String.format("    - %-20s : %.2f\n", calculo.getNombre(), nota));
                            }
                        }
                        nodoCalcStr = nodoCalcStr.getNext();
                    }
                }
                bw.write("--------------------------------------------------\n");
                nodoEst = nodoEst.getNext();
            }
            System.out.println("¡Reporte TXT exportado exitosamente en: " + rutaArchivo + "!");
        } catch (IOException e) {
            System.out.println("Error al exportar el reporte TXT: " + e.getMessage());
        }
    }

    // Exporta el reporte generado en formato CSV
    public static void exportarReporteCsv(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos, ListaCompuesta<String, Object> actSeleccionadas, ListaCompuesta<String, Object> calcSeleccionados, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            StringBuilder header = new StringBuilder("Estudiante,ID_Estudiante");
            NodoCompuesto<String, Object> nodoActStr = actSeleccionadas.getHeader();
            while (nodoActStr != null) {
                header.append(",\"Act: ").append(obtenerNombreActividad(actividades, nodoActStr.getData())).append("\"");
                nodoActStr = nodoActStr.getNext();
            }
            NodoCompuesto<String, Object> nodoCalcStr = calcSeleccionados.getHeader();
            while (nodoCalcStr != null) {
                header.append(",\"Calc: ").append(obtenerNombreCalculo(calculos, nodoCalcStr.getData())).append("\"");
                nodoCalcStr = nodoCalcStr.getNext();
            }
            header.append("\n");
            bw.write(header.toString());
            NodoCompuesto<Estudiante, Entrega> nodoEst = estudiantes.getHeader();
            while (nodoEst != null) {
                Estudiante est = nodoEst.getData();
                StringBuilder row = new StringBuilder(String.format("\"%s %s\",\"%s\"", est.getNombre(), est.getApellido(), est.getId()));
                nodoActStr = actSeleccionadas.getHeader();
                while (nodoActStr != null) {
                    double nota = buscarNotaDirecta(nodoEst.getSecundaria(), nodoActStr.getData());
                    row.append(String.format(",\"%.1f\"", nota == -1 ? 0.0 : nota));
                    nodoActStr = nodoActStr.getNext();
                }
                nodoCalcStr = calcSeleccionados.getHeader();
                while (nodoCalcStr != null) {
                    Calculo calculo = obtenerObjetoCalculo(calculos, nodoCalcStr.getData());
                    if (calculo != null) {
                        double nota = EvaluadorCalculos.evaluarFormula(calculo.getFormulaPosfija(), nodoEst.getSecundaria());
                        if (calculo.getId().equals("C8") && nota > 100.0) {
                            row.append(String.format(",\"REPROBADO (%.2f)\"", nota));
                        } else if (calculo.getId().equals("C8") && nota <= 0.0) {
                            row.append(",\"APROBADO\"");
                        } else {
                            row.append(String.format(",\"%.2f\"", nota));
                        }
                    }
                    nodoCalcStr = nodoCalcStr.getNext();
                }
                row.append("\n");
                bw.write(row.toString());
                nodoEst = nodoEst.getNext();
            }
            System.out.println("¡Reporte CSV exportado exitosamente en: " + rutaArchivo + "!");
        } catch (IOException e) {
            System.out.println("Error al exportar el reporte CSV: " + e.getMessage());
        }
    }

    // Verifica si el usuario ingreso un comando de cancelacion
    private static boolean cancelado(String input) {
        return input.equalsIgnoreCase("s") || input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("salir");
    }

    // Obtiene el id real segun el indice seleccionado en la lista mostrada
    private static String obtenerIdPorIndice(String tipo, ListaCompuesta<Actividad, Entrega> listaAct, ListaCompuesta<Calculo, Object> listaCalc, int indiceBuscado) {
        int i = 1;
        if (tipo.equals("Actividad")) {
            NodoCompuesto<Actividad, Entrega> actual = listaAct.getHeader();
            while (actual != null) {
                if (i == indiceBuscado) return actual.getData().getId();
                i++;
                actual = actual.getNext();
            }
        } else {
            NodoCompuesto<Calculo, Object> actual = listaCalc.getHeader();
            while (actual != null) {
                if (i == indiceBuscado) return actual.getData().getId();
                i++;
                actual = actual.getNext();
            }
        }
        return null;
    }

    // Verifica si un id ya existe dentro de la lista de seleccionados
    private static boolean contieneId(ListaCompuesta<String, Object> lista, String id) {
        NodoCompuesto<String, Object> actual = lista.getHeader();
        while (actual != null) {
            if (actual.getData().equals(id)) return true;
            actual = actual.getNext();
        }
        return false;
    }

    // Busca la nota directa de una actividad especifica en la lista de entregas
    private static double buscarNotaDirecta(ListaCompuesta<Entrega, Object> entregas, String idActividad) {
        if (entregas == null) return -1;
        NodoCompuesto<Entrega, Object> actual = entregas.getHeader();
        while (actual != null) {
            if (actual.getData().getActividad().getId().equalsIgnoreCase(idActividad)) {
                return actual.getData().isCalificada() ? actual.getData().getCalificacion() : -1;
            }
            actual = actual.getNext();
        }
        return -1;
    }

    // Obtiene el nombre de una actividad segun su id
    private static String obtenerNombreActividad(ListaCompuesta<Actividad, Entrega> actividades, String id) {
        NodoCompuesto<Actividad, Entrega> actual = actividades.getHeader();
        while (actual != null) {
            if (actual.getData().getId().equals(id)) return actual.getData().getNombre();
            actual = actual.getNext();
        }
        return id;
    }

    // Obtiene el nombre de un calculo segun su id
    private static String obtenerNombreCalculo(ListaCompuesta<Calculo, Object> calculos, String id) {
        Calculo c = obtenerObjetoCalculo(calculos, id);
        return c != null ? c.getNombre() : id;
    }

    // Obtiene el objeto calculo completo segun su id
    private static Calculo obtenerObjetoCalculo(ListaCompuesta<Calculo, Object> calculos, String id) {
        NodoCompuesto<Calculo, Object> actual = calculos.getHeader();
        while (actual != null) {
            if (actual.getData().getId().equals(id)) return actual.getData();
            actual = actual.getNext();
        }
        return null;
    }
}