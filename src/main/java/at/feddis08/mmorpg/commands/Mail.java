package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.MailObject;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.RankObject;
import at.feddis08.mmorpg.listeners.onChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

import static at.feddis08.mmorpg.listeners.onChat.getChatColor;

public class Mail implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mail")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                try {
                    if (Rank.isPlayer_allowedTo(dbPlayer.id, "doMail") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                        Boolean validCommand = false;
                        if (args.length == 4) {
                            if (args[0].equalsIgnoreCase("send")) {
                                PlayerObject dbReceiver = Functions.getPlayer("display_name", args[1]);
                                MailObject mail = new MailObject();
                                mail.receiver_id = dbReceiver.id;
                                mail.sender_id = dbPlayer.id;
                                mail.message = args[3];
                                mail.title = args[2];
                                mail.date = Instant.now().toString();
                                mail.id = Functions.getMails("receiver_id", dbPlayer.id, "receiver_id", dbPlayer.id).size() + 1;

                                Functions.createMail(mail);
                                if (Objects.equals(dbReceiver.online, "1") && Objects.equals(dbReceiver.didStartup, "true")) {
                                    RankObject dbRank = Functions.getRank("name", dbReceiver.player_rank);
                                    ChatColor color_prefix = getChatColor(dbRank.prefix_color);
                                    ChatColor color_rank = getChatColor(dbRank.rank_color);
                                    sender.getServer().getPlayer(dbReceiver.player_name).sendMessage(ChatColor.GOLD
                                            + "You got mail by " + (ChatColor.GRAY + "[" + color_prefix
                                            + dbRank.prefix + ChatColor.GRAY
                                            + "][" + color_rank
                                            + dbReceiver.display_name + ChatColor.GRAY
                                            + "]") + ChatColor.DARK_PURPLE + " (" + mail.title + ")");
                                }
                                sender.sendMessage(ChatColor.GREEN + "Sent mail");
                                validCommand = true;
                            }
                        }
                        if (args.length == 2) {
                            if (args[0].equalsIgnoreCase("open")) {
                                MailObject mail = Functions.getMail("id", args[1], "receiver_id", dbPlayer.id);
                                sender.sendMessage(ChatColor.BLUE + "The mail with the id" + args[1] + "! The format:");
                                sender.sendMessage(ChatColor.AQUA + "From | Date UTC | Seen/Opened | Title | Id");
                                PlayerObject dbSender = Functions.getPlayer("id", mail.sender_id);
                                RankObject dbRank = Functions.getRank("name", dbSender.player_rank);
                                ChatColor color_prefix = getChatColor(dbRank.prefix_color);
                                ChatColor color_rank = getChatColor(dbRank.rank_color);
                                Functions.updateWhereAnd("mails", "opened", "true", dbPlayer.id, "receiver_id", args[1], "id");
                                sender.sendMessage(ChatColor.GRAY
                                        + "Auther: "
                                        + (ChatColor.GRAY + "[" + color_prefix
                                        + dbRank.prefix + ChatColor.GRAY
                                        + "][" + color_rank
                                        + dbSender.display_name + ChatColor.GRAY
                                        + "]"));
                                sender.sendMessage(ChatColor.GRAY + "Title: " + ChatColor.AQUA + mail.title);
                                sender.sendMessage(ChatColor.GRAY + "Date UTC: " + ChatColor.AQUA + mail.date);
                                sender.sendMessage(ChatColor.GRAY + "Seen/Opended: " + ChatColor.AQUA + mail.opened);
                                sender.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.AQUA + mail.id);
                                sender.sendMessage(ChatColor.GRAY + "Message: " + ChatColor.AQUA + mail.message);
                            }
                            validCommand = true;
                        }
                        if (args.length == 2) {
                            if (args[0].equalsIgnoreCase("showMailsFrom")) {
                                ArrayList<MailObject> mails = Functions.getMails("receiver_id", dbPlayer.id, "sender_id", Functions.getPlayer("display_name", args[1]).id);
                                Integer i = 0;
                                sender.sendMessage(ChatColor.BLUE + "All your mails you got from " + args[1] + "! The format:");
                                sender.sendMessage(ChatColor.AQUA + "From | Date UTC | Seen/Opened | Title | Id");
                                MMORPG.consoleLog(String.valueOf(mails.size()));
                                while (!(i >= mails.size())) {
                                    PlayerObject dbSender = Functions.getPlayer("id", mails.get(i).sender_id);
                                    RankObject dbRank = Functions.getRank("name", dbSender.player_rank);
                                    ChatColor color_prefix = getChatColor(dbRank.prefix_color);
                                    ChatColor color_rank = getChatColor(dbRank.rank_color);
                                    sender.sendMessage(ChatColor.GRAY
                                            + (ChatColor.GRAY + "[" + color_prefix
                                            + dbRank.prefix + ChatColor.GRAY
                                            + "][" + color_rank
                                            + dbSender.display_name + ChatColor.GRAY
                                            + "]")
                                            + " | "
                                            + mails.get(i).date
                                            + " | "
                                            + mails.get(i).opened
                                            + " | "
                                            + mails.get(i).title
                                            + " | "
                                            + mails.get(i).id);
                                    i = i + 1;
                                }
                                sender.sendMessage(ChatColor.DARK_BLUE + "No more mails.");
                                validCommand = true;
                            }
                        }
                        if (args.length == 1) {
                            if (args[0].equalsIgnoreCase("showAllMails")) {
                                ArrayList<MailObject> mails = Functions.getMails("receiver_id", dbPlayer.id, "receiver_id", dbPlayer.id);
                                Integer i = 0;
                                sender.sendMessage(ChatColor.BLUE + "All your mails you got! The format:");
                                sender.sendMessage(ChatColor.AQUA + "From | Date UTC | Seen/Opened | Title | Id");
                                MMORPG.consoleLog(String.valueOf(mails.size()));
                                while (!(i >= mails.size())) {
                                    PlayerObject dbSender = Functions.getPlayer("id", mails.get(i).sender_id);
                                    RankObject dbRank = Functions.getRank("name", dbSender.player_rank);
                                    ChatColor color_prefix = getChatColor(dbRank.prefix_color);
                                    ChatColor color_rank = getChatColor(dbRank.rank_color);
                                    sender.sendMessage(ChatColor.GRAY
                                            + (ChatColor.GRAY + "[" + color_prefix
                                            + dbRank.prefix + ChatColor.GRAY
                                            + "][" + color_rank
                                            + dbSender.display_name + ChatColor.GRAY
                                            + "]")
                                            + " | "
                                            + mails.get(i).date
                                            + " | "
                                            + mails.get(i).opened
                                            + " | "
                                            + mails.get(i).title
                                            + " | "
                                            + mails.get(i).id);
                                    i = i + 1;
                                }
                                sender.sendMessage(ChatColor.DARK_BLUE + "No more mails.");
                                validCommand = true;
                            }
                        }
                        if (!(validCommand)) {
                            sender.sendMessage(ChatColor.RED + "Wrong usage!");


                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You need the permission: 'doMail'!");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return true;
    }
}
