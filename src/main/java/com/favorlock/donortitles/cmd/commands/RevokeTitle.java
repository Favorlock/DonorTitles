package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;
import com.favorlock.donortitles.util.FontFormat;

public class RevokeTitle extends BaseCommand {

	private final DonorTitles plugin;

	public RevokeTitle(DonorTitles plugin) {
		super("Titles Revoke");
		this.plugin = plugin;
		setDescription("Revoke a title from a user");
		setUsage("/titles revoke <user> <id>");
		setArgumentRange(2, 2);
		setIdentifiers(new String[] { "titles revoke" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		String playerName = args[0];
		String titleId = args[1];

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (DonorTitles.perms.has(player, "donortitles.revoke")) {
				try {
					if (!DBManager.titleExists(DBManager
							.getTitleFromId(titleId))) {
						sender.sendMessage("No titles exist with this id.");
						return false;
					}
					if (!DBManager.hasTitle(playerName, titleId)) {
						sender.sendMessage("Player does not have this title.");
						return false;
					}
					if (DBManager.getTitleFromId(titleId) == null) {
						sender.sendMessage("This title does not exist.");
						return false;
					}
					if (DBManager.getTitleFromId(titleId) != null) {
						DBManager.revokeTitle(playerName, titleId);
						sender.sendMessage(FontFormat.translateString("Player " + playerName
								+ " has lost the title: "
								+ DBManager.getTitleFromId(titleId) + "&r."));
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
