package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;
import com.favorlock.donortitles.util.FontFormat;

public class SetTitle extends BaseCommand {

	private final DonorTitles plugin;

	public SetTitle(DonorTitles plugin) {
		super("Titles Set");
		this.plugin = plugin;
		setDescription("Set your title");
		setUsage("/titles set <id>");
		setArgumentRange(1, 1);
		setIdentifiers(new String[] { "titles set" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		Player player = (Player) sender;
		String titleId = args[0];

		if (sender instanceof Player) {
			if (DonorTitles.perms.has(player, "donortitles.set")) {
				try {
					if (!DBManager.idExist(titleId)) {
						sender.sendMessage("No titles exist with this id.");
						return false;
					}
					if (DBManager.hasTitle(sender.getName(), titleId)) {
						DonorTitles.chat.setPlayerPrefix(player,
								DBManager.getTitleFromId(titleId));
						sender.sendMessage(FontFormat.translateString("Your title has been set to "
								+ DBManager.getTitleFromId(titleId) + "&r."));
						return true;
					} else {
						sender.sendMessage("You do not have this title.");
					}
				} catch (Exception ex) {
					plugin.getLogger().log(Level.SEVERE,
							"Error executing Title Set command.", ex);
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
