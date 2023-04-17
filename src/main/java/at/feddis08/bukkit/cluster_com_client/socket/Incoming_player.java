package at.feddis08.bukkit.cluster_com_client.socket;

public class Incoming_player {
    public String player_id;
    public String from_server;
    public boolean arrived = false;

    public Incoming_player(String player_id, String from_server, boolean arrived){
        this.arrived = arrived;
        this.player_id = player_id;
        this.from_server = from_server;
    }
}
