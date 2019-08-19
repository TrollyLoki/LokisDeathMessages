package net.trollyloki.LokisDeathMessages.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import net.trollyloki.LokisDeathMessages.Main;

public class DeathMessageCommand implements CommandExecutor {
	
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {   
        if (cmd.getName().equalsIgnoreCase("deathmsg")) {
        	
        	if (args.length > 0) {
        		
        		
        		if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("deathmsg.reload")) {
        			
        			Main.getPlugin().saveDefaultConfig();
        			Main.getPlugin().reloadConfig();
        			sender.sendMessage(Main.prefix() + Main.getString("lang.reload"));
        			return true;
        			
        		}
        		
        		
        	}
        	
        	
        	sender.sendMessage(Main.prefix() + ChatColor.GREEN + "This server is running LokisDeathMessages "
        			+ Main.getPlugin().getDescription().getVersion() + " by TrollyLoki");
        	return false;
        	
        }
		return false;   
    }
	
}