package org.scarlettparker.videogameslifeserver.commands.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.scarlettparker.videogameslifeserver.objects.TPlayer;

import java.util.Objects;

import static org.scarlettparker.videogameslifeserver.manager.ConfigManager.playerExists;

public class GiveTokens implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String receiver;
        int tokens;

        // for cleanliness
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage. Correct usage: /givetokens player tokens");
            return true;
        }

        try {
            tokens = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid integer for number of tokens.");
            return true;
        }

        if (tokens < 1) {
            sender.sendMessage(ChatColor.RED + "You must send at least 1 token.");
            return true;
        }

        if (!playerExists(args[0]) || Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.RED + "Specified player does not exist/is not online.");
            return true;
        } else {
            receiver = args[0];
            if (Objects.equals(receiver, sender.getName())) {
                sender.sendMessage(ChatColor.RED + "You cannot give yourself tokens.");
                return true;
            }
        }

        // sender and receiver
        TPlayer sPlayer = new TPlayer(sender.getName());
        TPlayer rPlayer = new TPlayer(receiver);

        if (sPlayer.getLives() < 1) {
            sender.sendMessage(ChatColor.RED + "You are dead. You cannot give tokens.");
            return true;
        }

        if (sPlayer.getTokens() - tokens < 0) {
            sender.sendMessage(ChatColor.RED + "You don't have enough tokens to give.");
            return true;
        }

        // update player tokens with some basic addition
        sPlayer.setTokens(sPlayer.getTokens() - tokens);
        rPlayer.setTokens(rPlayer.getTokens() + tokens);

        // player gets confirmation message
        sender.sendMessage("Successfully sent " + ChatColor.GOLD + tokens
                + " tokens" + ChatColor.WHITE + " to " + receiver + ".");
        Bukkit.getPlayer(receiver).sendMessage(sender.getName() + " has given you " + ChatColor.GOLD
                + tokens + " tokens" + ChatColor.WHITE + ".");

        return true;
    }
}
