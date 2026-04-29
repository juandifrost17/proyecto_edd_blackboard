package proyecto.interfaz;
import proyecto.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.File;
import java.text.DecimalFormat;

public class ModuloReporte extends VBox {
    private final ModeloDatosFX modelo;
    private final ObservableList<String> actDisponibles;
    private final ObservableList<String> calcDisponibles;
    private final ObservableList<String> actSeleccionadas = FXCollections.observableArrayList();
    private final ObservableList<String> calcSeleccionados = FXCollections.observableArrayList();
    private final ListaCompuesta<Estudiante, Entrega> estudiantes;
    private final ListaCompuesta<Calculo, Object> calculos;
    private final TableView<ObservableList<String>> tabla = new TableView<>();
    private final ComboBox<String> comboFormato = new ComboBox<>();
    private final Button btnDescargar = new Button("Descargar");
    private final DecimalFormat df = new DecimalFormat("0.00");

    // Construye el módulo de reportes preparando selectores, acciones y la tabla de resultados.
    public ModuloReporte(ListaCompuesta<Estudiante, Entrega> estudiantes, ListaCompuesta<Actividad, Entrega> actividades, ListaCompuesta<Calculo, Object> calculos, ModeloDatosFX modelo) {
        this.estudiantes = estudiantes;
        this.calculos = calculos;
        this.modelo = modelo;
        this.actDisponibles = modelo.getActDisponibles();
        this.calcDisponibles = modelo.getCalcDisponibles();
        setPadding(new Insets(20));
        setSpacing(16);
        Label titulo = new Label("Generador de Reportes Personalizado");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox selectorAct = crearSelector("Actividades", actDisponibles, actSeleccionadas);
        HBox selectorCalc = crearSelector("Cálculos", calcDisponibles, calcSeleccionados);
        Button btnGenerar = new Button("Generar Reporte");
        btnGenerar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGenerar.setOnAction(e -> generar());
        comboFormato.getItems().addAll("CSV", "TXT");
        comboFormato.setValue("CSV");
        comboFormato.setPrefWidth(100);
        btnDescargar.setDisable(true);
        btnDescargar.setOnAction(e -> descargar());
        HBox acciones = new HBox(10, btnGenerar, comboFormato, btnDescargar);
        acciones.setAlignment(Pos.CENTER_LEFT);
        tabla.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        tabla.widthProperty().addListener((o, a, b) -> ajustarAnchos());
        getChildren().addAll(titulo, selectorAct, selectorCalc, acciones, tabla);
    }

    // Crea el selector doble (disponibles/seleccionados) para actividades o cálculos.
    private HBox crearSelector(String titulo, ObservableList<String> disponibles, ObservableList<String> seleccionados) {
        ListView<String> lvDisp = new ListView<>(disponibles);
        lvDisp.setPrefWidth(340);
        lvDisp.setPrefHeight(220);
        ListView<String> lvSel = new ListView<>(seleccionados);
        lvSel.setPrefWidth(340);
        lvSel.setPrefHeight(220);
        VBox izq = new VBox(6, new Label(titulo + " Disponibles:"), lvDisp);
        VBox der = new VBox(6, new Label("Seleccionados:"), lvSel);
        Button add = new Button(">>");
        Button rem = new Button("<<");
        add.setOnAction(e -> FXListUtils.moverYOrdenar(lvDisp, disponibles, seleccionados));
        rem.setOnAction(e -> FXListUtils.moverYOrdenar(lvSel, seleccionados, disponibles));
        VBox botones = new VBox(10, add, rem);
        botones.setAlignment(Pos.CENTER);

        return new HBox(15, izq, botones, der);
    }

