package com.favorlock.donortitles.cmd.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;
import com.favorlock.donortitles.util.FontFormat;

public class DeleteTitle extends BaseCommand {

	private final DonorTitles plugin;

	public DeleteTitle(DonorTitles plugin) {
		super("Title Delete");
		this.plugin = plugin;
		setDescription("Delete a title");
		setUsage("/title delete <id>");
		setArgumentRange(1, 1);
		setPermission("donortitles.delete");
		setIdentifiers(new String[] { "title delete" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		String titleId = args[0];
		String title = "";

		try {
			title = DBManager.getTitleFromId(titleId);
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE,
					"Failed to assign title to variable", ex);
			return false;
		}

		if (sender instanceof Player) {
			try {
				if (!DBManager.idExist(titleId)) {
					sender.sendMessage("This id does not exist.");
					return false;
				}
				if (DBManager.deleteTitle(titleId)) {
					sender.sendMessage(FontFormat.translateString("The title "
							+ title + "&r has been deleted."));
					return true;
				} else {
					sender.sendMessage("There was an error deleting the title.");
				}
			} catch (Exception ex) {
				plugin.getLogger().log(Level.SEVERE,
						"Error executing Title Delete command.", ex);
				return false;
			}
			return false;
		}
		sender.sendMessage("Only players can use this command");
		return false;
	}

}
