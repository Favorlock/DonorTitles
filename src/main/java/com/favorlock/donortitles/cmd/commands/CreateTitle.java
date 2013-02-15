package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;
import com.favorlock.donortitles.util.FontFormat;

public class CreateTitle extends BaseCommand {
	
	private final DonorTitles plugin;

	public CreateTitle(DonorTitles plugin) {
		super("Title Create");
		this.plugin = plugin;
		setDescription("Creat a new title");
		setUsage("/title create <identifier> <title>");
		setArgumentRange(2, 2);
		setIdentifiers(new String[] { "title create" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier, String[] args) {
		String titleName = args[1];
		String titleId = args[0];
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (DonorTitles.perms.has(player, "donortitles.create")) {
				try {
					if (DBManager.idExist(titleId)) {
						sender.sendMessage("This id already exist.");
						return false;
					}
					if (DBManager.titleExists(titleName)) {
						sender.sendMessage("This title already exist.");
						return false;
					}
					if (DBManager.createTitle(titleId, titleName)) {
						sender.sendMessage(FontFormat.translateString("The title " + titleName + "&r was created."));
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
