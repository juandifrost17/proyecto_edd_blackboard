package proyecto.interfaz;
import proyecto.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VistaBienvenida extends BorderPane {
    private final ListaCompuesta<Estudiante, Entrega> estudiantes;
    private final ListaCompuesta<Actividad, Entrega> actividades;
    private final ListaCompuesta<Calculo, Object> calculos;
    private final ModeloDatosFX modelo;
    private final ModuloReporte moduloReporte;
    private final ModuloConsulta moduloConsulta;
    private final ModuloCrearActividad moduloCrearActividad;
    private final StackPane areaCentral;

    // Construye la pantalla principal, inicializa módulos y configura el layout con menú lateral y área central.
    public VistaBienvenida(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos, ModeloDatosFX modelo) {
        this.estudiantes = estudiantes;
        this.actividades = actividades;
        this.calculos = calculos;
        this.modelo = modelo;
        this.moduloReporte = new ModuloReporte(estudiantes, actividades, calculos, modelo);
        this.moduloConsulta = new ModuloConsulta(estudiantes, actividades, calculos);
        this.moduloCrearActividad = new ModuloCrearActividad(actividades, modelo);
        this.areaCentral = new StackPane();
        this.areaCentral.setStyle("-fx-background-color: #ecf0f1;");
        Label msjBienvenida = new Label("Bienvenido al Sistema de Calificaciones\nSeleccione una opción en el menú lateral.");
        msjBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        msjBienvenida.setTextFill(Color.web("#7f8c8d"));
        this.areaCentral.getChildren().add(msjBienvenida);
        this.setCenter(areaCentral);
        this.setLeft(crearMenuLateral());
    }

    // Crea el menú lateral con botones para navegar entre módulos y salir del sistema.
    private VBox crearMenuLateral() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220);
        Label lblMenu = new Label("MENÚ");
        lblMenu.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblMenu.setTextFill(Color.WHITE);
        Button btnConsultas = crearBotonMenu("Consultas");
        Button btnReportes = crearBotonMenu("Reportes");
        Button btnCrearActividad = crearBotonMenu("Crear Actividad");
        Button btnSalir = crearBotonMenu("Salir");
        btnConsultas.setOnAction(e -> {
            areaCentral.getChildren().clear();
            areaCentral.getChildren().add(moduloConsulta);
        });
        btnReportes.setOnAction(e -> {
            areaCentral.getChildren().clear();
            areaCentral.getChildren().add(moduloReporte);
        });
        btnCrearActividad.setOnAction(e -> {
            areaCentral.getChildren().clear();
            areaCentral.getChildren().add(moduloCrearActividad);
        });
        btnSalir.setOnAction(e -> System.exit(0));
        sidebar.getChildren().addAll(lblMenu, btnConsultas, btnReportes, btnCrearActividad, btnSalir);
        return sidebar;
    }

    // Construye un botón del menú con estilo y efectos hover.
    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: white; " + "-fx-font-size: 14px; " + "-fx-padding: 10 15;"
        );
        btn.setOnMouseEntered(e ->
                btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 15;")
        );
        btn.setOnMouseExited(e ->
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 15;")
        );
        return btn;
    }
}