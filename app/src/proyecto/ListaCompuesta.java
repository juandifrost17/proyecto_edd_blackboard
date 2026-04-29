package proyecto;
import java.util.Comparator;

public class ListaCompuesta<E, F> {
    private NodoCompuesto<E, F> header;
    private NodoCompuesto<E, F> tail;
    private int size;

    //Constructor
    public ListaCompuesta() {
        header = null;
        tail = null;
        size = 0;
    }

    //Getters
    public NodoCompuesto<E, F> getHeader() {return header;}
    public NodoCompuesto<E, F> getTail() {return tail;}
    public int getSize() {return size;}

    // Comprobar si esta vacia o no la lista
    public boolean isEmpty() {
        return size == 0 || header == null;
    }

    // Inserta un nodo al final de la lista principal
    public void anadir(NodoCompuesto<E, F> nodo) {
        if (nodo == null) {
            return;
        }
        nodo.setNext(null);
        if (size == 0) {
            header = tail = nodo;
        } else {
            tail.setNext(nodo);
            tail = nodo;
        }
        size++;
    }

    // Agrega un elemento a la lista secundaria de un nodo principal
    public void agregarElementoListaSecundaria(NodoCompuesto<E, F> nodo, F elemento) {
        if (nodo == null || elemento == null) {
            return;
        }
        NodoCompuesto<F, Object> nodoSecundario = new NodoCompuesto<>(elemento);
        nodo.getSecundaria().anadir(nodoSecundario);
    }

    // Filtra nodos principales si alguno de sus secundarios cumple con el comparador (menor, mayor o igual)
    public ListaCompuesta<E, F> filtrarPorCriterioSecundario(Comparator<F> comparador, F valor, int tipo) {
        ListaCompuesta<E, F> nueva = new ListaCompuesta<>();
        if (header == null || comparador == null || valor == null) {
            return nueva;
        }
        for (NodoCompuesto<E, F> i = header; i != null; i = i.getNext()) {
            if (i.getSecundaria() == null || i.getSecundaria().getHeader() == null) {
                continue;
            }
            boolean cumple = false;
            for (NodoCompuesto<F, Object> j = i.getSecundaria().getHeader(); j != null; j = j.getNext()) {
                int resultado = comparador.compare(j.getData(), valor);
                if ((tipo == -1 && resultado < 0) || (tipo ==  1 && resultado > 0) || (tipo ==  0 && resultado == 0)) {
                    cumple = true;
                    break;
                }
            }
            if (cumple) {
                NodoCompuesto<E, F> copia = new NodoCompuesto<>(i.getData());
                copiarListaSecundaria(i, copia);
                nueva.anadir(copia);
            }
        }
        return nueva;
    }

    // Filtra nodos principales comparando directamente su dato principal (menor, mayor o igual)
    public ListaCompuesta<E, F> filtrarPorCriterioPrincipal(Comparator<E> comparador, E valor, int tipo) {
        ListaCompuesta<E, F> nueva = new ListaCompuesta<>();
        if (header == null || comparador == null || valor == null) {
            return nueva;
        }
        for (NodoCompuesto<E, F> i = header; i != null; i = i.getNext()) {
            int resultado = comparador.compare(i.getData(), valor);
            if ((tipo == -1 && resultado < 0) || (tipo ==  1 && resultado > 0) || (tipo ==  0 && resultado == 0)) {
                NodoCompuesto<E, F> copia = new NodoCompuesto<>(i.getData());
                copiarListaSecundaria(i, copia);
                nueva.anadir(copia);
            }
        }
        return nueva;
    }

    // Filtra principales donde existan 2 secundarios con mismo criterio (cmp1) pero diferente en otro (cmp2)
    public ListaCompuesta<E, F> filtrarPrincipalesPorCoincidenciaDoble(Comparator<F> cmp1, Comparator<F> cmp2) {
        ListaCompuesta<E, F> nueva = new ListaCompuesta<>();
        if (header == null || cmp1 == null || cmp2 == null) return nueva;
        for (NodoCompuesto<E, F> i = header; i != null; i = i.getNext()) {
            if (i.getSecundaria() == null || i.getSecundaria().getHeader() == null) continue;
            boolean cumple = false;
            for (NodoCompuesto<F, Object> a = i.getSecundaria().getHeader(); a != null && !cumple; a = a.getNext()) {
                for (NodoCompuesto<F, Object> b = a.getNext(); b != null; b = b.getNext()) {
                    F x = a.getData();
                    F y = b.getData();
                    if (x == null || y == null) continue;
                    if (cmp1.compare(x, y) == 0 && cmp2.compare(x, y) != 0) {
                        cumple = true;
                        break;
                    }
                }
            }
            if (cumple) {
                NodoCompuesto<E, F> copia = new NodoCompuesto<>(i.getData());
                copiarListaSecundaria(i, copia);
                nueva.anadir(copia);
            }
        }
        return nueva;
    }

