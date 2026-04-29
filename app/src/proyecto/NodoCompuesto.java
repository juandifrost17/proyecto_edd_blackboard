package proyecto;

public class NodoCompuesto<E, F> {
    private E data;
    private NodoCompuesto<E, F> next;
    private ListaCompuesta<F, Object> secundaria;

    // Constructores
    public NodoCompuesto(E data) {
        this.data = data;
        this.next = null;
        this.secundaria = new ListaCompuesta<F, Object>(); // Siempre se inicializa la lista secundaria
    }

    // Constructor que permite crear el nodo principal con un primer elemento secundario
    public NodoCompuesto(E data, F dataSecundario) {
        this.data = data;
        this.secundaria = new ListaCompuesta<>();
        this.next = null;
        // Si existe dato secundario, se agrega automáticamente a la lista secundaria
        if (dataSecundario != null) {
            this.secundaria.anadir(new NodoCompuesto<>(dataSecundario));
        }
    }

    // Getters
    public E getData() {return data;}
    public NodoCompuesto<E, F> getNext() {return next;}
    public ListaCompuesta<F, Object> getSecundaria() {return secundaria;}

    // Setters
    public void setNext(NodoCompuesto<E, F> next) {this.next = next;}
    public void setSecundaria(ListaCompuesta<F, Object> secundaria) {this.secundaria = secundaria;}

    @Override
    public String toString() {
        String texto = "";
        texto += data;
        return texto;
    }
}
