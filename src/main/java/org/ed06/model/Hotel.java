package org.ed06.model;

import java.time.LocalDate;
import java.util.*;

public class Hotel {
    private String nombre;
    private String direccion;
    private String telefono;

    private final Map<Integer, Cliente> clientes = new HashMap<>();
    private final List<Habitacion> habitaciones = new ArrayList<>();
    private final Map<Integer, List<Reserva>> reservasPorHabitacion = new HashMap<>();

    public Hotel(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Método para agregar una nueva habitación al hotel
    public void registrarHabitacion(String tipo, double precioBase) {
        Habitacion habitacion = new Habitacion(habitaciones.size() + 1, tipo, precioBase);
        habitaciones.add(habitacion);
        reservasPorHabitacion.put(habitacion.getNumero(), new ArrayList<>());
    }

    public void listarHabitacionesDisponibles() {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.isDisponible()) {
                System.out.println("Habitación #" + habitacion.getNumero() + " - Tipo: " + habitacion.getTipo() + " - Precio base: " + habitacion.getPrecioBase());
            }
        }
    }

    public Habitacion getHabitacion(int numero) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getNumero() == numero) {
                return habitacion;
            }
        }
        return null;
    }

    //Método para realizar una reserva.
    public int reservarHabitacion(int clienteId, String tipo, LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (habitaciones.isEmpty()) {
            System.out.println("No hay habitaciones en el hotel");
            return -4;
        } else {
            if (this.clientes.get(clienteId) == null) {
                System.out.println("No existe el cliente con id " + clienteId);
                return -3;
            } else {
                Cliente cliente = this.clientes.get(clienteId);
                if (!fechaEntrada.isBefore(fechaSalida)) {
                    System.out.println("La fecha de entrada es posterior a la fecha de salida");
                    return -2;
                } else {
                    Integer habitacion = buscadorHabitacion(tipo, fechaEntrada, fechaSalida, cliente);
                    if (habitacion != null) return habitacion;

                    System.out.println("No hay habitaciones disponibles del tipo " + tipo);
                    return -1;
                }
            }
        }
    }

    private Integer buscadorHabitacion(String tipo, LocalDate fechaEntrada, LocalDate fechaSalida, Cliente cliente) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getTipo().equals(tipo.toUpperCase()) && habitacion.isDisponible()) {
                int numReservas = conseguirNumReservas(cliente);
                clienteEsVip(numReservas, cliente);
                crearReserva(fechaEntrada, fechaSalida, habitacion, cliente);
                return habitacion.getNumero();
            }
        }
        return null;
    }

    private int conseguirNumReservas(Cliente cliente) {
        int numReservas = 0;
        for (List<Reserva> reservasHabitacion : reservasPorHabitacion.values()) {
            for (Reserva reservaCliente : reservasHabitacion) {
                if (reservaCliente.getCliente().equals(cliente)) {
                    if (reservaCliente.getFechaInicio().isAfter(LocalDate.now().minusYears(1))) {
                        numReservas++;
                    }
                }
            }
        }
        return numReservas;
    }

    private void crearReserva(LocalDate fechaEntrada, LocalDate fechaSalida, Habitacion habitacion, Cliente cliente) {
        Reserva reserva = new Reserva(reservasPorHabitacion.size() + 1, habitacion, cliente, fechaEntrada, fechaSalida);
        reservasPorHabitacion.get(habitacion.getNumero()).add(reserva);
        habitacion.reservar();

        System.out.println("Reserva realizada con éxito");
    }

    private static void clienteEsVip(int numReservas, Cliente cliente) {
        if (numReservas > 3 && !cliente.isEsVip()) {
            cliente.setEsVip(true);
            System.out.println("El cliente " + cliente.getNombre() + " ha pasado a ser VIP");
        }
    }

    public void listarReservas() {
        reservasPorHabitacion.forEach((key, value) -> {
            System.out.println("Habitación #" + key);
            value.forEach(reserva -> System.out.println(
                    "Reserva #" + reserva.getId() + " - Cliente: " + reserva.getCliente().nombre
                            + " - Fecha de entrada: " + reserva.getFechaInicio()
                            + " - Fecha de salida: " + reserva.getFechaFin()));
        });
    }

    public void listarClientes() {
        for (Cliente cliente : clientes.values()) {
            System.out.println("Cliente #" + cliente.getId() + " - Nombre: " + cliente.getNombre() + " - DNI: " + cliente.getDni() + " - VIP: " + cliente.isEsVip());
        }
    }

    public void registrarCliente(String nombre, String email, String dni, boolean esVip) {
        Cliente cliente = new Cliente(clientes.size() + 1, nombre, dni, email, esVip);
        clientes.put(cliente.id, cliente);
    }
}
