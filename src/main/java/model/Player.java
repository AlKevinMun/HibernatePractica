package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "player")
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player")
    private int id;

    @Column(name = "player_name")
    private String name;

    @Column(name = "last_activity")
    private String lastActivity;

    @Column(name = "official_rating")
    private String officialRating;

    @Column(name = "wld")
    private String wld;

    @Column(name = "winrate")
    private int winrate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "commander_name", referencedColumnName = "commander_name")
    private Commander commander;

    public Player(String name, String lastActivity, String officialRating, String wld, int winrate, Commander commander) {
        super();
        this.name = name;
        this.lastActivity = lastActivity;
        this.officialRating = officialRating;
        this.wld = wld;
        this.winrate = winrate;
        this.commander = commander;
    }

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
}
