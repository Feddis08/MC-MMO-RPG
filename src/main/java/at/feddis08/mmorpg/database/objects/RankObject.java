package at.feddis08.mmorpg.database;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(appliesTo = "players")
public class RankObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="permissions")
    public String permissions;
    @Column(name="name")
    public String name;
    @Column(name="id")
    public Integer id;
    @Column(name="rank_level")
    public Integer rank_level;
    @Column(name="rank_color")
    public String rank_color;
    @Column(name="prefix_color")
    public String prefix_color;
    @Column(name="prefix")
    public String prefix;
}