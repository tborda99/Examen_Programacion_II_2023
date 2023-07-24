package uy.edu.um.examen;

import uy.edu.um.adt.hash.MyHash;
import uy.edu.um.adt.hash.MyHashImpl;
import uy.edu.um.adt.heap.MyHeap;
import uy.edu.um.adt.heap.MyHeapImpl;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;
import uy.edu.um.adt.queue.EmptyQueueException;
import uy.edu.um.adt.queue.MyQueue;
import uy.edu.um.examen.Entities.Medicion;
import uy.edu.um.examen.Entities.Queja;
import uy.edu.um.examen.Entities.Sensor;
import uy.edu.um.examen.Entities.Zona;
import uy.edu.um.examen.exceptions.EntidadNoExiste;
import uy.edu.um.examen.exceptions.InformacionInvalida;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;

public class ControladorProveedor {

    public static final double UMBRAL_CLORUROS = 5.0;
    public static final double UMBRAL_FLORUROS = 4.5;

    private MyQueue<Queja> quejas = new MyLinkedListImpl<>();
    private MyHash<Integer, Sensor> sensores = new MyHashImpl<>();
    private MyHash<String, Zona> zonas = new MyHashImpl<>();

    /**
     * Método para registrar una nueva medición.
     * Si no existe el medidor lo crea. (Utiliza buscarOCrearSensor())
     * Si no existe la zona la crea. (Utiliza buscarOCrearZona())
     *
     * @param idSensor integer que identifica al sensor.
     * @param nombreZona string del nombre de la zona
     * @param nivelCluoruros double de los cloruros medidos en esa fecha
     * @param nivelFluoruros double de los fluoruros medidos en esa fecha
     * @param fecha LocalDateTime fecha donde se realizo la medición
     */
    public void registrarMedicion(Integer idSensor, String nombreZona, double nivelCluoruros, double nivelFluoruros, LocalDateTime fecha) throws InformacionInvalida {
        if(idSensor == null || nombreZona == null || fecha == null){
            throw new InformacionInvalida();
        }

        if(nivelFluoruros<0 || nivelCluoruros<0){
            throw new InformacionInvalida();
        }

        nombreZona = nombreZona.toLowerCase().trim();


        //Busco o creo las zona y sensor
        Zona zona = buscarOCrearZona(nombreZona);
        Sensor sensor = buscarOCrearSensor(idSensor, zona);

        Medicion medicionNueva = new Medicion(fecha,nivelFluoruros,nivelCluoruros);
        sensor.agregarMedicion(fecha,medicionNueva);

        //Promedios
        zona.nuevaMedicion(medicionNueva);

    }
    /**
     * Método para registrar una nueva queja.
     *
     * @param nombreZonaAfectada Zona donde se realiza la queja
     * @param descripcionQueja descripcion brindada por el individio.
     * @param fecha fecha y hora cuando se realizo la queja
     *
     */
    public void registrarQueja(String nombreZonaAfectada, String descripcionQueja, LocalDateTime fecha) throws InformacionInvalida, EntidadNoExiste {
        if(nombreZonaAfectada == null || descripcionQueja == null || fecha == null){
            throw new InformacionInvalida();
        }

        //Strings a minuscula
        nombreZonaAfectada = nombreZonaAfectada.toLowerCase().trim();
        descripcionQueja = descripcionQueja.toLowerCase().trim();

        if(zonas.contains(nombreZonaAfectada)){
            Zona zonaAfectada = buscarOCrearZona(nombreZonaAfectada);
            zonaAfectada.sumarQueja();
            Queja queja = new Queja(descripcionQueja,fecha,zonaAfectada);
            quejas.enqueue(queja);

        }else{
            throw new EntidadNoExiste();
        }


    }

    /**
     * Metodo para procesa una Queja.
     *
     * @return devuelve true si la zona asociada a la queja tiene promedio de niveles de Flurouro o Cloruro
     * más grandes que los nivels del umbral
     */

