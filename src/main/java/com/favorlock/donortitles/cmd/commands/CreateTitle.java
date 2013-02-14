package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;

public class CreateTitle extends BaseCommand {
	
	private final DonorTitles plugin;

	public CreateTitle(DonorTitles plugin) {
		super("Titles Create");
		this.plugin = plugin;
		setDescription("Creat a new title");
		setUsage("/titles create <name>");
		setArgumentRange(1, 1);
		setIdentifiers(new String[] { "titles create" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier, String[] args) {
		String titleName = args[0];
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (DonorTitles.perms.has(player, "donortitles.create")) {
				try {
					if (DBManager.titleExists(titleName)) {
						sender.sendMessage("This title already exist.");
						return false;
					}
					if (DBManager.createTitle(titleName)) {
						sender.sendMessage("The title " + titleName + " was created.");
						return true;
					} else {
						sender.sendMessage("There was an error creating the title.");
					}
				} catch (Exception ex) {
					plugin.getLogger().log(Level.SEVERE, "Error executing Title Create command.", ex);
					return false;
				}
				return false;
			}
			sender.sendMessage("You do not have permission");
			return false;
		}
		sender.sendMessage("Only players can use this command");
		return false;
	}

}
