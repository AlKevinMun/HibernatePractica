package model;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Access(AccessType.FIELD)
@Table(name = "map")
public class Map implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_map")
    private int id;
    @Column(name = "map_name")
    private String mapName;
    @Column(name = "creator")
    private String creator;
    @Column(name = "max_players")
    private int maxPlayers;
    @Column(name = "size")
    private String size;

    public Map(String mapName, String creator, int maxPlayers, String size) {
        super();
        this.mapName = mapName;
        this.creator = creator;
        this.maxPlayers = maxPlayers;
        this.size = size;
    }

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
