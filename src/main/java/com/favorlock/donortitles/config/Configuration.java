package com.favorlock.donortitles.config;

import java.io.File;

import com.favorlock.donortitles.DonorTitles;

public class Configuration {
	
	public static String 	backend = "",
		 					host = "",
		 					database = "",
		 					username = "",
		 					password = "",
		 					prefix = "";

	public static int		port,
							maxTowns;

	public static boolean	debug = false;

	public Configuration(DonorTitles plugin) {
		File file = new File(plugin.getDataFolder() + File.separator + "config.yml");

		if(!file.exists()) {
			plugin.getConfig().options().copyDefaults(true);
			plugin.saveConfig();
		}

		// Load database settings.
		backend = plugin.getConfig().getString("data.backend");
		host = plugin.getConfig().getString("data.db.host");
		port = plugin.getConfig().getInt("data.db.port");
		database = plugin.getConfig().getString("data.db.database");
		username = plugin.getConfig().getString("data.db.username");
		password = plugin.getConfig().getString("data.db.password");
		prefix = plugin.getConfig().getString("data.db.prefix");
	}

}
