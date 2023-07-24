package uy.edu.um.examen.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Queja {
    private String descripcion;
    private LocalDateTime fecha;
    private Zona zona;

    public Queja(String descripcion, LocalDateTime fecha, Zona zona) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.zona = zona;
    }

    public Queja() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queja queja = (Queja) o;
        return Objects.equals(fecha, queja.fecha) && Objects.equals(zona, queja.zona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, zona);
    }


}
