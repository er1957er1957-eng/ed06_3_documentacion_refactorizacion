package org.ed06.model;

import java.time.LocalDate;

public class Reserva {
    public static final double APLICAR_DESCUENTO_VIP = 0.9;
    public static final int DIAS_PARA_DESCUENTO = 7;
    public static final double APLICAR_DESCUENTO_DIAS = 0.95;
    private int id;
    private Habitacion habitacion;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precioTotal;

    public Reserva(int id, Habitacion habitacion, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this.id = id;
        this.habitacion = habitacion;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioTotal = calcularPrecioFinal();
    }

    public int getId() {
        return id;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public double calcularPrecioFinal() {
        int numeroDiasReservados = fechaFin.getDayOfYear() - fechaInicio.getDayOfYear();
        double precioFinal = habitacion.getPrecioBase() * numeroDiasReservados;

        if (cliente.esVip) {
            precioFinal *= APLICAR_DESCUENTO_VIP;
        }

        if (numeroDiasReservados > DIAS_PARA_DESCUENTO) {
            precioFinal *= APLICAR_DESCUENTO_DIAS;
        }

        return precioFinal;
    }


}
