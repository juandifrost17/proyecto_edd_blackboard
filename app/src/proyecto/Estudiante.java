package proyecto;

public class Estudiante {
    private String id;
    private String nombre;
    private String apellido;
    private int edad;
    private String email;

    //Constructor
    public Estudiante(String id, String nombre,String apellido, int edad, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
    }

    //Constructor auxiliar (solo id)
    public Estudiante(String id) {
        this.id = id;
    }

    //Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getEmail() { return email; }

    //Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) {this.apellido = apellido;}
    public void setEdad(int edad) {this.edad = edad;}
    public void setEmail(String email) { this.email = email; }

    //ToString
    @Override
    public String toString() {
        return "ID: " + id + "\n" + "Nombre: " + nombre + "\n" + "Apellido: " + apellido + "\n";
    }

    //Sobreescritura del metodo equals
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Estudiante)) {
            return false;
        }
        Estudiante otro = (Estudiante) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(otro.id);
    }

    //Sobreescritura del metodo hashCode
    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }
}