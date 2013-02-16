package com.favorlock.donortitles.cmd.commands;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;
import com.favorlock.donortitles.util.FontFormat;

public class ListTitles extends BaseCommand {
	
	private final DonorTitles plugin;

	public ListTitles(DonorTitles plugin) {
		super("Title List");
		this.plugin = plugin;
		setDescription("List your owned titles");
		setUsage("/title list");
		setArgumentRange(0, 0);
		setIdentifiers(new String[] { "title list" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier, String[] args) {
		Player player = (Player) sender;

		if (sender instanceof Player) {
			if (DonorTitles.perms.has(player, "donortitles.list")) {
				try {
					ArrayList<String> list = new ArrayList<String>();
					String ownedIds = "";
					list = DBManager.getOwnedTitles(sender.getName());
					
					for (String id : list) {
						ownedIds += id + ", ";
					}
					
					sender.sendMessage(FontFormat.translateString("&eYour Owned Titles:\n&r" + ownedIds));
				} catch (Exception ex) {
					plugin.getLogger().log(Level.SEVERE,
							"Error executing Title List command.", ex);
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
