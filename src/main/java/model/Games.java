package model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
/**
 * Esto es la clase Game. Los @ de Entity es para dar a entender a Hibernate que es una entidad de la base de datos
 * el access es para gestionar el acceso y el table para denominar su nombre de tabla.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "games")
public class Games {
    /**
     * Este atributo se trata de la id. como ya dice el @ es la clave primaria de la entidad, ademas de tener el generate
     * value que genera automaticamente el valor de la id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game")
    private int id;
    /**
     * Atributo para la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "game_name")
    private String name;

    /**
     * Este fragmento de codigo significa la relación de muchos a uno con la clase mapa.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_map", referencedColumnName = "id_map")
    private Map map;
    /**
     * Este fragmento de codigo significa la relación de muchos a muchos con la clase de player.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players = new HashSet<>();

    /**
     * Constructor de la clase
     * @param name Nombre de la partida
     * @param map Mapa de la partida
     * @param players Jugadores que forman parte de la partida
     */
    public Games(String name, Map map, Set<Player> players) {
        super();
        this.name = name;
        this.map = map;
        this.players = players;
    }

    /**
     * Constructor vacio que solicita Hibernate
     */
    public Games() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("| %-10s | %-20s | %-20s |", "id_game", "game_name", "id_map"));
        if (map != null) {
            sb.append(String.format("\n| %-10s | %-20s | %-20s |", id, name, map.getId()));
        } else {
            sb.append(String.format("\n| %-10s | %-20s | %-20s |", id, name, ""));
        }
        if (!players.isEmpty()) {
            sb.append("\nPlayers:");
            for (Player player : players) {
                sb.append(String.format("\n| %-10s | %-20s |", "", player.getName()));
            }
        }
        return sb.toString();
    }

}