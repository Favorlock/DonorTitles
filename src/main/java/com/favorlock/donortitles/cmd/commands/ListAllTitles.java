package com.favorlock.donortitles.cmd.commands;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.db.DBManager;
import com.favorlock.donortitles.util.FontFormat;

public class ListAllTitles extends BaseCommand {

	private final DonorTitles plugin;

	public ListAllTitles(DonorTitles plugin) {
		super("Title ListAll");
		this.plugin = plugin;
		setDescription("List all titles");
		setUsage("/title listall");
		setArgumentRange(0, 0);
		setPermission("donortitles.listall");
		setIdentifiers(new String[] { "title listall" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {

		try {
			ArrayList<String> list = new ArrayList<String>();
			String titles = "";
			list = DBManager.getAllTitles();

			if (list.size() >= 1) {
				for (String id : list) {
					titles += id + ", ";
				}
				titles = titles.substring(0, titles.length() - 2);
			} else {
				sender.sendMessage("There are no titles.");
				return false;
			}

			sender.sendMessage(FontFormat
					.translateString("&eExisting Titles:\n&r" + titles));
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE,
					"Error executing Title List command.", ex);
			return false;
		}
		return false;
	}

}
