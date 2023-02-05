package at.feddis08.mmorpg.io.database;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.objects.*;

import java.util.ArrayList;

public class Save {
    public ArrayList<PlayerObject> players;
    public ArrayList<WorldObject> worlds;
    public ArrayList<MailObject> mails;
    public ArrayList<UserObject> users;
    public ArrayList<WarpObject> warps;
    public ArrayList<RankObject> ranks;
    public ArrayList<Rank_permissionObject> ranks_permissions;
    public ArrayList<Player_questObject> quests;
    public ArrayList<InventoryTrackObject> inventoryTracks;
    public ArrayList<Player_balanceObject> players_balances;
    public ArrayList<PlayerInWorlds> playerInWorlds;
    public ArrayList<Discord_playerObject> discord_players;
    public ArrayList<PortalTrackObject> portalTracks;
    public void save_to_file(){
    };
}
