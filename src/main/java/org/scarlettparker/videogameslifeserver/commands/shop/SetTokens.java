package org.scarlettparker.videogameslifeserver.commands.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.scarlettparker.videogameslifeserver.objects.TPlayer;

import static org.scarlettparker.videogameslifeserver.manager.ConfigManager.*;

public class SetTokens implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // must be an operator to use the command
        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You must be an operator to run this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage. Correct usage: /settokens player tokens");
            return true;
        }

        // for cleanliness
        int tokens;
        Player player = Bukkit.getPlayer(args[0]);

        if (!jsonFileExists(playerFile)) {
            sender.sendMessage(ChatColor.RED
                    + "Player file not yet initialized. Make sure to run /startlife and then /starttasks.");
            return true;
        }

        if (!playerExists(args[0]) || player == null) {
            sender.sendMessage(ChatColor.RED + "Specified player is not online.");
            return true;
        }

        try {
            tokens = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid integer for number of tokens.");
            return true;
        }

        if (tokens < 0) {
            sender.sendMessage(ChatColor.RED + "You can not set a player's tokens to below 0.");
            return true;
        }

        TPlayer tempPlayer = new TPlayer(player.getName());

        // update tokens and display
        tempPlayer.setTokens(tokens);
        player.sendMessage(ChatColor.GOLD + "You have been set to " + tokens + " tokens.");

        return true;
    }
}
