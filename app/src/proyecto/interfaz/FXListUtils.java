package proyecto.interfaz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class FXListUtils {
    // Mueve el elemento seleccionado entre listas y reordena ambas por ID.
    public static void moverYOrdenar(ListView<String> vistaOrigen, ObservableList<String> origen, ObservableList<String> destino) {
        String sel = vistaOrigen.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        origen.remove(sel);
        destino.add(sel);
        ordenarPorId(origen);
        ordenarPorId(destino);
    }

    // Ordena una lista de strings según el ID que aparece al inicio del texto.
    public static void ordenarPorId(ObservableList<String> lista) {
        FXCollections.sort(lista, (s1, s2) -> compararIds(idDeItem(s1), idDeItem(s2)));
    }

    // Extrae el ID de un item con formato "ID - Descripción".
    private static String idDeItem(String item) {
        if (item == null) return "";
        String[] p = item.split(" - ");
        return p.length > 0 ? p[0].trim() : item.trim();
    }

    // Compara dos IDs priorizando prefijo (letra) y luego número si existe.
    private static int compararIds(String a, String b) {
        if (a.isEmpty() && b.isEmpty()) return 0;
        if (a.isEmpty()) return -1;
        if (b.isEmpty()) return 1;
        char pa = a.charAt(0);
        char pb = b.charAt(0);
        if (pa != pb) return Character.compare(pa, pb);
        int na = extraerNumero(a);
        int nb = extraerNumero(b);
        if (na != -1 && nb != -1) return Integer.compare(na, nb);
        return a.compareToIgnoreCase(b);
    }

    // Extrae y convierte a entero los dígitos contenidos en un ID.
    private static int extraerNumero(String id) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (Character.isDigit(c)) sb.append(c);
        }
        if (sb.length() == 0) return -1;
        return Integer.parseInt(sb.toString());
    }
}