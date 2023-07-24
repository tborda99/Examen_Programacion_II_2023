package uy.edu.um.examen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uy.edu.um.adt.linkedlist.MyList;
import uy.edu.um.adt.queue.EmptyQueueException;
import uy.edu.um.examen.Entities.Queja;
import uy.edu.um.examen.exceptions.EntidadNoExiste;
import uy.edu.um.examen.exceptions.InformacionInvalida;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ControladorProveedorTest {

    private ControladorProveedor controlador;

    @BeforeEach
    void setUp(){
        controlador = new ControladorProveedor();
    }

    @Test
    void registrarMedicion() throws InformacionInvalida {
        controlador.registrarMedicion(1,"Malvin",12,12, LocalDateTime.now());

        assertEquals(controlador.getSensores().size(),1);
        assertEquals(controlador.getZonas().size(),1);
        assertEquals(controlador.getZonas().get("malvin").getNombre(),"malvin");

    }

    @Test
    void registrarQueja() throws InformacionInvalida, EntidadNoExiste, EmptyQueueException {
        controlador.registrarMedicion(1,"Malvin",12,12, LocalDateTime.now());
        controlador.registrarQueja("malvin","mi agua tiene mucha sal",LocalDateTime.now());

        assertEquals(controlador.getQuejas().size(),1);

        Queja queja = controlador.getQuejas().dequeue();
        assertEquals(queja.getZona().getNombre(),"malvin");
        assertEquals(queja.getDescripcion(),"mi agua tiene mucha sal");

    }

    @Test
    void procesarQueja() throws InformacionInvalida, EntidadNoExiste, EmptyQueueException {

        controlador.registrarMedicion(1,"Malvin",12,12, LocalDateTime.now());
        controlador.registrarMedicion(2,"pocitos",1,1, LocalDateTime.now());
        assertEquals(controlador.getQuejas().size(),0); //Size antes de agregar quejas igual a 0
        controlador.registrarQueja("malvin","mi agua tiene mucha sal",LocalDateTime.now());
        controlador.registrarQueja("pocitos","NO ME BANCO MAS LA SAL HERMANO",LocalDateTime.now());

        assertEquals(controlador.getQuejas().size(),2);
        assertTrue(controlador.procesarQueja()); //Espero que la primera exceda
        assertEquals(controlador.getQuejas().size(),1);
        assertFalse(controlador.procesarQueja()); //Espero que la segunda no.
        assertEquals(controlador.getQuejas().size(),0);

        //Chequear en el valor exacto, que deberia devolver False
        controlador.registrarMedicion(2,"Malvin",5.0,4.5, LocalDateTime.of(2023,12,12,0,0));
        controlador.registrarQueja("malvin","mi agua tiene mucha sal",LocalDateTime.of(2023,12,12,0,0));
        assertFalse(controlador.procesarQueja()); //No debe excede, ya que es el mismo valor.
    }

    @Test
    void generarInformeEstadisticas() throws InformacionInvalida, EntidadNoExiste {
        controlador.registrarMedicion(1,"Malvin",12,12, LocalDateTime.now());
        controlador.registrarMedicion(2,"pocitos",1,1, LocalDateTime.now());
        controlador.registrarQueja("malvin","mi agua tiene mucha sal",LocalDateTime.now());
        controlador.registrarQueja("pocitos","NO ME BANCO MAS LA SAL HERMANO",LocalDateTime.now());


        System.out.println(controlador.generarInformeEstadisticas());
        //El print es el test:
        //REPORTE POR ZONAS

        //MALVIN:
        //Promedio de Cloruros: 12.0mg/L
        //Promedio de Floruros: 12.0mg/L
        //Cantidad de Quejas 1

        //POCITOS:
        //Promedio de Cloruros: 1.0mg/L
        //Promedio de Floruros: 1.0mg/L
        //Cantidad de Quejas 1


        //Chequeando Orden Alfabetico
        controlador.registrarMedicion(3,"carrasco norte",1,1, LocalDateTime.now());
        controlador.registrarMedicion(3,"carrasco norte",3,3, LocalDateTime.now());
        controlador.registrarMedicion(4,"ciudad vieja",1,1, LocalDateTime.now());
        controlador.registrarMedicion(4,"aguada",1,1, LocalDateTime.now());
        controlador.registrarMedicion(4,"union",1,1, LocalDateTime.now());
        System.out.println(controlador.generarInformeEstadisticas());
    }

    @Test
    void obtenerTop3ZonasCloruros() throws InformacionInvalida, EntidadNoExiste {
        controlador.registrarMedicion(1,"Malvin",12,12, LocalDateTime.now());
        controlador.registrarMedicion(2,"pocitos",10,10, LocalDateTime.now());
        controlador.registrarQueja("malvin","mi agua tiene mucha sal",LocalDateTime.now());
        controlador.registrarQueja("pocitos","NO ME BANCO MAS LA SAL HERMANO",LocalDateTime.now());

        controlador.registrarMedicion(3,"carrasco norte",2,2, LocalDateTime.now());
        controlador.registrarMedicion(4,"ciudad vieja",1,1, LocalDateTime.now());
        controlador.registrarMedicion(4,"aguada",1,1, LocalDateTime.now());
        controlador.registrarMedicion(4,"union",1,1, LocalDateTime.now());

        MyList<String> top3 = controlador.obtenerTop3ZonasCloruros(LocalDateTime.now().minusDays(1),LocalDateTime.now().plusDays(1));

        //No deberia exceder 3.
        assertEquals(top3.size(),3);


        assertEquals(top3.get(0),"malvin"); //El primero para esa fecha deberia ser Malvin
        assertEquals(top3.get(1),"pocitos"); //El segundo Pocitos
        assertEquals(top3.get(2),"carrasco norte"); //El tercero carrasco Norte.

    }

    @Test
    void diasEntreFechas(){
        MyList<ChronoLocalDate> fechas = controlador.diasEntreFechas(LocalDateTime.of(2023,2,2,0,0),LocalDateTime.of(2023,2,4,0,0));

        assertEquals(fechas.size(),3);
        assertEquals(fechas.get(0).toString(),"2023-02-02");
        assertEquals(fechas.get(1).toString(),"2023-02-03");
        assertEquals(fechas.get(2).toString(),"2023-02-04");

    }
}