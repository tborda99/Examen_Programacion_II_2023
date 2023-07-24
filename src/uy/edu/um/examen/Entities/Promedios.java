package uy.edu.um.examen.Entities;

import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

public class Promedios {

    private int cuenta;
    private double fluoruros;
    private double cluoruros;
    private ChronoLocalDate fecha;

    public Promedios() {
        this.cuenta = 0;
    }

    public Promedios(double fluoruros, double cluoruros, ChronoLocalDate fecha) {
        this.fluoruros = fluoruros;
        this.cluoruros = cluoruros;
        this.fecha = fecha;
        this.cuenta = 1;
    }

    public double getFluoruros() {
        return fluoruros;
    }

    public void setFluoruros(double fluoruros) {
        this.fluoruros = fluoruros;
    }

    public double getCluoruros() {
        return cluoruros;
    }

    public void setCluoruros(double cluoruros) {
        this.cluoruros = cluoruros;
    }

    public ChronoLocalDate getFecha() {
        return fecha;
    }

    public void setFecha(ChronoLocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCuenta() {
        return cuenta;
    }

    public void setCuenta(int cuenta) {
        this.cuenta = cuenta;
    }

    public void aumentarCuenta(){
        this.cuenta +=1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promedios promedios = (Promedios) o;
        return Objects.equals(fecha, promedios.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha);
    }
}
