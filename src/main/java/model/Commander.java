package model;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Access(AccessType.FIELD)
@Table(name = "commander")
public class Commander implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commander_id")
    private int id;
    @Column(name = "commander_name")
    private String commanderName;

    public Commander(String commanderName) {
        super();
        this.commanderName = commanderName;
    }

    public Commander() {
        super();
    }

    public String getCommanderName() {
        return commanderName;
    }

    public void setCommanderName(String commanderName) {
        this.commanderName = commanderName;
    }
}
