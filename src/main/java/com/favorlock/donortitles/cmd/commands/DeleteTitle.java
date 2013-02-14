package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;

public class DeleteTitle extends BaseCommand {
	
	private final DonorTitles plugin;

	public DeleteTitle(DonorTitles plugin) {
		super("Titles Delete");
		this.plugin = plugin;
		setDescription("Delete a title");
		setUsage("/titles delete <id>");
		setArgumentRange(1, 1);
		setIdentifiers(new String[] { "titles delete" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier, String[] args) {
		String title = args[0];
		int titleId = Integer.parseInt(title);
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (DonorTitles.perms.has(player, "donortitles.delete")) {
				try {
					if (!DBManager.titleExists(DBManager.getTitleFromId(titleId))) {
						sender.sendMessage("The title does not exist.");
						return false;
					}
					if (DBManager.deleteTitle(DBManager.getTitleFromId(titleId))) {
						sender.sendMessage("The title has been deleted.");
						return true;
					} else {
						sender.sendMessage("There was an error deleting the title.");
					}
				} catch (Exception ex) {
					plugin.getLogger().log(Level.SEVERE, "Error executing Title Delete command.", ex);
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
