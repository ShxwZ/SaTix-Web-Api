package com.gabriel.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class EventTableView implements Serializable {

    private Event event;
    private Status status;

    /**
     * Establece el status del evento en función de la fecha actual.
     * Si estamos en una fecha antes que la de inicio del evento el status sera "PROXIMAMENTE".
     * Si estamos en la misma fecha o despues de la de inicio y aun no hemos pasado la fecha de finalización sera "ACTIVO".
     * En caso de que estamos en una fecha más tardia que la de finalización sera "TERMINADO".
     *
     * @param event El evento para el que se establecerá el status
     */
    public EventTableView(Event event) {
        this.event = event;

        LocalDateTime fechaInicio = event.getStart_date();
        LocalDateTime fechaFin = event.getFinish_date();
        LocalDateTime fechaActual = LocalDateTime.now();

        if (fechaActual.isBefore(fechaInicio)) {
            this.status = Status.PROXIMAMENTE;
        } else if (fechaActual.isEqual(fechaInicio) || (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin))) {
            this.status = Status.ACTIVO;
        } else {
            this.status = Status.TERMINADO;
        }

    }


    public enum Status {
        PROXIMAMENTE("Próximamente"),
        ACTIVO("Activo"),
        TERMINADO("Terminado");

        private final String label;

        Status(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

}



