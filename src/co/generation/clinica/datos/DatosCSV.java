package co.generation.clinica.datos;

import co.generation.clinica.model.*;
import co.generation.clinica.service.ClinicaService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatosCSV {
    // clase DatosCSV
    private static final String CARPETA_DATOS = "datos/";
    private static final String ARCHIVO_PACIENTES = CARPETA_DATOS + "pacientes.csv";
    private static final String ARCHIVO_MEDICOS = CARPETA_DATOS + "medicos.csv";
    private static final String ARCHIVO_TURNOS = CARPETA_DATOS + "turnos.csv";
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void cargar(ClinicaService servicio) {
        new File(CARPETA_DATOS).mkdirs();
        cargarPacientes(servicio);
        cargarMedicos(servicio);
        cargarTurnos(servicio);
    }

    private static void cargarPacientes(ClinicaService servicio) {
        File archivoPacientes = new File(ARCHIVO_PACIENTES);
        if (!archivoPacientes.exists()) return;
        try (BufferedReader lectorLineas = new BufferedReader(new FileReader(archivoPacientes))) {
            String linea;
            while ((linea = lectorLineas.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = linea.split(",", -1);
                servicio.getPacientes().add(new Paciente(
                        Integer.parseInt(campos[0].trim()),
                        campos[1].trim(), campos[2].trim(), campos[3].trim(), campos[4].trim()));
            }
        } catch (IOException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static void cargarMedicos(ClinicaService servicio) {
        File archivoMedicos = new File(ARCHIVO_MEDICOS);
        if (!archivoMedicos.exists()) return;
        try (BufferedReader lectorLineas = new BufferedReader(new FileReader(archivoMedicos))) {
            String linea;
            while ((linea = lectorLineas.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = linea.split(",", -1);
                servicio.getMedicos().add(new Medico(
                        Integer.parseInt(campos[0].trim()), campos[1].trim(), campos[2].trim(),
                        Especialidad.valueOf(campos[3].trim())));
            }
        } catch (IOException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static void cargarTurnos(ClinicaService servicio) {
        File archivoTurnos = new File(ARCHIVO_TURNOS);
        if (!archivoTurnos.exists()) return;
        try (BufferedReader lectorLineas = new BufferedReader(new FileReader(archivoTurnos))) {
            String linea;
            while ((linea = lectorLineas.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = linea.split(",", -1);
                Paciente paciente = servicio.buscarPorCedula(campos[1].trim());
                Medico medico = servicio.buscarPorNombreApellido(campos[2].trim(), campos[3].trim());
                if (paciente == null || medico == null) continue;
                servicio.getTurnos().add(new Turno(
                        Integer.parseInt(campos[0].trim()), paciente, medico,
                        LocalDateTime.parse(campos[4].trim(), FORMATO_FECHA_HORA),
                        EstadoTurno.valueOf(campos[5].trim())));
            }
        } catch (IOException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    public static void guardar(ClinicaService servicio) {
        new File(CARPETA_DATOS).mkdirs();
        guardarPacientes(servicio.getPacientes());
        guardarMedicos(servicio.getMedicos());
        guardarTurnos(servicio.getTurnos());
    }

    private static void guardarPacientes(List<Paciente> listaPacientes) {
        try (PrintWriter escritorArchivo = new PrintWriter(new FileWriter(ARCHIVO_PACIENTES))) {
            for (Paciente paciente : listaPacientes) {
                escritorArchivo.println(paciente.getId() + "," + paciente.getCedula() + "," +
                        paciente.getNombre() + "," + paciente.getApellido() + "," + paciente.getTelefono());
            }
        } catch (IOException excepcion) {
            System.out.println("Error al guardar pacientes: " + excepcion.getMessage());
        }
    }

    private static void guardarMedicos(List<Medico> listaMedicos) {
        try (PrintWriter escritorArchivo = new PrintWriter(new FileWriter(ARCHIVO_MEDICOS))) {
            for (Medico medico : listaMedicos) {
                escritorArchivo.println(medico.getId() + "," + medico.getNombre() + "," +
                        medico.getApellido() + "," + medico.getEspecialidad());
            }
        } catch (IOException excepcion) {
            System.out.println("Error al guardar médicos: " + excepcion.getMessage());
        }
    }

    private static void guardarTurnos(List<Turno> listaTurnos) {
        try (PrintWriter escritorArchivo = new PrintWriter(new FileWriter(ARCHIVO_TURNOS))) {
            for (Turno turno : listaTurnos) {
                escritorArchivo.println(turno.getId() + "," +
                        turno.getPaciente().getCedula() + "," +
                        turno.getMedico().getNombre() + "," +
                        turno.getMedico().getApellido() + "," +
                        turno.getFechaHora().format(FORMATO_FECHA_HORA) + "," +
                        turno.getEstado());
            }
        } catch (IOException excepcion) {
            System.out.println("Error al guardar turnos: " + excepcion.getMessage());
        }
    }
}
