package uy.edu.um.examen.Entities;


import java.time.LocalDateTime;
import java.util.Objects;


import uy.edu.um.adt.hash.MyHash;
import uy.edu.um.adt.hash.MyHashImpl;

import uy.edu.um.examen.exceptions.InformacionInvalida;

public class Sensor {
    private Integer id;
    private Zona zona;
    private MyHash<LocalDateTime,Medicion> mediciones;

    public Sensor() {
        this.mediciones = new MyHashImpl<>();
    }

    public Sensor(int id, Zona zona) {
        this.id = id;
        this.zona = zona;
        this.mediciones = new MyHashImpl<>();
    }

    public int getId() {
        return id;
    }


    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MyHash<LocalDateTime, Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(MyHash<LocalDateTime, Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return id == sensor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //AUXILIAR

    /**
     * Método agrega una nueva medicion al sensor. La agrega al hash.
     *
     * @param fecha fecha de la medicion
     * @param medicion la medicion como objeto Medicion
     *
     */
    public void agregarMedicion(LocalDateTime fecha,Medicion medicion) throws InformacionInvalida {
        if(medicion == null){
            throw new InformacionInvalida();
        }
        this.mediciones.put(fecha,medicion);
    }

}