    public boolean procesarQueja() throws EntidadNoExiste, EmptyQueueException, InformacionInvalida {
        if(quejas.size() == 0){
            throw new EntidadNoExiste();
        }

        Queja queja = quejas.dequeue();

        LocalDateTime fecha =  queja.getFecha();
        Zona zona = queja.getZona();

        double ClPromedio = zona.PromedioCluoruroFecha(fecha);
        double FlPromedio = zona.PromedioFluorurosFecha(fecha);

        if(ClPromedio > UMBRAL_CLORUROS){
            return true;
        }else if (FlPromedio > UMBRAL_FLORUROS) {
            return true;
        }else{
            return false;
        }

    }

    /**
     * Método genera un informe de estadisticas detallando cada barrio y su estadisticas.
     * Estas incluyen: promedio cloruros, promedio fluoruros, cantidad de quejas(Historicas)
     *
     * @return informe en formato String. Ordenado Alfabeticamente
     */
    public String generarInformeEstadisticas(){
        StringBuilder reporte = new StringBuilder();

        reporte.append("REPORTE POR ZONAS");
        reporte.append("\n");


        MyList<Zona> zonasList = zonas.values();

        //Ordendo alfabeticamente
        MyList<Zona> sortedZonas = new MyLinkedListImpl<>();
        while (zonasList.size() > 0) {
            int indiceMasChico = 0;
            for (int i = 1; i < zonasList.size(); i++) {
                if (zonasList.get(i).getNombre().compareTo(zonasList.get(indiceMasChico).getNombre()) < 0) {
                    indiceMasChico = i;
                }
            }
            Zona zonaMasChica = zonasList.get(indiceMasChico);
            sortedZonas.add(zonaMasChica);
            zonasList.remove(zonaMasChica);
        }

        //Recorro todas las zonas agregando al reporte
        for (int i = 0; i < sortedZonas.size(); i++) {
            Zona zonaAux = sortedZonas.get(i);
            reporte.append("\n");
            reporte.append(zonaAux.getNombre().toUpperCase()+": ");
            reporte.append("\n");
            reporte.append("Promedio de Cloruros: " + zonaAux.getPromediosTotalZona().getCluoruros()/zonaAux.getPromediosTotalZona().getCuenta() + "mg/L");
            reporte.append("\n");
            reporte.append("Promedio de Floruros: " + zonaAux.getPromediosTotalZona().getFluoruros()/zonaAux.getPromediosTotalZona().getCuenta() + "mg/L");
            reporte.append("\n");
            reporte.append("Cantidad de Quejas: " + zonaAux.getCantidadQuejas());
            reporte.append("\n");

        }

        return reporte.toString();
    }

    /**
     * Método devuelve las tres zonas con mayores cloruros para las fechas
     * contenidas entre fecha inicial y fecha final
     *
     * @param fechaInicial apartir de cuando
     * @param fechaFinal hasta cuando
     * @return MyList<String> conteniendo 3 nombres de zonas
     */
    public MyList<String> obtenerTop3ZonasCloruros(LocalDateTime fechaInicial, LocalDateTime fechaFinal) throws InformacionInvalida {
        MyHash<String, Zona> zonasRanking = new MyHashImpl<>();

        MyList<Zona> zonasTotalList = zonas.values();
        MyList<ChronoLocalDate> fechas = diasEntreFechas(fechaInicial,fechaFinal);

        for (int i = 0; i < zonasTotalList.size(); i++) {
            Zona aux = zonasTotalList.get(i);
            Zona auxNueva = new Zona(aux.getNombre());
            for (int j = 0; j < fechas.size(); j++) {
                if(aux.getPromedios().contains(fechas.get(j))) {

                    //Este desastre es porque utilizo distintos tipos de fechas.
                    ChronoLocalDate fechaC = fechas.get(j);
                    LocalDate fechaLD =  LocalDate.from(fechas.get(j));
                    LocalDateTime fechaLDT = fechaLD.atStartOfDay();

                    Medicion medicionNueva = new Medicion(fechaLDT,aux.PromedioFluorurosFecha(fechaLDT),aux.PromedioFluorurosFecha(fechaLDT));

                    auxNueva.nuevaMedicion(medicionNueva);
                    if(zonasRanking.contains(aux.getNombre())){
                        //Ya existe
                        zonasRanking.get(aux.getNombre()).nuevaMedicion(medicionNueva);
                    }else{
                        //La creo
                        zonasRanking.put(auxNueva.getNombre(),auxNueva);
                    }

                }

            }

        }

        //HASTA ESTE PUNTO TENGO UN HASH CON LAS ZONAS QUE TUVIERON MEDICIONES EN ESAS FECHAS
        //LOS PROMEDIOS DE ESAS ZONAS SON LOS PROMEDIOS DE SOLO LAS FECHAS

        //HEAP SORT POR CLORUROS
        MyList<Zona> zonasNoOrdenadas = zonasRanking.values();
        MyHeap<Zona> zonasHeap = new MyHeapImpl<>(false); // False: Mayor a menor, True: Menor a Mayo
        MyList<Zona> zonasOrdenadas = new MyLinkedListImpl<>();

        for (int i = 0; i < zonasNoOrdenadas.size(); i++) {
            zonasHeap.insert(zonasNoOrdenadas.get(i));
        }

        int sizeHeap = zonasHeap.size(); //Va ir cambiando mejor guardarlo como variable
        int count = 1;
        for (int i = 0; i < sizeHeap; i++) {
            if(count <= 3) { //Solo quiero top 3
                zonasOrdenadas.add(zonasHeap.delete());
                count++;
            }else{
                break;
            }
        }

        //Hay que retornar Strings por letra

        MyList<String> stringTopZonas = new MyLinkedListImpl<>();
        for (int i = 0; i < zonasOrdenadas.size(); i++) {
            stringTopZonas.add(zonasOrdenadas.get(i).getNombre());
        }

        return stringTopZonas;
    }

