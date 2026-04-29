package proyecto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SistemaConsola {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    //Inicia el flujo principal del programa
    public static void iniciarMenuPrincipal(Scanner sc, ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos) {
        SDF.setLenient(false); // Asegura que no se acepten fechas inventadas (ej. 32 de enero)
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("          MENÚ PRINCIPAL");
            System.out.println("=".repeat(40));
            System.out.println("1. Consultas");
            System.out.println("2. Reporte de Calificaciones");
            System.out.println("3. Crear Actividad Manualmente");
            System.out.println("exit. Salir del programa");
            System.out.print("Ingrese opción: ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("exit") || input.equals("salir")) {
                System.out.println("\n[i] Guardando información... Saliendo del sistema. ¡Hasta pronto!");
                break;
            }
            switch (input) {
                case "1":
                    GestorConsultas.menuConsultas(sc, estudiantes, actividades, calculos);
                    break;
                case "2":
                    GestorReportes.menuReporte(sc, estudiantes, actividades, calculos);
                    break;
                case "3":
                    crearActividadManual(sc, actividades);
                    break;
                default:
                    System.out.println("[!] Opción inválida. Intente de nuevo.");
            }
        }
    }

    //Permite al profesor registrar una nueva actividad
    private static void crearActividadManual(Scanner sc, ListaCompuesta<Actividad, Entrega> actividades) {
        System.out.println("\n--- CREAR ACTIVIDAD MANUALMENTE ---");
        System.out.print("Ingrese ID de la actividad (Ej. A005) o 'exit' para cancelar: ");
        String id = sc.nextLine().trim();
        if (id.equalsIgnoreCase("exit") || id.equalsIgnoreCase("s")) return;
        System.out.print("Ingrese el nombre de la actividad (Ej. Tarea 3): ");
        String nombre = sc.nextLine().trim();
        String tipo = "";
        while (true) {
            System.out.print("Ingrese el tipo/categoría (Tarea, Examen, Proyecto): ");
            tipo = sc.nextLine().trim().toLowerCase();

            if (tipo.equals("tarea") || tipo.equals("examen") || tipo.equals("proyecto")) {
                tipo = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
                break;
            } else {
                System.out.println("[!] Tipo inválido. Por favor, ingrese estrictamente 'Tarea', 'Examen' o 'Proyecto'.");
            }
        }
        Date fechaLimite = null;
        while (fechaLimite == null) {
            System.out.print("Ingrese la fecha límite (yyyy-MM-dd): ");
            String fechaStr = sc.nextLine().trim();
            try {
                fechaLimite = SDF.parse(fechaStr);
            } catch (ParseException e) {
                System.out.println("[!] Formato de fecha incorrecto. Asegúrese de usar el formato yyyy-MM-dd.");
            }
        }
        Actividad nuevaActividad = new Actividad(id, nombre, fechaLimite, tipo);
        actividades.anadir(new NodoCompuesto<>(nuevaActividad));
        guardarActividadEnCSV(nuevaActividad, "actividades.csv");
        System.out.println("\n[+] Actividad '" + nombre + "' (" + id + ") creada y añadida exitosamente al sistema.");
    }

    // Agrega una actividad al final del archivo CSV formateando la fecha y manejando posibles errores de E/S.
    private static void guardarActividadEnCSV(Actividad act, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String fechaTexto = SDF.format(act.getFechaLimite());
            String nuevaLinea = String.format("%s,%s,%s,%s", act.getId(), act.getNombre(), fechaTexto, act.getTipo());
            bw.newLine();
            bw.write(nuevaLinea);
        } catch (java.io.IOException e) {
            System.out.println("\n[!] Error al intentar guardar la actividad en el archivo: " + e.getMessage());
        }
    }
}