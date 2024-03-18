package model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Access(AccessType.FIELD)
@Table(name = "games")
public class Games {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game")
    private int id;

    @Column(name = "game_name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_map", referencedColumnName = "id_map")
    private Map map;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players = new HashSet<>();

    public Games(String name, Map map, Set<Player> players) {
        super();
        this.name = name;
        this.map = map;
        this.players = players;
    }

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