    // Agrega a la lista actual los principales de otra lista, evitando repetidos
    public void agregarSinRepetidosDesde(ListaCompuesta<E, F> fuente) {
        if (fuente == null || fuente.getHeader() == null) return;

        for (NodoCompuesto<E, F> p = fuente.getHeader(); p != null; p = p.getNext()) {
            E dato = p.getData();
            if (dato != null && !contieneEnNodos(this.getHeader(), dato)) {
                this.anadir(new NodoCompuesto<>(dato));
            }
        }
    }

    // Une dos listas sin repetir elementos principales
    public ListaCompuesta<E, F> unionSinRepetidos(ListaCompuesta<E, F> otra) {
        ListaCompuesta<E, F> r = new ListaCompuesta<>();
        r.agregarSinRepetidosDesde(this);
        r.agregarSinRepetidosDesde(otra);
        return r;
    }

    // Obtiene intersección sin repetir: solo elementos que estén en ambas listas
    public ListaCompuesta<E, F> interseccionSinRepetidos(ListaCompuesta<E, F> otra) {
        ListaCompuesta<E, F> r = new ListaCompuesta<>();
        if (otra == null || this.getHeader() == null || otra.getHeader() == null) return r;
        for (NodoCompuesto<E, F> i = this.getHeader(); i != null; i = i.getNext()) {
            E dato = i.getData();
            if (dato != null
                    && contieneEnNodos(otra.getHeader(), dato)
                    && !contieneEnNodos(r.getHeader(), dato)) {
                r.anadir(new NodoCompuesto<>(dato));
            }
        }
        return r;
    }

    // Busca un nodo principal por igualdad del dato
    public NodoCompuesto<E, F> buscarNodoPrincipal(E dato) {
        if (dato == null) return null;
        for (NodoCompuesto<E, F> i = header; i != null; i = i.getNext()) {
            if (dato.equals(i.getData())) {
                return i;
            }
        }
        return null;
    }

    // Copia todos los elementos secundarios de un nodo a otro (para construir listas resultado)
    private void copiarListaSecundaria(NodoCompuesto<E, F> origen, NodoCompuesto<E, F> destino) {
        if (origen.getSecundaria() != null) {
            for (NodoCompuesto<F, Object> j = origen.getSecundaria().getHeader(); j != null; j = j.getNext()) {
                destino.getSecundaria().anadir(new NodoCompuesto<>(j.getData()));
            }
        }
    }

    // Verifica si un dato existe en una cadena de nodos (usado para evitar repetidos)
    private <J, K> boolean contieneEnNodos(NodoCompuesto<J, K> cabeza, J dato) {
        if (cabeza == null || dato == null) return false;
        for (NodoCompuesto<J, K> i = cabeza; i != null; i = i.getNext()) {
            if (dato.equals(i.getData())) return true;
        }
        return false;
    }

    // Devuelve una lista con todos los secundarios que cumplan un criterio (menor, mayor o igual)
    public ListaCompuesta<F, Object> obtenerSecundariaFiltrada(Comparator<F> comparador, F valor, int tipo) {
        ListaCompuesta<F, Object> resultado = new ListaCompuesta<>();
        if (header == null || comparador == null || valor == null) {
            return resultado;
        }
        for (NodoCompuesto<E, F> i = header; i != null; i = i.getNext()) {
            if (i.getSecundaria() == null || i.getSecundaria().getHeader() == null) {
                continue;
            }
            for (NodoCompuesto<F, Object> j = i.getSecundaria().getHeader(); j != null; j = j.getNext()) {
                int r = comparador.compare(j.getData(), valor);
                boolean cumple = false;
                if (tipo == -1 && r < 0) cumple = true;
                else if (tipo == 1 && r > 0) cumple = true;
                else if (tipo == 0 && r == 0) cumple = true;

                if (cumple) {
                    resultado.anadir(new NodoCompuesto<>(j.getData()));
                }
            }
        }
        return resultado;
    }

