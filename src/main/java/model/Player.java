package model;

import javax.persistence.*;
import java.io.Serializable;
/**
 * Esto es la clase Player. Los @ de Entity es para dar a entender a Hibernate que es una entidad de la base de datos
 * el access es para gestionar el acceso y el table para denominar su nombre de tabla.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "player")
public class Player implements Serializable {
    /**
     * Este atributo se trata de la id. como ya dice el @ es la clave primaria de la entidad, ademas de tener el generate
     * value que genera automaticamente el valor de la id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player")
    private int id;
    /**
     * Atributo para el nombre de la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "player_name")
    private String name;
    /**
     * Atributo para la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "last_activity")
    private String lastActivity;
    /**
     * Atributo para la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "official_rating")
    private String officialRating;
    /**
     * Atributo para la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "wld")
    private String wld;
    /**
     * Atributo para la entidad. el @ indica el nombre de la columna.
     */
    @Column(name = "winrate")
    private int winrate;
    /**
     * Este fragmento pequeño de codigo significa la relación que hay entre las tablas.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "commander_name", referencedColumnName = "commander_name")
    private Commander commander;

    /**
     * Constructor de la calse
     * @param name Nombre del jugador
     * @param lastActivity Ultima vez que se conecto
     * @param officialRating Su rango oficial
     * @param wld Su Wins - Loses - Draws
     * @param winrate Su winrate
     * @param commander El comandante mas jugado
     */
    public Player(String name, String lastActivity, String officialRating, String wld, int winrate, Commander commander) {
        super();
        this.name = name;
        this.lastActivity = lastActivity;
        this.officialRating = officialRating;
        this.wld = wld;
        this.winrate = winrate;
        this.commander = commander;
    }

    /**
     * Constructor vacio que solicita Hibernate
     */
    public Player() {
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

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getOfficialRating() {
        return officialRating;
    }

    public void setOfficialRating(String officialRating) {
        this.officialRating = officialRating;
    }

    public String getWld() {
        return wld;
    }

    public void setWld(String wld) {
        this.wld = wld;
    }

    public int getWinrate() {
        return winrate;
    }

    public void setWinrate(int winrate) {
        this.winrate = winrate;
    }

    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-20s | %-15s | %-10s | %-20s |",
                "id_player", "player_name", "last_activity", "official_rating", "wld", "winrate") +
                String.format("\n| %-10s | %-20s | %-20s | %-15s | %-10s | %-20s |",
                        id, name, lastActivity, officialRating, wld, winrate);
    }

}
