package co.generation.clinica.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Turno {
    // clase Turno
    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private EstadoTurno estado;

    public Turno(Paciente paciente, Medico medico, LocalDateTime fechaHora) {
        setPaciente(paciente);
        setMedico(medico);
        setFechaHora(fechaHora);
        this.estado = EstadoTurno.PENDIENTE;
    }

    public Turno(int id, Paciente paciente, Medico medico, LocalDateTime fechaHora, EstadoTurno estado) {
        setId(id);
        setPaciente(paciente);
        setMedico(medico);
        setFechaHora(fechaHora);
        setEstado(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del turno debe ser mayor a cero.");
        }
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo.");
        }
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El médico no puede ser nulo.");
        }
        this.medico = medico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no puede ser nula.");
        }
        this.fechaHora = fechaHora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del turno no puede ser nulo.");
        }
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "[" + estado + "] " + paciente.getNombre() + " " + paciente.getApellido()
                + " - Dr. " + medico.getNombre() + " " + medico.getApellido()
                + " (" + medico.getEspecialidad() + ") - " + fechaHora;
    }

    @Override
    public boolean equals(Object otroObjeto) {
        if (this == otroObjeto) {
            return true;
        }
        if (!(otroObjeto instanceof Turno)) {
            return false;
        }
        Turno otroTurno = (Turno) otroObjeto;
        return Objects.equals(medico, otroTurno.medico) && Objects.equals(fechaHora, otroTurno.fechaHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medico, fechaHora);
    }
}
