package proyecto.interfaz;
import proyecto.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.Date;

public class ModuloConsulta extends VBox {
    private final ServicioConsultas servicio;
    private final ComboBox<String> comboConsultas = new ComboBox<>();
    private final VBox panelParametros = new VBox(8);
    private final TextArea areaResultados = new TextArea();
    private final TextField txtNumero = new TextField();
    private final TextField txtId = new TextField();
    private final DatePicker dpFecha = new DatePicker();
    private final ComboBox<String> comboCalculos = new ComboBox<>();
    private final ComboBox<String> comboOrden = new ComboBox<>();

    // Constructor: inicializa la interfaz y conecta con ServicioConsultas
    public ModuloConsulta(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos) {
        this.servicio = new ServicioConsultas(estudiantes, actividades, calculos);
        this.setPadding(new Insets(20));
        this.setSpacing(12);
        dpFecha.setEditable(false);
        Label titulo = new Label("Módulo de Consultas");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        comboConsultas.getItems().addAll(
                "1 - Actividades cuya fecha límite ya feneció",
                "2 - Actividades con entregas incompletas (por caracteres)",
                "3 - Actividades con calificaciones menores a un valor",
                "4 - Entregas luego de cierta fecha y aún no calificadas",
                "5 - Estudiantes con % de entregas mayor a un % dado",
                "6 - Estudiantes que no han respondido actividades ya expiradas",
                "7 - Estudiantes con la misma nota en dos actividades diferentes",
                "8 - Cálculos no ejecutables (faltan calificaciones)",
                "9 - Cálculos que involucren una actividad dada (por ID)",
                "10 - Ordenar estudiantes por un cálculo definido"
        );
        comboConsultas.setPromptText("Seleccione una consulta...");
        comboConsultas.setPrefWidth(520);
        comboConsultas.setOnAction(e -> configurarParametros());
        Button btnEjecutar = new Button("Ejecutar Consulta");
        btnEjecutar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEjecutar.setOnAction(e -> ejecutar());
        HBox top = new HBox(10, comboConsultas, btnEjecutar);
        panelParametros.setPadding(new Insets(10));
        panelParametros.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d0d0d0;");
        areaResultados.setEditable(false);
        areaResultados.setFont(javafx.scene.text.Font.font("Monospaced", 13));
        VBox.setVgrow(areaResultados, Priority.ALWAYS);
        cargarComboCalculos(calculos);
        comboOrden.getItems().addAll("Ascendente", "Descendente");
        comboOrden.setValue("Ascendente");
        this.getChildren().addAll(titulo, top, panelParametros, areaResultados);
        configurarParametros();
    }

    // Carga los cálculos disponibles en el ComboBox
    private void cargarComboCalculos(ListaCompuesta<Calculo, Object> calculos) {
        comboCalculos.getItems().clear();
        NodoCompuesto<Calculo, Object> c = calculos.getHeader();
        while (c != null) {
            comboCalculos.getItems().add(c.getData().getId() + " - " + c.getData().getNombre());
            c = c.getNext();
        }
    }

    // Configura dinámicamente los parámetros requeridos según la consulta seleccionada
    private void configurarParametros() {
        panelParametros.getChildren().clear();
        int idx = comboConsultas.getSelectionModel().getSelectedIndex();
        txtNumero.clear();
        txtId.clear();
        dpFecha.setValue(null);

        if (idx == -1) {
            panelParametros.getChildren().add(new Label("Seleccione una consulta."));
            return;
        }
        switch (idx) {
            case 0:
                panelParametros.getChildren().add(new Label("Sin parámetros."));
                break;
            case 1:
                txtNumero.setPromptText("Mínimo de caracteres");
                panelParametros.getChildren().addAll(new Label("Parámetro requerido."), txtNumero);
                break;
            case 2:
                txtNumero.setPromptText("Umbral de nota (0-100)");
                panelParametros.getChildren().addAll(new Label("Parámetro requerido."), txtNumero);
                break;
            case 3:
                panelParametros.getChildren().addAll(new Label("Seleccione una fecha."), dpFecha);
                break;
            case 4:
                txtNumero.setPromptText("Porcentaje (0-100)");
                panelParametros.getChildren().addAll(new Label("Parámetro requerido."), txtNumero);
                break;
            case 5:
            case 6:
            case 7:
                panelParametros.getChildren().add(new Label("Sin parámetros."));
                break;
            case 8:
                txtId.setPromptText("ID Actividad (Ej: A001)");
                panelParametros.getChildren().addAll(new Label("Parámetro requerido."), txtId);
                break;
            case 9:
                panelParametros.getChildren().addAll(new Label("Seleccione cálculo y orden."), comboCalculos, comboOrden);
                break;
        }
    }

    // Ejecuta la consulta seleccionada y muestra el resultado en pantalla
    private void ejecutar() {
        areaResultados.clear();
        int idx = comboConsultas.getSelectionModel().getSelectedIndex();

        if (idx == -1) {
            areaResultados.setText("Seleccione una consulta.");
            return;
        }

        try {
            String resultado = ejecutarConsulta(idx);
            areaResultados.setText(resultado);
        } catch (Exception ex) {
            areaResultados.setText("Error: " + ex.getMessage());
        }
    }

    // Enruta la consulta al metodo correspondiente de ServicioConsultas
    private String ejecutarConsulta(int idx) {
        switch (idx) {
            case 0:
                return servicio.c1_ActividadesExpiradas();
            case 1:
                return servicio.c2_ActividadesIncompletas(
                        (int) leerDoubleEnRango(txtNumero.getText(), 1, 5000));
            case 2:
                return servicio.c3_CalificacionesMenoresA(
                        leerDoubleEnRango(txtNumero.getText(), 0, 100));
            case 3:
                return servicio.c4_EntregasTardiasSinCalificar(
                        leerFecha(dpFecha.getValue()));
            case 4:
                return servicio.c5_EstudiantesPorcentajeEntregas(
                        leerDoubleEnRango(txtNumero.getText(), 0, 100));
            case 5:
                return servicio.c6_EstudiantesFaltanExpiradas();
            case 6:
                return servicio.c7_EstudiantesMismaNota();
            case 7:
                return servicio.c8_CalculosNoEjecutables();
            case 8:
                return servicio.c9_CalculosPorActividad(txtId.getText().trim());
            case 9:
                String sel = comboCalculos.getSelectionModel().getSelectedItem();
                if (sel == null) throw new IllegalArgumentException("Seleccione un cálculo.");
                String idCalc = sel.split(" - ")[0];
                boolean desc = "Descendente".equals(comboOrden.getSelectionModel().getSelectedItem());
                return servicio.c10_OrdenarEstudiantesPorCalculo(idCalc, desc);
            default:
                return "Opción inválida.";
        }
    }

    // Valida y convierte un número dentro de un rango permitido
    private double leerDoubleEnRango(String texto, double min, double max) {
        if (texto == null || texto.isEmpty()) throw new IllegalArgumentException("Debe ingresar un valor numérico.");
        double v;
        try {
            v = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingrese un número válido.");
        }
        if (v < min || v > max)
            throw new IllegalArgumentException("El valor debe estar entre " + min + " y " + max);

        return v;
    }

    // Convierte un LocalDate del DatePicker a Date
    private Date leerFecha(LocalDate ld) {
        if (ld == null)
            throw new IllegalArgumentException("Seleccione una fecha.");
        return java.sql.Date.valueOf(ld);
    }
}