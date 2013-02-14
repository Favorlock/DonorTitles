package com.favorlock.donortitles.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.favorlock.donortitles.DonorTitles;

public class CommandHandler {
	
	protected LinkedHashMap<String, Command> commands;
	protected HashMap<String, Command> identifiers;
	private final DonorTitles plugin;
	
	public CommandHandler(DonorTitles plugin) {
		this.plugin = plugin;
		this.commands = new LinkedHashMap();
		this.identifiers = new HashMap();
	}
	
	public void addCommand(Command command) {
		this.commands.put(command.getName().toLowerCase(), command);
		for (String ident : command.getIdentifiers()) {
			this.identifiers.put(ident.toLowerCase(), command);
		}
	}
	
	public boolean dispatch(CommandSender sender, String commandName, String label, String[] args) {
		for (int argsIncluded = args.length; argsIncluded >= 0; argsIncluded--) {
			StringBuilder identifierBuilder = new StringBuilder(commandName);
			for(int i = 0; i < argsIncluded; i++) {
				identifierBuilder.append(' ').append(args[i]);
			}
			
			String identifier = identifierBuilder.toString();
			Command cmd = getCmdFromIdent(identifier, sender);
			if (cmd != null) {
				String[] realArgs = (String[])Arrays.copyOfRange(args, argsIncluded, args.length);
				
				if (!cmd.isInProgress(sender)) {
					if ((realArgs.length < cmd.getMaxArguments()) || (realArgs.length > cmd.getMaxArguments())) {
						displayCommandHelp(cmd, sender);
						return true;
					}
					if ((realArgs.length > 0) && (realArgs[0].equals("?"))) {
						displayCommandHelp(cmd, sender);
						return true;
					}
				}
				
				if (!hasPermission(sender, cmd.getPermission())) {
					sender.sendMessage("Insufficient permission.");
					return true;
				}
				
				cmd.execute(sender, identifier, realArgs);
				return true;
			}
		}
		sender.sendMessage("Unrecognized command!");
		return true;
	}
	
	private void displayCommandHelp(Command cmd, CommandSender sender) {
		sender.sendMessage(new StringBuilder().append("§cCommand:§e ").append(cmd.getName()).toString());
		sender.sendMessage(new StringBuilder().append("§cDescription:§e ").append(cmd.getDescription()).toString());
		sender.sendMessage(new StringBuilder().append("§cUsage:§e ").append(cmd.getUsage()).toString());
		if (cmd.getNotes() != null) {
			for (String note : cmd.getNotes()) {
				sender.sendMessage(new StringBuilder().append("§e").append(note).toString());
			}
		}
	}
	
	public Command getCmdFromIdent(String ident, CommandSender sender) {
		if (this.identifiers.get(ident.toLowerCase()) == null) {
			for (Command cmd : this.commands.values()) {
				if (cmd.isIdentifier(sender, ident)) {
					return cmd;
				}
			}
		}
		return (Command)this.identifiers.get(ident.toLowerCase());
	}
	
	public Command getCommand(String name) {
		return (Command)this.commands.get(name.toLowerCase());
	}
	
	public List<Command> getCommands() {
		return new ArrayList(this.commands.values());
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		if ((!(sender instanceof Player)) || (permission == null) || (permission.isEmpty())) {
			return true;
		}
		Player player = (Player)sender;
		return (player.isOp()) || (DonorTitles.perms.has(player, permission));
	}

}
