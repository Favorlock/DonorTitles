package com.favorlock.donortitles.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBManager {
	
	public static boolean titleExists(String title) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("SELECT * FROM titles WHERE titlename = ?");
			ps.setString(1, title);
			rs = ps.executeQuery();
			
			retVal = rs.next();
			
			rs.close();
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error trying to lookup a title: " + title + ".", ex);
		}
		
		return retVal;
	}
	
	public static boolean createTitle(String titleId, String title) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("INSERT INTO titles (titleid, titlename) VALUES (?, ?);");
			ps.setString(1, titleId);
			ps.setString(2, title);
			retVal = ps.executeUpdate() > 0;
			
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error trying to add a title to the database:" + title + ".", ex);
		}
		
		return retVal;
	}
	
	public static boolean deleteTitleChildren(String titleId) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("DELETE FROM players WHERE titleId = ?;");
			ps.setString(1, titleId);
			retVal = ps.executeUpdate() > 0;
			
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error trying to delete player data.", ex);
		}
		
		return retVal;
	}
	
	public static boolean deleteTitle(String titleId) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		
		try {
			deleteTitleChildren(titleId);
			
			ps = SQLDatabase.dbm.getConnection().prepareStatement("DELETE FROM titles WHERE titleid = ?;");
			ps.setString(1, titleId);
			retVal = ps.executeUpdate() > 0;
			
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error trying to delete a title: " + titleId + ".", ex);
		}
		
		return retVal;
	}
	
	public static boolean idExist(String titleId) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("SELECT * FROM titles WHERE titleid = ?");
			ps.setString(1, titleId);
			rs = ps.executeQuery();
			
			retVal = rs.next();
			
			rs.close();
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error trying to lookup an id: " + titleId + ".", ex);
		}
		
		return retVal;
	}
	
	public static String getTitleFromId(String titleId) throws SQLException {
		String titleName = "";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("SELECT titlename FROM titles WHERE titleid = ?;");
			ps.setString(1, titleId);
			rs = ps.executeQuery();
			while (rs.next() != false) {
				titleName = rs.getString(1);
			}
			
			ps.close();
			rs.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error trying to retrieve title with id: " + titleId + ".", ex);
		}
		
		return titleName;
	}
	
	public static boolean grantTitle(String playerName, String titleId) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("INSERT INTO players (playername, titleid) VALUES (?, ?);");
			ps.setString(1, playerName);
			ps.setString(2, titleId);
			retVal = ps.executeUpdate() > 0;
			
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error granting a player a title with the id: " + titleId + ".", ex);
		}
		
		return retVal;
	}
	
	public static boolean revokeTitle(String playerName, String titleId) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("DELETE FROM players WHERE playername = ? AND titleid = ?;");
			ps.setString(1, playerName);
			ps.setString(2, titleId);
			retVal = ps.executeUpdate() > 0;
			
			ps.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error rovking a title from a player.", ex);
		}
		
		return retVal;
	}
	
	public static boolean hasTitle(String playerName, String titleId) throws SQLException {
		boolean retVal = false;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("SELECT playername FROM players WHERE playername = ? AND titleid = ?;");
			ps.setString(1, playerName);
			ps.setString(2, titleId);
			rs = ps.executeQuery();
			retVal = rs.next();
			
			ps.close();
			rs.close();
		} catch (SQLException ex) {
			throw new SQLException("There was an error checking a player for title with id: " + titleId + ".", ex);
		}
		
		return retVal;
	}
	
	public static ArrayList<String> getOwnedTitles(String playerName) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> al = new ArrayList<String>();
		
		try {
			ps = SQLDatabase.dbm.getConnection().prepareStatement("SELECT titleid FROM players WHERE playername = ?;");
			ps.setString(1, playerName);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				al.add(rs.getString("titleid"));
			}
		} catch (SQLException ex) {
			throw new SQLException("There was an error getting the players titles", ex);
		}
		
		return al;
	}

}
