package at.feddis08.mmorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class install implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender){
            if (label.equalsIgnoreCase("install")){
                if (args.length == 3){
                    String name = args[0];
                    String ownerName = args[1];
                    String ownerUuid = args[2];

                    //MMORPG.start(name, ownerName, ownerUuid);
                    sender.sendMessage("The installation is done. The server will reload ...");
                    sender.getServer().reload();
                }
                sender.sendMessage("Do you want to install the MMORPG? If you want perform this command" +
                        ": install {name of the mmo} {owner of the mmo(playerName)} {owners uuid}");
            }
        }else{
            sender.sendMessage("You need to be the CONSOLE to run install");
            return true;
        }
        return false;
    }
}
