package uy.edu.um.examen.Entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Medicion{
    private LocalDateTime fecha;
    private double nivelFluoruros;
    private double nivelCloruros;

    public Medicion() {
    }

    public Medicion(LocalDateTime fecha, double nivelFluoruros, double nivelCloruros) {
        this.fecha = fecha;
        this.nivelFluoruros = nivelFluoruros;
        this.nivelCloruros = nivelCloruros;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getNivelFluoruros() {
        return nivelFluoruros;
    }

    public void setNivelFluoruros(double nivelFluoruros) {
        this.nivelFluoruros = nivelFluoruros;
    }

    public double getNivelCloruros() {
        return nivelCloruros;
    }

    public void setNivelCloruros(double nivelCloruros) {
        this.nivelCloruros = nivelCloruros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicion medicion = (Medicion) o;
        return Objects.equals(fecha, medicion.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha);
    }

}
