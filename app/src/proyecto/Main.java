package proyecto;
import java.util.Scanner;
import javafx.application.Application;
import proyecto.interfaz.App;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("==================================================");
            System.out.println(" BIENVENIDO AL SISTEMA DE CALIFICACIONES");
            System.out.println("==================================================");
            System.out.println("¿En qué entorno deseas iniciar la aplicación?");
            System.out.println("1. Modo Consola ");
            System.out.println("2. Modo Interfaz Gráfica ");
            System.out.print("Elige una opción (1 o 2): ");
            String opcion = sc.nextLine().trim();
            if (opcion.equals("1")) {
                System.out.println("\n[+] Iniciando entorno de Consola...\n");
                CargadorDatos cargador = new CargadorDatos();
                cargador.cargarEstudiantes("estudiantes.csv");
                cargador.cargarActividades("actividades.csv");
                cargador.cargarEntregas("entregas.csv");
                cargador.cargarCalculos("calculos.csv");
                ListaCompuesta<Estudiante, Entrega> estConEnt = cargador.getEstudiantesConEntregas();
                ListaCompuesta<Actividad, Entrega> actConEnt = cargador.getActividadesConEntregas();
                ListaCompuesta<Calculo, Object> calculos = cargador.getCalculos();
                SistemaConsola.iniciarMenuPrincipal(sc, estConEnt, actConEnt, calculos);
            } else if (opcion.equals("2")) {
                System.out.println("\n[+] Abriendo ventana de Interfaz Gráfica...");
                Application.launch(App.class, args);
            } else {
                System.out.println("\n[!] Opción no válida. Saliendo del sistema...");
            }
        } catch (Exception e) {System.out.println("\n[!] Error: "+e.getMessage()) ;}
    }
}