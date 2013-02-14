package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;

public class GrantTitle extends BaseCommand {

	private final DonorTitles plugin;

	public GrantTitle(DonorTitles plugin) {
		super("Titles Grant");
		this.plugin = plugin;
		setDescription("Grant a user a title");
		setUsage("/titles grant <user> <id>");
		setArgumentRange(2, 2);
		setIdentifiers(new String[] { "titles grant" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		String playerName = args[0];
		String title = args[1];
		int titleId = Integer.parseInt(title);

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (DonorTitles.perms.has(player, "donortitles.grant")) {
				try {
					if (!DBManager.titleExists(DBManager
							.getTitleFromId(titleId))) {
						sender.sendMessage("No titles exist with this id.");
						return false;
					}
					if (DBManager.hasTitle(playerName, titleId)) {
						sender.sendMessage("Player already has this title.");
						return false;
					}
					if (DBManager.getTitleFromId(titleId) == null) {
						sender.sendMessage("This title does not exist");
						return false;
					}
					if (DBManager.getTitleFromId(titleId) != null) {
						DBManager.grantTitle(playerName, titleId);
						sender.sendMessage("Player " + playerName
								+ " has been given the title: "
								+ DBManager.getTitleFromId(titleId));
						return true;
					} else {
						sender.sendMessage("There was an error granting a player a title");
					}
				} catch (Exception ex) {
					plugin.getLogger().log(Level.SEVERE,
							"Error executing Title Grant command.", ex);
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