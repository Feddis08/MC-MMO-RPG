package at.feddis08.mmorpg.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

public class DataObject {
    public String _id = "0";
    public Integer ranks;
    public String name;
    public String owner_name;
    public Integer online;
    public Integer enabled;
    public Integer registerdPlayers;
}
