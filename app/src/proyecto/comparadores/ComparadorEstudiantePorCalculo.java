package proyecto.comparadores;
import proyecto.*;
import java.util.Comparator;

// Compara nodos de estudiantes segun el resultado de un calculo especifico
public class ComparadorEstudiantePorCalculo implements Comparator<NodoCompuesto<Estudiante, Entrega>> {
    private final Calculo calculo;
    private final boolean descendente;

    // Constructor
    public ComparadorEstudiantePorCalculo(Calculo calculo, boolean descendente) {
        this.calculo = calculo;
        this.descendente = descendente;
    }

    // Compara dos estudiantes usando el resultado del calculo
    @Override
    public int compare(NodoCompuesto<Estudiante, Entrega> n1, NodoCompuesto<Estudiante, Entrega> n2) {
        double v1 = EvaluadorCalculos.evaluarFormula(calculo.getFormulaPosfija(), n1.getSecundaria());
        double v2 = EvaluadorCalculos.evaluarFormula(calculo.getFormulaPosfija(), n2.getSecundaria());
        if (descendente) {
            return Double.compare(v2, v1);
        } else {
            return Double.compare(v1, v2);
        }
    }
}