package at.feddis08.tools.remote_interface.server.entities;

public class Player {
    public String id = "";
    public String spriteName = "res/sprites/entities/player/player1.png";
    public Integer x = 32;
    public Integer y = 32;
    public String player_name = "";
    public boolean logged_in = false;
    public Player(Integer id){
        this.id = String.valueOf (id);
    }
}