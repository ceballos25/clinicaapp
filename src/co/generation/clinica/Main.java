package co.generation.clinica;

import co.generation.clinica.datos.DatosCSV;
import co.generation.clinica.model.Especialidad;
import co.generation.clinica.model.EstadoTurno;
import co.generation.clinica.model.Medico;
import co.generation.clinica.model.Paciente;
import co.generation.clinica.model.Turno;
import co.generation.clinica.service.ClinicaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    // clase Main
    public static void main(String[] args) {
        ClinicaService servicio = new ClinicaService();
        DatosCSV.cargar(servicio);

        Scanner lectorConsola = new Scanner(System.in);
        int opcionMenu;

        do {
            mostrarMenu();
            System.out.print("Selecciona una opción: ");
            opcionMenu = leerEntero(lectorConsola);

            switch (opcionMenu) {
                case 1:
                    registrarPaciente(lectorConsola, servicio);
                    break;
                case 2:
                    registrarMedico(lectorConsola, servicio);
                    break;
                case 3:
                    asignarTurno(lectorConsola, servicio);
                    break;
                case 4:
                    listarTurnosDelDia(lectorConsola, servicio);
                    break;
                case 5:
                    cancelarTurno(lectorConsola, servicio);
                    break;
                case 6:
                    verTurnosPorMedico(lectorConsola, servicio);
                    break;
                case 7:
                    verTurnosPorPaciente(lectorConsola, servicio);
                    break;
                case 8:
                    cambiarEstadoTurno(lectorConsola, servicio);
                    break;
                case 9:
                    servicio.listarPacientes();
                    break;
                case 10:
                    servicio.listarMedicos();
                    break;
                case 0:
                    DatosCSV.guardar(servicio);
                    System.out.println("Hasta pronto. Datos guardados.");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }

            System.out.println();
        } while (opcionMenu != 0);
    }

    private static void mostrarMenu() {
        System.out.println("****************************************");
        System.out.println(" CLINICAAPP - MENU");
        System.out.println("========================================");
        System.out.println(" 1. Registrar paciente");
        System.out.println(" 2. Registrar médico");
        System.out.println(" 3. Asignar turno");
        System.out.println(" 4. Listar turnos del día");
        System.out.println(" 5. Cancelar turno");
        System.out.println(" 6. Ver turnos por médico");
        System.out.println(" 7. Ver turnos por paciente");
        System.out.println(" 8. Cambiar estado de turno");
        System.out.println(" 9. Listar pacientes");
        System.out.println(" 10. Listar médicos");
        System.out.println(" 0. Salir");
        System.out.println("========================================");
    }

    private static void registrarPaciente(Scanner lectorConsola, ClinicaService servicio) {
        try {
            System.out.print("Cédula: ");
            String cedula = lectorConsola.nextLine();
            System.out.print("Nombre: ");
            String nombre = lectorConsola.nextLine();
            System.out.print("Apellido: ");
            String apellido = lectorConsola.nextLine();
            System.out.print("Teléfono: ");
            String telefono = lectorConsola.nextLine();

            Paciente pacienteNuevo = new Paciente(cedula, nombre, apellido, telefono);
            servicio.registrarPaciente(pacienteNuevo);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Error al registrar paciente: " + excepcion.getMessage());
        }
    }

    private static void registrarMedico(Scanner lectorConsola, ClinicaService servicio) {
        try {
            System.out.print("Nombre: ");
            String nombre = lectorConsola.nextLine();
            System.out.print("Apellido: ");
            String apellido = lectorConsola.nextLine();

            Especialidad especialidad = leerEspecialidad(lectorConsola);
            Medico medicoNuevo = new Medico(nombre, apellido, especialidad);
            servicio.registrarMedico(medicoNuevo);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Error al registrar médico: " + excepcion.getMessage());
        }
    }

    private static void asignarTurno(Scanner lectorConsola, ClinicaService servicio) {
        System.out.print("Cédula del paciente: ");
        String cedula = lectorConsola.nextLine();
        Paciente paciente = servicio.buscarPorCedula(cedula);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        System.out.print("Nombre del médico: ");
        String nombreMedico = lectorConsola.nextLine();
        System.out.print("Apellido del médico: ");
        String apellidoMedico = lectorConsola.nextLine();
        Medico medico = servicio.buscarPorNombreApellido(nombreMedico, apellidoMedico);
        if (medico == null) {
            System.out.println("Médico no encontrado.");
            return;
        }

        try {
            System.out.print("Año: ");
            int anio = leerEntero(lectorConsola);
            System.out.print("Mes: ");
            int mes = leerEntero(lectorConsola);
            System.out.print("Día: ");
            int dia = leerEntero(lectorConsola);
            System.out.print("Hora: ");
            int hora = leerEntero(lectorConsola);
            System.out.print("Minuto: ");
            int minuto = leerEntero(lectorConsola);

            LocalDateTime fechaHora = LocalDateTime.of(anio, mes, dia, hora, minuto);
            Turno turnoNuevo = new Turno(paciente, medico, fechaHora);
            servicio.asignarTurno(turnoNuevo);
        } catch (Exception excepcion) {
            System.out.println("Fecha/hora inválida: " + excepcion.getMessage());
        }
    }

    private static void listarTurnosDelDia(Scanner lectorConsola, ClinicaService servicio) {
        try {
            System.out.print("Año: ");
            int anio = leerEntero(lectorConsola);
            System.out.print("Mes: ");
            int mes = leerEntero(lectorConsola);
            System.out.print("Día: ");
            int dia = leerEntero(lectorConsola);

            LocalDate fecha = LocalDate.of(anio, mes, dia);
            List<Turno> turnosDelDia = servicio.listarTurnosDelDia(fecha);
            if (turnosDelDia.isEmpty()) {
                System.out.println("No hay turnos para esa fecha.");
                return;
            }
            for (Turno turno : turnosDelDia) {
                System.out.println(turno);
            }
        } catch (Exception excepcion) {
            System.out.println("Fecha inválida: " + excepcion.getMessage());
        }
    }

    private static void cancelarTurno(Scanner lectorConsola, ClinicaService servicio) {
        System.out.print("ID del turno a cancelar: ");
        int idTurno = leerEntero(lectorConsola);
        servicio.cancelarTurno(idTurno);
    }

    private static void verTurnosPorMedico(Scanner lectorConsola, ClinicaService servicio) {
        System.out.print("Nombre del médico: ");
        String nombre = lectorConsola.nextLine();
        System.out.print("Apellido del médico: ");
        String apellido = lectorConsola.nextLine();

        Medico medico = servicio.buscarPorNombreApellido(nombre, apellido);
        if (medico == null) {
            System.out.println("Médico no encontrado.");
            return;
        }

        List<Turno> turnosDelMedico = servicio.buscarPorMedico(medico);
        if (turnosDelMedico.isEmpty()) {
            System.out.println("No hay turnos para ese médico.");
            return;
        }
        for (Turno turno : turnosDelMedico) {
            System.out.println(turno);
        }
    }

    private static void verTurnosPorPaciente(Scanner lectorConsola, ClinicaService servicio) {
        System.out.print("Cédula del paciente: ");
        String cedula = lectorConsola.nextLine();
        Paciente paciente = servicio.buscarPorCedula(cedula);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        List<Turno> turnosDelPaciente = servicio.buscarPorPaciente(paciente);
        if (turnosDelPaciente.isEmpty()) {
            System.out.println("No hay turnos para ese paciente.");
            return;
        }
        for (Turno turno : turnosDelPaciente) {
            System.out.println(turno);
        }
    }

    private static void cambiarEstadoTurno(Scanner lectorConsola, ClinicaService servicio) {
        System.out.print("ID del turno: ");
        int idTurno = leerEntero(lectorConsola);
        EstadoTurno estadoNuevo = leerEstadoTurno(lectorConsola);
        servicio.cambiarEstadoTurno(idTurno, estadoNuevo);
    }

    private static Especialidad leerEspecialidad(Scanner lectorConsola) {
        System.out.println("Especialidades disponibles:");
        Especialidad[] especialidades = Especialidad.values();
        for (int indice = 0; indice < especialidades.length; indice++) {
            System.out.println((indice + 1) + ". " + especialidades[indice]);
        }
        System.out.print("Elige una opción: ");
        int numeroElegido = leerEntero(lectorConsola);
        if (numeroElegido < 1 || numeroElegido > especialidades.length) {
            throw new IllegalArgumentException("Especialidad inválida.");
        }
        return especialidades[numeroElegido - 1];
    }

    private static EstadoTurno leerEstadoTurno(Scanner lectorConsola) {
        System.out.println("Estados disponibles:");
        EstadoTurno[] estados = EstadoTurno.values();
        for (int indice = 0; indice < estados.length; indice++) {
            System.out.println((indice + 1) + ". " + estados[indice]);
        }
        System.out.print("Elige una opción: ");
        int numeroElegido = leerEntero(lectorConsola);
        if (numeroElegido < 1 || numeroElegido > estados.length) {
            throw new IllegalArgumentException("Estado inválido.");
        }
        return estados[numeroElegido - 1];
    }

    private static int leerEntero(Scanner lectorConsola) {
        while (true) {
            String textoIngresado = lectorConsola.nextLine();
            try {
                return Integer.parseInt(textoIngresado.trim());
            } catch (NumberFormatException excepcion) {
                System.out.print("Ingresa un número válido: ");
            }
        }
    }


}
