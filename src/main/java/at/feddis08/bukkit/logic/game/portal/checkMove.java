package at.feddis08.bukkit.logic.game.portal;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.PortalTrackObject;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static at.feddis08.bukkit.logic.scripts.Main.script_start_by_event_name;

public class checkMove {

    public static void check (PlayerMoveEvent event) throws SQLException, IOException, InterruptedException {
        ArrayList<PortalTrackObject> dataObjs = Functions.getAllPortalTracks();
        Integer x1 = event.getTo().getBlockX();
        Integer y1 = event.getTo().getBlockY();
        Integer z1 = event.getTo().getBlockZ();
        Integer x2 = event.getTo().getBlockX();
        Integer y2 = event.getTo().getBlockY() + 1;
        Integer z2 = event.getTo().getBlockZ();
        Integer index = 0;
        while (index < dataObjs.size()){
            PortalTrackObject dbPortalTrack = dataObjs.get(index);
            if (Objects.equals(dbPortalTrack.from_world, event.getPlayer().getWorld().getName())){
                if (Methods.checkCollision(dbPortalTrack.x1, dbPortalTrack.y1, dbPortalTrack.z1, dbPortalTrack.x2, dbPortalTrack.y2, dbPortalTrack.z2, x1, y1 ,z1, x2, y2, z2)){
                    Player player = event.getPlayer();
                    PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
                    Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                    Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                    Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                    Functions.update("players", "current_world_id", dbPortalTrack.to_world, dbPlayer.id, "id");
                    event.getPlayer().teleport(new Location(MMORPG.Server.getWorld(dbPortalTrack.to_world), Double.parseDouble(String.valueOf(dbPortalTrack.x3)), Double.parseDouble(String.valueOf(dbPortalTrack.y3)), Double.parseDouble(String.valueOf(dbPortalTrack.z3))));
                    Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                    Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                    Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                    ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
                    varObjects.add(new VarObject("player_id", "STRING", dbPlayer.id));
                    varObjects.add(new VarObject("portal_id", "STRING", dbPortalTrack.id));
                    try {
                        script_start_by_event_name ("PLAYER_TELEPORTED_BY_PORTAL", varObjects, false);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            index = index + 1;
        }
    }
}
