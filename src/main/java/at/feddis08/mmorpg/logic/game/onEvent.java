package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.logic.game.portal.checkMove;
import at.feddis08.mmorpg.logic.scripts.Main;
import at.feddis08.mmorpg.logic.scripts.VarObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.swing.text.html.parser.Entity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class onEvent {
    public static void onEntityClick (PlayerInteractEntityEvent event) throws SQLException {
        MMORPG.consoleLog(event.getRightClicked().getUniqueId().toString());
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        varObjects.add(new VarObject("who_clicked_id", "STRING", event.getPlayer().getUniqueId().toString()));
        varObjects.add(new VarObject("clicked_entity_id", "STRING", event.getRightClicked().getUniqueId().toString()));
        Main.script_PLAYER_CLICK_ENTITY_event(varObjects);
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
    public static void onPlayerMove(PlayerMoveEvent event) throws SQLException {
        checkMove.check(event);
    }
    public static void onBlockPlaced(BlockPlaceEvent event) throws SQLException {
        PlayerObject dbPlayer = Functions.getPlayer("id", event.getPlayer().getUniqueId().toString());
        if (Rank.isPlayer_allowedTo(dbPlayer.id, "doPlaceBlock") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
            if (Objects.equals(event.getBlockPlaced().getType().toString(), "TNT")){
                if (Rank.isPlayer_allowedTo(dbPlayer.id, "doPlaceTntBlock") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
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
    }
}
