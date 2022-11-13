package at.feddis08.mmorpg.minecraft.tools;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.logic.game.Var;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class Methods {
    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.HOUR_OF_DAY) + 1) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
    }
    public static Boolean checkCollision(Integer x1, Integer y1, Integer z1, Integer x2, Integer y2, Integer z2, Integer x3, Integer y3, Integer z3, Integer x4, Integer y4, Integer z4) {
        Boolean result = false;
        if (x2 >= x3 && x1 <= x4) {
            if (y1 <= y4 && y3 <= y2) {
                if (z1 <= z4 && z3 <= z2) {
                    result = true;
                }
            }
        }
        return result;
    }
    public static void send_minecraft_message_by_id(String id, String message){
        Objects.requireNonNull(MMORPG.Server.getPlayer(UUID.fromString(id))).sendMessage(message);
    }
    public static void open_inv_on_minecraft_player(String player_id, String inv_display_name){
        Objects.requireNonNull(MMORPG.Server.getPlayer(UUID.fromString(player_id))).closeInventory();
        Objects.requireNonNull(MMORPG.Server.getPlayer(UUID.fromString(player_id))).openInventory(Var.get_inventory_by_display_name(inv_display_name).inv);
    }

    public static void update_all_players_online_state() throws SQLException {
        ArrayList<PlayerObject> dbPlayers = Functions.getPlayers("online", "1");
        Integer index = 0;
        while (index < dbPlayers.size()){
            PlayerObject dbPlayer = dbPlayers.get(index);
            Functions.update("players", "online", "0", dbPlayer.id, "id");
            index = index + 1;
        }
        for(Player p : MMORPG.Server.getOnlinePlayers()) {
            PlayerObject dbPlayer = Functions.getPlayer("id" , p.getUniqueId().toString());
            Functions.update("players", "online", "1", dbPlayer.id, "id");
            index = index + 1;
        }
    }
    public static PlayerObject get_player_by_dcId(String dcId) throws SQLException {
        return Functions.getPlayer("id", Functions.getDiscordPlayer("id", dcId).discord_id);
    }
    public static String get_hash(String passwordToHash){
        String generatedPassword = null;

        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
