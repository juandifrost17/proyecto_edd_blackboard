package proyecto;
import java.util.ArrayDeque;
import java.util.Deque;

public class EvaluadorCalculos {
    // Evalua una formula en notacion posfija usando las entregas del estudiante como fuente de valores
    public static double evaluarFormula(String formulaPosfija, ListaCompuesta<Entrega, Object> entregasEstudiante) {
        Deque<Double> pila = new ArrayDeque<>();
        String[] letras = formulaPosfija.split(" ");
        for (String letra : letras) {
            letra = letra.trim();
            if (letra.isEmpty()) {continue;}
            if (isOper(letra)) {
                if (pila.size() < 2) {
                    continue;
                }
                double b = pila.pop();
                double a = pila.pop();
                double resultado = calcular(a, b, letra);
                pila.push(resultado);
            }
            else if (isNumber(letra)) {
                double numero = Double.parseDouble(letra);
                pila.push(numero);
            }
            else {
                double valor = obtenerValor(entregasEstudiante, letra);
                pila.push(valor);
            }
        }
        if (pila.isEmpty()) {return 0.0;}
        else {return pila.pop();}
    }

    // Obtiene el valor de un simbolo: primero intenta como id de actividad, si no coincide lo trata como tipo y promedia
    private static double obtenerValor(ListaCompuesta<Entrega, Object> entregas, String token) {
        NodoCompuesto<Entrega, Object> actual = entregas.getHeader();
        while (actual != null) {
            Entrega entrega = actual.getData();
            if (entrega == null || entrega.getActividad() == null) {
                actual = actual.getNext();
                continue;
            }
            if (entrega.getActividad().getId().equalsIgnoreCase(token)) {
                if (entrega.isCalificada()) {
                    return entrega.getCalificacion();
                }
                else {
                    return 0.0;
                }
            }
            actual = actual.getNext();
        }
        return calcularPromedioPorTipo(entregas, token);
    }

    // Calcula el promedio de calificaciones de las entregas cuyo tipo coincide
    private static double calcularPromedioPorTipo(ListaCompuesta<Entrega, Object> entregas, String tipoBuscado) {
        double suma = 0;
        int contador = 0;
        NodoCompuesto<Entrega, Object> actual = entregas.getHeader();
        while (actual != null) {
            Entrega entrega = actual.getData();
            if (entrega == null || entrega.getActividad() == null) {
                actual = actual.getNext();
                continue;
            }
            String tipoActividad = entrega.getActividad().getTipo();
            if (tipoActividad != null && tipoActividad.equalsIgnoreCase(tipoBuscado)) {
                if (entrega.isCalificada()) {
                    suma += entrega.getCalificacion();
                    contador++;
                }
            }
            actual = actual.getNext();
        }
        if (contador == 0) {
            return 0.0;
        }
        return suma / contador;
    }

    // Verifica si la formula se puede ejecutar: todos los tokens no numericos ni operadores deben tener nota valida
    public static boolean calculoEjecutable(String formulaPosfija, ListaCompuesta<Entrega, Object> entregasEstudiante) {
        String[] letras = formulaPosfija.split(" ");
        for (String letra : letras) {
            letra = letra.trim();
            if (letra.isEmpty()) {
                continue;
            }
            if (!isNumber(letra) && !isOper(letra)) {
                if (!tieneNotasParaToken(entregasEstudiante, letra)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Verifica si el token existe como id o como tipo y que haya al menos una entrega calificada asociada
    private static boolean tieneNotasParaToken(ListaCompuesta<Entrega, Object> entregas, String token) {
        if (entregas == null || entregas.getHeader() == null) {
            return false;
        }
        NodoCompuesto<Entrega, Object> actual = entregas.getHeader();
        boolean tieneAlMenosUnaNotaDeEseTipo = false;
        while (actual != null) {
            Entrega e = actual.getData();
            if (e == null || e.getActividad() == null) {
                actual = actual.getNext();
                continue;
            }

            if (e.getActividad().getId().equalsIgnoreCase(token)) {
                return e.isCalificada();
            }

            if (e.getActividad().getTipo() != null && e.getActividad().getTipo().equalsIgnoreCase(token)) {
                if (e.isCalificada()) {
                    tieneAlMenosUnaNotaDeEseTipo = true;
                }
            }
            actual = actual.getNext();
        }
        return tieneAlMenosUnaNotaDeEseTipo;
    }

    // Verifica si una formula incluye una actividad por id o por tipo
    public static boolean calculoInvolucraActividad(String formula, String idActividad, String tipoActividad) {
        if (formula == null) {
            return false;
        }
        String[] tokens = formula.split(" ");
        for (String token : tokens) {
            token = token.trim();
            if (token.equalsIgnoreCase(idActividad)) {
                return true;
            }
            if (tipoActividad != null && token.equalsIgnoreCase(tipoActividad)) {
                return true;
            }
        }
        return false;
    }

    // Verifica si es un operador matematico
    private static boolean isOper(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    // Verifica si es numerico
    private static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    // Aplica la operacion matematica entre a y b segun el operador
    private static double calcular(double a, double b, String op) {
        switch (op) {
            case "+" -> {
                return a + b;
            }
            case "-" -> {
                return a - b;
            }
            case "*" -> {
                return a * b;
            }
            case "/" -> {
                if (b != 0) {
                    return a / b;
                }
                else {
                    return 0;
                }
            }
        }
        return 0;
    }

}