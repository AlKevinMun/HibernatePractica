package model;

import javax.persistence.*;
import java.io.Serializable;
/**
 * Esto es la clase Map. Los @ de Entity es para dar a entender a Hibernate que es una entidad de la base de datos
 * el access es para gestionar el acceso y el table para denominar su nombre de tabla.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "map")
public class Map implements Serializable{
    /**
     * Este atributo se trata de la id. como ya dice el @ es la clave primaria de la entidad, ademas de tener el generate
     * value que genera automaticamente el valor de la id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_map")
    private int id;
    /**
     * Atributo para el nombre de la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "map_name")
    private String mapName;
    /**
     * Atributo para el creador de la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "creator")
    private String creator;
    /**
     * Atributo para el numero maximo de jugadores de la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "max_players")
    private int maxPlayers;
    /**
     * Atributo para el tamaño del mapa de la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "size")
    private String size;

    /**
     * Constructor de la clase
     * @param mapName Nombre del mapa
     * @param creator Valor del creador
     * @param maxPlayers Valor de los jugadores maximos
     * @param size Tamaño del mapa
     */
    public Map(String mapName, String creator, int maxPlayers, String size) {
        super();
        this.mapName = mapName;
        this.creator = creator;
        this.maxPlayers = maxPlayers;
        this.size = size;
    }

    /**
     * Constructor vacio que solicita Hibernate
     */
    public Map() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-20s | %-15s | %-10s |",
                "id_map", "map_name", "creator", "max_players", "size") +
                String.format("\n| %-10s | %-20s | %-20s | %-15s | %-10s |",
                        id, mapName, creator, maxPlayers, size);
    }


}
