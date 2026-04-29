package proyecto;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CargadorDatos {
    private ListaCompuesta<Estudiante, Entrega> estudiantesConEntregas;
    private ListaCompuesta<Actividad, Entrega> actividadesConEntregas;
    private ListaCompuesta<Calculo,Object> calculos;

    // Constructor
    public CargadorDatos() {
        this.estudiantesConEntregas = new ListaCompuesta<Estudiante, Entrega>();
        this.actividadesConEntregas = new ListaCompuesta<Actividad, Entrega>();
        this.calculos = new ListaCompuesta<Calculo, Object>();
    }

    // Getters
    public ListaCompuesta<Estudiante, Entrega> getEstudiantesConEntregas() { return estudiantesConEntregas; }
    public ListaCompuesta<Actividad, Entrega> getActividadesConEntregas() { return actividadesConEntregas; }
    public ListaCompuesta<Calculo, Object> getCalculos() { return calculos; }

    // Metodos para leer archivos CSV y llenar estructuras (Est/Act/Ent/Calc)
    public void cargarEstudiantes(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        br.readLine();
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length < 5) continue;
            String id = partes[0].trim();
            String nombre = partes[1].trim();
            String apellido = partes[2].trim();
            int edad;
            try {
                edad = Integer.parseInt(partes[3].trim());
            } catch (NumberFormatException ex) {
                continue;
            }
            String email = partes[4].trim();
            Estudiante est = new Estudiante(id, nombre, apellido, edad, email);

            boolean existe = false;
            NodoCompuesto<Estudiante, Entrega> actual = estudiantesConEntregas.getHeader();
            while (actual != null) {
                if (actual.getData().equals(est)) {
                    existe = true;
                    break;
                }
                actual = actual.getNext();
            }

            if (!existe) {
                NodoCompuesto<Estudiante, Entrega> nodo = new NodoCompuesto<>(est);
                estudiantesConEntregas.anadir(nodo);
            }
        }
        br.close();
    }

    // Lee el archivo CSV de actividades y carga la lista principal evitando duplicados por id
    public void cargarActividades(String ruta) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        br.readLine();
        String linea;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length < 4) continue;
            String id = partes[0].trim();
            String nombre = partes[1].trim();
            Date fechaLimite = sdf.parse(partes[2].trim());
            String tipo = partes[3].trim();
            Actividad act = new Actividad(id, nombre, fechaLimite, tipo);
            // Evitar duplicados por id
            boolean existe = false;
            NodoCompuesto<Actividad, Entrega> actual = actividadesConEntregas.getHeader();
            while (actual != null) {
                if (actual.getData().equals(act)) {
                    existe = true;
                    break;
                }
                actual = actual.getNext();
            }
            if (!existe) {
                NodoCompuesto<Actividad, Entrega> nodo = new NodoCompuesto<>(act);
                actividadesConEntregas.anadir(nodo);
            }
        }
        br.close();
    }

    // Lee el archivo CSV de entregas y las vincula tanto por estudiante como por actividad
    public void cargarEntregas(String ruta) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        br.readLine();
        String linea;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length < 6) continue;
            String idEst = partes[0].trim();
            String idAct = partes[1].trim();
            String contenido = partes[2].trim();
            String califStr = partes[3].trim();
            String fechaEntStr = partes[4].trim();
            boolean calificada = Boolean.parseBoolean(partes[5].trim());

            Estudiante estudiante = buscarEstudiantePorId(idEst);
            Actividad actividad = buscarActividadPorId(idAct);
            if (estudiante == null || actividad == null) {
                System.err.println("️ Estudiante o Actividad no encontrada: " + idEst + " - " + idAct);
                continue;
            }

            Date fechaEntrega = null;
            if (!fechaEntStr.isEmpty()) {
                try {
                    fechaEntrega = sdf.parse(fechaEntStr);
                } catch (ParseException e) {
                    System.err.println(" Error parseando fecha: " + fechaEntStr);
                }
            }

            double calificacion = 0.0;
            if (calificada && !califStr.isEmpty()) {
                try {
                    calificacion = Double.parseDouble(califStr);
                } catch (NumberFormatException e) {
                    calificacion = 0.0;
                }
            }
            Entrega entrega = new Entrega(estudiante, actividad, contenido, calificacion, fechaEntrega, calificada);
            agregarEntregaAEstudiante(estudiante, entrega);
            agregarEntregaAActividad(actividad, entrega);
        }
        br.close();
    }

    // Lee el archivo CSV de calculos y carga la lista principal con sus formulas
    public void cargarCalculos(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length < 3) continue;

            String id = partes[0].trim();
            String nombre = partes[1].trim();
            String formula = partes[2].trim();

            Calculo calculo = new Calculo(id, nombre, formula);
            calculos.anadir(new NodoCompuesto<>(calculo));
        }
        br.close();
    }

    // Busca un estudiante por id dentro de la lista principal de estudiantes
    private Estudiante buscarEstudiantePorId(String id) {
        Estudiante llave = new Estudiante(id);
        NodoCompuesto<Estudiante, Entrega> nodo = estudiantesConEntregas.buscarNodoPrincipal(llave);
        if (nodo == null) {
            return null;
        }
        return nodo.getData();
    }

    // Busca una actividad por id dentro de la lista principal de actividades
    private Actividad buscarActividadPorId(String id) {
        Actividad llave = new Actividad(id);
        NodoCompuesto<Actividad, Entrega> nodo = actividadesConEntregas.buscarNodoPrincipal(llave);
        if (nodo == null) {
            return null;
        }
        return nodo.getData();
    }

    // Agrega una entrega a la lista secundaria del estudiante correspondiente
    private void agregarEntregaAEstudiante(Estudiante estudiante, Entrega entrega) {
        NodoCompuesto<Estudiante, Entrega> actual = estudiantesConEntregas.getHeader();
        while (actual != null) {
            if (actual.getData().equals(estudiante)) {
                estudiantesConEntregas.agregarElementoListaSecundaria(actual, entrega);
                return;
            }
            actual = actual.getNext();
        }
        NodoCompuesto<Estudiante, Entrega> nuevoNodo = new NodoCompuesto<>(estudiante);
        estudiantesConEntregas.anadir(nuevoNodo);
        estudiantesConEntregas.agregarElementoListaSecundaria(nuevoNodo, entrega);
    }

    // Agrega una entrega a la lista secundaria de la actividad correspondiente
    private void agregarEntregaAActividad(Actividad actividad, Entrega entrega) {
        NodoCompuesto<Actividad, Entrega> actual = actividadesConEntregas.getHeader();
        while (actual != null) {
            if (actual.getData().equals(actividad)) {
                actividadesConEntregas.agregarElementoListaSecundaria(actual, entrega);
                return;
            }
            actual = actual.getNext();
        }
        NodoCompuesto<Actividad, Entrega> nuevoNodo = new NodoCompuesto<>(actividad);
        actividadesConEntregas.anadir(nuevoNodo);
        actividadesConEntregas.agregarElementoListaSecundaria(nuevoNodo, entrega);
    }
}