package proyecto.interfaz;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class ExportadorReporte {
    // Abre un FileChooser para que el usuario elija dónde guardar el reporte segun el formato
    public static File elegirArchivo(javafx.scene.Scene scene, String formato) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar reporte");
        if ("TXT".equalsIgnoreCase(formato)) {
            fc.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Archivo TXT", "*.txt"));
            fc.setInitialFileName("reporte.txt");
        } else {
            fc.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
            fc.setInitialFileName("reporte.csv");
        }
        return fc.showSaveDialog(scene.getWindow());
    }

    // Exporta el contenido de la tabla a un archivo CSV incluyendo encabezados y filas
    public static boolean exportarCSV(TableView<ObservableList<String>> tabla, File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (int c = 0; c < tabla.getColumns().size(); c++) {
                String h = tabla.getColumns().get(c).getText();
                bw.write(escapeCSV(h));
                if (c < tabla.getColumns().size() - 1) bw.write(",");
            }
            bw.newLine();
            for (ObservableList<String> fila : tabla.getItems()) {
                for (int c = 0; c < fila.size(); c++) {
                    bw.write(escapeCSV(fila.get(c)));
                    if (c < fila.size() - 1) bw.write(",");
                }
                bw.newLine();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Exporta el contenido de la tabla a un archivo TXT con un formato tabular simple
    public static boolean exportarTXT(TableView<ObservableList<String>> tabla, File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("REPORTE DE CALIFICACIONES");
            bw.newLine();
            bw.write("------------------------------------------------------------");
            bw.newLine();
            StringBuilder header = new StringBuilder();
            for (int c = 0; c < tabla.getColumns().size(); c++) {
                header.append(tabla.getColumns().get(c).getText());
                if (c < tabla.getColumns().size() - 1) header.append(" | ");
            }
            bw.write(header.toString());
            bw.newLine();
            bw.write("------------------------------------------------------------");
            bw.newLine();
            for (ObservableList<String> fila : tabla.getItems()) {
                StringBuilder line = new StringBuilder();
                for (int c = 0; c < fila.size(); c++) {
                    line.append(fila.get(c));
                    if (c < fila.size() - 1) line.append(" | ");
                }
                bw.write(line.toString());
                bw.newLine();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Escapa un valor para que sea válido en CSV (comillas, comas y saltos de línea)
    private static String escapeCSV(String s) {
        if (s == null) return "";
        boolean q = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String t = s.replace("\"", "\"\"");
        return q ? ("\"" + t + "\"") : t;
    }
}