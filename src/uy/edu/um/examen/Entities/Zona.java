package uy.edu.um.examen.Entities;

import uy.edu.um.adt.binarytree.MySearchBinaryTree;
import uy.edu.um.adt.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.examen.exceptions.InformacionInvalida;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.Objects;

public class Zona implements Comparable<Zona>{
    private String nombre;
    private MySearchBinaryTree<ChronoLocalDate, Promedios> promedios;
    private int cantidadQuejas;
    private Promedios promediosTotalZona;

    public Zona() {
        this.promedios = new MySearchBinaryTreeImpl<>();
        this.cantidadQuejas = 0;
        this.promediosTotalZona = new Promedios();
    }

    public Zona(String nombre) {
        this.nombre = nombre;
        this.promedios = new MySearchBinaryTreeImpl<>();
        this.cantidadQuejas = 0;
        this.promediosTotalZona = new Promedios();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public MySearchBinaryTree<ChronoLocalDate, Promedios> getPromedios() {
        return promedios;
    }

    public void setPromedios(MySearchBinaryTree<ChronoLocalDate, Promedios> promedios) {
        this.promedios = promedios;
    }

    public int getCantidadQuejas() {
        return cantidadQuejas;
    }

    public void setCantidadQuejas(int cantidadQuejas) {
        this.cantidadQuejas = cantidadQuejas;
    }

    public Promedios getPromediosTotalZona() {
        return promediosTotalZona;
    }

    public void setPromediosTotalZona(Promedios promediosTotalZona) {
        this.promediosTotalZona = promediosTotalZona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zona zona = (Zona) o;
        return Objects.equals(nombre, zona.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }


    //METODOS AUXILIARES
    /**
     * Este metodo agrega una nueva medicion a la zona.
     * Suma al promedio de la fecha, y al promedio total.
     *
     * @param medicion Medicion como objeto medicion
     *
     */

    public void nuevaMedicion(Medicion medicion) throws InformacionInvalida {
        if(medicion == null){
            throw new InformacionInvalida();
        }

        LocalDate fecha = LocalDate.from(medicion.getFecha());
        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fecha.toEpochDay());

        if(promedios.contains(fechaC)) {
            Promedios promedioFecha = promedios.find(fechaC);

            promedioFecha.setCluoruros(promedioFecha.getCluoruros() + medicion.getNivelCloruros());
            promedioFecha.setFluoruros(promedioFecha.getFluoruros() + medicion.getNivelFluoruros());
            promedioFecha.aumentarCuenta();
        }else{
            Promedios promedioNuevo = new Promedios(medicion.getNivelFluoruros(), medicion.getNivelCloruros(), fechaC);
            this.promedios.add(fechaC,promedioNuevo);
        }


        //Promedio Total
        this.promediosTotalZona.setFluoruros(promediosTotalZona.getFluoruros()+ medicion.getNivelFluoruros());
        this.promediosTotalZona.setCluoruros(promediosTotalZona.getCluoruros() + medicion.getNivelCloruros());
        this.promediosTotalZona.aumentarCuenta();

    }

    /**
     * Devuelve el Cluoruro Promedio para una fecha dada.
     *
     * @param fecha fecha que se quiere saber el promedio.
     */
    public double PromedioCluoruroFecha(LocalDateTime fecha) throws InformacionInvalida {
        if(fecha == null){
            throw new InformacionInvalida();
        }
        LocalDate fechaL = LocalDate.from(fecha);
        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fechaL.toEpochDay());

        if(promedios.contains(fechaC)){
            Promedios promedio = promedios.find(fechaC);
            return (promedio.getCluoruros()/promedio.getCuenta());
        }else{
            return 0;
        }

    }

    /**
     * Devuelve el Flluoruros Promedio para una fecha dada.
     *
     * @param fecha fecha que se quiere saber el promedio.
     */
    public double PromedioFluorurosFecha(LocalDateTime fecha) throws InformacionInvalida {
        if(fecha == null){
            throw new InformacionInvalida();
        }
        LocalDate fechaL = LocalDate.from(fecha);
        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fechaL.toEpochDay());

        if(promedios.contains(fechaC)){
            Promedios promedio = promedios.find(fechaC);
            return (promedio.getFluoruros()/promedio.getCuenta());
        }else{
            return 0;
        }
    }

    /**
     * Suma una queja a la cantidad de quejas por zona.
     *
     */
    public void sumarQueja(){
        this.cantidadQuejas +=1;
    }


    //Compare to de clorudos.
    @Override
    public int compareTo(Zona o) {
        if (this.promediosTotalZona.getCluoruros() < o.promediosTotalZona.getCluoruros()) {
            return -1; //Menor
        } else if (this.promediosTotalZona.getCluoruros() > o.promediosTotalZona.getCluoruros()) {
            return 1; //Mayor
        } else {
            return 0; //Igual
        }
    }
}
