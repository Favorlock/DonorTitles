package com.favorlock.donortitles.cmd.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;

public class ClearTitle extends BaseCommand {
	
	private final DonorTitles plugin;

	public ClearTitle(DonorTitles plugin) {
		super("Title Clear");
		this.plugin = plugin;
		setDescription("Clear used title");
		setUsage("/title clear");
		setArgumentRange(0, 0);
		setIdentifiers(new String[] { "title clear" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (DonorTitles.perms.has(player, "donortitles.clear")) {
				if (DonorTitles.chat.getPlayerPrefix(player) != null && DonorTitles.chat.getPlayerPrefix(player) != "") {
					DonorTitles.chat.setPlayerPrefix(player, "");
					sender.sendMessage("Your title has been cleared.");
					return true;
				}
		
				sender.sendMessage("You do not have a title set.");
				return false;
			}
			sender.sendMessage("You do not have permission");
			return false;
		}
		sender.sendMessage("Only players can use this command");
		return false;
	}

}
