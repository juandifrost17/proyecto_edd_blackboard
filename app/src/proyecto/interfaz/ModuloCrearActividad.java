package proyecto.interfaz;
import proyecto.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

public class ModuloCrearActividad extends VBox {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private final ListaCompuesta<Actividad, Entrega> actividades;
    private final ModeloDatosFX modelo;
    private final TextField txtId = new TextField();
    private final TextField txtNombre = new TextField();
    private final ComboBox<String> comboTipo = new ComboBox<>();
    private final DatePicker datePicker = new DatePicker();

    // Construye el formulario de creación de actividades y configura sus controles y eventos.
    public ModuloCrearActividad(ListaCompuesta<Actividad, Entrega> actividades, ModeloDatosFX modelo) {
        this.actividades = actividades;
        this.modelo = modelo;
        setPadding(new Insets(20));
        setSpacing(12);
        Label titulo = new Label("Crear Actividad");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        txtId.setPromptText("ID (Ej: A005)");
        txtNombre.setPromptText("Nombre (Ej: Tarea 3)");
        comboTipo.getItems().addAll("Tarea", "Examen", "Proyecto");
        comboTipo.setPromptText("Tipo");
        datePicker.setEditable(false);
        datePicker.setPromptText("Fecha límite");
        Button btnGuardar = new Button("Guardar Actividad");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> guardar());
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);
        form.add(new Label("Nombre:"), 0, 1);
        form.add(txtNombre, 1, 1);
        form.add(new Label("Tipo:"), 0, 2);
        form.add(comboTipo, 1, 2);
        form.add(new Label("Fecha límite:"), 0, 3);
        form.add(datePicker, 1, 3);
        getChildren().addAll(titulo, form, btnGuardar);
    }

    // Valida los campos, crea la actividad, la añade a memoria, la guarda en CSV y actualiza la UI en tiempo real.
    private void guardar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = comboTipo.getSelectionModel().getSelectedItem();
        if (id.isEmpty() || nombre.isEmpty() || tipo == null || datePicker.getValue() == null) {
            alerta("Faltan datos", "Complete ID, Nombre, Tipo y Fecha.");
            return;
        }
        if (!id.matches("[A-Za-z]\\d+")) {
            alerta("ID inválido", "Use formato como A005, A10, etc.");
            return;
        }
        Actividad buscar = new Actividad(id);
        if (actividades.buscarNodoPrincipal(buscar) != null) {
            alerta("ID duplicado", "Ya existe una actividad con ese ID.");
            return;
        }
        Date fecha = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Actividad nueva = new Actividad(id, nombre, fecha, tipo);
        actividades.anadir(new NodoCompuesto<>(nueva));
        if (!appendCSV(nueva, "actividades.csv")) {
            alerta("Advertencia", "Se creó en memoria, pero falló guardar en CSV.");
            return;
        }
        modelo.agregarActividadEnTiempoReal(nueva);
        info("Éxito", "Actividad creada: " + id + " - " + nombre);
        limpiar();
    }

    // Agrega la actividad al final del archivo CSV con el formato esperado.
    private boolean appendCSV(Actividad a, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            String f = SDF.format(a.getFechaLimite());
            bw.newLine();
            bw.write(a.getId() + "," + a.getNombre() + "," + f + "," + a.getTipo());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Limpia los campos del formulario para permitir un nuevo registro.
    private void limpiar() {
        txtId.clear();
        txtNombre.clear();
        comboTipo.getSelectionModel().clearSelection();
        datePicker.setValue(null);
    }

    // Muestra una alerta de advertencia con un título y mensaje.
    private void alerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    // Muestra un mensaje informativo con un título y mensaje.
    private void info(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}