    // Genera la tabla del reporte creando columnas dinámicas y calculando valores por estudiante.
    private void generar() {
        tabla.getColumns().clear();
        tabla.getItems().clear();
        if (actSeleccionadas.isEmpty() && calcSeleccionados.isEmpty()) {
            alerta("Atención", "Seleccione al menos una actividad o cálculo.");
            btnDescargar.setDisable(true);
            return;
        }
        TableColumn<ObservableList<String>, String> colEst = new TableColumn<>("Estudiante");
        colEst.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(0)));
        colEst.setSortable(false);
        colEst.setReorderable(false);
        tabla.getColumns().add(colEst);
        int colIndex = 1;
        for (String act : actSeleccionadas) {
            String idAct = act.split(" - ")[0];
            TableColumn<ObservableList<String>, String> c = new TableColumn<>(idAct);
            int idx = colIndex++;
            c.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(idx)));
            c.setSortable(false);
            c.setReorderable(false);
            tabla.getColumns().add(c);
        }
        for (String calc : calcSeleccionados) {
            String idCalc = calc.split(" - ")[0];
            TableColumn<ObservableList<String>, String> c = new TableColumn<>(idCalc);
            int idx = colIndex++;
            c.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(idx)));
            c.setSortable(false);
            c.setReorderable(false);
            tabla.getColumns().add(c);
        }
        NodoCompuesto<Estudiante, Entrega> ne = estudiantes.getHeader();
        while (ne != null) {
            ObservableList<String> fila = FXCollections.observableArrayList();
            Estudiante est = ne.getData();
            fila.add(est.getNombre() + " " + est.getApellido());
            for (String act : actSeleccionadas) {
                String idAct = act.split(" - ")[0];
                double nota = buscarNota(ne.getSecundaria(), idAct);
                fila.add(df.format(nota));
            }
            for (String calc : calcSeleccionados) {
                String idCalc = calc.split(" - ")[0];
                Calculo c = buscarCalculo(idCalc);
                double res = (c == null) ? 0.0 : EvaluadorCalculos.evaluarFormula(c.getFormulaPosfija(), ne.getSecundaria());
                fila.add(df.format(res));
            }
            tabla.getItems().add(fila);
            ne = ne.getNext();
        }
        ajustarAnchos();
        btnDescargar.setDisable(tabla.getItems().isEmpty());
    }

    // Exporta la tabla generada al formato seleccionado (CSV o TXT) usando un FileChooser.
    private void descargar() {
        if (tabla.getItems().isEmpty()) {
            alerta("Atención", "Genere el reporte antes de descargar.");
            return;
        }
        String formato = comboFormato.getValue();
        File file = ExportadorReporte.elegirArchivo(getScene(), formato);
        if (file == null) return;
        boolean ok = "TXT".equalsIgnoreCase(formato)
                ? ExportadorReporte.exportarTXT(tabla, file)
                : ExportadorReporte.exportarCSV(tabla, file);

        if (ok) info("Éxito", "Archivo generado correctamente.");
        else alerta("Error", "No se pudo generar el archivo.");
    }

    // Ajusta el ancho de las columnas de la tabla para que se distribuyan de forma uniforme.
    private void ajustarAnchos() {
        if (tabla.getColumns().isEmpty()) return;
        double total = tabla.getWidth();
        if (total <= 0) return;
        double anchoEst = 260;
        double min = 90;
        double max = 220;
        int totalCols = tabla.getColumns().size();
        int dinamicas = totalCols - 1;
        TableColumn<ObservableList<String>, ?> c0 = tabla.getColumns().get(0);
        c0.setPrefWidth(anchoEst);
        c0.setMinWidth(anchoEst);
        c0.setMaxWidth(anchoEst);
        if (dinamicas <= 0) return;
        double restante = total - anchoEst - 20;
        double sugerido = restante / dinamicas;
        double w = Math.max(min, Math.min(max, sugerido));
        for (int i = 1; i < tabla.getColumns().size(); i++) {
            TableColumn<ObservableList<String>, ?> c = tabla.getColumns().get(i);
            c.setPrefWidth(w);
            c.setMinWidth(w);
            c.setMaxWidth(w);
        }
    }

    // Busca la calificación de un estudiante para una actividad específica dentro de sus entregas.
    private double buscarNota(ListaCompuesta<Entrega, Object> entregas, String idActividad) {
        if (entregas == null) return 0.0;
        NodoCompuesto<Entrega, Object> n = entregas.getHeader();
        while (n != null) {
            Entrega e = n.getData();
            if (e != null && e.getActividad() != null && e.getActividad().getId() != null) {
                if (e.getActividad().getId().equalsIgnoreCase(idActividad)) {
                    return e.isCalificada() ? e.getCalificacion() : 0.0;
                }
            }
            n = n.getNext();
        }
        return 0.0;
    }

    // Localiza un cálculo por ID dentro de la lista enlazada de cálculos cargados.
    private Calculo buscarCalculo(String id) {
        NodoCompuesto<Calculo, Object> n = calculos.getHeader();
        while (n != null) {
            if (n.getData().getId().equalsIgnoreCase(id)) return n.getData();
            n = n.getNext();
        }
        return null;
    }

    // Muestra una alerta de tipo WARNING con un título y mensaje.
    private void alerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    // Muestra una alerta informativa con un título y mensaje.
    private void info(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}