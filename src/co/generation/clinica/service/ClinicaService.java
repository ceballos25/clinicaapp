package co.generation.clinica.service;

import co.generation.clinica.interfaces.Consultable;
import co.generation.clinica.model.EstadoTurno;
import co.generation.clinica.model.Medico;
import co.generation.clinica.model.Paciente;
import co.generation.clinica.model.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClinicaService implements Consultable {
    // clase ClinicaService
    private final List<Paciente> pacientes = new ArrayList<>();
    private final List<Medico> medicos = new ArrayList<>();
    private final List<Turno> turnos = new ArrayList<>();

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void registrarPaciente(Paciente paciente) {
        if (paciente == null || !paciente.esValido()) {
            System.out.println("Paciente inválido. Verifica los datos ingresados.");
            return;
        }
        if (pacientes.contains(paciente)) {
            System.out.println("Ya existe un paciente con esa cédula.");
            return;
        }
        paciente.setId(siguienteIdPaciente());
        pacientes.add(paciente);
        System.out.println("Paciente registrado: " + paciente.getDatosRegistro());
    }

    public Paciente buscarPorCedula(String cedula) {
        for (Paciente paciente : pacientes) {
            if (paciente.getCedula().equals(cedula)) {
                return paciente;
            }
        }
        return null;
    }

    public void listarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        List<Paciente> listaPacientesOrdenada = new ArrayList<>(pacientes);
        listaPacientesOrdenada.sort((pacienteUno, pacienteDos) -> {
            int compararApellidos = pacienteUno.getApellido().compareTo(pacienteDos.getApellido());
            if (compararApellidos != 0) {
                return compararApellidos;
            }
            int compararNombres = pacienteUno.getNombre().compareTo(pacienteDos.getNombre());
            return compararNombres;
        });
        for (Paciente paciente : listaPacientesOrdenada) {
            System.out.println(paciente);
        }
    }

    public void registrarMedico(Medico medico) {
        if (medico == null || !medico.esValido()) {
            System.out.println("Médico inválido. Verifica los datos ingresados.");
            return;
        }
        if (medicos.contains(medico)) {
            System.out.println("Ya existe un médico con ese nombre y apellido.");
            return;
        }
        medico.setId(siguienteIdMedico());
        medicos.add(medico);
        System.out.println("Médico registrado: " + medico.getDatosRegistro());
    }

    public Medico buscarPorNombreApellido(String nombre, String apellido) {
        for (Medico medico : medicos) {
            if (medico.getNombre().equalsIgnoreCase(nombre)
                    && medico.getApellido().equalsIgnoreCase(apellido)) {
                return medico;
            }
        }
        return null;
    }

    public void listarMedicos() {
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos registrados.");
            return;
        }
        List<Medico> listaMedicosOrdenada = new ArrayList<>(medicos);
        listaMedicosOrdenada.sort((medicoUno, medicoDos) -> {
            int compararEspecialidad = medicoUno.getEspecialidad().compareTo(medicoDos.getEspecialidad());
            if (compararEspecialidad != 0) {
                return compararEspecialidad;
            }
            int compararApellidos = medicoUno.getApellido().compareTo(medicoDos.getApellido());
            return compararApellidos;
        });
        for (Medico medico : listaMedicosOrdenada) {
            System.out.println(medico);
        }
    }

    public void asignarTurno(Turno turno) {
        if (turno == null) {
            System.out.println("Turno inválido.");
            return;
        }
        Paciente paciente = buscarPorCedula(turno.getPaciente().getCedula());
        if (paciente == null) {
            System.out.println("El paciente no existe.");
            return;
        }
        Medico medico = buscarPorNombreApellido(turno.getMedico().getNombre(), turno.getMedico().getApellido());
        if (medico == null) {
            System.out.println("El médico no existe.");
            return;
        }
        Turno turnoParaCompararConflicto = new Turno(paciente, medico, turno.getFechaHora());
        if (turnos.contains(turnoParaCompararConflicto)) {
            System.out.println("Conflicto de agenda: el médico ya tiene un turno en esa fecha y hora.");
            return;
        }
        turno.setId(siguienteIdTurno());
        turnos.add(turno);
        System.out.println("Turno asignado: " + turno);
    }

    public void cancelarTurno(int idTurno) {
        Turno turno = buscarTurnoPorId(idTurno);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }
        if (turno.getEstado() == EstadoTurno.ATENDIDO || turno.getEstado() == EstadoTurno.CANCELADO) {
            System.out.println("No se puede cancelar un turno en estado " + turno.getEstado() + ".");
            return;
        }
        turno.setEstado(EstadoTurno.CANCELADO);
        System.out.println("Turno cancelado correctamente.");
    }

    public void cambiarEstadoTurno(int idTurno, EstadoTurno nuevoEstado) {
        Turno turno = buscarTurnoPorId(idTurno);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }
        turno.setEstado(nuevoEstado);
        System.out.println("Estado del turno actualizado a: " + nuevoEstado);
    }

    @Override
    public List<Turno> listarTurnosDelDia(LocalDate fecha) {
        List<Turno> listaTurnos = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.getFechaHora().toLocalDate().equals(fecha)) {
                listaTurnos.add(turno);
            }
        }
        listaTurnos.sort((turnoUno, turnoDos) -> {
            int compararFechaHora = turnoUno.getFechaHora().compareTo(turnoDos.getFechaHora());
            return compararFechaHora;
        });
        return listaTurnos;
    }

    @Override
    public List<Turno> buscarPorMedico(Medico medico) {
        List<Turno> listaTurnos = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.getMedico().equals(medico)) {
                listaTurnos.add(turno);
            }
        }
        return listaTurnos;
    }

    @Override
    public List<Turno> buscarPorPaciente(Paciente paciente) {
        List<Turno> listaTurnos = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.getPaciente().equals(paciente)) {
                listaTurnos.add(turno);
            }
        }
        return listaTurnos;
    }

    private Turno buscarTurnoPorId(int idTurno) {
        for (Turno turno : turnos) {
            if (turno.getId() == idTurno) {
                return turno;
            }
        }
        return null;
    }

    private int siguienteIdPaciente() {
        int idMaximo = 0;
        for (Paciente paciente : pacientes) {
            if (paciente.getId() > idMaximo) {
                idMaximo = paciente.getId();
            }
        }
        return idMaximo + 1;
    }

    private int siguienteIdMedico() {
        int idMaximo = 0;
        for (Medico medico : medicos) {
            if (medico.getId() > idMaximo) {
                idMaximo = medico.getId();
            }
        }
        return idMaximo + 1;
    }

    private int siguienteIdTurno() {
        int idMaximo = 0;
        for (Turno turno : turnos) {
            if (turno.getId() > idMaximo) {
                idMaximo = turno.getId();
            }
        }
        return idMaximo + 1;
    }
}
