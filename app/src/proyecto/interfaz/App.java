package proyecto.interfaz;
import proyecto.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    // Inicializa la aplicacion JavaFX cargando los datos y mostrando la ventana principal
    @Override
    public void start(Stage primaryStage) {
        try {
            CargadorDatos cargador = new CargadorDatos();
            cargador.cargarEstudiantes("estudiantes.csv");
            cargador.cargarActividades("actividades.csv");
            cargador.cargarEntregas("entregas.csv");
            cargador.cargarCalculos("calculos.csv");

            ListaCompuesta<Estudiante, Entrega> estConEnt = cargador.getEstudiantesConEntregas();
            ListaCompuesta<Actividad, Entrega> actConEnt = cargador.getActividadesConEntregas();
            ListaCompuesta<Calculo, Object> calculos = cargador.getCalculos();

            ModeloDatosFX modelo = new ModeloDatosFX();
            modelo.cargarDesdeTDAs(actConEnt, calculos);

            VistaBienvenida dashboard = new VistaBienvenida(estConEnt, actConEnt, calculos, modelo);

            Scene scene = new Scene(dashboard, 1000, 600);
            primaryStage.setTitle("Libro de Calificaciones - Estructuras de Datos");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fatal al cargar los archivos CSV.");
        }
    }

    // Punto de entrada que lanza el ciclo de vida de la aplicacion JavaFX
    public static void main(String[] args) {
        launch(args);
    }
}