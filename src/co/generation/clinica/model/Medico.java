package co.generation.clinica.model;

import co.generation.clinica.interfaces.Registrable;

import java.util.Objects;

public class Medico implements Registrable {
    // clase Medico
    private int id;
    private String nombre;
    private String apellido;
    private Especialidad especialidad;

    public Medico(String nombre, String apellido, Especialidad especialidad) {
        setNombre(nombre);
        setApellido(apellido);
        setEspecialidad(especialidad);
    }

    public Medico(int id, String nombre, String apellido, Especialidad especialidad) {
        setId(id);
        setNombre(nombre);
        setApellido(apellido);
        setEspecialidad(especialidad);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del médico debe ser mayor a cero.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo ni vacío.");
        }
        this.apellido = apellido.trim();
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula.");
        }
        this.especialidad = especialidad;
    }

    @Override
    public String getDatosRegistro() {
        return toString();
    }

    @Override
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty()
                && apellido != null && !apellido.trim().isEmpty()
                && especialidad != null;
    }

    @Override
    public String toString() {
        return "Dr. " + nombre + " " + apellido + " - " + especialidad;
    }

    @Override
    public boolean equals(Object otroObjeto) {
        if (this == otroObjeto) {
            return true;
        }
        if (!(otroObjeto instanceof Medico)) {
            return false;
        }
        Medico otroMedico = (Medico) otroObjeto;
        return nombre.equalsIgnoreCase(otroMedico.nombre) && apellido.equalsIgnoreCase(otroMedico.apellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre == null ? null : nombre.toLowerCase(),
                apellido == null ? null : apellido.toLowerCase());
    }
}
