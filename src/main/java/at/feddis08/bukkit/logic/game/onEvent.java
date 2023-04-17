package at.feddis08.bukkit.logic.game;

import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.bukkit.logic.game.portal.checkMove;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static at.feddis08.bukkit.logic.scripts.Main.script_start_by_event_name;

public class onEvent {
    public static void onEntityClick (PlayerInteractEntityEvent event) throws SQLException {
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        varObjects.add(new VarObject("who_clicked_id", "STRING", event.getPlayer().getUniqueId().toString()));
        varObjects.add(new VarObject("clicked_entity_id", "STRING", event.getRightClicked().getUniqueId().toString()));
        Main.vars_AFTER_PLAYER_CLICK_EVENT = varObjects;
        Main.run_AFTER_PLAYER_CLICK_EVENT = true;
    }
    public static void onInventoryClick (InventoryClickEvent event) throws SQLException {
        CheckInventoryTrack.checkInvClicked(event);
    }
    public static void onInventoryOpened(InventoryOpenEvent event) throws SQLException {
        CheckInventoryTrack.checkInvOpen(event);
    }
    public static void onInventoryDrag(InventoryDragEvent event){
        CheckInventoryTrack.checkInvDrag(event);
    }
    public static void onPlayerMove(PlayerMoveEvent event) throws SQLException, IOException, InterruptedException {
        checkMove.check(event);
    }
    public static void damage(EntityDamageEvent ev) throws IOException, InterruptedException {
        if (ev.getEntity() instanceof Player){
            Player player = (Player) ev.getEntity();
            if (player.getHealth() - ev.getFinalDamage() <= 0){
                ev.setCancelled(true);
                ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
                varObjects.add(new VarObject("player_id", "STRING", player.getUniqueId().toString()));
                varObjects.add(new VarObject("death_cause", "STRING", ev.getCause().toString()));
                varObjects.add(new VarObject("final_damage", "INTEGER", String.valueOf(ev.getFinalDamage())));
                varObjects.add(new VarObject("player_health", "INTEGER", String.valueOf(player.getHealth())));
                script_start_by_event_name ("PLAYER_DEATH", varObjects, false);
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    public static void onBlockPlaced(BlockPlaceEvent event) throws SQLException {
        PlayerObject dbPlayer = Functions.getPlayer("id", event.getPlayer().getUniqueId().toString());
        if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doPlaceBlock") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
            if (Objects.equals(event.getBlockPlaced().getType().toString(), "TNT")){
                if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doPlaceTntBlock") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "I hope, you know what you are doing!");
                }else{
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "You need permissions: doPlaceTntBlock!");
                    event.getPlayer().kickPlayer("You need permissions: doPlaceTntBlock!");
                }
            }
        }else{
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You need permissions: doPlaceBlock!");
        }
        ArrayList<VarObject> varObjects = new ArrayList<>();
        varObjects.add(new VarObject("block_type_name", "STRING", event.getBlock().getType().name()));
        varObjects.add(new VarObject("world_name", "STRING", event.getBlock().getWorld().getName()));
        varObjects.add(new VarObject("player_id", "STRING", event.getPlayer().getUniqueId().toString()));
        varObjects.add(new VarObject("block_x", "INTEGER", String.valueOf(event.getBlock().getX())));
        varObjects.add(new VarObject("block_y", "INTEGER", String.valueOf(event.getBlock().getY())));
        varObjects.add(new VarObject("block_z", "INTEGER", String.valueOf(event.getBlock().getZ())));
        try {
            Main.script_start_by_event_name("BLOCK_PLACED", varObjects, false);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
