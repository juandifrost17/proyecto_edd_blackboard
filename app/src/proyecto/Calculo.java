package proyecto;
import java.util.Objects;

public class Calculo {

    private String id;
    private String nombre;
    private String formulaPosfija;

    //Constructor
    public Calculo(String id, String nombre, String formulaPosfija) {
        this.id = id;
        this.nombre = nombre;
        this.formulaPosfija = formulaPosfija;
    }

    //Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getFormulaPosfija() { return formulaPosfija; }

    //toString
    @Override
    public String toString() {
        return nombre + " (" + id + "): " + formulaPosfija;
    }

    //Sobreescritura del metodo equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calculo calculo = (Calculo) o;
        return Objects.equals(id, calculo.id);
    }

    //Sobreescritura del metodo hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}