    // Filtra principales según el porcentaje de secundarios que tienen respecto a un total de referencias
    public ListaCompuesta<E, F> filtrarPorcentajeSecundariosMayor(double porcentajeMinimo, int totalReferencias) {
        ListaCompuesta<E, F> resultado = new ListaCompuesta<>();
        if (header == null || totalReferencias <= 0) {
            return resultado;
        }
        for (NodoCompuesto<E, F> actual = header; actual != null; actual = actual.getNext()) {
            int cantidadSecundarios = 0;
            if (actual.getSecundaria() != null) {
                cantidadSecundarios = actual.getSecundaria().getSize();
            }
            double porcentaje = (cantidadSecundarios * 100.0) / totalReferencias;
            if (porcentaje > porcentajeMinimo) {
                NodoCompuesto<E, F> copia = new NodoCompuesto<>(actual.getData());
                copiarListaSecundaria(actual, copia);
                resultado.anadir(copia);
            }
        }
        return resultado;
    }

    // Filtra principales que NO contienen todas las referencias esperadas en su lista secundaria
    public ListaCompuesta<E, F> filtrarPrincipalesConFaltantesEnReferencias(ListaCompuesta<F, Object> referencias, Comparator<F> comparador) {
        ListaCompuesta<E, F> resultado = new ListaCompuesta<>();
        if (header == null || referencias == null || referencias.getHeader() == null || comparador == null) {
            return resultado;
        }
        for (NodoCompuesto<E, F> p = header; p != null; p = p.getNext()) {
            boolean faltaAlguna = false;
            for (NodoCompuesto<F, Object> ref = referencias.getHeader(); ref != null; ref = ref.getNext()) {
                boolean encontrado = false;
                if (p.getSecundaria() != null && p.getSecundaria().getHeader() != null) {
                    for (NodoCompuesto<F, Object> s = p.getSecundaria().getHeader(); s != null; s = s.getNext()) {
                        if (comparador.compare(s.getData(), ref.getData()) == 0) {
                            encontrado = true;
                            break;
                        }
                    }
                }
                if (!encontrado) {
                    faltaAlguna = true;
                    break;
                }
            }
            if (faltaAlguna) {
                NodoCompuesto<E, F> copia = new NodoCompuesto<>(p.getData());
                copiarListaSecundaria(p, copia);
                resultado.anadir(copia);
            }
        }
        return resultado;
    }

    // Ordena la lista principal usando cualquier criterio definido por Comparator
    public void ordenar(Comparator<NodoCompuesto<E, F>> comparator) {
        if (comparator == null) return;
        if (header == null || header.getNext() == null) return;
        NodoCompuesto<E, F> ordenada = null;
        NodoCompuesto<E, F> actual = header;
        while (actual != null) {
            NodoCompuesto<E, F> siguiente = actual.getNext();
            actual.setNext(null);
            ordenada = insertarEnOrden(ordenada, actual, comparator);
            actual = siguiente;
        }
        header = ordenada;
        tail = header;
        while (tail != null && tail.getNext() != null) {
            tail = tail.getNext();
        }
    }

    // Inserta un nodo en la posicion correcta dentro de una lista ya ordenada segun el comparator
    private NodoCompuesto<E, F> insertarEnOrden(NodoCompuesto<E, F> cabeza, NodoCompuesto<E, F> nodo, Comparator<NodoCompuesto<E, F>> comparator) {
        if (cabeza == null) {
            return nodo;
        }
        if (comparator.compare(nodo, cabeza) <= 0) {
            nodo.setNext(cabeza);
            return nodo;
        }
        NodoCompuesto<E, F> anterior = cabeza;
        NodoCompuesto<E, F> actual = cabeza.getNext();
        while (actual != null && comparator.compare(nodo, actual) > 0) {
            anterior = actual;
            actual = actual.getNext();
        }
        anterior.setNext(nodo);
        nodo.setNext(actual);
        return cabeza;
    }

    //toString
    @Override
    public String toString() {
        if (header == null) {
            return "[Lista vacía]";
        }
        String texto = "";
        NodoCompuesto<E, F> p = header;
        while (p != null) {
            texto += p.getData() + "\n";
            if (p.getSecundaria() != null && !p.getSecundaria().isEmpty()) {
                texto += "Entregas:\n";
                texto += p.getSecundaria() + "\n";
            }
            texto += "\n";
            p = p.getNext();
        }
        return texto;
    }
}