    //AUXILIARES

    /**
     * Este método crea o busca un sensor.
     *
     * @param idSensor integer identificador del sensor
     * @param zona nombre de la zona
     * @return Sensor buscada o recientemente creada
     */
    public Sensor buscarOCrearSensor(Integer idSensor, Zona zona) throws InformacionInvalida {
        if(idSensor == null){
            throw new InformacionInvalida();
        }
        if(sensores.contains(idSensor)){
            return sensores.get(idSensor);
        }else{
            Sensor sensorNuevo = new Sensor(idSensor,zona);
            sensores.put(idSensor,sensorNuevo);
            return sensorNuevo;
        }
    }

    /**
     * Este método crea o busca una zona.
     *
     * @param nombreZona nombre de la zona
     * @return Zona buscada o recientemente creada
     */
    public Zona buscarOCrearZona(String nombreZona) throws InformacionInvalida {
        if(nombreZona == null){
            throw new InformacionInvalida();
        }

        //Nombres a minusuculas siempre en la base.
        nombreZona =nombreZona.toLowerCase().trim();

        if(zonas.contains(nombreZona)){
            return zonas.get(nombreZona);
        }else{
            Zona zonaNueva = new Zona(nombreZona);
            zonas.put(nombreZona,zonaNueva);
            return zonaNueva;
        }
    }

    /**
     * Este método devuelve una lista de días en formato ChronolocalDate entre dos.
     * Elegi ChronolocalDate porque orginalmente planeaba armar un BST que necesitaba comparable.
     * @param fechaInicial LocalDateTime fecha desde
     * @param fechaFinal LocalDateTime fecha hasta
     * @return MyList<ChronoLocalDate> Lista de dias entre ambas fechas formato Chronolocaldate.
     */
    public MyList<ChronoLocalDate> diasEntreFechas(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        MyList<ChronoLocalDate> dates = new MyLinkedListImpl<>();

        LocalDate inicio = fechaInicial.toLocalDate();
        LocalDate fin = fechaFinal.toLocalDate();

        while (!inicio.isAfter(fin)) {
            dates.add(inicio);  // Add the current date to the list
            inicio = inicio.plus(1, ChronoUnit.DAYS);  // Move to the next day
        }

        return dates;
    }


    //GETTER PARA TESTING

    public MyQueue<Queja> getQuejas() {
        return quejas;
    }

    public void setQuejas(MyQueue<Queja> quejas) {
        this.quejas = quejas;
    }

    public MyHash<Integer, Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(MyHash<Integer, Sensor> sensores) {
        this.sensores = sensores;
    }

    public MyHash<String, Zona> getZonas() {
        return zonas;
    }

    public void setZonas(MyHash<String, Zona> zonas) {
        this.zonas = zonas;
    }
}
