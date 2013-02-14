package com.favorlock.donortitles.db;

import java.sql.SQLException;

import com.favorlock.PatPeter.SQLibrary.MySQL;
import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.config.Configuration;

public class SQLDatabase {
	
	public static MySQL dbm;
	public static int taskID;
	
	public SQLDatabase(DonorTitles plugin) throws SQLException {
		dbm = new MySQL(plugin.getLogger(), "TownScape", Configuration.host, Configuration.port,
						Configuration.database, Configuration.username, Configuration.password);
		dbm.open();
		if(!dbm.checkConnection()) {
			throw new SQLException("Failed to connect to MySQL database!");
		}
		
		taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				try {
					dbm.query("SELECT 1 FROM DUAL");
				} catch (Exception e) {	}
			}
		}, 300, (30 * 20));
	}
	
	public static boolean isDBSchemaValid() {
		boolean retVal = false;
		
		if (dbm.checkConnection()) {
			retVal = dbm.isTable("titles") && dbm.isTable("players");
		}
		
		return retVal;
	}
	
	public static boolean setupTables() throws SQLException {
		boolean retVal = false;
		
		if(!(dbm.isTable("titles") || dbm.isTable("players"))) {
			try {
				dbm.query("CREATE TABLE IF NOT EXISTS titles (titleid VARCHAR(20) PRIMARY KEY, titlename VARCHAR(20));");
				dbm.query("CREATE TABLE IF NOT EXISTS players (playername VARCHAR(20) PRIMARY KEY, titleid VARCHAR(20));");
				
				dbm.query("ALTER TABLE players ADD CONSTRAINT FK_TITLES_ID FOREIGN KEY (titleid) REFERENCES titles(titleid);");
				
				retVal = true;
			} catch (SQLException ex) {
				dbm.writeError("Error creating tables for DonorTitles database.\n" + ex.getMessage(), true);
				ex.printStackTrace();
				
				try {
					if (dbm.isTable("titles")) {
						dbm.query("DROP TABLE titles;");
					}
					if (dbm.isTable("players")) {
						dbm.query("DROP TABLE players;");
					}
				} catch (SQLException exs) {
					throw new SQLException ("Error dropping tables.", exs);
				}
			}
		}
		
		return retVal;
	}

}
