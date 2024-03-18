package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Esto es la clase comandante. Los @ de Entity es para dar a entender a Hibernate que es una entidad de la base de datos
 * el access es para gestionar el acceso y el table para denominar su nombre de tabla.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "commander")
public class Commander implements Serializable{
    /**
     * Este atributo se trata de la id. como ya dice el @ es la clave primaria de la entidad, ademas de tener el generate
     * value que genera automaticamente el valor de la id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commander_id")
    private int id;
    /**
     * El siguiente atributo es el nombre de la entidad, y el @ indica como se llamara la columna que contendra este atributo.
     */
    @Column(name = "commander_name")
    private String commanderName;

    /**
     * Constructor de la clase
     * @param commanderName Valor para el nombre del comandante
     */
    public Commander(String commanderName) {
        super();
        this.commanderName = commanderName;
    }

    /**
     * Constructor vacio que solicita Hibernate
     */
    public Commander() {
        super();
    }

    public String getCommanderName() {
        return commanderName;
    }

    public void setCommanderName(String commanderName) {
        this.commanderName = commanderName;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s |", "commander_id", "commander_name") +
                String.format("\n| %-10s | %-20s |", id, commanderName);
    }
}
