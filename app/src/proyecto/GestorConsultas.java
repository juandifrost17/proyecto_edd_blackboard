package proyecto;
import proyecto.comparadores.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GestorConsultas {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    // Muestra el menu de consultas y ejecuta la opcion seleccionada
    public static void menuConsultas(Scanner sc, ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos) {
        ServicioConsultas servicio = new ServicioConsultas(estudiantes, actividades, calculos);
        while (true) {
            System.out.println("\n--- MÓDULO DE CONSULTAS ---");
            System.out.println(">> ACTIVIDADES:");
            System.out.println("  1. Actividades cuya fecha de entrega límite ya feneció");
            System.out.println("  2. Actividades en las cuales las entregas estén incompletas (por caracteres)");
            System.out.println("  3. Actividades con calificaciones menores a un valor dado");
            System.out.println(">> ENTREGAS:");
            System.out.println("  4. Entregas enviadas luego de cierta fecha y aún no calificadas");
            System.out.println(">> ESTUDIANTES:");
            System.out.println("  5. Estudiantes con porcentaje de entregas mayor a un % dado");
            System.out.println("  6. Estudiantes que no han respondido actividades enviadas y ya expiradas");
            System.out.println("  7. Estudiantes que tienen la misma nota en dos actividades diferentes");
            System.out.println(">> CÁLCULOS:");
            System.out.println("  8. Cálculos que no se pueden ejecutar por falta de calificaciones");
            System.out.println("  9. Cálculos que involucren una actividad dada");
            System.out.println("  10. Ordenar estudiantes por un cálculo definido");
            System.out.println("exit. Salir al Menú Principal");
            System.out.print("Ingrese opción: ");
            String input = sc.nextLine().trim().toLowerCase();
            if (cancelado(input)) return;
            switch (input) {
                case "1":
                    System.out.println(servicio.c1_ActividadesExpiradas());
                    break;
                case "2":
                    int umbralChar = (int) pedirDoubleRango(sc,
                            "Mínimo de caracteres para que la entrega se considere completa: ",
                            1, 5000);
                    if (umbralChar != -1) {
                        System.out.println(servicio.c2_ActividadesIncompletas(umbralChar));
                    }
                    break;
                case "3":
                    double nota = pedirDoubleRango(sc,
                            "Ingrese el umbral de calificación (0 a 100): ",
                            0.0, 100.0);
                    if (nota != -1) {
                        System.out.println(servicio.c3_CalificacionesMenoresA(nota));
                    }
                    break;
                case "4":
                    Date fecha = pedirFecha(sc, "Ingrese la fecha límite de referencia (yyyy-MM-dd): ");
                    if (fecha != null) {
                        System.out.println(servicio.c4_EntregasTardiasSinCalificar(fecha));
                    }
                    break;
                case "5":
                    double porcentaje = pedirDoubleRango(sc,
                            "Ingrese el porcentaje mínimo (0 a 100): ",
                            0.0, 100.0);
                    if (porcentaje != -1) {
                        System.out.println(servicio.c5_EstudiantesPorcentajeEntregas(porcentaje));
                    }
                    break;
                case "6":
                    System.out.println(servicio.c6_EstudiantesFaltanExpiradas());
                    break;
                case "7":
                    System.out.println(servicio.c7_EstudiantesMismaNota());
                    break;
                case "8":
                    System.out.println("\n--- Cálculos que NO se pueden ejecutar ---");
                    GestorCalculos.mostrarCalculosNoEjecutables(calculos, estudiantes);
                    break;
                case "9":
                    String idAct = pedirDato(sc, "Ingrese el ID de la Actividad (Ej: A001): ");
                    if (!cancelado(idAct)) {
                        GestorCalculos.mostrarCalculosPorActividad(calculos, actividades, idAct);
                    }
                    break;
                case "10":
                    c10_OrdenarEstudiantesPorCalculo(sc, calculos, servicio);
                    break;
                default:
                    System.out.println("[!] Opción inválida.");
            }
        }
    }

    // Consulta 10: ordena estudiantes segun el resultado de un calculo seleccionado
    private static void c10_OrdenarEstudiantesPorCalculo(Scanner sc, ListaCompuesta<Calculo, Object> calculos, ServicioConsultas servicio) {
        if (calculos == null || calculos.getHeader() == null) {
            System.out.println("No hay cálculos cargados.");
            return;
        }
        System.out.println("\n--- ORDENAR ESTUDIANTES POR CÁLCULO ---");

        int i = 1;
        NodoCompuesto<Calculo, Object> nc = calculos.getHeader();
        while (nc != null) {
            System.out.println("  " + i + ") " + nc.getData().getId() + " - " + nc.getData().getNombre());
            nc = nc.getNext();
            i++;
        }
        System.out.print("Elige el número del cálculo (exit para cancelar): ");
        String in = sc.nextLine().trim();
        if (cancelado(in)) return;

        int indice;
        try {
            indice = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("[!] Debes ingresar un número válido.");
            return;
        }

        String idCalculo = servicio.obtenerIdCalculoPorIndice(indice);
        if (idCalculo == null) {
            System.out.println("[!] Índice fuera de rango.");
            return;
        }

        System.out.print("Orden (1=Ascendente, 2=Descendente): ");
        String ord = sc.nextLine().trim();

        boolean desc = false;
        if (ord.equals("2")) {
            desc = true;
        }

        System.out.println(servicio.c10_OrdenarEstudiantesPorCalculo(idCalculo, desc));
    }

    // Verifica si el usuario ingreso un comando de salida
    private static boolean cancelado(String input) {
        return input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("salir");
    }

    // Solicita un texto al usuario y lo retorna sin espacios al inicio/fin
    private static String pedirDato(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    // Solicita un numero dentro de un rango y lo valida
    private static double pedirDoubleRango(Scanner sc, String mensaje, double min, double max) {
        while (true) {
            String input = pedirDato(sc, mensaje);
            if (cancelado(input)) return -1;
            try {
                double val = Double.parseDouble(input);
                if (val >= min && val <= max) return val;
                System.out.println("[!] El valor debe estar entre " + min + " y " + max);
            } catch (NumberFormatException e) {
                System.out.println("[!] Ingrese un número válido.");
            }
        }
    }

    // Solicita una fecha en formato yyyy-MM-dd y la convierte a Date
    private static Date pedirFecha(Scanner sc, String mensaje) {
        while (true) {
            String input = pedirDato(sc, mensaje);
            if (cancelado(input)) return null;
            try {
                return SDF.parse(input);
            } catch (ParseException e) {
                System.out.println("[!] Formato incorrecto. Use yyyy-MM-dd.");
            }
        }
    }
}